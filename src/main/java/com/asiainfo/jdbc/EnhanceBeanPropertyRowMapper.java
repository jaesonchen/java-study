package com.asiainfo.jdbc;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Transient;
import javax.persistence.Embedded;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import com.asiainfo.jdbc.converter.Converter;
import com.asiainfo.jdbc.converter.IConvertService;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年6月25日  下午12:34:50
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class EnhanceBeanPropertyRowMapper<T> implements RowMapper<T> {

		/** Logger available to subclasses */
		protected final Log logger = LogFactory.getLog(getClass());

		/** The class we are mapping to */
		private Class<T> mappedClass;

		/** Whether we're strictly validating */
		private boolean checkFullyPopulated = false;

		/** Whether we're defaulting primitives when mapping a null value */
		private boolean primitivesDefaultedForNullValue = false;

		/** Map of the fields we provide mapping for */
		private Map<String, PropertyDescriptor> mappedFields;

		/** Set of bean properties we provide mapping for */
		private Set<String> mappedProperties;

		/** Map of the fields we provide converter for */
		private Map<String, IConvertService<?>> mappedConverter;

		/** Map of the embedded fields */
		private Map<String, PropertyDescriptor> mappedEmbedded;
		
		/**
		 * Create a new {@code BeanPropertyRowMapper} for bean-style configuration.
		 * @see #setMappedClass
		 * @see #setCheckFullyPopulated
		 */
		public EnhanceBeanPropertyRowMapper() {
		}

		/**
		 * Create a new {@code BeanPropertyRowMapper}, accepting unpopulated
		 * properties in the target bean.
		 * <p>Consider using the {@link #newInstance} factory method instead,
		 * which allows for specifying the mapped type once only.
		 * @param mappedClass the class that each row should be mapped to
		 */
		public EnhanceBeanPropertyRowMapper(Class<T> mappedClass) {
			initialize(mappedClass);
		}

		/**
		 * Create a new {@code BeanPropertyRowMapper}.
		 * @param mappedClass the class that each row should be mapped to
		 * @param checkFullyPopulated whether we're strictly validating that
		 * all bean properties have been mapped from corresponding database fields
		 */
		public EnhanceBeanPropertyRowMapper(Class<T> mappedClass, boolean checkFullyPopulated) {
			initialize(mappedClass);
			this.checkFullyPopulated = checkFullyPopulated;
		}


		/**
		 * Set the class that each row should be mapped to.
		 */
		public void setMappedClass(Class<T> mappedClass) {
			if (this.mappedClass == null) {
				initialize(mappedClass);
			} else {
				if (this.mappedClass != mappedClass) {
					throw new InvalidDataAccessApiUsageException("The mapped class can not be reassigned to map to " +
							mappedClass + " since it is already providing mapping for " + this.mappedClass);
				}
			}
		}

		/**
		 * Get the class that we are mapping to.
		 */
		public final Class<T> getMappedClass() {
			return this.mappedClass;
		}

		/**
		 * Set whether we're strictly validating that all bean properties have been mapped
		 * from corresponding database fields.
		 * <p>Default is {@code false}, accepting unpopulated properties in the target bean.
		 */
		public void setCheckFullyPopulated(boolean checkFullyPopulated) {
			this.checkFullyPopulated = checkFullyPopulated;
		}

		/**
		 * Return whether we're strictly validating that all bean properties have been
		 * mapped from corresponding database fields.
		 */
		public boolean isCheckFullyPopulated() {
			return this.checkFullyPopulated;
		}

		/**
		 * Set whether we're defaulting Java primitives in the case of mapping a null value
		 * from corresponding database fields.
		 * <p>Default is {@code false}, throwing an exception when nulls are mapped to Java primitives.
		 */
		public void setPrimitivesDefaultedForNullValue(boolean primitivesDefaultedForNullValue) {
			this.primitivesDefaultedForNullValue = primitivesDefaultedForNullValue;
		}

		/**
		 * Return whether we're defaulting Java primitives in the case of mapping a null value
		 * from corresponding database fields.
		 */
		public boolean isPrimitivesDefaultedForNullValue() {
			return this.primitivesDefaultedForNullValue;
		}


		/**
		 * Initialize the mapping metadata for the given class.
		 * @param mappedClass the mapped class
		 */
		protected void initialize(Class<T> mappedClass) {
			
			this.mappedClass = mappedClass;
			this.mappedFields = new HashMap<String, PropertyDescriptor>(16);
			this.mappedProperties = new HashSet<String>();
			this.mappedConverter = new HashMap<>(16);
			this.mappedEmbedded = new HashMap<>(16);
			PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(mappedClass);
			for (PropertyDescriptor pd : pds) {
				if (pd.getWriteMethod() != null) {
					//如果field上标有@Transient，则忽略
					if (null != this.getFieldAnnotation(this.mappedClass, pd.getName(), Transient.class)) {
						continue;
					}
					//如果field上标有@Column，则使用column中标记的列名
					Column column = this.getFieldAnnotation(this.mappedClass, pd.getName(), Column.class);
					Embedded embedded = this.getFieldAnnotation(this.mappedClass, pd.getName(), Embedded.class);
					if (null != column) {
						this.mappedFields.put(lowerCaseName(column.name()), pd);
					//如果field上标有@Embedded，则取embedded class的属性
					} else if (null != embedded) {
						this.mappedEmbedded(pd, this.mappedFields, this.mappedProperties, 
								this.mappedConverter, this.mappedEmbedded);
					} else {
						this.mappedFields.put(lowerCaseName(pd.getName()), pd);
						String underscoredName = underscoreName(pd.getName());
						if (!lowerCaseName(pd.getName()).equals(underscoredName)) {
							this.mappedFields.put(underscoredName, pd);
						}
					}
					//如果setter上标有@Converter，则使用setter上的方法进行转换
					Converter convert = pd.getWriteMethod().getAnnotation(Converter.class);
					if (null != convert) {
						try {
							this.mappedConverter.put(pd.getName(), this.generateConvertService(convert));
						} catch (InstantiationException | IllegalAccessException e) {
							e.printStackTrace();
						}
					}
					
					//保存映射属性名
					this.mappedProperties.add(pd.getName());
				}
			}
		}
		
		/**
		 * 
		 * @Description: 获取@Embedded描述的实体类，暂时没有实现递归embedded
		 * 
		 * @param embeddedDescriptor
		 * @param _mappedFields
		 * @param _mappedProperties
		 * @param _mappedConverter
		 * @param _mappedEmbedded
		 */
		protected void mappedEmbedded(PropertyDescriptor embeddedDescriptor, Map<String, PropertyDescriptor> embeddedMappedFields, 
				Set<String> embeddedMappedProperties, Map<String, IConvertService<?>> embeddedMappedConverter, 
				Map<String, PropertyDescriptor> embeddedMappedEmbedded) {
			
			PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(embeddedDescriptor.getPropertyType());
			for (PropertyDescriptor pd : pds) {
				if (pd.getWriteMethod() != null) {
					//如果field上标有@Transient，则忽略
					if (null != this.getFieldAnnotation(embeddedDescriptor.getPropertyType(), pd.getName(), Transient.class)) {
						continue;
					}
					//如果field上标有@Column，则使用column中标记的列名
					Column column = this.getFieldAnnotation(embeddedDescriptor.getPropertyType(), pd.getName(), Column.class);
					if (null != column) {
						embeddedMappedFields.put(lowerCaseName(column.name()), pd);
						embeddedMappedEmbedded.put(lowerCaseName(column.name()), embeddedDescriptor);
					} else {
						embeddedMappedFields.put(lowerCaseName(pd.getName()), pd);
						embeddedMappedEmbedded.put(lowerCaseName(pd.getName()), embeddedDescriptor);
						String underscoredName = underscoreName(pd.getName());
						if (!lowerCaseName(pd.getName()).equals(underscoredName)) {
							embeddedMappedFields.put(underscoredName, pd);
							embeddedMappedEmbedded.put(underscoredName, embeddedDescriptor);
						}
					}
					//如果setter上标有@Converter，则使用setter上的方法进行转换
					Converter convert = pd.getWriteMethod().getAnnotation(Converter.class);
					if (null != convert) {
						try {
							embeddedMappedConverter.put(pd.getName(), this.generateConvertService(convert));
						} catch (InstantiationException | IllegalAccessException e) {
							e.printStackTrace();
						}
					}
					
					//保存映射属性名
					embeddedMappedProperties.add(pd.getName());
				}
			}
		}
		
		/**
		 * 
		 * @Description: 生成converter实例
		 * 
		 * @param convertAnnotation
		 * @return
		 * @throws InstantiationException
		 * @throws IllegalAccessException
		 */
		protected IConvertService<?> generateConvertService(Converter convertAnnotation) throws InstantiationException, IllegalAccessException {
			
			Class<?>[] clazzArray = convertAnnotation.value();
			IConvertService<?> prefixService = null;
			IConvertService<?> service = null;
			for (Class<?> serviceClazz : clazzArray) {
				service = (IConvertService<?>) serviceClazz.newInstance();
				if (null != prefixService) {
					service.setDelegate(prefixService);
				}
				prefixService = service;
			}
			return service;
		}
		
		/**
		 * 
		 * @Description: 获取指定属性的注解
		 * 
		 * @param clazz
		 * @param propertyName
		 * @param annotationClazz
		 * @return
		 */
		protected <M extends Annotation> M getFieldAnnotation(Class<?> clazz, String propertyName, Class<M> annotationClazz) {
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				if (field.getName().equals(propertyName)) {
					return field.getAnnotation(annotationClazz);
				}
			}
			return null;
		}

		/**
		 * Convert a name in camelCase to an underscored name in lower case.
		 * Any upper case letters are converted to lower case with a preceding underscore.
		 * @param name the original name
		 * @return the converted name
		 * @since 4.2
		 * @see #lowerCaseName
		 */
		protected String underscoreName(String name) {
			if (!StringUtils.hasLength(name)) {
				return "";
			}
			StringBuilder result = new StringBuilder();
			result.append(lowerCaseName(name.substring(0, 1)));
			for (int i = 1; i < name.length(); i++) {
				String s = name.substring(i, i + 1);
				String slc = lowerCaseName(s);
				if (!s.equals(slc)) {
					result.append("_").append(slc);
				} else {
					result.append(s);
				}
			}
			return result.toString();
		}

		/**
		 * Convert the given name to lower case.
		 * By default, conversions will happen within the US locale.
		 * @param name the original name
		 * @return the converted name
		 * @since 4.2
		 */
		protected String lowerCaseName(String name) {
			return name.toLowerCase(Locale.US);
		}


		/**
		 * Extract the values for all columns in the current row.
		 * <p>Utilizes public setters and result set metadata.
		 * @see java.sql.ResultSetMetaData
		 */
		@Override
		public T mapRow(ResultSet rs, int rowNumber) throws SQLException {
			
			Assert.state(this.mappedClass != null, "Mapped class was not specified");
			T mappedObject = BeanUtils.instantiate(this.mappedClass);
			BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(mappedObject);
			initBeanWrapper(bw);

			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			Set<String> populatedProperties = (isCheckFullyPopulated() ? new HashSet<String>() : null);

			Map<PropertyDescriptor, BeanWrapper> embeddedObjectMap = new HashMap<>(16);
			for (int index = 1; index <= columnCount; index++) {
				String column = JdbcUtils.lookupColumnName(rsmd, index);
				String field = lowerCaseName(column.replaceAll(" ", ""));
				PropertyDescriptor pd = this.mappedFields.get(field);
				if (pd != null) {
					try {
						//对应的convert不为空时，使用convert转换
						Object value = (null == this.mappedConverter.get(pd.getName())) ? 
								getColumnValue(rs, index, pd) : getColumnValue(rs, index, this.mappedConverter.get(pd.getName()));
						
						if (rowNumber == 0 && logger.isDebugEnabled()) {
							logger.debug("Mapping column '" + column + "' to property '" + pd.getName() +
									"' of type [" + ClassUtils.getQualifiedName(pd.getPropertyType()) + "]");
						}
						//判断是否是embedded属性，不是embedded直接注入
						if (null == this.mappedEmbedded.get(field)) {
							try {
								bw.setPropertyValue(pd.getName(), value);
							} catch (TypeMismatchException ex) {
								if (value == null && this.primitivesDefaultedForNullValue) {
									if (logger.isDebugEnabled()) {
										logger.debug("Intercepted TypeMismatchException for row " + rowNumber +
												" and column '" + column + "' with null value when setting property '" +
												pd.getName() + "' of type [" +
												ClassUtils.getQualifiedName(pd.getPropertyType()) +
												"] on object: " + mappedObject, ex);
									}
								} else {
									throw ex;
								}
							}
						//如果是embedded的列，从map中取出embeddedObject注入
						} else {
							PropertyDescriptor embeddedDescriptor = this.mappedEmbedded.get(field);
							//logger.debug("embeddedDescriptor:" + embeddedDescriptor);
							BeanWrapper embeddedObject = embeddedObjectMap.get(embeddedDescriptor);
							try {
								if (null == embeddedObject) {
									embeddedObject = PropertyAccessorFactory.forBeanPropertyAccess(embeddedDescriptor.getPropertyType().newInstance());
									embeddedObjectMap.put(embeddedDescriptor, embeddedObject);
								}
								//注入到embeddedObject
								embeddedObject.setPropertyValue(pd.getName(), value);
							} catch (InstantiationException |IllegalAccessException e) {
								e.printStackTrace();
							} catch (TypeMismatchException ex) {
								if (value == null && this.primitivesDefaultedForNullValue) {
									if (logger.isDebugEnabled()) {
										logger.debug("Intercepted TypeMismatchException for row " + rowNumber +
												" and column '" + column + "' with null value when setting property '" +
												pd.getName() + "' of type [" +
												ClassUtils.getQualifiedName(pd.getPropertyType()) +
												"] on object: " + embeddedObject, ex);
									}
								} else {
									throw ex;
								}
							}
						}

						if (populatedProperties != null) {
							populatedProperties.add(pd.getName());
						}
					} catch (NotWritablePropertyException ex) {
						throw new DataRetrievalFailureException(
								"Unable to map column '" + column + "' to property '" + pd.getName() + "'", ex);
					}
				} else {
					// No PropertyDescriptor found
					if (rowNumber == 0 && logger.isDebugEnabled()) {
						logger.debug("No property found for column '" + column + "' mapped to field '" + field + "'");
					}
				}
			}
			//把embeddedObject注入到实体中
			if (null != embeddedObjectMap && !embeddedObjectMap.isEmpty()) {
				for (java.util.Map.Entry<PropertyDescriptor, BeanWrapper> entry : embeddedObjectMap.entrySet()) {
					try {
						bw.setPropertyValue(entry.getKey().getName(), entry.getValue().getWrappedInstance());
					} catch (TypeMismatchException ex) {
						if (entry.getValue() == null && this.primitivesDefaultedForNullValue) {
							if (logger.isDebugEnabled()) {
								logger.debug("Intercepted TypeMismatchException for row " + rowNumber +
										" and property '" + entry.getKey() + "' with null value when setting property '" +
										entry.getKey() + "' of type [" +
										ClassUtils.getQualifiedName(entry.getKey().getPropertyType()) +
										"] on object: " + mappedObject, ex);
							}
						} else {
							throw ex;
						}
					}
				}
			}

			if (populatedProperties != null && !populatedProperties.equals(this.mappedProperties)) {
				throw new InvalidDataAccessApiUsageException("Given ResultSet does not contain all fields " +
						"necessary to populate object of class [" + this.mappedClass.getName() + "]: " +
						this.mappedProperties);
			}

			return mappedObject;
		}

		/**
		 * Initialize the given BeanWrapper to be used for row mapping.
		 * To be called for each row.
		 * <p>The default implementation is empty. Can be overridden in subclasses.
		 * @param bw the BeanWrapper to initialize
		 */
		protected void initBeanWrapper(BeanWrapper bw) {
		}

		/**
		 * Retrieve a JDBC object value for the specified column.
		 * <p>The default implementation calls
		 * {@link JdbcUtils#getResultSetValue(java.sql.ResultSet, int, Class)}.
		 * Subclasses may override this to check specific value types upfront,
		 * or to post-process values return from {@code getResultSetValue}.
		 * @param rs is the ResultSet holding the data
		 * @param index is the column index
		 * @param pd the bean property that each result object is expected to match
		 * (or {@code null} if none specified)
		 * @return the Object value
		 * @throws SQLException in case of extraction failure
		 * @see org.springframework.jdbc.support.JdbcUtils#getResultSetValue(java.sql.ResultSet, int, Class)
		 */
		protected Object getColumnValue(ResultSet rs, int index, PropertyDescriptor pd) throws SQLException {
			return JdbcUtils.getResultSetValue(rs, index, pd.getPropertyType());
		}

		protected Object getColumnValue(ResultSet rs, int index, IConvertService<?> converter) throws SQLException {
			return converter.convert(JdbcUtils.getResultSetValue(rs, index, converter.getClazz()));
		}

		/**
		 * Static factory method to create a new {@code BeanPropertyRowMapper}
		 * (with the mapped class specified only once).
		 * @param mappedClass the class that each row should be mapped to
		 */
		public static <T> BeanPropertyRowMapper<T> newInstance(Class<T> mappedClass) {
			return new BeanPropertyRowMapper<T>(mappedClass);
		}
	}

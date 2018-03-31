package com.asiainfo.springutil;

/**
 * TODO
 * 
 * @author       zq
 * @date         2018年3月30日  下午3:28:31
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class SpringUtilsExample {

    /** 
     * TODO
     * 
     * @param args
     */
    public static void main(String[] args) {
        
        // ReflectionUtils
        // Field findField(Class<?> clazz, String name)
        // Method findMethod(Class<?> clazz, String name)
        // void handleReflectionException(Exception ex)
        
        // ClassUtils
        // ClassLoader getDefaultClassLoader()
        // boolean isAssignable(Class<?> lhsType, Class<?> rhsType)
        // Class<?>[] getAllInterfaces(Object instance)
        // Class<?> determineCommonAncestor(Class<?> clazz1, Class<?> clazz2)
        
        
        // StringUtils
        // boolean isEmpty(Object str)
        // boolean hasLength(String str)
        // boolean hasText(String str)
        // boolean startsWithIgnoreCase(String str, String prefix)
        // boolean endsWithIgnoreCase(String str, String suffix)
        // int countOccurrencesOf(String str, String sub)
        // String quote(String str)
        // String unqualify(String qualifiedName, char separator)
        // String capitalize(String str)
        // String uncapitalize(String str)
        // String getFilename(String path)
        // String getFilenameExtension(String path)
        // String[] addStringToArray(String[] array, String str)
        // String[] concatenateStringArrays(String[] array1, String[] array2)
        // String[] toStringArray(Collection<String> collection)
        // String[] removeDuplicateStrings(String[] array)
        // String collectionToDelimitedString(Collection<?> coll, String delim)
        // String arrayToDelimitedString(Object[] arr, String delim)
        
        // ObjectUtils
        // boolean isArray(Object obj)
        // boolean isEmpty(Object[] array)
        // boolean isEmpty(Object obj)
        // boolean containsElement(Object[] array, Object element)
        // <A, O extends A> A[] addObjectToArray(A[] array, O obj)
        // Object[] toObjectArray(Object source)
        // boolean nullSafeEquals(Object o1, Object o2)
        
        // CollectionUtils
        // boolean isEmpty(Collection<?> collection)
        // boolean isEmpty(Map<?, ?> map)
        // <E> void mergeArrayIntoCollection(Object array, Collection<E> collection)
        // <K, V> void mergePropertiesIntoMap(Properties props, Map<K, V> map)
        // boolean contains(Iterator<?> iterator, Object element)
        // 使用==比较
        // boolean containsInstance(Collection<?> collection, Object element)
        
        
        // FileSystemUtils
        // boolean deleteRecursively(File root)
        // void copyRecursively(File src, File dest) throws IOException
        
        // FileCopyUtils
        // int copy(File in, File out) throws IOException
        
        // StreamUtils
        // int copy(InputStream in, OutputStream out) throws IOException
        // String copyToString(InputStream in, Charset charset) throws IOException
        // byte[] copyToByteArray(InputStream in) throws IOException
    }

}

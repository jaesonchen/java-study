package com.asiainfo.rpc;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年8月22日  上午10:08:51
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class RpcRequest implements Serializable {
	
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	private String interfaceName;		//目标接口名
	private String methodName;			//目标方法名
	private Class<?>[] parameterTypes;	//参数类型
	private Object[] parameterValues;	//参数值
	
	public RpcRequest(){}
	public RpcRequest(String interfaceName, String methodName, Class<?>[] parameterTypes, Object[] parameterValues) {
		this.interfaceName = interfaceName;
		this.methodName = methodName;
		this.parameterTypes = parameterTypes;
		this.parameterValues = parameterValues;
	}
	
	public String getInterfaceName() {
		return interfaceName;
	}
	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}
	public void setParameterTypes(Class<?>[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}
	public Object[] getParameterValues() {
		return parameterValues;
	}
	public void setParameterValues(Object[] parameterValues) {
		this.parameterValues = parameterValues;
	}
	
	@Override
	public String toString() {
		return "Request [interfaceName=" + interfaceName + ", methodName=" + methodName + ", parameterTypes="
				+ Arrays.toString(parameterTypes) + ", parameterValues=" + Arrays.toString(parameterValues) + "]";
	}
}

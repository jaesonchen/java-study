package com.asiainfo.clazz;

public class CONSTANT_InterfaceMethodref_Info extends ConstantPoolType {

	private int classrefIndex;
	private int nameTypeIndex;
	
	public CONSTANT_InterfaceMethodref_Info(int classrefIndex, int nameTypeIndex) {
		super();
		this.classrefIndex = classrefIndex;
		this.nameTypeIndex = nameTypeIndex;
	}
	public int getClassrefIndex() {
		return classrefIndex;
	}
	public void setClassrefIndex(int classrefIndex) {
		this.classrefIndex = classrefIndex;
	}
	public int getNameTypeIndex() {
		return nameTypeIndex;
	}
	public void setNameTypeIndex(int nameTypeIndex) {
		this.nameTypeIndex = nameTypeIndex;
	}
}

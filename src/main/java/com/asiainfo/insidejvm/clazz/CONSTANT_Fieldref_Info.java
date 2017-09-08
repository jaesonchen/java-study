package com.asiainfo.insidejvm.clazz;

public class CONSTANT_Fieldref_Info extends ConstantPoolType {

	private int classrefIndex;
	private int nameTypeIndex;
	
	public CONSTANT_Fieldref_Info(int classrefIndex, int nameTypeIndex) {
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

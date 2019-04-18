package com.asiainfo.clazz;

public class CONSTANT_NameAndType_Info extends ConstantPoolType {

	private int nameIndex;
	private int descIndex;
	public CONSTANT_NameAndType_Info(int nameIndex, int descIndex) {
		super();
		this.nameIndex = nameIndex;
		this.descIndex = descIndex;
	}
	public int getNameIndex() {
		return nameIndex;
	}
	public void setNameIndex(int nameIndex) {
		this.nameIndex = nameIndex;
	}
	public int getDescIndex() {
		return descIndex;
	}
	public void setDescIndex(int descIndex) {
		this.descIndex = descIndex;
	}
}

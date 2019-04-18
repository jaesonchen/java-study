package com.asiainfo.insidejvm;

/**
 * 
 * switch要求使用一个选择因子，并且必须是int 或char 那样的整数值(String和Enum实际上在编译后被转换为int)。
 * 例如，假若将一个浮点数作为选择因子使用，那么它们在switch 语句里是不会工作的；对于非整数类型，则必须使用一系列if 语句。
 * 每个case 均以一个break 结尾。这样可使执行流程跳转至switch 主体的末尾。
 * 若省略break，会继续执行后面的case 语句的代码，直到遇到一个break 为止。
 */
public class LabeledLoop {

	public static void main(String[] args) {
	    
		int i = 0;
		outer: // Can't have statements here
		for(; true ;) {
			inner: // Can't have statements here
			for(; i < 10; i++) {
				prt("i = " + i);
				if(i == 2) {
					prt("continue");
					continue;
				}
				if(i == 3) {
					prt("break");
					i++; // Otherwise i never gets incremented.
					break;
				}
				if(i == 7) {
					prt("continue outer");
					i++; // Otherwise i never gets incremented.
					continue outer;
				}
				if(i == 8) {
					prt("break outer");
					break outer;
				}
				for(int k = 0; k < 5; k++) {
					if(k == 3) {
						prt("continue inner");
						continue inner;
					}
				}
			}
		}
		// Can't break or continue to labels here
	}
	
	static void prt(String s) {
		System.out.println(s);
	}
}

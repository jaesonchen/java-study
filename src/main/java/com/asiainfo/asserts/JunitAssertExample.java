package com.asiainfo.asserts;

import static org.junit.Assert.*;

/**
 * junitAssert throws AssertionError extends Error
 * 
 * @author       zq
 * @date         2018年3月30日  下午2:18:58
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class JunitAssertExample {

    /** 
     * TODO
     * 
     * @param args
     */
    public static void main(String[] args) {
        
        // void assertTrue(String message, boolean condition)
        // void assertFalse(String message, boolean condition)
        assertTrue("1 < 2", 1 < 2);
        
        // void assertEquals(String message, Object expected, Object actual)
        assertEquals("expected 1", 1, 1);
        // void assertNotEquals(String message, Object unexpected, Object actual)
        // void assertArrayEquals(String message, Object[] expecteds, Object[] actuals)
        assertArrayEquals("expecteds {1, 2}", new Object[] {1, 2}, new Object[] {1, 2});
        
        // void assertNull(String message, Object object)
        // void assertNotNull(String message, Object object)
        assertNull("not null", null);
        assertNotNull("null", "");
        
        //必须是同一个对象，equals相同也不行
        // void assertSame(String message, Object expected, Object actual)
        // void assertNotSame(String message, Object unexpected, Object actual)
        assertSame("", 1, 1);
        assertSame("", new Integer(1), new Integer(1));
    }
}

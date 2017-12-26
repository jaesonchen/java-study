package com.asiainfo.datastructure;

import java.util.Iterator;

/**
 * 迭代接口
 * 
 * @author       zq
 * @date         2017年12月25日  下午3:18:32
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface MyIterator<E> extends Iterator<E> {
    
    /**
     * 是否还有元素
     * 
     * @return
     */
    boolean hasNext();
    
    /**
     * 返回前一个（坐标前移一位）
     * 
     * @return
     */
    E previous();
    
    /**
     * 下一个
     * 
     * @return
     */
    E next();
    
    /**
     * 第一个
     * 
     * @return
     */
    E first();
    
    /**
     * 最后一个
     * 
     * @return
     */
    E last();
}

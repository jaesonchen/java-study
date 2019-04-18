package com.asiainfo.jdbc.converter;

import java.util.Arrays;
import java.util.List;

/**   
 * @Description: String字段转List<String>，用于setter上
 * 
 * @author chenzq  
 * @date 2019年3月27日 下午6:29:45
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class String2ListConvertServiceImpl implements IConvertService<List<String>> {

    private IConvertService<?> delegate;
    
    /* (非 Javadoc)
     * @Title: getClazz
     * @author chenzq
     * @date 2019年3月27日 下午6:29:45
     * @return
     * @see com.asiainfo.jdbc.converter.IConvertService#getClazz()
     */
    @Override
    public Class<?> getClazz() {
        return null == this.delegate ? String.class : this.delegate.getClazz();
    }

    /* (非 Javadoc)
     * @Title: convert
     * @author chenzq
     * @date 2019年3月27日 下午6:29:45
     * @param obj
     * @return
     * @see com.asiainfo.jdbc.converter.IConvertService#convert(java.lang.Object)
     */
    @Override
    public List<String> convert(Object obj) {
        Object result = null == this.delegate ? obj : this.delegate.convert(obj);
        return null == result ? null : Arrays.asList(String.valueOf(result).split(","));
    }

    /* (非 Javadoc)
     * @Title: setDelegate
     * @author chenzq
     * @date 2019年3月27日 下午6:29:45
     * @param delegate
     * @see com.asiainfo.jdbc.converter.IConvertService#setDelegate(com.asiainfo.jdbc.converter.IConvertService)
     */
    @Override
    public void setDelegate(IConvertService<?> delegate) {
        this.delegate = delegate;
    }
}

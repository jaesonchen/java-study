package com.asiainfo.jdbc.converter;

/**   
 * @Description: java.sql.Blob字段转byte[]，用于setter上
 * 
 * @author chenzq  
 * @date 2019年3月27日 下午6:23:10
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class Blob2ByteArrayConvertServiceImpl implements IConvertService<byte[]> {

    /* (非 Javadoc)
     * @Title: getClazz
     * @author chenzq
     * @date 2019年3月27日 下午6:23:10
     * @return
     * @see com.asiainfo.jdbc.converter.IConvertService#getClazz()
     */
    @Override
    public Class<?> getClazz() {
        return java.sql.Blob.class;
    }

    /* (非 Javadoc)
     * @Title: convert
     * @author chenzq
     * @date 2019年3月27日 下午6:23:10
     * @param obj
     * @return
     * @see com.asiainfo.jdbc.converter.IConvertService#convert(java.lang.Object)
     */
    @Override
    public byte[] convert(Object obj) {
        return ConvertUtil.blob2Byte((java.sql.Blob) obj);
    }

    /* (非 Javadoc)
     * @Title: setDelegate
     * @author chenzq
     * @date 2019年3月27日 下午6:23:10
     * @param delegate
     * @see com.asiainfo.jdbc.converter.IConvertService#setDelegate(com.asiainfo.jdbc.converter.IConvertService)
     */
    @Override
    public void setDelegate(IConvertService<?> delegate) {
        throw new UnsupportedOperationException("BlobConverter do not support delegate!");
    }
}

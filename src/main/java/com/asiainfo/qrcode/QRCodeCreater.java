package com.asiainfo.qrcode;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年8月23日  下午3:16:08
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class QRCodeCreater {

	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		final int width = 300;
		final int height = 300;
		final String format = "png";
		final String content = "www.asiainfo.com";
		
		//定义二维码参数
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
		hints.put(EncodeHintType.MARGIN, 2);
		
		try {
			BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
			Path file = new File("d:/img.png").toPath();
			MatrixToImageWriter.writeToPath(bitMatrix, format, file);
		} catch(Exception e) {
			e.printStackTrace();;
		}
	}
}

package com.asiainfo.qrcode;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年8月23日  下午3:19:41
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class QRCodeDecoder {

	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		MultiFormatReader formatReader = new MultiFormatReader();
		try {
			File file = new File("d:/img.png");
			BufferedImage image = ImageIO.read(file);
			BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));

			// 定义二维码参数
			Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();
			hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");

			Result result = formatReader.decode(binaryBitmap, hints);
			
			System.out.println("解析结果： " + result.toString());
			System.out.println("二维码格式类型: " + result.getBarcodeFormat());
			System.out.println("二维码文本内容: " + result.getText());
		} catch (NotFoundException | IOException e) {
			e.printStackTrace();
		}
	}
}

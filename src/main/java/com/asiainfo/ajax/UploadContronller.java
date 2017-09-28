package com.asiainfo.ajax;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年9月14日  上午10:46:51
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@RestController
public class UploadContronller {

	@RequestMapping("/upload")
	public String upload(@RequestParam(value = "file") MultipartFile file, 
			HttpServletRequest request) {
		
		if (file.isEmpty()) {
			return "empty";
		}
		System.out.println("upload");
		String path = request.getSession().getServletContext().getRealPath("upload");
		System.out.println(path);
		
		String fileName = file.getOriginalFilename();
		System.out.println(fileName);
		File targetFile = new File(path, fileName);
		if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
		
		try {
            file.transferTo(targetFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

		return "success";
	}
}

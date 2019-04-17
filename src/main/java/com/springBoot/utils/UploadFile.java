package com.springBoot.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author chenkuan
 * @version v1.0
 * @desc uploadFile工具类
 * @date 2019/4/17 017 23:56
 */
public class UploadFile {

	public static void upload(byte[] file, String filePath, String fileName) throws IOException {
		File targetFile = new File(filePath);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		FileOutputStream out = new FileOutputStream(filePath + fileName);
		out.write(file);
		out.flush();
		out.close();
	}
}

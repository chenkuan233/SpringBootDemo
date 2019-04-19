package com.springBoot.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author chenkuan
 * @version v1.0
 * @desc uploadFile工具类
 * @date 2019/4/17 017 23:56
 */
@Slf4j
public class UploadUtil {

	/**
	 * 文件上传
	 *
	 * @param file     文件字节数组byte[]
	 * @param filePath 文件存放路径
	 * @param fileName 文件名
	 */
	public static String upload(byte[] file, String filePath, String fileName) {
		FileOutputStream out = null;
		try {
			// 判断目标文件夹是否存在，不存在则新建
			File targetFile = new File(filePath);
			if (!targetFile.exists()) {
				targetFile.mkdirs();
			} else {
				// 判断目标文件是否存在，存在则重命名
				fileName = existFile(filePath, fileName);
			}
			out = new FileOutputStream(filePath + fileName);
			out.write(file);
			// 返回最终写入的文件名
			return fileName;
		} catch (IOException e) {
			log.error(filePath + fileName + "文件上传失败", e);
			return null;
		} finally {
			try {
				closeStream(out);
			} catch (IOException e) {
				log.error("文件流关闭失败", e);
			}
		}
	}

	private static void closeStream(FileOutputStream out) throws IOException {
		if (out != null) {
			out.flush();
			out.close();
		}
	}

	/**
	 * 判断目标文件是否存在，存在则重命名 file file1 file2
	 *
	 * @param filePath 文件路径
	 * @param fileName 文件名
	 * @return String 文件名
	 */
	private static String existFile(String filePath, String fileName) {
		File file = new File(filePath + fileName);
		String fileNameTemp = fileName;
		int num = 1;
		while (file.exists()) {
			fileNameTemp = fileName;
			int lastPointIndex = fileNameTemp.lastIndexOf(".");
			if (lastPointIndex != -1) {
				fileNameTemp = fileNameTemp.substring(0, lastPointIndex) + "(" + num + ")" + fileNameTemp.substring(lastPointIndex);
			} else {
				fileNameTemp += num;
			}
			file = new File(filePath + fileNameTemp);
			num++;
		}
		return fileNameTemp;
	}

}

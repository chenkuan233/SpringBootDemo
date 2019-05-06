package com.springBoot.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * @author chenkuan
 * @version v1.0
 * @desc File操作工具类
 * @date 2019/4/17 017 23:56
 */
@Slf4j
public final class FileUtil {

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

	/**
	 * 文件上传
	 *
	 * @param inputStream 文件流
	 * @param filePath    文件存放路径
	 * @param fileName    文件名
	 */
	public static String upload(InputStream inputStream, String filePath, String fileName) {
		BufferedInputStream bis = null;
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
			byte[] buffer = new byte[1024 * 8];
			bis = new BufferedInputStream(inputStream);
			out = new FileOutputStream(filePath + fileName);
			int i;
			while ((i = bis.read(buffer)) != -1) {
				out.write(buffer, 0, i);
			}
			// 返回最终写入的文件名
			return fileName;
		} catch (IOException e) {
			log.error(filePath + fileName + "文件上传失败", e);
			return null;
		} finally {
			try {
				closeStream(out);
				if (bis != null) {
					bis.close();
				}
			} catch (IOException e) {
				log.error("文件流关闭失败", e);
			}
		}
	}

	/**
	 * 将byte[] file写入File文件，并返回File
	 *
	 * @param file     文件字节数组
	 * @param filePath 缓存文件夹
	 * @param fileName 文件名
	 * @return File
	 */
	public static File bytesToFile(byte[] file, String filePath, String fileName) {
		FileOutputStream out = null;
		try {
			// 判断目标文件夹是否存在，不存在则新建
			File targetFile = new File(filePath);
			if (!targetFile.exists()) {
				targetFile.mkdirs();
			}
			out = new FileOutputStream(filePath + fileName);
			out.write(file);
			// 返回缓存文件
			return new File(filePath + fileName);
		} catch (IOException e) {
			log.error(filePath + fileName + "缓存文件写入失败", e);
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
	 * 判断目标文件是否存在，存在则重命名 file file (1) file (2)
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
				fileNameTemp = fileNameTemp.substring(0, lastPointIndex) + " (" + num + ")" + fileNameTemp.substring(lastPointIndex);
			} else {
				fileNameTemp += " (" + num + ")";
			}
			file = new File(filePath + fileNameTemp);
			num++;
		}
		return fileNameTemp;
	}

	/**
	 * 下载文件 支持迅雷等的断点续传及分片下载
	 *
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 * @param file     下载的文件
	 */
	public static void downloadFile(HttpServletRequest request, HttpServletResponse response, File file) throws UnsupportedEncodingException {
		// 文件总大小
		long fileLength = file.length();
		// 获得客户端的ip地址
		String host = request.getRemoteAddr();

		// 迅雷等工具
		// 用于记录客户端要求下载的数据范围字串(用于断点续传)
		String rangeBytes = request.getHeader("Range");
		// 客户端需要的第一个字节的位置
		Long lenStart = 0L;
		// 客户端需要的字节区间 最后一个字节位置 - 第一个字节的位置
		Long lenEnd = fileLength;
		if (StringUtils.isNotEmpty(rangeBytes)) {
			// 返回码 200 Ok (不使用断点续传方式), 206 Partial Content (使用断点续传方式)
			response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
			rangeBytes = rangeBytes.replace("bytes=", "");
			String startBytes;
			String endBytes;
			if (rangeBytes.indexOf('-') == rangeBytes.length() - 1) {
				// 无结束字符 网际快车等
				startBytes = rangeBytes.substring(0, rangeBytes.indexOf('-'));
				lenStart = Long.parseLong(startBytes.trim());
				lenEnd = fileLength - lenStart;
			} else {
				// 迅雷等
				startBytes = rangeBytes.substring(0, rangeBytes.indexOf('-'));
				endBytes = rangeBytes.substring(rangeBytes.indexOf('-') + 1, rangeBytes.length());
				lenStart = Long.parseLong(startBytes.trim());
				lenEnd = Long.parseLong(endBytes.trim()) - lenStart;
			}
			// 通知客户端允许断点续传，响应格式为：Accept-Ranges: bytes
			response.setHeader("Accept-Ranges", "bytes");
		}
		if (lenStart != 0) {
			// 断点续传
			// 服务器返回当前接受的范围和文件总大小
			// 响应格式 Content-Range: bytes (unit first byte pos) - [last byte pos]/[entity legth]
			String contentRange = rangeBytes + "/" + String.valueOf(fileLength);
			response.setHeader("Content-Range", contentRange);
		}

		// 文件名
		String fileName = file.getName();
		// 告诉浏览器输出内容格式 application/octet-stream，application/force-download
		response.setContentType("application/octet-stream");
		// 设置下载而并不是打开 两种属性：attachment保存，inline直接显示  浏览器点保存后，文件以filename的值命名
		response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
		// 设置文件大小
		response.setContentLengthLong(fileLength);
		// response.addHeader("Content-Length", String.valueOf(fileLength));

		long hasReadBytes = 0L; // 已总共读取字节数
		long notReadBytes = lenEnd - hasReadBytes; // 客户端要求还未读取字节数
		int initTem = 1024 * 8; // 默认一次缓存字节读取数
		byte[] buffer = null;
		if (notReadBytes < initTem) {
			buffer = new byte[(int) notReadBytes];
		} else {
			buffer = new byte[initTem];
		}
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(file, "r");
			// 从指定位置开始读取
			if (lenStart != 0) raf.seek(lenStart);
			OutputStream os = response.getOutputStream();
			int num;
			while ((num = raf.read(buffer)) != -1 && notReadBytes > 0) {
				os.write(buffer, 0, num);
				hasReadBytes += num;
				notReadBytes = lenEnd - hasReadBytes;
				if (notReadBytes < initTem) buffer = new byte[(int) notReadBytes];
			}
			log.info(host + " " + (StringUtils.isEmpty(rangeBytes) ? fileName : fileName + "（" + rangeBytes + "）") + "下载完成");
		} catch (ClientAbortException e) {
			// log.error("客户端中断了连接");
		} catch (IOException e) {
			log.error(fileName + "下载失败", e);
		} finally {
			if (raf != null) {
				try {
					raf.close();
				} catch (IOException e) {
					log.error("IO流关闭失败", e);
				}
			}
		}
	}

}

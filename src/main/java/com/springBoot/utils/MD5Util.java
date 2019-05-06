package com.springBoot.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author chenkuan
 * @version v1.0
 * @desc MD5加密工具类
 * @date 2019/3/22 022 16:02
 */
@Slf4j
public final class MD5Util {

	public static String md5(String text) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(text.getBytes());
			byte[] digest = md.digest();
			return toHexString(digest);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public static String md5(InputStream in) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] buf = new byte[1024 * 2];
			int i = 0;
			while ((i = in.read(buf)) != -1) {
				if (i == 0) {
					continue;
				}
				md.update(buf, 0, i);
			}
			byte[] digest = md.digest();
			return toHexString(digest);
		} catch (NoSuchAlgorithmException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String md5(File file) {
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			return md5(in);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					log.error("MD5加密文件流关闭失败", e);
				}
			}
		}
	}

	public static String sha(String text) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA");
			md.update(text.getBytes());
			byte[] digest = md.digest();
			return toHexString(digest);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public static String toHexString(byte[] bytes) {
		StringBuilder buf = new StringBuilder(50);
		int t;
		for (byte aByte : bytes) {
			t = aByte;
			if (t < 0) {
				t += 256;
			}
			if (t < 16) {
				buf.append("0");
			}
			buf.append(Integer.toHexString(t));
		}
		return buf.toString();
	}
}

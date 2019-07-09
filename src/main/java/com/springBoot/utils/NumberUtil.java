package com.springBoot.utils;

import java.util.Random;
import java.util.UUID;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 数字相关工具类 随机，6位验证码等
 * @date 2019/7/9 009 16:27
 */
public class NumberUtil {

	/**
	 * 获取随机uuid
	 */
	public static String getRandomUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * 生成指定长度的验证码(包括数字和大小写字母)
	 *
	 * @param codeLength 需要的验证码长度
	 */
	public static String getVerificationCode(int codeLength) {
		//所有候选组成验证码的字符，可以用中文
		String[] verificationCodeArrary = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
				"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
				"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
		};
		int verificationCodeArraryLength = verificationCodeArrary.length;
		StringBuilder verificationCode = new StringBuilder();
		Random random = new Random();
		//此处是生成验证码的核心了，利用一定范围内的随机数做为验证码数组的下标，循环组成我们需要长度的验证码，做为页面输入验证、邮件、短信验证码验证都行
		for (int i = 0; i < codeLength; i++) {
			verificationCode.append(verificationCodeArrary[random.nextInt(verificationCodeArraryLength)]);
		}
		return verificationCode.toString();
	}

	/**
	 * 生成指定长度的验证码(只包括数字)
	 *
	 * @param codeLength 需要的验证码长度
	 */
	public static String getNumberCode(int codeLength) {
		StringBuilder verificationCode = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < codeLength; i++) {
			verificationCode.append(random.nextInt(10));
		}
		return verificationCode.toString();
	}

	/**
	 * 生成6位数字验证码
	 */
	public static String getSixNumberCode() {
		return getNumberCode(6);
	}
}

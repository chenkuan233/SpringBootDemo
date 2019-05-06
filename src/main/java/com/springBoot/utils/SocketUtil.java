package com.springBoot.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author chenkuan
 * @version v1.0
 * @desc
 * @date 2019/5/6 006 11:21
 */
@Slf4j
public class SocketUtil {

	/**
	 * 关闭连接
	 *
	 * @param in     输入流
	 * @param out    输出流
	 * @param socket socket
	 */
	public static void close(InputStream in, OutputStream out, Socket socket) {
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
				log.error("in关闭失败", e);
			}
		}
		if (out != null) {
			try {
				out.close();
			} catch (IOException e) {
				log.error("out关闭失败", e);
			}
		}
		if (socket != null && socket.isConnected()) {
			try {
				socket.close();
			} catch (IOException e) {
				log.error("socket关闭失败", e);
			}
		}
	}

}

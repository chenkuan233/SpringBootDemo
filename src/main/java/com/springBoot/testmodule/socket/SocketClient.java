package com.springBoot.testmodule.socket;

import com.springBoot.utils.SocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author chenkuan
 * @version v1.0
 * @desc socket客户端
 * @date 2019/5/6 006 10:00
 */
@Slf4j
@Service
public class SocketClient {

	public static void main(String[] args) throws IOException {
		// 要连接的服务端IP地址和端口
		String host = "127.0.0.1";
		int port = 55533;
		// 与服务端建立连接
		Socket socket = new Socket(host, port);

		// 建立连接后获得输出流
		OutputStream out = socket.getOutputStream();
		String message = "你好，我是客户端发送的测试消息233";
		out.write(message.getBytes("UTF-8"));
		out.flush();

		// 通过shutdownOutput告诉服务器已经发送完数据，后续只能接受数据
		// 如果不做约定，也不关闭它，那么服务端永远不知道客户端是否发送完消息，那么服务端会一直等待下去，直到读取超时
		socket.shutdownOutput();

		// 接受服务端返回的消息数据
		InputStream in = socket.getInputStream();
		StringBuilder sb = new StringBuilder();
		byte[] bytes = new byte[1024];
		int len;
		while ((len = in.read(bytes)) != -1) {
			sb.append(new String(bytes, 0, len, "UTF-8"));
		}
		log.info("get message from server: " + sb);

		// 关闭连接
		SocketUtil.close(in, out, socket);
	}
}

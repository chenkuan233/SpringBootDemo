package com.springBoot.impl.socket;

import com.springBoot.service.socket.SocketServerService;
import com.springBoot.utils.SocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author chenkuan
 * @version v1.0
 * @desc socket服务端
 * @date 2019/5/6 006 10:00
 */
@Slf4j
@Service("socketServerService")
public class SocketServerServiceImpl implements SocketServerService {

	public static void main(String[] args) {
		ServerSocket server = null;
		try {
			// 监听指定端口
			int port = 55533;
			server = new ServerSocket(port);

			// 如果使用多线程，那就需要线程池，防止并发过高时创建过多线程耗尽资源
			ExecutorService threadPool = Executors.newFixedThreadPool(50);

			while (true) {
				// server将一直等待连接的到来
				Socket socket = server.accept();
				// socket.setSoTimeout(60000);

				Runnable runnable = () -> {
					InputStream in = null;
					OutputStream out = null;
					try {
						// 建立好连接后，从socket中获取输入流，并建立缓冲区进行读取
						in = socket.getInputStream();
						StringBuilder sb = new StringBuilder();
						byte[] bytes = new byte[1024];
						int len;
						// 只有当客户端关闭它的输出流的时候，服务端才能取得结尾的-1
						while ((len = in.read(bytes)) != -1) {
							// 注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
							sb.append(new String(bytes, 0, len, "UTF-8"));
						}
						log.info("get message from client: " + sb);

						// 给客户端返回消息
						out = socket.getOutputStream();
						out.write("Hello Client,I get the message.".getBytes("UTF-8"));
						out.flush();
					} catch (IOException e) {
						log.error("socket连接处理异常", e);
					} finally {
						// 关闭连接
						SocketUtil.close(in, out, socket);
					}
				};
				// 开启线程
				threadPool.submit(runnable);
			}
		} catch (IOException e) {
			log.error("ServerSocket异常", e);
		} finally {
			if (server != null) {
				try {
					server.close();
				} catch (IOException e) {
					log.error("server关闭失败", e);
				}
			}
		}
	}

}

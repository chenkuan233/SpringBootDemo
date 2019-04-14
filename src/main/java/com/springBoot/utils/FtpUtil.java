package com.springBoot.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chenkuan
 * @version v1.0
 * @desc ftp工具类
 * @date 2019/3/22 022 16:02
 */
@Slf4j
public class FtpUtil {

	private String server;
	private String username;
	private String password;

	private FTPClient ftp;

	private static final String JS_FILE_SEPARATOR = "/";

	/**
	 * true:binary模式下载 false:ascii模式下载
	 */
	private boolean binaryTransfer = true;

	/**
	 * @param server   ftp服务器地址
	 * @param username ftp服务器登陆用户
	 * @param password ftp用户密码
	 */
	public FtpUtil(String server, String username, String password) {
		this.server = server;
		this.username = username;
		this.password = password;
		this.ftp = new FTPClient();
	}

	/**
	 * 连接FTP服务器
	 */
	public void connect() {
		try {
			ftp.connect(server);
			// 连接后检测返回码来校验连接是否成功
			int reply = ftp.getReplyCode();

			if (FTPReply.isPositiveCompletion(reply)) {
				if (ftp.login(username, password)) {
					ftp.enterLocalPassiveMode(); // 设置为passive模式
				}
			} else {
				ftp.disconnect();
				log.error("连接FTP服务器失败：FTP服务器拒绝连接.");
			}
		} catch (IOException e) {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
					log.error("连接FTP服务器失败：关闭FTP服务器失败.", ioe);
				}
			}
			log.error("连接FTP服务器失败：无法连接服务器.", e);
		}
	}

	/**
	 * 列出远程目录下所有的文件
	 *
	 * @param remotePath 远程目录名
	 * @return 远程目录下所有文件名的列表，目录不存在或者目录下没有文件时返回0长度的数组
	 */
	public String[] listNames(String remotePath) {
		String[] fileNames = null;
		try {
			FTPFile[] remotefiles = ftp.listFiles(remotePath);
			fileNames = new String[remotefiles.length];
			for (int i = 0; i < remotefiles.length; i++) {
				fileNames[i] = remotefiles[i].getName();
			}
		} catch (IOException e) {
			log.error("无法列出FTP服务器文件列表.", e);
		}
		return fileNames;
	}

	/**
	 * 列出远程目录下所有的文件
	 *
	 * @param remotePath 远程目录名
	 * @return 远程目录下所有文件名的列表，目录不存在或者目录下没有文件时返回0长度的数组
	 */
	public String[] listNames2(String remotePath) {
		List<String> fileNameList = new ArrayList<>();
		String[] fileNames = null;
		try {
			FTPFile[] remotefiles = ftp.listFiles(remotePath);
			for (int i = 0; i < remotefiles.length; i++) {
				if (remotefiles[i].isFile()) {
					fileNameList.add(remotefiles[i].getName());
				}
			}
			fileNames = new String[fileNameList.size()];

			if (fileNameList.size() < 1) {
				return null;
			} else {
				for (int i = 0; i < fileNameList.size(); i++) {
					fileNames[i] = (String) fileNameList.get(i);
				}
			}
		} catch (IOException e) {
			log.error("无法列出FTP服务器文件列表.", e);
		}
		return fileNames;
	}


	// =======================上传文件========================

	/**
	 * 上传多个文件到远程目录中
	 *
	 * @param localPath  本地文件所在路径(路径：path)
	 * @param remotePath 目标文件所在路径(路径：path)
	 * @param fileNames  本地文件名数组(文件名：filename)数组
	 */
	public boolean[] putFilesToPath(String localPath, String remotePath, String[] fileNames) {
		String[] localFullPaths = new String[fileNames.length];
		String[] remoteFullPaths = new String[fileNames.length];

		for (int i = 0; i < fileNames.length; i++) {
			localFullPaths[i] = localPath + JS_FILE_SEPARATOR + fileNames[i];
			remoteFullPaths[i] = remotePath + JS_FILE_SEPARATOR + fileNames[i];
		}

		return this.putFilesToFiles(localFullPaths, remoteFullPaths);
	}

	/**
	 * 上传多个文件到远程路径中
	 *
	 * @param localFullPaths  本地文件名数组(完整全路径：path+filename)
	 * @param remoteFullPaths 目标路径数组(完整全路径：path+filename)
	 */
	public boolean[] putFilesToFiles(String[] localFullPaths, String[] remoteFullPaths) {
		// 初始化返回结构数组
		boolean[] result = new boolean[localFullPaths.length];
		for (int j = 0; j < result.length; j++) {
			result[j] = false;
		}
		for (int i = 0; i < localFullPaths.length; i++) {
			String localFullPath = localFullPaths[i];
			String remoteFullPath = remoteFullPaths[i];
			result[i] = this.putFileToFile(localFullPath, remoteFullPath);
		}
		return result;
	}

	/**
	 * 上传一个文件到远程指定路径中
	 *
	 * @param localFullPath  本地文件名(完整全路径：path+filename)
	 * @param remoteFullPath 目标路径(完整全路径：path+filename)
	 * @return 成功时，返回true，失败返回false
	 */
	public boolean putFileToFile(String localFullPath, String remoteFullPath) {
		return this.put(localFullPath, remoteFullPath);
	}

	/**
	 * 上传一个本地文件到远程指定文件
	 *
	 * @param localFullFile  本地文件名(包括完整路径)
	 * @param remoteFullFile 远程文件名(包括完整路径)
	 * @return 成功时，返回true，失败返回false
	 */
	private boolean put(String localFullFile, String remoteFullFile) {
		boolean result = false;
		InputStream input = null;
		try {
			// 设置文件传输类型
			if (binaryTransfer) {
				ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			} else {
				ftp.setFileType(FTPClient.ASCII_FILE_TYPE);
			}
			// 处理传输
			input = new FileInputStream(localFullFile); // 将本地文件读成数据流
			result = ftp.storeFile(remoteFullFile, input); // 储存本地文件到远程FTP目录下
			if (result) {
				log.info("上传文件" + localFullFile + "到FTP成功");
			}
		} catch (FileNotFoundException e) {
			log.error("本地文件" + localFullFile + "上传失败：无法找到本地文件", e);
		} catch (IOException e) {
			log.error("本地文件" + localFullFile + "上传失败：无法上传", e);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					log.error("关闭上传的本地文件" + localFullFile + "失败", e);
				}
			}
		}
		return result;
	}

	/**
	 * 上传一个本地文件到远程指定文件
	 *
	 * @param remoteFullFile  远程文件名(包括完整路径)
	 * @param fileInputStream 本地文件流
	 * @return 成功时，返回true，失败返回false
	 */
	public boolean putFileInputStream(InputStream fileInputStream, String remoteFullFile) {
		boolean result = false;
		try {
			// 设置文件传输类型
			if (binaryTransfer) {
				ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			} else {
				ftp.setFileType(FTPClient.ASCII_FILE_TYPE);
			}
			// 处理传输
			result = ftp.storeFile(remoteFullFile, fileInputStream); // 储存本地文件到远程FTP目录下
			if (result) {
				log.info("上传本地文件到FTP成功");
			}
		} catch (FileNotFoundException e) {
			log.error("本地文件上传失败：无法找到本地文件", e);
		} catch (IOException e) {
			log.error("本地文件上传失败：无法上传", e);
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					log.error("关闭上传的本地文件失败", e);
				}
			}
		}
		return result;
	}

	// =======================下载文件========================

	/**
	 * 下载多个文件到本地目录中
	 *
	 * @param localPath  本地文件所在路径(路径：path)
	 * @param remotePath 目标文件所在路径(路径：path)
	 * @param fileNames  本地文件名数组(文件名：filename)数组
	 */
	public boolean[] getFilesToPath(String localPath, String remotePath, String[] fileNames) {
		String[] localFullPaths = new String[fileNames.length];
		String[] remoteFullPaths = new String[fileNames.length];
		for (int i = 0; i < fileNames.length; i++) {
			localFullPaths[i] = localPath + JS_FILE_SEPARATOR + fileNames[i];
			remoteFullPaths[i] = remotePath + JS_FILE_SEPARATOR + fileNames[i];
		}
		return this.getFilesToFiles(localFullPaths, remoteFullPaths);
	}

	/**
	 * 下载多个文件到本地路径中
	 *
	 * @param localFullPaths  本地文件名数组(完整全路径：path+filename)
	 * @param remoteFullPaths 目标路径数组(完整全路径：path+filename)
	 * @return
	 */
	public boolean[] getFilesToFiles(String[] localFullPaths, String[] remoteFullPaths) {
		// 初始化返回结构数组
		boolean[] result = new boolean[localFullPaths.length];
		for (int j = 0; j < result.length; j++) {
			result[j] = false;
		}
		for (int i = 0; i < localFullPaths.length; i++) {
			String localFullPath = localFullPaths[i];
			String remoteFullPath = remoteFullPaths[i];
			result[i] = this.getFileToFile(localFullPath, remoteFullPath);
		}
		return result;
	}

	/**
	 * 下载一个文件到本地路径中
	 *
	 * @param localFullPath  localFullPath 本地文件名(完整全路径：path+filename)
	 * @param remoteFullPath 目标路径(完整全路径：path+filename)
	 * @return 成功时，返回true，失败返回false
	 */
	public boolean getFileToFile(String localFullPath, String remoteFullPath) {
		return this.get(localFullPath, remoteFullPath);
	}

	/**
	 * 下载一个远程文件到本地的指定文件
	 *
	 * @param remoteFullFile 远程文件名(包括完整路径)
	 * @param localFullFile  本地文件名(包括完整路径)
	 * @return 成功时，返回true，失败返回false
	 */
	public boolean get(String localFullFile, String remoteFullFile) {
		boolean result = false;
		OutputStream output = null;
		try {
			// 设置文件传输类型
			if (binaryTransfer) {
				ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			} else {
				ftp.setFileType(FTPClient.ASCII_FILE_TYPE);
			}
			// 处理传输
			output = new FileOutputStream(localFullFile); // 获得本地文件的输出流
			result = ftp.retrieveFile(remoteFullFile, output); // 获取文件
			if (result) {
				log.info("下载文件" + localFullFile + "成功");
			}
		} catch (IOException e) {
			log.error("无法从FTP服务器下载文件." + remoteFullFile, e);
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					log.error("关闭下载的本地文件输出流" + localFullFile + "失败", e);
				}
			}
		}
		return result;
	}

	// =======================FTP模式设置========================

	// binaryTransfer:true,binary模式下载;false,ascii模式下载
	public void setBinaryTransfer() {
		this.binaryTransfer = true;
	}

	public void setAsciiTransfer() {
		this.binaryTransfer = false;
	}

	/**
	 * 断开FTP服务器连接
	 */
	public void disconnect() {
		try {
			ftp.logout();
			if (ftp.isConnected()) {
				ftp.disconnect();
			}
		} catch (IOException e) {
			log.error("关闭连接FTP服务器失败.", e);
		}
	}

	/**
	 * 重命名文件
	 *
	 * @param oldName  原文件名
	 * @param fileName 新文件名
	 */
	public void rename(String oldName, String fileName) {
		try {
			ftp.rename(oldName, fileName);
		} catch (IOException e) {
			log.error("文件重命名失败.", e);
		}
	}

	/**
	 * 创建文件夹
	 */
	public void makeDir(String pathName) {
		try {
			ftp.makeDirectory(pathName);
		} catch (IOException e) {
			log.error("创建远程文件夹失败.", e);
		}
	}

	// 远程FTP服务器创建多级目录，创建目录失败或发生异常则返回false
	@SuppressWarnings("finally")
	public boolean makeDirs(String multiDirectory) {
		boolean bool = false;
		try {
			String[] dirs = multiDirectory.split("/");
			ftp.changeWorkingDirectory("/");
			//按顺序检查目录是否存在，不存在则创建目录
			for (int i = 1; i < dirs.length; i++) {
				if (!ftp.changeWorkingDirectory(dirs[i])) {
					if (ftp.makeDirectory(dirs[i])) {
						if (!ftp.changeWorkingDirectory(dirs[i])) {
							return false;
						}
					} else {
						return false;
					}
				}
			}
			bool = true;
		} catch (IOException e) {
			log.error("创建远程文件夹失败！.", e);
		} finally {
			return bool;
		}
	}

	/**
	 * 删除文件(删除文件夹及文件夹下文件)
	 */
	public boolean deleteDir(String pathName) {
		boolean delFlag = false;
		try {
			FTPFile[] ftpFiles = ftp.listFiles(pathName);
			if (ftpFiles != null && ftpFiles.length > 0) {
				// 删除文件
				for (FTPFile file : ftpFiles) {
					delFlag = ftp.deleteFile(pathName + "/" + file.getName());
				}
			}
			// 删除文件夹
			String parePathName = pathName.substring(0, pathName.lastIndexOf("/"));
			ftp.changeWorkingDirectory(parePathName);
			delFlag = ftp.removeDirectory(pathName);
		} catch (Exception e) {
			log.error("删除远程文件夹失败", e);
		}
		return delFlag;
	}

	/**
	 * 删除文件(删除文件)
	 *
	 * @param pathAllName 完整路径和文件名
	 */
	public boolean deleteFile(String pathAllName) {
		boolean delFlag = false;
		try {
			delFlag = ftp.deleteFile(pathAllName);
		} catch (Exception e) {
			log.error("删除远程文件夹失败.", e);
		}
		return delFlag;
	}

	/**
	 * 判断Ftp目录是否存在
	 */
	public boolean isDirExist(String dir) throws IOException {
		return ftp.changeWorkingDirectory(dir);
	}

}

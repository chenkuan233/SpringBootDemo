package com.springBoot.impl;

import com.springBoot.entity.FileInfo;
import com.springBoot.mapper.mapper.FileMapper;
import com.springBoot.service.FileService;
import com.springBoot.utils.DateUtil;
import com.springBoot.utils.FileUtil;
import com.springBoot.utils.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 文件操作
 * @date 2019/4/27 027 12:46
 */
@Slf4j
@Service("fileService")
public class FileServiceImpl implements FileService {

	@Autowired
	private FileMapper fileMapper;

	@Value("${upload.filePath}")
	private String filePath;

	/**
	 * 上传文件到指定路径
	 *
	 * @param file File固定参数不能更改
	 * @return 文件全路径
	 */
	@Override
	public Map<String, String> uploadFile(File file) throws FileNotFoundException {
		String fileName = file.getName();
		InputStream in = new FileInputStream(file);
		fileName = FileUtil.upload(in, filePath, fileName);
		if (StringUtils.isEmpty(fileName)) {
			log.error(fileName + "上传失败");
			return MessageUtil.message("-1", fileName + "上传失败");
		}
		return MessageUtil.message("0", filePath + fileName);
	}

	@Override
	public Map<String, String> saveFile(String filePath) {
		java.io.File fileIO = new java.io.File(filePath);
		if (fileIO.exists()) {
			String fileName = fileIO.getName();
			FileInfo file = new FileInfo(fileName, filePath, DateUtil.date(), DateUtil.time());
			fileMapper.saveFile(file);
			return MessageUtil.message("0", filePath + "上传成功");
		} else {
			return MessageUtil.message("-1", filePath + "不存在");
		}
	}

	@Override
	public List<FileInfo> findAllFile() {
		return fileMapper.findAllFile();
	}

	@Override
	public void deleteFile(FileInfo file) {
		fileMapper.deleteFile(file.getId());
		java.io.File fileIO = new java.io.File(file.getFilePath());
		if (fileIO.exists()) {
			fileIO.delete();
		}
	}

}

package com.springBoot.impl;

import com.springBoot.entity.File;
import com.springBoot.mapper.mapper.FileMapper;
import com.springBoot.service.FileService;
import com.springBoot.utils.DateUtil;
import com.springBoot.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 文件操作
 * @date 2019/4/27 027 12:46
 */
@Service("fileService")
public class FileServiceImpl implements FileService {

	@Autowired
	private FileMapper fileMapper;

	@Override
	public Map<String, String> saveFile(String filePath) {
		java.io.File fileIO = new java.io.File(filePath);
		if (fileIO.exists()) {
			String fileName = fileIO.getName();
			File file = new File(fileName, filePath, DateUtil.date(), DateUtil.time());
			fileMapper.saveFile(file);
			return MessageUtil.message("0", filePath + "上传成功");
		} else {
			return MessageUtil.message("-1", filePath + "不存在");
		}
	}

	@Override
	public List<File> findAllFile() {
		return fileMapper.findAllFile();
	}

	@Override
	public void deleteFile(File file) {
		fileMapper.deleteFile(file.getId());
		java.io.File fileIO = new java.io.File(file.getFilePath());
		if (fileIO.exists()) {
			fileIO.delete();
		}
	}

}

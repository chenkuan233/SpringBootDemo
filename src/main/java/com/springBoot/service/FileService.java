package com.springBoot.service;

import com.springBoot.entity.File;

import java.util.List;
import java.util.Map;

/**
 * @author chenkuan
 * @version v1.0
 * @desc
 * @date 2019/4/27 027 12:47
 */
public interface FileService {
	Map<String, String> saveFile(String filePath);

	List<File> findAllFile();

	void deleteFile(File file);
}

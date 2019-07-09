package com.springBoot.service;

import com.springBoot.entity.FileInfo;
import com.springBoot.entity.PersonalImage;
import com.springBoot.utils.Response;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

/**
 * @author chenkuan
 * @version v1.0
 * @desc
 * @date 2019/4/27 027 12:47
 */
public interface FileService {
	Map<String, String> uploadFile(File file) throws FileNotFoundException;

	Map<String, String> saveFile(String filePath);

	List<FileInfo> findAllFile();

	void deleteFile(FileInfo file);

	//个人中心 上传个人相册图片
	Response uploadPersonalImage(File file) throws FileNotFoundException;

	//查询登陆用户的个人相册
	List<PersonalImage> findPersonalImage();

	//根据文件name、url删除
	Response deletePersonalImage(PersonalImage personalImage);
}

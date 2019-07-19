package com.springBoot.impl;

import com.springBoot.entity.FileInfo;
import com.springBoot.entity.PersonalImage;
import com.springBoot.mapper.mapper.FileMapper;
import com.springBoot.mapper.mapper.PersonalImageMapper;
import com.springBoot.repository.PersonalImageRepository;
import com.springBoot.service.FileService;
import com.springBoot.utils.DateUtil;
import com.springBoot.utils.FileUtil;
import com.springBoot.utils.Response;
import com.springBoot.utils.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
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

	@Value("${upload.uploadPath}")
	private String uploadPath;

	@Autowired
	private PersonalImageRepository personalImageRepository;

	@Autowired
	private PersonalImageMapper personalImageMapper;

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
		fileName = FileUtil.upload(in, FileUtil.getUserDir() + uploadPath, fileName);
		if (StringUtils.isEmpty(fileName)) {
			log.error(fileName + "上传失败");
			return Response.message("-1", fileName + "上传失败");
		}
		return Response.message("0", uploadPath + fileName);
	}

	@Override
	public Map<String, String> saveFile(String filePath) {
		java.io.File fileIO = new java.io.File(filePath);
		if (fileIO.exists()) {
			String fileName = fileIO.getName();
			FileInfo file = new FileInfo(fileName, filePath, DateUtil.date(), DateUtil.time());
			fileMapper.saveFile(file);
			return Response.message("0", filePath + "上传成功");
		} else {
			return Response.message("-1", filePath + "不存在");
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

	//个人中心 上传个人相册图片
	@Override
	public Response uploadPersonalImage(File file) throws FileNotFoundException {
		String filePath = uploadPath + "personalImage/";
		String fileName = file.getName();
		fileName = FileUtil.upload(new FileInputStream(file), FileUtil.getUserDir() + filePath, fileName);
		if (StringUtils.isEmpty(fileName)) {
			log.error(fileName + "上传失败");
			return new Response("-1", fileName + "上传失败");
		}
		//保存到PersonalImage
		savePersonalImage(fileName, filePath + fileName, null);
		return new Response("0", filePath + fileName);
	}

	//保存到PersonalImage
	private void savePersonalImage(String fileName, String url, String remark) {
		String userName = SessionUtil.getUserName();
		PersonalImage personalImage = new PersonalImage();
		personalImage.setId(null);
		personalImage.setName(fileName);
		personalImage.setUrl(url);
		personalImage.setUserName(userName);
		personalImage.setUploadDate(DateUtil.date());
		personalImage.setUploadTime(DateUtil.time());
		personalImage.setRemark(remark);
		personalImageRepository.save(personalImage);
	}

	//查询登陆用户的个人相册
	@Override
	public List<PersonalImage> findPersonalImage() {
		return personalImageMapper.findByUserName(SessionUtil.getUserName());
	}

	//根据文件name、url删除
	@Override
	public Response deletePersonalImage(PersonalImage personalImage) {
		try {
			FileUtils.forceDelete(new File(FileUtil.getUserDir() + personalImage.getUrl()));
		} catch (FileNotFoundException e) {
			log.warn("[删除文件] -- " + personalImage.getUrl() + "不存在");
		} catch (IOException e) {
			return new Response("-1", "删除失败");
		}
		personalImageMapper.deleteByNameAndUrl(personalImage.getName(), personalImage.getUrl());
		return new Response("0", "删除成功");
	}
}

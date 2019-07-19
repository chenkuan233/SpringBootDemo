package com.springBoot.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 个人相册表
 * @date 2019/7/8 008 17:49
 */
@Getter
@Setter
@Entity
@Table(name = "t_personal_image")
public class PersonalImage implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	//文件名
	@Column(name = "name", length = 255)
	@NotNull
	private String name;

	//文件路径
	@Column(name = "url", length = 1000)
	@NotNull
	private String url;

	//用户名称
	@Column(name = "user_name", length = 50)
	@NotNull
	private String userName;

	//上传日期
	@Column(name = "upload_date", length = 8)
	@NotNull
	private String uploadDate;

	//上传时间
	@Column(name = "upload_time", length = 8)
	@NotNull
	private String uploadTime;

	//备注
	@Column(name = "remark", length = 255)
	private String remark;

	public PersonalImage() {
	}
}

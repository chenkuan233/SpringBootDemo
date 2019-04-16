package com.springBoot.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 用户表
 * @date 2019/1/17 017 11:45
 */
@Getter
@Setter
@Entity
@Table(name = "t_user", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_name"})})
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 数据库自增
	@Column(name = "id")
	private Long id;

	@Column(name = "user_name", nullable = false)
	@NotNull
	private String userName;

	@Column(name = "password", nullable = false)
	@NotNull
	private String password;

	@Column(name = "reg_date", nullable = false)
	@NotNull
	private String regDate;

	@Column(name = "reg_time", nullable = false)
	@NotNull
	private String regTime;

	// 加密盐值
	@Column(name = "credentials_salt")
	@NotNull
	private String credentialsSalt;

	public User() {
	}

	public User(@NotNull String userName) {
		this.userName = userName;
	}

}

package com.springBoot.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 用户表
 * @date 2019/1/17 017 11:45
 */
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public String getRegTime() {
		return regTime;
	}

	public void setRegTime(String regTime) {
		this.regTime = regTime;
	}

	public String getCredentialsSalt() {
		return credentialsSalt;
	}

	public void setCredentialsSalt(String credentialsSalt) {
		this.credentialsSalt = credentialsSalt;
	}
}

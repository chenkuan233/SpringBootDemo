package com.springBoot.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 用户与角色关系表
 * @date 2019/1/17 017 11:45
 */
@Setter
@Getter
@Entity
@Table(name = "t_user_role")
public class UserRole implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "user_id", nullable = false)
	@NotNull
	private Long userId;

	@Column(name = "role_id", nullable = false)
	@NotNull
	private Long roleId;
}

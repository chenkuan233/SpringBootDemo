package com.springBoot.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author chenkuan
 * @version v1.0
 * @desc
 * @date 2019/4/4 004 15:44
 */
@Setter
@Getter
@Entity
@Table(name = "t_man")
public class Man implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "name")
	@NotNull
	private String name;

	@Column(name = "nick")
	@NotNull
	private String nick;

	public Man() {
	}

	public Man(@NotNull String name, @NotNull String nick) {
		this.name = name;
		this.nick = nick;
	}
}

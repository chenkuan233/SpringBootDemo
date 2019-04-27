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
 * @date 2019/4/27 027 12:48
 */
@Getter
@Setter
@Entity
@Table(name = "t_file")
public class File implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@NotNull
	@Column(name = "file_name")
	private String fileName;

	@NotNull
	@Column(name = "file_path")
	private String filePath;

	@NotNull
	@Column(name = "date")
	private String date;

	@NotNull
	@Column(name = "time")
	private String time;

	public File() {
	}

	public File(@NotNull String fileName, @NotNull String filePath) {
		this.fileName = fileName;
		this.filePath = filePath;
	}

	public File(@NotNull String fileName, @NotNull String filePath, @NotNull String date, @NotNull String time) {
		this.fileName = fileName;
		this.filePath = filePath;
		this.date = date;
		this.time = time;
	}
}

package com.chny.sftp.pool.config;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class FileInfo {

	private String name;

	private Integer size;

	private Date updateTime;
	
	private byte[] content;
	
	private boolean isDirectory;

	public FileInfo(String name, Integer size, Date updateTime) {
		this.name = name;
		this.size = size;
		this.updateTime = updateTime;
		this.isDirectory = false;
	}
	
	public FileInfo(String name, Integer size, Date updateTime, boolean isDirectory) {
		this(name, size, updateTime);
		this.isDirectory = isDirectory;
	}

}

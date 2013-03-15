package com.genebio.nextprot.domain;

import java.util.Date;

public class Publication {

	private String author;
	private String description;
	private Date date = new Date();

	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

}

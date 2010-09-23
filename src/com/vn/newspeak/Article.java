package com.vn.newspeak;

import java.util.Date;

public class Article extends Object {

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getURL() {
		return itemURL;
	}
	public void setURL(String uRL) {
		itemURL = uRL;
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

	private String title;
	private String itemURL;
	private String description;
	private Date date;

}

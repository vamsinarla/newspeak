package com.vn.newspeak;

import java.io.Serializable;

public class Feed implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String getNewsPaper() {
		return newsPaper;
	}
	public void setNewsPaper(String newsPaper) {
		this.newsPaper = newsPaper;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getFeedURL() {
		return feedURL;
	}
	public void setFeedURL(String feedURL) {
		this.feedURL = feedURL;
	}
	public int getSubscribed() {
		return subscribed;
	}
	public void setSubscribed(int subscribed) {
		this.subscribed = subscribed;
	}
	
	private String newsPaper;
	private String category;
	private String feedURL;
	private int subscribed;
}

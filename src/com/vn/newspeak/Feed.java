package com.vn.newspeak;

public class Feed {
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

package com.vn.newspeak;

import java.util.ArrayList;

public class AdapterData {
	
	public ArrayList<String> getNewsPapers() {
		return newsPapers;
	}
	public void setNewsPapers(ArrayList<String> newsPapers) {
		this.newsPapers = newsPapers;
	}
	public ArrayList<ArrayList<Feed>> getCategories() {
		return categories;
	}
	public void setCategories(ArrayList<ArrayList<Feed>> categories) {
		this.categories = categories;
	}
	
	private ArrayList<String> newsPapers;
	private ArrayList<ArrayList<Feed>> categories;	
}

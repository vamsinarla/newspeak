package com.vn.newspeak;

import java.net.URL;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class ArticlesList extends ListActivity {
	
	private Feed feed;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
		try {
			Intent intent = this.getIntent();		
			Bundle bundle = intent.getExtras();
			
			// Get the data out of the intent
			feed = (Feed) bundle.getSerializable("com.vn.newspeak.ArticlesList");
			if (feed != null) {
				this.processFeed(feed);							
			}
			else {
				Log.e("ArticlesList::onCreate", "Feed is NULL!");
			}
		} catch (Exception exception) {
			Log.e("ArticlesList::onCreate", exception.getMessage());
		}
	}
	
	protected void processFeed(Feed feed) {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			FeedHandler feedHandler = new FeedHandler(this);
			URL url = new URL(feed.getFeedURL());
			
			parser.parse(url.openConnection().getInputStream(), feedHandler);
			List<Article> articles = feedHandler.getArticles();
			
		} catch (Exception exception) {
			Log.e("ArticlesList::processFeed", exception.getMessage());
		}
	}
}


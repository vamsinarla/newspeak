package com.vn.newspeak;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;

public class FeedHandler extends DefaultHandler {
	
	private List<Article> Articles;
	private Article currentArticle;
	private StringBuilder builder;
	
	private String titleTag;
	private String linkTag;
	private String pubDateTag;
	private String descriptionTag;
	private String itemTag;
	
	private Context appCtx;
	
	FeedHandler(Context ctx) {
		appCtx = ctx;
		
		titleTag = appCtx.getString(R.string.article_title);
		linkTag = appCtx.getString(R.string.article_link);
		pubDateTag = appCtx.getString(R.string.article_date);
		descriptionTag = appCtx.getString(R.string.article_description);
		itemTag = appCtx.getString(R.string.article_item);
	}
	
	
	public List<Article> getArticles(){
		return this.Articles;
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
		builder.append(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		super.endElement(uri, localName, name);
		
		if (this.currentArticle != null){
			if (localName.equalsIgnoreCase(titleTag)) {
				currentArticle.setTitle(builder.toString());
			} else if (localName.equalsIgnoreCase(linkTag)) {
				currentArticle.setURL(builder.toString());
			} else if (localName.equalsIgnoreCase(descriptionTag)) {
				currentArticle.setDescription(builder.toString());
			} else if (localName.equalsIgnoreCase(pubDateTag)) {
				// Wed, 22 Sep 2010 21:20:48 EDT
				/*SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
				try {
					currentArticle.setDate(dateFormat.parse(builder.toString()));
				} catch (ParseException e) {
					Log.e("Handler::endElement", e.getMessage());
				}*/
			} else if (localName.equalsIgnoreCase(itemTag)) {
				Articles.add(currentArticle);
			}
			
			builder.setLength(0);	
		}
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		Articles = new ArrayList<Article>();
		builder = new StringBuilder();
	}

	@Override
	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, name, attributes);
		if (localName.equalsIgnoreCase(itemTag)){
			this.currentArticle = new Article();
			builder.setLength(0);
		}
	}
}
	
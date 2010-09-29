package com.vn.newspeak;

import java.net.URL;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ArticlesList extends ListActivity implements TextToSpeech.OnInitListener {
	
	private Feed feed;
	private List<Article> articles;
	private TextToSpeech ttsEngine;
	
	private final int BUSY_SPINNER_DIALOG = 1; 
	static private int TTS_INSTALLED_CHECK_CODE = 1;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
		try {
			Intent intent = this.getIntent();		
			Bundle bundle = intent.getExtras();
			
			// Get the data out of the intent
			feed = (Feed) bundle.getSerializable("com.vn.newspeak.ArticlesList");
			if (feed != null) {
				showDialog(BUSY_SPINNER_DIALOG);
				
				setTitle(feed.getNewsPaper() + " > " + feed.getCategory());
				
				processFeed(feed);
				setListAdapter(new ArticlesListAdapter(this, articles));
				installAndStartTTSEngine();
			
				removeDialog(BUSY_SPINNER_DIALOG);
				
				
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
			articles = feedHandler.getArticles();
			
		} catch (Exception exception) {
			Log.e("ArticlesList::processFeed", exception.getMessage());
		}
	}
	
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		switch (id) {
			case BUSY_SPINNER_DIALOG:
				dialog = ProgressDialog.show(this, "", "Loading ..."); 
				break;
			default:
				dialog = null;
				break;
		}
		
		return dialog;
	}
	
	protected void installAndStartTTSEngine() {
		Intent checkIntent = new Intent();
		
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent, TTS_INSTALLED_CHECK_CODE);		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == TTS_INSTALLED_CHECK_CODE) {
	        if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
	            // success, create the TTS instance
	            ttsEngine = new TextToSpeech(this, this);
	            
	        } else {
	            // missing data, install it
	            Intent installIntent = new Intent();
	            installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
	            
	            startActivity(installIntent);
	        }
	    }
	}
	
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
	           ttsEngine.setLanguage(Locale.US);
	            
	           ttsEngine.speak("Hello and welcome to this application ", TextToSpeech.QUEUE_ADD, null);	      	 
		}
			
	}
	
	@Override
	protected void onDestroy() {
		ttsEngine.stop();
		ttsEngine.shutdown();
		
		super.onDestroy();
	}
	
	private class ArticlesListAdapter extends BaseAdapter {

		private Context mContext;
		private List<Article> mArticles;
		
		public ArticlesListAdapter(Context context, List<Article> articles) {
			mContext = context;
			mArticles = articles; 
		}
		
		@Override
		public int getCount() {
			return mArticles.size();
		}

		@Override
		public Object getItem(int position) {
			return mArticles.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			TextView articleView;
			
			if (convertView == null) {
				articleView = new TextView(mContext);
				
				AbsListView.LayoutParams params = new AbsListView.LayoutParams(
		                ViewGroup.LayoutParams.FILL_PARENT, 64);
	
				articleView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
				articleView.setPadding(10, 0, 0, 0);
		        
				articleView.setLayoutParams(params);
			} else {
				articleView = (TextView) convertView;
			}
			
			Article article = (Article) getItem(position);
			articleView.setText(article.getTitle());
			articleView.setOnClickListener(new OnItemClickListener(article, mContext));
			
			return articleView;
		}
		
		private class OnItemClickListener implements View.OnClickListener {

			private Article article;
			private Context appCtx;
			
			OnItemClickListener(Article article, Context context) {
				this.article = article;
				this.appCtx = context;
			}
			
			@Override
			public void onClick(View v) {
				Intent articleDisplayIntent = new Intent();
				
				articleDisplayIntent.setClass(appCtx, ArticleDisplay.class);
				articleDisplayIntent.putExtra("com.vn.newspeak.articleURL", article.getURL());
			
				// appCtx.startActivity(articleDisplayIntent);
			}
			
		}
	}
}


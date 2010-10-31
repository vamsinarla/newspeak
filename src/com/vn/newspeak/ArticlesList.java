package com.vn.newspeak;

import java.net.URL;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.ListView;

public class ArticlesList extends Activity implements TextToSpeech.OnInitListener {
	
	private Feed feed;
	private List<Article> articles;
	private TextToSpeech ttsEngine;
	
	private final int BUSY_SPINNER_DIALOG = 1; 
	static private int TTS_INSTALLED_CHECK_CODE = 1;
	
	private Article currentArticle;
	private int currentArticleIndex;
	
	private ArticleSpeakService mSpeakService;
	
	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			mSpeakService = ((ArticleSpeakService.ArticleSpeakBinder)service).getService();
		}

		public void onServiceDisconnected(ComponentName name) {
			mSpeakService = null;
		}
		
		
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
		setContentView(R.layout.article_list);
		
		try {
			Intent intent = this.getIntent();		
			Bundle bundle = intent.getExtras();
			
			// Get the data out of the intent
			feed = (Feed) bundle.getSerializable("com.vn.newspeak.ArticlesList");
			if (feed != null) {
				showDialog(BUSY_SPINNER_DIALOG);
				
				setTitle(feed.getNewsPaper() + " > " + feed.getCategory());
				
				// Read articles and display as a list
				readFeedAndFetchArticles(feed);
				ListView articleList = (ListView) this.findViewById(R.id.articleList);
				articleList.setAdapter(new ArticleListAdapter(this, articles));
				
				currentArticleIndex = 0;
				currentArticle = articles.get(currentArticleIndex);
				
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
	
	protected void readFeedAndFetchArticles(Feed feed) {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		articles = null;
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
	
	@Override
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
	            
	           ttsEngine.speak("Now reading " + this.feed.getNewsPaper() + "'s " + this.feed.getCategory(), 
	        		   TextToSpeech.QUEUE_ADD, null);	      	 
		}
			
	}
	
	@Override
	protected void onDestroy() {
		ttsEngine.stop();
		ttsEngine.shutdown();
		
		super.onDestroy();
	}
	
}

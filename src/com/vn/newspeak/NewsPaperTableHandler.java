package com.vn.newspeak;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

// Class to create the Table in the DB
public class NewsPaperTableHandler {
	
	private final String databaseName;
	private final String tableName;
	
	private SQLiteDatabase db;
	private Context appCtx;
	
	private final String CREATE_TABLE_FEEDS = "create table " +
			"FeedsTable" + 
			"(feed_id integer primary key autoincrement, " +
			"NewsPaperName text not null," +
			"Category text not null," +
			"FeedURL text not null," +
			"Subscribed integer not null);";
	
	NewsPaperTableHandler(Context ctx) {
		appCtx = ctx;
		databaseName = appCtx.getString(R.string.databaseName);
		tableName = appCtx.getString(R.string.feedsTable);
	}
	
	// For now I'll have this create the DB as well
	public void createTable() {
		
		try {
			db = appCtx.openOrCreateDatabase(databaseName, Context.MODE_PRIVATE, null);
			db.execSQL(CREATE_TABLE_FEEDS);		
			Log.d("Path for the DB", db.getPath());
			db.close();
		} catch (SQLiteException exception) {
			Log.e("NewsPaperTableHandler::createTable", "Could not create the DB or the tables.");
		}
	}
	
	public void populateTable() {
		ContentValues values = new ContentValues();
		values.put("NewsPaperName", "Wall Street Journal");
		values.put("Category", "US Business");
		values.put("FeedURL", "http://online.wsj.com/xml/rss/3_7014.xml");
		values.put("Subscribed", 0);
		
		try {
			db = appCtx.openOrCreateDatabase(databaseName, Context.MODE_PRIVATE, null);
			db.insert(tableName, null, values);
			db.close();
		} catch(SQLiteException exception) {
			Log.e("NewsPaperTableHandler::populateTable", "Could not populate the table");
		}
	}
}

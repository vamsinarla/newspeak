package com.vn.newspeak;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
	
	public ArrayList<String> getNewsPaperNames() {
		Cursor result;
		ArrayList<String> newsPaperNames = new ArrayList<String>(); 
		
		try {
			db = appCtx.openOrCreateDatabase(databaseName, Context.MODE_PRIVATE, null);
			result = db.query(true,
					tableName,
					new String[] { "NewsPaperName" },
					null,
					null,
					null,
					null,
					null,
					null);
						
			result.moveToFirst();
			
			int newsPaperNameIndex = result.getColumnIndex("NewsPaperName");			
			while(!result.isAfterLast()) {
				newsPaperNames.add(result.getString(newsPaperNameIndex));
				result.moveToNext();
			}
			
			result.close();
			db.close();
		} catch (SQLiteException exception) {
			Log.e("NewsPaperTableHandler::getNewsPaperNames", "Failed to get newspaper names");
		}
		catch (Exception exception) {
			Log.e("NewsPaperTableHandler::getNewsPaperNames", exception.getMessage());
		}
		
		return newsPaperNames;
	}
	
	public ArrayList<ArrayList<Feed>> getCategoriesForNewsPapers(ArrayList<String> newsPaperNames) {
		Cursor result = null;
		ArrayList<ArrayList<Feed>> categories = new ArrayList<ArrayList<Feed>>();
		ArrayList<Feed> category = new ArrayList<Feed>();
		
		try {
			db = appCtx.openOrCreateDatabase(databaseName, Context.MODE_PRIVATE, null);
			
			for (String newsPaper : newsPaperNames) {
				result = db.query(tableName,
							new String[] { "Category", "Subscribed" },
							"NewsPaperName=?",
							new String [] { newsPaper },
							null,
							null,
							null
							);
				result.moveToFirst();
				Feed feed = new Feed();
				
				int categoryIndex = result.getColumnIndex("Category");
				int subscribedIndex = result.getColumnIndex("Subscribed");
				
				// Add to corresponding array list
				while (!result.isAfterLast()) {
					feed.setCategory(result.getString(categoryIndex));
					feed.setSubscribed(result.getInt(subscribedIndex));
					
					category.add(feed);
					result.moveToNext();
				}
				categories.add(category);
			}
			result.close();
			db.close();
			
		} catch (SQLiteException exception) {
			Log.e("NewsPaperTableHandler::getCategoriesForNewsPapers", "Categories could not be got " + exception.getMessage());
		}
		catch (Exception exception) {
			Log.e("NewsPaperTableHandler::getCategoriesForNewsPapers", exception.getMessage());
		}
		
		return categories;
	}
	
	public void prepareDataForAdapter(NewsPaperListAdapter adapter) {
		ArrayList<String> newsPapers = getNewsPaperNames();
		ArrayList<ArrayList<Feed>> categories = getCategoriesForNewsPapers(newsPapers);
		
		adapter.setData(newsPapers, categories);
	}
	
}

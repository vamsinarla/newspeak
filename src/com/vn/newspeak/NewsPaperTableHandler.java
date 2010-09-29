package com.vn.newspeak;

import java.util.ArrayList;

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
		
		try {
			db = appCtx.openOrCreateDatabase(databaseName, Context.MODE_PRIVATE, null);

			values.put("NewsPaperName", "Wall Street Journal");
			values.put("Category", "US Business");
			values.put("FeedURL", "http://online.wsj.com/xml/rss/3_7014.xml");
			values.put("Subscribed", 0);
			db.insert(tableName, null, values);

			values.put("NewsPaperName", "New York Times");
			values.put("Category", "Home Page US");
			values.put("FeedURL", "http://feeds.nytimes.com/nyt/rss/HomePage");
			values.put("Subscribed", 0);
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
							new String[] { "Category", "FeedURL", "Subscribed" },
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
				int feedUrlIndex = result.getColumnIndex("FeedURL");
				// Add to corresponding array list
				while (!result.isAfterLast()) {
					feed.setNewsPaper(newsPaper);
					feed.setCategory(result.getString(categoryIndex));
					feed.setFeedURL(result.getString(feedUrlIndex));
					feed.setSubscribed(result.getInt(subscribedIndex));
					
					category.add(feed);
					result.moveToNext();
				}
				categories.add(category);
				category = new ArrayList<Feed>();
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
	
	public AdapterData prepareDataForAdapter() {
		ArrayList<String> newsPapers = getNewsPaperNames();
		
		AdapterData data = new AdapterData();
		data.setNewsPapers(newsPapers);
		data.setCategories(getCategoriesForNewsPapers(newsPapers));
		
		return data;
	}
	
	public boolean updateQuery(ContentValues values, String whereClause, String[] whereArgs) {
		int numRows = 0;
		try {
			db = appCtx.openOrCreateDatabase(databaseName, Context.MODE_PRIVATE, null);
			numRows = db.update(tableName, values, whereClause, whereArgs);
			db.close();
		} catch (SQLiteException exception) {
			Log.e("NewsPaperTableHandler::updateQuery", "Update checkbox failed" + exception.getMessage());
		}
		return numRows == 1 ? true : false;
	}
	
	// DEBUG ONLY :: Remove in release
	public void deleteTable() {
		
		try {
			db = appCtx.openOrCreateDatabase(databaseName, Context.MODE_PRIVATE, null);
			db.delete(tableName, null, null);
			db.close();
		} catch (SQLiteException exception) {
			Log.e("deleteTable", exception.getMessage());
		}
	}
 	
}

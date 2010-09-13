package com.vn.newspeak;

import android.app.ExpandableListActivity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

public class NewsPapers extends ExpandableListActivity {
    
	private static final String firstRun = "FIRST_RUN";
	
	NewsPaperListAdapter newsPaperListAdapter;
	ExpandableListView newsPaperListView;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        NewsPaperTableHandler newsPaperTable = new NewsPaperTableHandler(this);
        newsPaperListAdapter = new NewsPaperListAdapter(this);
        
        // Check if this is the first time the app is being run
        if (isFirstRun()) {
        	// This is the first run so create the DB and tables
        	newsPaperTable.createTable();
        	newsPaperTable.populateTable();
        	Log.d("NewsPapers::onCreate", "App initialization done");
        }
        
        newsPaperTable.prepareDataForAdapter(newsPaperListAdapter);
        setListAdapter(newsPaperListAdapter);
        
        newsPaperListView = this.getExpandableListView();
        ExpandableListView.OnChildClickListener onChildClickListener = new ExpandableListView.OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				Log.d("childlistener", "clicked on " + Integer.toString(groupPosition) + Integer.toString(childPosition));
				return true;
			}
		};
		
        newsPaperListView.setOnChildClickListener(onChildClickListener);
    }
    
    private boolean isFirstRun() {
    	
    	// Read from pref. file
    	SharedPreferences settings = this.getPreferences(MODE_PRIVATE);
    	boolean result = settings.getBoolean(firstRun, false);
    	
    	if (!result) {
    		SharedPreferences.Editor editor = settings.edit();
    		editor.putBoolean(firstRun, true);
    		editor.commit();
    	}
    	
    	return !result;
    }
}
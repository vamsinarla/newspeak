package com.vn.newspeak;

import android.app.ExpandableListActivity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
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
    	
        // Check if this is the first time the app is being run
        if (isFirstRun()) {
        	// This is the first run so create the DB and tables
        	newsPaperTable.createTable();
        	newsPaperTable.populateTable();
        	Log.d("NewsPapers::onCreate", "App initialization done");
        }
        else {
        	// Populate the list adapter to show entries
        	// Cursor cursor = newsPaperTable.prepareDataForListAdapter();
        }
        newsPaperListView = (ExpandableListView) this.findViewById(R.layout.main_parent);
        newsPaperListView.setAdapter(newsPaperListAdapter);
        // setListAdapter(newsPaperListAdapter);
        this.setContentView(newsPaperListView);
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
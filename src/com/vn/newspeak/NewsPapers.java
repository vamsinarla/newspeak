package com.vn.newspeak;

import android.app.ExpandableListActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListAdapter;

public class NewsPapers extends ExpandableListActivity {
    
	private static final String firstRun = "FIRST_RUN";
	
	ExpandableListAdapter mNewsPaperListAdapter;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Check if this is the first time the app is being run
        if (isFirstRun()) {
        	// This is the first run so create the DB and tables
        	NewsPaperTableHandler newsPaperTable = new NewsPaperTableHandler(this);
        	newsPaperTable.createTable();
        	newsPaperTable.populateTable();
        	Log.d("NewsPapers::onCreate", "App initialization done");
        }
        else {
        	// Populate the list adapter to show entries
        	
        }
        
        // mNewsPaperListAdapter = new NewsPaperListAdapater(this);
        // setListAdapter(mNewsPaperListAdapter);
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
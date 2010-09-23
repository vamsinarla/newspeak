package com.vn.newspeak;

import android.app.ExpandableListActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

public class NewsPapers extends ExpandableListActivity {
    
	private static final String firstRun = "FIRST_RUN";
	
	private NewsPaperListAdapter newsPaperListAdapter;
	private NewsPaperTableHandler newsPaperTable;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        newsPaperTable = new NewsPaperTableHandler(this);
        newsPaperListAdapter = new NewsPaperListAdapter(this);
        
        // Check if this is the first time the app is being run
        if (isFirstRun()) {
        	// This is the first run so create the DB and tables
        	newsPaperTable.createTable();
        	newsPaperTable.populateTable();
        	Log.d("NewsPapers::onCreate", "App initialization done");
        }
        
        AdapterData data = newsPaperTable.prepareDataForAdapter();
        newsPaperListAdapter.setData(data);
        newsPaperListAdapter.setTableHandler(newsPaperTable);
        setListAdapter(newsPaperListAdapter);
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
    
    public NewsPaperTableHandler getNewsPaperTableHandler() {
    	return this.newsPaperTable;
    }
    
}
package com.vn.newspeak;

import java.util.ArrayList;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsPaperListAdapter extends BaseExpandableListAdapter {
	
	private Context appCtx;
	
	private ArrayList<String> newsPapers;
	private ArrayList<ArrayList<Feed>> categories;
	private ArrayList<Integer> newsPaperIconIds;
	
	public NewsPaperListAdapter(Context ctx) {
		appCtx = ctx;
		
		// Set the image resource IDs for the Newspaper icons
		newsPaperIconIds = new ArrayList<Integer>();
		newsPaperIconIds.add(R.drawable.the_new_york_times_logo);
		newsPaperIconIds.add(R.drawable.wall_street_journal_logo);
	}
	
	public boolean hasStableIds() {
        return true;
    }

	public Object getChild(int groupPosition, int childPosition) {
		Feed feed = categories.get(groupPosition).get(childPosition);
		return feed;
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}
	
	public TextView getGenericView() {
        // Layout parameters for the ExpandableListView
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT, 64);

        TextView textView = new TextView(appCtx);
        textView.setLayoutParams(lp);
        // Center the text vertically
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        // Set the text starting position
        textView.setPadding(36, 0, 0, 0);
        return textView;
    }
	
	/*public CheckBox getGenericView2() {
        // Layout parameters for the ExpandableListView
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT, 64);

        CheckBox checkBox = new CheckBox(appCtx);
        checkBox.setLayoutParams(lp);
        // Center the text vertically
        checkBox.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        // Set the text starting position
        checkBox.setPadding(36, 0, 36, 0);
        return checkBox;
    }
	
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
	    CheckBox checkBox = getGenericView2();
        
	    Feed feed = categories.get(groupPosition).get(childPosition);
	    checkBox.setText(feed.getCategory());
	    checkBox.setChecked(feed.getSubscribed() == 0 ? false : true);
	    
	    // Insert a tag that can help us identify the group and the child as "group,child"
	    String tag = Integer.toString(groupPosition) + "," + Integer.toString(childPosition);
	    checkBox.setTag(tag);
	    
	    checkBox.setOnClickListener(this);
	    return checkBox;
    } */
	
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
				
		TextView textView = getGenericView();
		
		Feed feed = categories.get(groupPosition).get(childPosition);
		
		textView.setText(feed.getCategory());
		textView.setOnClickListener(new OnChildClickListener(appCtx, feed));
		
		return textView;
	}
	
	public int getChildrenCount(int groupPosition) {
		return categories.get(groupPosition).size();
	}

	public Object getGroup(int groupPosition) {
		return newsPapers.get(groupPosition);
	}

	public int getGroupCount() {
		return newsPapers.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
    
		/*TextView textView = getGenericView();
		textView.setText(getGroup(groupPosition).toString());
    
		return textView;*/
		
		// Instantiate an ImageView and define its properties
	    ImageView i = new ImageView(appCtx);
	    i.setImageResource(newsPaperIconIds.get(groupPosition));
	    i.setAdjustViewBounds(true); // set the ImageView bounds to match the Drawable's dimensions
	    i.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT ,
	    		AbsListView.LayoutParams.FILL_PARENT));
	    
	    i.setPadding(36, 0, 0, 0);
	    
		return i;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	public void setData(AdapterData data) {
		this.newsPapers = data.getNewsPapers();
		this.categories = data.getCategories();
	}
	
	/* @Override
	public void onClick(View v) {
		// Probably should add a check to see if its any other control
		CheckBox checkBox = (CheckBox) v;
		String tag = (String) checkBox.getTag();
		
		try {
			// Extract the checkBox group and child position and also its state
			boolean newState = checkBox.isChecked();
			String[] tagString = tag.split(",");
			int group = Integer.parseInt(tagString[0]);
			int child = Integer.parseInt(tagString[1]);
			
			// Construct the query and update the database
			
			ContentValues newValues = new ContentValues();
			newValues.put("Subscribed", newState);
			
			String whereClause = "NewsPaperName=? AND Category=?";
			
			String[] whereArgs = { null, null };
			whereArgs[0] = (String) getGroup(group);
			Feed feed = (Feed) getChild(group, child);
			whereArgs[1] = feed.getCategory();
			
			// Update the Feed object resident in memory and the DB
			feed.setSubscribed(newState == false ? 0 : 1);
			newsPaperTable.updateQuery(newValues, whereClause, whereArgs);
			
			// For now we have the click here showing the list activity ahead. 
			// Prepare an intent and call the list activity
			Intent listArticlesInFeed = new Intent();
			listArticlesInFeed.setClass(appCtx, ArticlesList.class);
			
			listArticlesInFeed.putExtra("com.vn.newspeak.ArticlesList", feed);
			appCtx.startActivity(listArticlesInFeed);

		} catch (ActivityNotFoundException exception) {
			Log.e("NewsPaperListAdapter::onClick", exception.getMessage());
		}
		catch (Exception exception) {
			Log.e("NewsPaperListAdapter::onClick", exception.getMessage());
		}
	} */

	public void setTableHandler(NewsPaperTableHandler newsPaperTable) {
	}
	
	private class OnChildClickListener implements View.OnClickListener {

		Feed mFeed;
		
		OnChildClickListener(Context appCtx, Feed feed) {
			this.mFeed = feed;
		}
		
		public void onClick(View v) {

			try {
				// For now we have the click here showing the list activity ahead. 
				// Prepare an intent and call the list activity
				Intent listArticlesInFeed = new Intent();
				listArticlesInFeed.setClass(appCtx, ArticlesList.class);
				
				listArticlesInFeed.putExtra("com.vn.newspeak.ArticlesList", mFeed);
				appCtx.startActivity(listArticlesInFeed);

			} catch (ActivityNotFoundException exception) {
			Log.e("NewsPaperListAdapter::onClick", exception.getMessage());
			}
		}
	}
}

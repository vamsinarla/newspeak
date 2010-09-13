package com.vn.newspeak;

import java.util.ArrayList;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class NewsPaperListAdapter extends BaseExpandableListAdapter {
	
	private Context appCtx;
	
	private ArrayList<String> newsPapers;
	private ArrayList<ArrayList<Feed>> categories;
    
	public NewsPaperListAdapter(Context ctx) {
		appCtx = ctx;
	}
	
	public boolean hasStableIds() {
        return true;
    }

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		Feed feed = categories.get(groupPosition).get(childPosition);
		return feed;
	}

	@Override
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
	
	public CheckBox getGenericView2() {
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
        return checkBox;
    }

	@Override
	public int getChildrenCount(int groupPosition) {
		return categories.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return newsPapers.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return newsPapers.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
        TextView textView = getGenericView();
        textView.setText(getGroup(groupPosition).toString());
        return textView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	public void setData(ArrayList<String> newsPapers, ArrayList<ArrayList<Feed>> categories) {
		this.newsPapers = newsPapers;
		this.categories = categories;
	}

}

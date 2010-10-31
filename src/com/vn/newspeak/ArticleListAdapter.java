package com.vn.newspeak;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

class ArticleListAdapter extends BaseAdapter {
	Context mContext;
	private LayoutInflater mInflater;
	List<Article> mArticles;
	
	class ViewHolder {
        TextView text;
    }
	
	ArticleListAdapter(Context context, List<Article> articles)
	{
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mArticles = articles;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.article_list_item, null);
			
			holder = new ViewHolder();
			holder.text = (TextView) convertView.findViewById(R.id.text);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.text.setText(mArticles.get(position).getTitle());
		
		return convertView;
	}

	public int getCount() {
		return mArticles.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}
		
}

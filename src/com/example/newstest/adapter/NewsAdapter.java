package com.example.newstest.adapter;

import java.util.List;

import com.example.newstest.R;
import com.example.newstest.model.News;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NewsAdapter extends ArrayAdapter<News> {

	private LayoutInflater mInflater;
	private int mResource;

	/**
	 * 
	 * @param context
	 *            上下文
	 * @param resource
	 *            news_item
	 * @param objects
	 *            List<News>
	 */
	public NewsAdapter(Context context, int resource, List<News> objects) {
		super(context, resource, objects);
		this.mInflater = LayoutInflater.from(context);
		this.mResource = resource;
	}

	static class ViewHolder {
		TextView newsTitle, newsSource, newsTime;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;

		if (convertView == null) {
			viewHolder = new ViewHolder();

			convertView = mInflater.inflate(mResource, null);
			// 获得Item中的TextView控件
			viewHolder.newsTitle = (TextView) convertView
					.findViewById(R.id.newsTitle);
			viewHolder.newsSource = (TextView) convertView
					.findViewById(R.id.newsSource);
			viewHolder.newsTime = (TextView) convertView
					.findViewById(R.id.newsTime);
			convertView.setTag(viewHolder);

			// view = (LinearLayout) mInflater.inflate(mResource, null);
		} else {
			// view = (LinearLayout) convertView;
			viewHolder = (ViewHolder) convertView.getTag();
		}

		// 获得数据绑定到Item上的一个对象
		News news = getItem(position);

		// 设置值
		viewHolder.newsTitle.setText(news.getNewsTitle());
		viewHolder.newsSource.setText(news.getNewsSource());
		viewHolder.newsTime.setText(news.getNewsTime());

		return convertView;
	}

}

package com.supinfo.geekquote.model;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class QuoteListAdapter extends BaseAdapter {

	private Context context;
	private List<Quote> quotes;
	
	public QuoteListAdapter(Context context, List<Quote> quotes) {
		super();
		this.context = context;
		this.quotes = quotes;
	}

	public int getCount() {
		return this.quotes.size();
	}

	public Quote getItem(int position) {
		return quotes.get(position);
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
	
		TextView view = new TextView(context);
		if(position % 2 ==  0)
		{
			view.setBackgroundColor(parent.getResources().getColor(android.R.color.darker_gray));
			view.setTextColor(parent.getResources().getColor(android.R.color.black));
		}
		else
			view.setBackgroundColor(parent.getResources().getColor(android.R.color.secondary_text_light));
		view.setTextSize(20);
		view.setText(getItem(position).toString());
		return view;
	}

	

}

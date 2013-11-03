package com.wheaton.app.List;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class TwoTextArrayAdapter extends ArrayAdapter<Item> {

	public enum RowType {
		LIST_ITEM, HEADER_ITEM
	}
	
	public TwoTextArrayAdapter(Context context, List<Item> items) {
		super(context, 0, items);
		mContext = context;
		mSize = -1;
		mResults = items;
	}

	public TwoTextArrayAdapter(Context context, List<Item> items, int size) {
		super(context, 0, items);
		mContext = context;
		mSize = size;
		mResults = items;
	}
	
	@Override
	public int getCount() {
		if(mResults.size() <= 0)
			return 0;
		if(mSize < 0)
			return mResults.size();
		else
			return mSize;
	}

	@Override
	public int getViewTypeCount() {
		return RowType.values().length;
	}

	@Override
	public int getItemViewType(int position) {
		return getItem(position).getViewType();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getItem(position).getView(mContext, convertView, parent);
	}
	private Context mContext;
	private int mSize;
	private List<Item> mResults;
}
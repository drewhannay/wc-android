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
}
package com.wheaton.app;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HeaderList extends BaseAdapter {
	
    
    private static final Integer LIST_HEADER = 0;
    private static final Integer LIST_ITEM = 1;
    
    public HeaderList(Context context, ArrayList listItems, HashMap<Integer, String> headerItems) {
        mContext = context;
        mListItems = listItems;
        mHeaderItems = headerItems;
    }

    @Override
    public int getCount() {
        return mListItems.size();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String headerText = getHeader(position);
        if(headerText != null) {

            View item = convertView;
            if(convertView == null || convertView.getTag() == LIST_ITEM) {
                item = LayoutInflater.from(mContext).inflate(
                        R.layout.header_layout, parent, false);
                item.setTag(LIST_HEADER);
            }

            TextView headerTextView = (TextView)item.findViewById(R.id.list_hdr);
            headerTextView.setText(headerText);
            return item;
        }

        View item = convertView;
        if(convertView == null || convertView.getTag() == LIST_HEADER) {
            item = LayoutInflater.from(mContext).inflate(
                    R.layout.list_layout, parent, false);
            item.setTag(LIST_ITEM);
        }

        TextView header = (TextView)item.findViewById(R.id.item_header);
        header.setText((String) mListItems.get(position % mListItems.size()));

//        TextView subtext = (TextView)item.findViewById(R.id.item_subtext);
//        subtext.setText(SUBTEXTS[position % SUBTEXTS.length]);

        //Set last divider in a sublist invisible
        View divider = item.findViewById(R.id.item_separator);
//        if(position == HDR_POS2 -1) {
//            divider.setVisibility(View.INVISIBLE);
//        }


        return item;
    }

    private String getHeader(int position) {

        if(mHeaderItems.get(position) != null) {
            return mHeaderItems.get(position);
        }

        return null;
    }

    private final Context mContext;
    private final ArrayList mListItems;
    private final HashMap<Integer, String> mHeaderItems;
}
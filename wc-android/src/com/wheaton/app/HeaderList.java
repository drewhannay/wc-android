package com.wheaton.app;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HeaderList extends BaseAdapter {
	
    
    private static final Integer LIST_HEADER = 0;
    private static final Integer LIST_ITEM = 1;
    
    public HeaderList(Context context, int id, ArrayList<HashMap<String, String>> listItems, HashMap<Integer, String> headerItems) {
        mContext = context;
        mListItems = listItems;
        mRId = id;
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
            item = LayoutInflater.from(mContext).inflate(mRId, parent, false);
            item.setTag(LIST_ITEM);
        }
        
        Iterator<Entry<String, String>> it = mListItems.get(position % mListItems.size()).entrySet().iterator();
        
        while (it.hasNext()) {
            Map.Entry<String, String> pairs = (Map.Entry<String, String>)it.next();         
            int id = getResId(pairs.getKey(), mContext, R.id.class);
            
            if(id > 0) {
            	TextView view = (TextView)item.findViewById(id);
            	view.setText(pairs.getValue());
            }
        }

        //Set last divider in a sublist invisible
        View divider = item.findViewById(R.id.item_separator);
        if(position == mHeaderItems.size()-1) {
            divider.setVisibility(View.INVISIBLE);
        }


        return item;
    }

    private String getHeader(int position) {

        if(mHeaderItems.get(position) != null) {
            return mHeaderItems.get(position);
        }

        return null;
    }
    
    public static int getResId(String variableName, Context context, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            //e.printStackTrace();
            return -1;
        } 
    }

    private final Context mContext;
    private final int mRId;
    private final ArrayList<HashMap<String, String>> mListItems;
    private final HashMap<Integer, String> mHeaderItems;
}
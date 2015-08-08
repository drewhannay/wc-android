package com.wheaton.app.List;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.wheaton.app.R;
import com.wheaton.app.List.TwoTextArrayAdapter.RowType;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ListItem implements Item {
	
	public ListItem(HashMap<String, String> item, int id) {
		mItem = item;
		mRId = id;
	}
	
	@Override
	public int getViewType() {
		return RowType.LIST_ITEM.ordinal();
	}

	@Override
	public View getView(Context context, View convertView, ViewGroup parent) {
		View view;
		
		if (convertView == null) {
			view = (View) LayoutInflater.from(context).inflate(mRId, null);
			// Do some initialization
		} else {
			view = convertView;
		}
		
		Iterator<Entry<String, String>> it = mItem.entrySet().iterator();
        
        while (it.hasNext()) {
            Map.Entry<String, String> pairs = (Map.Entry<String, String>)it.next();         
            int id = getResId(pairs.getKey(), R.id.class);
            
            try {
            	if(id > 0) {
            		TextView txtView = (TextView)view.findViewById(id);
            		txtView.setText(pairs.getValue());
            	}
            } catch(Exception e) {
            	
            }
        }

		return view;
	}
	
	public static int getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            //e.printStackTrace();
            return -1;
        } 
    }


	private final HashMap<String, String> mItem;
	private final int mRId;

}
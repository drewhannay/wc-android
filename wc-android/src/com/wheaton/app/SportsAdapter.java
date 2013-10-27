package com.wheaton.app;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SportsAdapter extends BaseAdapter {

	public SportsAdapter(Context context, ArrayList<JSONObject> sportsSchedule, int size) {
		mResults = sportsSchedule;
		mContext = context;
		mSize = size;
	}

	@Override
	public int getCount() {
		if(mSize <= 0)
			return mResults.size();
		return mSize;
	}

	@Override
	public Object getItem(int position) {
		return mResults.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (convertView == null) {
			view = LayoutInflater.from(mContext).inflate(R.layout.sport_item, parent, false);
		} else {
			view = convertView;
		}
		
		JSONObject item = (JSONObject) getItem(position);
		try {
			TextView sport = (TextView) view.findViewById(R.id.sport);
			String sportStr = "";
			if(item.getString("gender").equals("women"))
				sportStr += "W. ";
			else
				sportStr += "M. ";
			sportStr += capitalize(item.getString("sport"));
			sport.setText(sportStr);
			TextView opponent = (TextView) view.findViewById(R.id.opponent);
			opponent.setText(item.getString("opponent"));
			
			TextView date = (TextView) view.findViewById(R.id.date);
			date.setText(item.getJSONObject("date").getString("month") + "/" + item.getJSONObject("date").getString("day"));
			
			TextView time = (TextView) view.findViewById(R.id.time);
			time.setText(item.getString("time"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return view;
	}
	
	private String capitalize(String line) {
	  return Character.toUpperCase(line.charAt(0)) + line.substring(1);
	}
	
	private final ArrayList<JSONObject> mResults;
	private final int mSize;
	private final Context mContext;
}
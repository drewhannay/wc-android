package com.wheaton.app;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SportsAdapter extends BaseAdapter {

	public SportsAdapter(Context context, JSONArray sportsSchedule, int size) {
		mResults = sportsSchedule;
		mContext = context;
		mSize = size;
	}

	@Override
	public int getCount() {
		if(mResults.length() <= 0)
			return 0;
		if(mSize < 0)
			return mResults.length();
		else
			return mSize;
	}

	@Override
	public Object getItem(int position) {
		try {
			return mResults.getJSONObject(position);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (convertView == null) {
			view = LayoutInflater.from(mContext).inflate(R.layout.item_sport, parent, false);
		} else {
			view = convertView;
		}

		JSONObject item = (JSONObject) getItem(position);
		try {
			JSONObject custom = (JSONObject) item.getJSONObject("custom");

			TextView sport = (TextView) view.findViewById(R.id.sport);
			String sportStr = "";
			if(custom.getString("gender").equals("women"))
				sportStr += "W. ";
			else
				sportStr += "M. ";
			sportStr += capitalize(item.getString("title"));
			sport.setText(sportStr);
			TextView opponent = (TextView) view.findViewById(R.id.opponent);
			opponent.setText(custom.getString("opponent"));

			Calendar c = new GregorianCalendar(); 
			c.setTimeInMillis((long)(item.getJSONArray("timeStamp").getDouble(0))*1000);

			SimpleDateFormat format1 = new SimpleDateFormat("MM/dd");
			SimpleDateFormat format2 = new SimpleDateFormat("h:mm a");

			String dateString = format1.format(c.getTime());
			String timeString = format2.format(c.getTime());

			TextView date = (TextView) view.findViewById(R.id.date);
			date.setText(dateString);

			TextView time = (TextView) view.findViewById(R.id.time);
			time.setText(timeString);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return view;
	}

	private String capitalize(String line) {
		return Character.toUpperCase(line.charAt(0)) + line.substring(1);
	}

	private final JSONArray mResults;
	private final int mSize;
	private final Context mContext;
}
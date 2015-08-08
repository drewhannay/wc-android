package com.wheaton.app;

import java.io.StringReader;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.wheaton.app.List.Header;
import com.wheaton.app.List.Item;
import com.wheaton.app.List.ListItem;
import com.wheaton.app.List.TwoTextArrayAdapter;
import com.wheaton.utility.LoadURLTask;

public class AcademicCalendarFragment extends TrackedFragment {

	public AcademicCalendarFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState); 

		mRootView = inflater.inflate(R.layout.calendar_list, container, false);
		
		mLoadURLTask = new LoadURLTask(MainScreen.ACADEMIC_CALENDAR, new LoadURLTask.RunnableOfT<String>() {
			@Override
			public void run(String result) {
				mLoadURLTask = null;
				try{
					onLoadURLSucceeded(result);
				} catch(Exception e) {

				}
			}
		});
		mLoadURLTask.execute();

		return mRootView;
	}

	@Override
	public void onPause(){
		super.onPause();

		if (mLoadURLTask != null)
			mLoadURLTask.cancel(false);
	}

	private void onLoadURLSucceeded(String data) {

		List<Item> items = new ArrayList<Item>();
		List<String> titles = new ArrayList<String>();
		HashMap<String, String> day = new HashMap<String, String>();

		try {
			JSONArray events = new JSONArray(data);
			Log.v("TAG", "LENGTH: " + events.length());
			Calendar calendar = Calendar.getInstance();
			boolean[] hasMonths = new boolean[12];

			for (int i = 0; i < events.length(); i++) {
				if(!titles.contains(events.getJSONObject(i).getString("title"))) {
					day = new HashMap<String, String>();
					day.put("item_header", events.getJSONObject(i).getString("title"));
					titles.add(events.getJSONObject(i).getString("title"));

					Date sessionDate = dateFromString(events.getJSONObject(i).getString("timeStamp"));

					if(hasMonths[(sessionDate.getMonth())] == false) {
						hasMonths[(sessionDate.getMonth())] = true;
						items.add(new Header(new DateFormatSymbols().getMonths()[sessionDate.getMonth()] + " - " + (sessionDate.getYear()+1900)));
					}
					day.put("item_date", "" + sessionDate.getDate());
					items.add(new ListItem(day, R.layout.item_calendar_single));
				}
			}
		}
		catch (JSONException e){

		}
		ListView lv = (ListView)getView().findViewById(R.id.calendarList);
		lv.setAdapter(new TwoTextArrayAdapter(getActivity(), items));

		boolean insideItem = false;

		/*
		try {
			int eventType = xpp.getEventType();
			SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy HH:mm:ss Z");

			HashMap<String, String> day = new HashMap<String, String>();

			Date date = new Date();

			Calendar calendar = lastDate = Calendar.getInstance();

			items.add(new Header(getMonthForInt(calendar.get(Calendar.MONTH)) + " - " + calendar.get(Calendar.YEAR)));

			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					if (xpp.getName().equalsIgnoreCase("item")) {
						day = new HashMap<String, String>();
						date = new Date();
						calendar = Calendar.getInstance();
						insideItem = true;
					} else if (xpp.getName().equalsIgnoreCase("title")) {
						if (insideItem)
							day.put("item_header", xpp.nextText());
					} else if (xpp.getName().equalsIgnoreCase("pubDate")) {
						if (insideItem) {
							try {  
								date = format.parse(xpp.nextText());   
							} catch (Exception e) {  
								e.printStackTrace();  
							}
							calendar.setTime(date);
							day.put("item_date", Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)));
						}
					}
				} else if (eventType == XmlPullParser.END_TAG
						&& xpp.getName().equalsIgnoreCase("item")) {
					if(calendar.get(Calendar.MONTH) != lastDate.get(Calendar.MONTH)
							|| calendar.get(Calendar.YEAR) != lastDate.get(Calendar.YEAR)) {
						items.add(new Header(getMonthForInt(calendar.get(Calendar.MONTH)) + " - " + calendar.get(Calendar.YEAR)));
					}
					items.add(new ListItem(day, R.layout.item_calendar_single));

					lastDate = calendar;
					insideItem = false;
				}

				eventType = xpp.next(); // move to next element
			}

		} catch(Exception e) {

		}
*/


	}

	public String getMonthForInt(int num) {
		String month = "wrong";
		DateFormatSymbols dfs = new DateFormatSymbols();
		String[] months = dfs.getMonths();
		if (num >= 0 && num <= 11 ) {
			month = months[num];
		}
		return month;
	}

	private Date dateFromString(String toConvert){

		String toParse = toConvert.substring(toConvert.indexOf('[') + 2, toConvert.indexOf(']') - 1);
		Long parsed = Long.parseLong(toParse);
		Long used = parsed * 1000;

		return new Date(used);
	}

	private LoadURLTask mLoadURLTask;
	private View mRootView;
	private Calendar lastDate;
}
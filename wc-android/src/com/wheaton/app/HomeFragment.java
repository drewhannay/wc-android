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

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.wheaton.app.List.Header;
import com.wheaton.app.List.Item;
import com.wheaton.app.List.ListItem;
import com.wheaton.app.List.TwoTextArrayAdapter;
import com.wheaton.utility.LoadURLTask;

public class HomeFragment extends TrackedFragment {
	
	public HomeFragment() {
        // Empty constructor required for fragment subclasses
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		super.onCreateView(inflater, container, savedInstanceState);
		
		mRootView = inflater.inflate(R.layout.fragment_home, container, false);
		
		mLoadEventsURLTask = new LoadURLTask(MainScreen.EVENTS_CALENDAR, new LoadURLTask.RunnableOfT<String>() {
			@Override
			public void run(String result) {
				mLoadEventsURLTask = null;
				if (getActivity() != null)
					onLoadEventsURLSucceeded(result);
			}
		});
		
		mLoadSportsURLTask = new LoadURLTask(MainScreen.SPORTS_URL, new LoadURLTask.RunnableOfT<String>() {
			@Override
			public void run(String result) {
				mLoadSportsURLTask = null;
				if (getActivity() != null)
					onLoadSportsURLSucceeded(result);
			}
		});
		
		mLoadEventsURLTask.execute();
		mLoadSportsURLTask.execute();
		
		return mRootView;
	}


	private void onLoadSportsURLSucceeded(String data) {
		try {
			TextView title = (TextView)getView().findViewById(R.id.sports_title);
			title.setTypeface(null, Typeface.BOLD);
			title.setText("Sports");

			ListView lv = (ListView)getView().findViewById(R.id.sports);
			lv.setAdapter(new SportsAdapter(getActivity(), new JSONArray(data), 6));
		} catch (JSONException e) {
			mErrorOccurred = true;
			Log.e(TAG, "onLoadURLSucceeded", e);
		}
	}
	
	private void onLoadEventsURLSucceeded(String data) {
		TextView title = (TextView)getView().findViewById(R.id.events_title);
		title.setTypeface(null, Typeface.BOLD);

		title.setText("Upcoming");
			List<Item> items = new ArrayList<Item>();
			List<String> titles = new ArrayList<String>();
			HashMap<String, String> day = new HashMap<String, String>();

			try {
				JSONArray events = new JSONArray(data);
				Log.v("TAG", "LENGTH: " + events.length());
				Calendar calendar = Calendar.getInstance();

				for (int i = 0; i < events.length(); i++) {
					if(!titles.contains(events.getJSONObject(i).getString("title"))) {
						day = new HashMap<String, String>();
						day.put("item_header", events.getJSONObject(i).getString("title"));
						titles.add(events.getJSONObject(i).getString("title"));

						Calendar sessionDate = dateFromString(events.getJSONObject(i).getString("timeStamp"));
						SimpleDateFormat format1 = new SimpleDateFormat("MM/dd");
						SimpleDateFormat format2 = new SimpleDateFormat("h:mm a");
						String dateString = format1.format(sessionDate.getTime());
						String timeString = format2.format(sessionDate.getTime());

						day.put("item_date", dateString);
						day.put("item_time", timeString);

						items.add(new ListItem(day, R.layout.item_calendar_event));
					}
				}
			}
			catch (JSONException e){

			}
			ListView lv = (ListView)getView().findViewById(R.id.events);
			lv.setAdapter(new TwoTextArrayAdapter(getActivity(), items, 6));

			/*
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		
			factory.setNamespaceAware(false);
			XmlPullParser xpp = factory.newPullParser();
			xpp.setInput(new StringReader(xml));
	
			List<Item> items = new ArrayList<Item>();
	
			boolean insideItem = false;
	
			try { 
				int eventType = xpp.getEventType();
				SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy HH:mm:ss Z");
				
				HashMap<String, String> day = new HashMap<String, String>();
				Date date = new Date();
				Calendar calendar = Calendar.getInstance();
				
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
								
								SimpleDateFormat format1 = new SimpleDateFormat("MM/dd");
							    SimpleDateFormat format2 = new SimpleDateFormat("h:mm a");

							    String dateString = format1.format(calendar.getTime());
							    String timeString = format2.format(calendar.getTime());
								
								day.put("item_date", dateString);
								day.put("item_time", timeString);
							}
						}
					} else if (eventType == XmlPullParser.END_TAG
							&& xpp.getName().equalsIgnoreCase("item")) {
						items.add(new ListItem(day, R.layout.item_calendar_event));
						insideItem = false;
					}
	
					eventType = xpp.next(); // move to next element
				}
			} catch(Exception e) {
	 
			}

	
			ListView lv = (ListView)getView().findViewById(R.id.events);
			lv.setAdapter(new TwoTextArrayAdapter(getActivity(), items, 6));
		} catch(XmlPullParserException e) {
			
		}
		*/
	}


	private Calendar dateFromString(String toConvert){
		String toParse = toConvert.substring(toConvert.indexOf('[') + 2, toConvert.indexOf(']') - 1);
		Long parsed = Long.parseLong(toParse);
		Long used = parsed * 1000;

		Calendar toReturn = Calendar.getInstance();
		toReturn.setTimeInMillis(used);
		return toReturn;
	}


	private static final String TAG = ChapelFragment.class.toString();

	private LoadURLTask mLoadEventsURLTask;
	private LoadURLTask mLoadSportsURLTask;
	private View mRootView;
	private boolean mErrorOccurred = false;
}
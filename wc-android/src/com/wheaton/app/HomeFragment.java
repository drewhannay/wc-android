package com.wheaton.app;

import java.io.StringReader;
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
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

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
		
		View mRootView = inflater.inflate(R.layout.fragment_home, container, false);
		
		m_loadEventsURLTask = new LoadURLTask(MainScreen.EVENTS_CALENDAR, new LoadURLTask.RunnableOfT<String>() {
			@Override
			public void run(String result) {
				m_loadEventsURLTask = null;
				onLoadEventsURLSucceeded(result);
			}
		});
		
		m_loadSportsURLTask = new LoadURLTask(MainScreen.SPORTS_URL, new LoadURLTask.RunnableOfT<String>() {
			@Override
			public void run(String result) {
				m_loadSportsURLTask = null;
				onLoadSportsURLSucceeded(result);
			}
		});
		
		m_loadEventsURLTask.execute();
		m_loadSportsURLTask.execute();
		
		return mRootView;
	}


	private void onLoadSportsURLSucceeded(String data) {
		try {
			TextView title = (TextView)getView().findViewById(R.id.sports_title);
			title.setText("Sports");

			ListView lv = (ListView)getView().findViewById(R.id.sports);
			lv.setAdapter(new SportsAdapter(getActivity(), new JSONArray(data), 6));
		} catch (JSONException e) {
			m_errorOccurred = true;
			Log.e(TAG, "onLoadURLSucceeded", e);
		}
	}
	
	private void onLoadEventsURLSucceeded(String xml) {
		try {
			
			TextView title = (TextView)getView().findViewById(R.id.events_title);
			title.setText("Upcoming");
			
			
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
	}

	private static final String TAG = ChapelFragment.class.toString();

	private LoadURLTask m_loadEventsURLTask;
	private LoadURLTask m_loadSportsURLTask;
	private View mRootView;
	private boolean m_errorOccurred = false;
}
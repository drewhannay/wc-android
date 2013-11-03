package com.wheaton.app;

import java.io.StringReader;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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

		View mRootView = inflater.inflate(R.layout.calendar_list, container, false);

		m_loadURLTask = new LoadURLTask(MainScreen.ACADEMIC_CALENDAR, new LoadURLTask.RunnableOfT<String>() {
			@Override
			public void run(String result) {
				m_loadURLTask = null;
				try{
					onLoadURLSucceeded(result);
				} catch(Exception e) {

				}
			}
		});
		m_loadURLTask.execute();

		return mRootView;
	}

	//	@Override
	//	protected void onPause()
	//	{
	//		super.onPause();
	//
	//		if (m_loadURLTask != null)
	//			m_loadURLTask.cancel(false);
	//	}

	private void onLoadURLSucceeded(String xml) throws XmlPullParserException {

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

		ListView lv = (ListView)getView().findViewById(R.id.calendarList);
		lv.setAdapter(new TwoTextArrayAdapter(getActivity(), items));

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

	private static final String TAG = ChapelFragment.class.toString();

	private LoadURLTask m_loadURLTask;
	private View mRootView;
	private Calendar lastDate;
	private boolean m_errorOccurred = false;
}
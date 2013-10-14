package com.wheaton.app;

import java.io.IOException;
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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.wheaton.utility.AcademicCalendarXMLParser;
import com.wheaton.utility.AcademicCalendarXMLParser.Entry;
import com.wheaton.utility.LoadURLTask;

public class AcademicCalendar extends Fragment {

	public AcademicCalendar() {
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
				onLoadURLSucceeded(result);
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

	private void onLoadURLSucceeded(String xml) {
		
		AcademicCalendarXMLParser acParser = new AcademicCalendarXMLParser();
		List<Entry> entries = null;
		
		try {
			entries = acParser.parse(new StringReader(xml));
		} catch (XmlPullParserException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		ArrayList<HashMap<String, String>> chapelList = new ArrayList<HashMap<String, String>>();
		HashMap<Integer, String> headerList = new HashMap<Integer, String>();

		int headerIndex = 0;
		
		Log.d("TAG", entries.toString());

		for (Entry en : entries) {
			HashMap<String, String> day = new HashMap<String, String>();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); 

			Date date = new Date();
			Calendar calendar = Calendar.getInstance();

			try {  
				date = format.parse(en.date);   
			} catch (Exception e) {  
				e.printStackTrace();  
			}

			calendar.setTime(date);

			day.put("cp_item_header", en.title);
			day.put("cp_item_date", Integer.toString(calendar.get(Calendar.DAY_OF_WEEK)));
			chapelList.add(day);

			if(calendar.hashCode() != lastDate) {
				headerList.put(headerIndex, getMonthForInt(calendar.get(Calendar.MONTH)) + "" + calendar.get(Calendar.YEAR));
			}

			lastDate = calendar.hashCode();
			headerIndex++;
		}
		
		ListView lv = (ListView)getView().findViewById(R.id.chapelList);
		lv.setAdapter(new HeaderList(getActivity(), R.layout.chapel_item, chapelList, headerList));
		
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

	private static final String TAG = ChapelSchedule.class.toString();

	private LoadURLTask m_loadURLTask;
	private View mRootView;
	private int lastDate;
	private boolean m_errorOccurred = false;
}
package com.wheaton.app;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.wheaton.utility.LoadURLTask;

public class HomeScreen extends Fragment {
	
	public HomeScreen() {
        // Empty constructor required for fragment subclasses
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		super.onCreateView(inflater, container, savedInstanceState);
		
		View mRootView = inflater.inflate(R.layout.home, container, false);
		
		m_loadEventsURLTask = new LoadURLTask(MainScreen.EVENTS_CALENDAR, new LoadURLTask.RunnableOfT<String>() {
			@Override
			public void run(String result) {
				m_loadEventsURLTask = null;
//				onLoadEventsURLSucceeded(result);
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

//	@Override
//	protected void onPause()
//	{
//		super.onPause();
//
//		if (m_loadURLTask != null)
//			m_loadURLTask.cancel(false);
//	}

	private void onLoadSportsURLSucceeded(String data) {
		try {
			TextView title = (TextView)getView().findViewById(R.id.sports_title);
			title.setText("Sports");
			
			ArrayList<JSONObject> afterDateSports = new ArrayList<JSONObject>();
			JSONArray sports = new JSONArray(data);
			for(int i = 0; i < sports.length(); i++) {
				JSONObject date = sports.getJSONObject(i).getJSONObject("date");
				try {
					Date currentDate = new Date();
					Date sportDate = new SimpleDateFormat("MM/d/yyyy").parse(date.getString("month") + "/" + date.getString("day") + "/2013");
					
					if(secondsSinceEpoch(sportDate) > secondsSinceEpoch(currentDate)) {
						afterDateSports.add(sports.getJSONObject(i));
					}				
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			
			Log.d("TAG", afterDateSports.size()+"");
			
			ListView lv = (ListView)getView().findViewById(R.id.sports);
			lv.setAdapter(new SportsAdapter(getActivity(), afterDateSports, 6));
		} catch (JSONException e) {
			m_errorOccurred = true;
			Log.e(TAG, "onLoadURLSucceeded", e);
		}
	}
	
	private long secondsSinceEpoch(Date date) {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		calendar.clear();
		calendar.setTime(date);
		return calendar.getTimeInMillis() / 1000L;
	}

	private static final String TAG = ChapelSchedule.class.toString();

	private LoadURLTask m_loadEventsURLTask;
	private LoadURLTask m_loadSportsURLTask;
	private View mRootView;
	private boolean m_errorOccurred = false;
}
package com.wheaton.app;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class ChapelFragment extends TrackedFragment {
	
	public ChapelFragment() {
        // Empty constructor required for fragment subclasses
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		super.onCreateView(inflater, container, savedInstanceState);
		
		mRootView = inflater.inflate(R.layout.calendar_list, container, false);
        
		mLoadURLTask = new LoadURLTask(MainScreen.CHAPEL_URL, new LoadURLTask.RunnableOfT<String>() {
			@Override
			public void run(String result) {
				mLoadURLTask = null;
				onLoadURLSucceeded(result);
			}
		});
		mLoadURLTask.execute();
		
		return mRootView;
	}

	@Override
	public void onPause() {
		super.onPause();

		if (mLoadURLTask != null)
			mLoadURLTask.cancel(false);
	}

	private void onLoadURLSucceeded(String data) {

		List<Item> items = new ArrayList<Item>();
		boolean[] hasMonths = new boolean[12];

		try {
			JSONArray chapels = new JSONArray(data);
			for (int i = 0; i < chapels.length(); i++) {
				HashMap<String, String> day = new HashMap<String, String>();

				Date sessionDate = dateFromString(chapels.getJSONObject(i).getString("timeStamp"));
				Log.v("TAG",":( " + sessionDate.getDate());

				if(hasMonths[(sessionDate.getMonth())] == false) {
					hasMonths[(sessionDate.getMonth())] = true;
					items.add(new Header(new DateFormatSymbols().getMonths()[sessionDate.getMonth()]));
				}
				day.put("item_header", chapels.getJSONObject(i).getString("title"));
				day.put("item_date", "" + sessionDate.getDate());

				if(!chapels.getJSONObject(i).getString("description").equals("")) {
					day.put("item_subtext", chapels.getJSONObject(i).getString("description"));
					items.add(new ListItem(day, R.layout.item_calendar));
				}
				else
					items.add(new ListItem(day, R.layout.item_calendar_single));

			}
			
			ListView lv = (ListView)getView().findViewById(R.id.calendarList);
			lv.setAdapter(new TwoTextArrayAdapter(getActivity(), items));
		} catch (JSONException e) {
			Log.e(TAG, "onLoadURLSucceeded", e);
		}

	}

	private Date dateFromString(String toConvert){

		String toParse = toConvert.substring(toConvert.indexOf('[') + 2, toConvert.indexOf(']') - 1);
		double parsed = Double.parseDouble(toParse);
		long used = (long) parsed * 1000;


		return new Date(used);
	}

	private static final String TAG = ChapelFragment.class.toString();

	private LoadURLTask mLoadURLTask;
	private View mRootView;
}
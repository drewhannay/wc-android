package com.wheaton.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

		try {
			JSONArray chapels = new JSONArray(data);
			for (int i = 0; i < chapels.length(); i++) {
				JSONObject month = chapels.getJSONObject(i);
				JSONArray speakers = month.getJSONArray("speakers");
				items.add(new Header(month.getString("month")));
				for(int j = 0; j < speakers.length(); j++) {
					HashMap<String, String> day = new HashMap<String, String>();
					
					day.put("item_header", speakers.getJSONObject(j).getString("title"));
					if(!speakers.getJSONObject(j).getString("subtitle").equals(""))
						day.put("item_subtext", speakers.getJSONObject(j).getString("subtitle"));
					day.put("item_date", speakers.getJSONObject(j).getString("date"));
					
					if(!speakers.getJSONObject(j).getString("subtitle").equals(""))
						items.add(new ListItem(day, R.layout.item_calendar));
					else
						items.add(new ListItem(day, R.layout.item_calendar_single));
				}
			}
			
			ListView lv = (ListView)getView().findViewById(R.id.calendarList);
			lv.setAdapter(new TwoTextArrayAdapter(getActivity(), items));
		} catch (JSONException e) {
			Log.e(TAG, "onLoadURLSucceeded", e);
		}

	}

	private static final String TAG = ChapelFragment.class.toString();

	private LoadURLTask mLoadURLTask;
	private View mRootView;
}
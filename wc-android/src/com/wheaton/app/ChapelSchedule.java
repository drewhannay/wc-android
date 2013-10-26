package com.wheaton.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

import com.wheaton.app.List.Header;
import com.wheaton.app.List.Item;
import com.wheaton.app.List.ListItem;
import com.wheaton.app.List.TwoTextArrayAdapter;
import com.wheaton.utility.LoadURLTask;

public class ChapelSchedule extends Fragment {
	
	public ChapelSchedule() {
        // Empty constructor required for fragment subclasses
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		super.onCreateView(inflater, container, savedInstanceState);
		
		View mRootView = inflater.inflate(R.layout.calendar_list, container, false);
        
		m_loadURLTask = new LoadURLTask(MainScreen.CHAPEL_URL, new LoadURLTask.RunnableOfT<String>() {
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
						items.add(new ListItem(day, R.layout.calendar_item));
					else
						items.add(new ListItem(day, R.layout.calendar_single_item));
				}
			}
			
			ListView lv = (ListView)getView().findViewById(R.id.calendarList);
			lv.setAdapter(new TwoTextArrayAdapter(getActivity(), items));
		} catch (JSONException e) {
			m_errorOccurred = true;
			Log.e(TAG, "onLoadURLSucceeded", e);
		}

	}

	private static final String TAG = ChapelSchedule.class.toString();

	private LoadURLTask m_loadURLTask;
	private View mRootView;
	private boolean m_errorOccurred = false;
}
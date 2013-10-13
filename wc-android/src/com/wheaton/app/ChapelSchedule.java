package com.wheaton.app;

import java.util.ArrayList;
import java.util.HashMap;

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

import com.wheaton.utility.LoadURLTask;

public class ChapelSchedule extends Fragment {
	
	public ChapelSchedule() {
        // Empty constructor required for fragment subclasses
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		super.onCreateView(inflater, container, savedInstanceState);
		
		View mRootView = inflater.inflate(R.layout.chapel, container, false);
        
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

		ArrayList<JSONObject> chapelList = new ArrayList<JSONObject>();
		HashMap<Integer, String> headerList = new HashMap<Integer, String>();
		
		int headerIndex = 0;
		try {
			JSONArray chapels = new JSONArray(data);
			for (int i = 0; i < chapels.length(); i++) {
				JSONObject month = chapels.getJSONObject(i);
				JSONArray speakers = month.getJSONArray("speakers");
				headerList.put(headerIndex, month.getString("month"));
				for(int j = 0; j < speakers.length(); j++) {
					chapelList.add(speakers.getJSONObject(j));
					headerIndex++;
				}
			}
			
			ListView lv = (ListView)getView().findViewById(R.id.chapelList);
	        lv.setAdapter(new HeaderList(getActivity(), chapelList, headerList));
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
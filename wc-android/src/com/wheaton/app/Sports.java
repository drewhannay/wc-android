package com.wheaton.app;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.wheaton.utility.LoadURLTask;

public class Sports extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);

		View mRootView = inflater.inflate(R.layout.sport_list, container, false);

		mLoadURLTask = new LoadURLTask(MainScreen.SPORTS_URL, new LoadURLTask.RunnableOfT<String>() {
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
	public void onPause() {
		super.onPause();

		if (mLoadURLTask != null)
			mLoadURLTask.cancel(false);
	}
	

	private void onLoadURLSucceeded(String data) {
		try {
			ListView lv = (ListView)getView().findViewById(R.id.sportList);
			lv.setAdapter(new SportsAdapter(getActivity(), new JSONArray(data), -1));
		} catch (JSONException e) {
			mErrorOccurred = true;
			Log.e(TAG, "onLoadURLSucceeded", e);
		}
	}

	private static final String TAG = ChapelSchedule.class.toString();

	private LoadURLTask mLoadURLTask;
	private View mRootView;
	private boolean mErrorOccurred = false;
}
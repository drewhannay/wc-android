package com.wheaton.app;

import org.json.JSONArray;

import com.wheaton.utility.LoadURLTask;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class WhosWhoSearch extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		// getActivity().getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		
		View mRootView = inflater.inflate(R.layout.whos_who_search, container, false);

		m_searchField = (EditText) mRootView.findViewById(R.id.text_box);
		m_searchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					performSearch();
					return true;
				}
				return false;
			}
		});

		getActivity().setProgressBarIndeterminate(true);
		getActivity().setProgressBarIndeterminateVisibility(false);
		
		return mRootView;
	}

	private void performSearch() {
		InputMethodManager inputMethodManager = (InputMethodManager) 
				getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		IBinder binder = m_searchField.getApplicationWindowToken();
		if (binder != null)
			inputMethodManager.hideSoftInputFromWindow(binder, 0);

		getActivity().setProgressBarIndeterminateVisibility(true);
		new LoadURLTask(MainScreen.WHOS_WHO_PREFIX + getSanitizedSearchQuery(), 
				new LoadURLTask.RunnableOfT<String>() {
			@Override
			public void run(String result) {
				onLoadURLSucceeded(result);
			}
		}).execute();
	}

	private String getSanitizedSearchQuery() {
		return Uri.encode(m_searchField.getText().toString().trim());
	}

	private void onLoadURLSucceeded(String data) {
		getActivity().setProgressBarIndeterminateVisibility(false);
		if (data == null || data.equals("")) {
			Toast.makeText(getActivity(), R.string.connect_to_wheaton, Toast.LENGTH_SHORT).show();
		} else {
			try {
				ListView lv = (ListView)getActivity().findViewById(R.id.whoswho_results);
				lv.setAdapter(new WhosWhoAdapter(getActivity(), new JSONArray(data)));
			} catch(Exception e) {
				Log.w("WhosWho", e);
			}
		}
	}

	private EditText m_searchField;
	private View mRootView;
}

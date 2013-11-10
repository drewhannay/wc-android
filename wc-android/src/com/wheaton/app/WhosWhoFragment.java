package com.wheaton.app;

import org.json.JSONArray;

import com.wheaton.utility.LoadURLTask;
import com.wheaton.utility.Utils;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.Toast;

public class WhosWhoFragment extends TrackedFragment implements OnQueryTextListener {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		
		mRootView = inflater.inflate(R.layout.fragment_whos_who, container, false);
		
		setHasOptionsMenu(true);

		getActivity().setProgressBarIndeterminate(true);
		getActivity().setProgressBarIndeterminateVisibility(false);
		
		return mRootView;
	}
	
	@Override 
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.add("Search");
        item.setIcon(android.R.drawable.ic_menu_search);
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        SearchView sv = new SearchView(getActivity());
        sv.setOnQueryTextListener(this);
        MenuItemCompat.setActionView(item, sv);
        
        mSearchView = sv;
        
        SearchView.SearchAutoComplete textView = 
        		(SearchView.SearchAutoComplete)sv.findViewById(R.id.search_src_text);
        textView.setTextColor(Color.WHITE);
    }

	private void performSearch() {
		InputMethodManager inputMethodManager = (InputMethodManager) 
				getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		
		inputMethodManager.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);

		new LoadURLTask(MainScreen.WHOS_WHO_PREFIX + getSanitizedSearchQuery(), 
				new LoadURLTask.RunnableOfT<String>() {
			@Override
			public void run(String result) {
				onLoadURLSucceeded(result);
			}
		}).execute();
	}

	private String getSanitizedSearchQuery() {
		return Uri.encode(mCurFilter.trim());
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
	
	@Override
	public void onPause() {
		InputMethodManager inputMethodManager = (InputMethodManager) 
				getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		
		inputMethodManager.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
		super.onPause();
	}

    public boolean onQueryTextChange(String newText) {
        mCurFilter = !TextUtils.isEmpty(newText) ? newText : null;
        return true;
    }

    @Override 
    public boolean onQueryTextSubmit(String query) {
        performSearch();
        return true;
    }

	private SearchView mSearchView;
	private String mCurFilter;
	private View mRootView;
}

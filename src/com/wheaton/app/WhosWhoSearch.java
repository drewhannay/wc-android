package com.wheaton.app;

import com.wheaton.utility.LoadURLTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class WhosWhoSearch extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.whos_who_search);

		m_searchField = (EditText) findViewById(R.id.text_box);
		m_searchField.setOnEditorActionListener(new TextView.OnEditorActionListener()
		{
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
			{
				if (actionId == EditorInfo.IME_ACTION_SEARCH)
				{
					performSearch();
					return true;
				}
				return false;
			}
		});

		((Button) findViewById(R.id.search_button)).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				performSearch();
			}
		});

		setProgressBarIndeterminate(true);
		setProgressBarIndeterminateVisibility(false);
	}

	private void performSearch()
	{
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		IBinder binder = m_searchField.getApplicationWindowToken();
		if (binder != null)
			inputMethodManager.hideSoftInputFromWindow(binder, 0);

		setProgressBarIndeterminateVisibility(true);
		new LoadURLTask(MainScreen.WHOS_WHO_PREFIX + getSanitizedSearchQuery(), new LoadURLTask.RunnableOfT<String>()
		{
			@Override
			public void run(String result)
			{
				onLoadURLSucceeded(result);
			}
		}).execute();
	}

	private String getSanitizedSearchQuery()
	{
		return Uri.encode(m_searchField.getText().toString().trim());
	}

	private void onLoadURLSucceeded(String data)
	{
		setProgressBarIndeterminateVisibility(false);
		if (data == null || data.equals(""))
		{
			Toast.makeText(this, R.string.connect_to_wheaton, Toast.LENGTH_SHORT).show();
		}
		else
		{
			Intent intent = new Intent(this, WhosWhoResults.class);
			intent.putExtra(WhosWhoResults.RESULTS_KEY, data);
			startActivity(intent);
		}
	}

	private EditText m_searchField;
}

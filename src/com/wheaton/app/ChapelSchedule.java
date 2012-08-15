package com.wheaton.app;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ViewAnimator;

import com.wheaton.utility.LoadURLTask;

public class ChapelSchedule extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.button_flipper);

		((ImageView) findViewById(R.id.headerImage)).setImageResource(R.drawable.chapel_header);

		m_viewAnimator = (ViewAnimator) findViewById(R.id.view_area);
		m_nextButton = (Button) findViewById(R.id.rightButton);
		m_previousButton = (Button) findViewById(R.id.leftButton);
		m_todayButton = (Button) findViewById(R.id.todayButton);

		setProgressBarIndeterminate(true);
		setProgressBarIndeterminateVisibility(true);

		m_loadURLTask = new LoadURLTask(MainScreen.CHAPEL_URL, new LoadURLTask.RunnableOfT<String>()
		{
			@Override
			public void run(String result)
			{
				m_loadURLTask = null;
				onLoadURLSucceeded(result);
			}
		});
		m_loadURLTask.execute();
	}

	@Override
	protected void onPause()
	{
		super.onPause();

		if (m_loadURLTask != null)
			m_loadURLTask.cancel(false);
	}

	private void setNextButtonVisibility()
	{
		if (m_viewAnimator.getDisplayedChild() == m_viewAnimator.getChildCount() - 1)
			m_nextButton.setVisibility(View.INVISIBLE);
		else
			m_nextButton.setVisibility(View.VISIBLE);
	}

	private void setPreviousButtonVisibility()
	{
		if (m_viewAnimator.getDisplayedChild() == 0)
			m_previousButton.setVisibility(View.INVISIBLE);
		else
			m_previousButton.setVisibility(View.VISIBLE);
	}

	private void onLoadURLSucceeded(String data)
	{
		m_nextButton.setOnClickListener(m_buttonClickListener);
		m_previousButton.setOnClickListener(m_buttonClickListener);
		m_todayButton.setOnClickListener(m_buttonClickListener);

		List<ChapelWeek> weekList = new ArrayList<ChapelWeek>();
		try
		{
			JSONArray chapels = new JSONObject(data).getJSONArray("chapels");
			ChapelWeek currentWeek = new ChapelWeek();
			for (int index = 0; index < chapels.length(); index++)
			{
				if (!currentWeek.addDay(chapels.getJSONObject(index)))
				{
					weekList.add(currentWeek);
					currentWeek = new ChapelWeek();
					currentWeek.addDay(chapels.getJSONObject(index));
				}
			}
			weekList.add(currentWeek);
		}
		catch (JSONException e)
		{
			m_errorOccurred = true;
			Log.e(TAG, "onLoadURLSucceeded", e);
		}
		
		for (View day : createViewList(weekList))
			m_viewAnimator.addView(day);

		m_viewAnimator.setDisplayedChild(m_todayIndex);

		setNextButtonVisibility();
		setPreviousButtonVisibility();

		setProgressBarIndeterminateVisibility(false);
	}

	private List<View> createViewList(List<ChapelWeek> weekList)
	{
		List<View> viewList = new ArrayList<View>();
		WebView webView;
		if (m_errorOccurred)
		{
			StringBuilder builder = new StringBuilder();
			webView = new WebView(this);
			builder.append("<html><head><style type=\"text/css\"> h1 { font-size: 1.2em; font-weight: bold; "
					+ "text-align: center; }</style></head><body><br/><br/><br/><br/><h1>The chapel schedule is not yet available. Check back soon!</h1></body></html>");
			webView.loadData(builder.toString(), "text/html", "utf-8");
			webView.setBackgroundColor(Color.TRANSPARENT);
			viewList.add(webView);
			return viewList;
		}
		for (int index = 0; index < weekList.size(); index++)
		{
			ChapelWeek week = weekList.get(index);

			if (m_todayIndex < 0 && week.isCurrentOrFutureWeek())
				m_todayIndex = index;

			webView = new WebView(this);
			webView.loadData(week.toString(), "text/html", "utf-8");
			webView.setBackgroundColor(Color.TRANSPARENT);
			viewList.add(webView);
		}
		return viewList;
	}

	private final OnClickListener m_buttonClickListener = new OnClickListener()
	{

		@Override
		public void onClick(View view)
		{
			if (m_todayIndex < 0)
				m_todayIndex = 0;

			switch (view.getId())
			{
			case R.id.rightButton:
				m_viewAnimator.showNext();
				setNextButtonVisibility();
				m_previousButton.setVisibility(View.VISIBLE);
				break;
			case R.id.leftButton:
				m_viewAnimator.showPrevious();
				setPreviousButtonVisibility();
				m_nextButton.setVisibility(View.VISIBLE);
				break;
			case R.id.todayButton:
				if (m_viewAnimator.getDisplayedChild() != m_todayIndex)
				{
					m_viewAnimator.setDisplayedChild(m_todayIndex);
					setNextButtonVisibility();
					setPreviousButtonVisibility();
				}
				break;
			}
		}
	};

	private static final String TAG = ChapelSchedule.class.toString();

	private Button m_nextButton;
	private Button m_previousButton;
	private Button m_todayButton;
	private ViewAnimator m_viewAnimator;
	private LoadURLTask m_loadURLTask;
	private int m_todayIndex = -1;
	private boolean m_errorOccurred = false;
}
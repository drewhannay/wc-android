package com.wheaton.app;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

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

public class OpenFloor extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.button_flipper);

		((ImageView) findViewById(R.id.headerImage)).setImageResource(R.drawable.open_floor_header);

		m_nextButton = (Button) findViewById(R.id.rightButton);
		m_previousButton = (Button) findViewById(R.id.leftButton);
		m_todayButton = (Button) findViewById(R.id.todayButton);
		m_viewAnimator = (ViewAnimator) findViewById(R.id.view_area);

		setProgressBarIndeterminate(true);
		setProgressBarIndeterminateVisibility(true);

		m_loadURLTask = new LoadURLTask(MainScreen.OPEN_FLOOR_URL, new LoadURLTask.RunnableOfT<String>()
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

		List<View> days = new ArrayList<View>();
		try
		{
			JSONObject jsonObject = new JSONObject(data);
			for (String weekday : weekdays)
				days.add(JSONToDay(jsonObject.getJSONObject(weekday)));
		}
		catch (JSONException e)
		{
			m_errorOccurred = true;
			Log.e(TAG, "onLoadURLSucceeded", e);
		}

		if (!m_errorOccurred && m_latestDate.after(m_todayDate))
		{
			for (View day : days)
				m_viewAnimator.addView(day);
			m_viewAnimator.setDisplayedChild(m_todayIndex);
		}
		else
		{
			loadErrorView();
			m_viewAnimator.setDisplayedChild(0);
		}

		setNextButtonVisibility();
		setPreviousButtonVisibility();

		setProgressBarIndeterminateVisibility(false);
	}

	private View JSONToDay(JSONObject jsonObject)
	{
		StringBuilder builder = new StringBuilder();
		try
		{
			builder.append("<head><style type=\"text/css\"> h1 { font-size: 1.1em; font-weight: bold; "
					+ "text-align: center; color:#CC6600; } h2 { text-align: center; font-size: 0.9em; } h3 { font-style:italic; font-size: 0.8em;}"
					+ "p { text-align: center; font-size: 0.7em; } </style></head>");
			builder.append("<h1>" + getFormattedDate(jsonObject.getString("Date")) + "</h1>");

			builder.append("<h2>Open Fischer Floors:</h2>");
			appendFloorData(builder, jsonObject.optJSONArray("Fischer"));

			builder.append("<br /><h2>Open Smith/Traber Floors:</h2>");
			appendFloorData(builder, jsonObject.optJSONArray("Smith/Traber"));

			builder.append("</body></html>");
		}
		catch (Exception e)
		{
			m_errorOccurred = true;
			Log.e(TAG, "JSONToDay", e);
		}

		WebView webView = new WebView(this);
		webView.loadData(builder.toString(), "text/html", "utf-8");
		webView.setBackgroundColor(Color.TRANSPARENT);
		return webView;
	}

	private void appendFloorData(StringBuilder builder, JSONArray building) throws JSONException
	{
		builder.append("<p>");

		if (building != null)
		{
			JSONObject floor;
			for (int index = 0; index < building.length(); index++)
			{
				floor = building.getJSONObject(index);
				builder.append(floor.getString("Floor"));
				builder.append(" - Open ");
				builder.append(floor.getString("Open Hours"));
				builder.append("<br />");
			}
		}
		else
		{
			builder.append("All Floors Closed");
		}

		builder.append("</p>");
	}

	private String getFormattedDate(String rawDate) throws ParseException
	{
		Date date = INPUT_FORMATTER.parse(rawDate + " 23:59");
		if (m_latestDate == null || date.after(m_latestDate))
			m_latestDate = date;

		return OUTPUT_FORMATTER.format(date);
	}

	private void loadErrorView()
	{
		WebView webView = new WebView(this);
		webView.loadData(ERROR_WEB_CODE, "text/html", "utf-8");
		webView.setBackgroundColor(Color.TRANSPARENT);
		m_viewAnimator.addView(webView);
	}

	private final OnClickListener m_buttonClickListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			switch (v.getId())
			{
			case R.id.rightButton:
				m_viewAnimator.setInAnimation(OpenFloor.this, R.anim.slide_in_right);
				m_viewAnimator.setOutAnimation(OpenFloor.this, R.anim.slide_out_left);
				m_viewAnimator.showNext();
				setNextButtonVisibility();
				m_previousButton.setVisibility(View.VISIBLE);
				break;
			case R.id.leftButton:
				m_viewAnimator.setInAnimation(OpenFloor.this, R.anim.slide_in_left);
				m_viewAnimator.setOutAnimation(OpenFloor.this, R.anim.slide_out_right);
				m_viewAnimator.showPrevious();
				setPreviousButtonVisibility();
				m_nextButton.setVisibility(View.VISIBLE);
				break;
			case R.id.todayButton:
				if (m_viewAnimator.getDisplayedChild() != m_todayIndex)
				{
					m_viewAnimator.setInAnimation(OpenFloor.this, android.R.anim.fade_in);
					m_viewAnimator.setOutAnimation(OpenFloor.this, android.R.anim.fade_out);
					m_viewAnimator.setDisplayedChild(m_todayIndex);
					setNextButtonVisibility();
					setPreviousButtonVisibility();
				}
				break;
			}
		}
	};

	private static final String TAG = OpenFloor.class.toString();
	private static final SimpleDateFormat INPUT_FORMATTER = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.US);
	private static final SimpleDateFormat OUTPUT_FORMATTER = new SimpleDateFormat("EEEE, MMMM d, y", Locale.US);
	private static final String[] weekdays = new String[] { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday",
			"Saturday" };
	private static final String ERROR_WEB_CODE = "<html><head><style type=\"text/css\"> h1 { font-size: 1.2em; font-weight: bold; "
			+ "text-align: center; }</style></head><body><br/><br/><br/><br/><h1>The open floor schedule is not yet available. Check back soon!</h1></body></html>";

	private final int m_todayIndex = new GregorianCalendar().get(Calendar.DAY_OF_WEEK) - 1;
	private final Date m_todayDate = new Date();

	private Button m_nextButton;
	private Button m_previousButton;
	private Button m_todayButton;
	private ViewAnimator m_viewAnimator;
	private LoadURLTask m_loadURLTask;
	private Date m_latestDate;
	private boolean m_errorOccurred = false;
}

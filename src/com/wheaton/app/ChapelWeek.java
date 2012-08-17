package com.wheaton.app;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ChapelWeek
{
	/**
	 * Add a day of chapel information to this ChapelWeek
	 * 
	 * @param chapelInfo The JSONObject containing the info about this chapel
	 * @return A boolean indicating whether the day was successfully added
	 */
	public boolean addDay(JSONObject chapelInfo)
	{
		try
		{
			if (m_scheduleList.size() != 0)
			{
				String lastDay = m_scheduleList.get(m_scheduleList.size() - 1).getString("Date");
				lastDay = lastDay.substring(0, lastDay.indexOf(','));
				String newDay = chapelInfo.getString("Date");
				newDay = newDay.substring(0, newDay.indexOf(','));

				for (String day : weekdays)
				{
					// if we find the newDay before lastDay, it must be a new week.
					if (day.equals(newDay))
						return false;
					if (day.equals(lastDay))
						break;
				}
			}

			Calendar calendar = new GregorianCalendar();
			calendar.setTime(m_formatter.parse(chapelInfo.getString("Date")));
			calendar.set(Calendar.HOUR_OF_DAY, 14);
			calendar.set(Calendar.MINUTE, 15);
			if (calendar.after(m_today))
				m_isCurrentOrFutureWeek = true;

			m_scheduleList.add(chapelInfo);
		}
		catch (Exception e)
		{
			Log.e(TAG, "addDay()", e);
		}

		return true;
	}

	public boolean isCurrentOrFutureWeek()
	{
		return m_isCurrentOrFutureWeek;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder("<html><head><style type=\"text/css\"> h1 { font-size: 1.1em; font-weight: bold; "
				+ "text-align: center; color:#CC6600; } h2 { text-align: center; font-size: 0.9em; } h3 { text-align: center; font-style:italic; font-size: 0.8em;}"
				+ "p { text-align: center; font-size: 0.7em; } </style></head><body>");

		for (JSONObject day : m_scheduleList)
		{
			try
			{
				boolean isSpecialSeries = day.getBoolean("Special Series");

				builder.append("<h1>" + day.getString("Date") + "</h1>");

				if (isSpecialSeries)
					builder.append("<span style=\"color:#000066;\">");

				builder.append("<h2>" + day.getString("Title") + "</h2>");
				builder.append("<h3>" + day.getString("Speakers") + "</h3>");
				builder.append("<p>" + day.getString("Description") + "</p>");

				if (isSpecialSeries)
					builder.append("</span>");

				builder.append("<hr />");
			}
			catch (JSONException e)
			{
				Log.e(TAG, "toString()", e);
			}
		}
		builder.append("</body></html>");
		return builder.toString();
	}

	private static final String TAG = ChapelWeek.class.toString();
	private static final String[] weekdays = new String[] { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday",
			"Saturday" };

	private final List<JSONObject> m_scheduleList = new ArrayList<JSONObject>(5);
	private final SimpleDateFormat m_formatter = new SimpleDateFormat("E, MMM d, y", Locale.US);
	private final Calendar m_today = new GregorianCalendar();

	private boolean m_isCurrentOrFutureWeek;
}

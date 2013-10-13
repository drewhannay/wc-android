package com.wheaton.app;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class WhosWhoResults extends ListActivity
{
	public static final String RESULTS_KEY = "searchResults";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whos_who_result_list);

		BITMAP_OPTIONS.inSampleSize = IN_SAMPLE_SIZE;

		try
		{
			JSONObject jsonObject = new JSONObject(getIntent().getExtras().getString(RESULTS_KEY));
			m_results = jsonObject.getJSONArray("search_results");
			setListAdapter(new WhosWhoAdapter());

			m_progressBar = (ProgressBar) findViewById(R.id.progressBar);
			m_progressBar.setVisibility(View.VISIBLE);
			m_progressBar.setEnabled(true);

			m_bitmaps = new ArrayList<Bitmap>();
			// Load profile images on a background thread
			m_loadBitmapTask = new LoadBitmapTask();
			m_loadBitmapTask.execute();

			getListView().setOnItemClickListener(m_sendEmailClickListener);
		}
		catch (Exception e)
		{
			Log.e(TAG, "onCreate", e);
		}
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		if (m_loadBitmapTask != null)
			m_loadBitmapTask.cancel(true);
	}

	private static Bitmap getBitmapFromURL(String src)
	{
		try
		{
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input, null, BITMAP_OPTIONS);
			return myBitmap;
		}
		catch (IOException e)
		{
			Log.e(TAG, "getBitmapFromURL", e);
			return null;
		}
	}

	private class WhosWhoAdapter extends BaseAdapter
	{
		@Override
		public int getCount()
		{
			return m_results.length();
		}

		@Override
		public Object getItem(int position)
		{
			try
			{
				return m_results.get(position);
			}
			catch (JSONException e)
			{
				Log.e(TAG, "getItem(position=" + position + ")", e);
				return null;
			}
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			ViewHolder holder;
			if (convertView == null)
			{
				convertView = View.inflate(WhosWhoResults.this, R.layout.whos_who_search_result, null);
				holder = new ViewHolder();
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.cpo = (TextView) convertView.findViewById(R.id.cpo);
				holder.type = (TextView) convertView.findViewById(R.id.type);
				holder.classification = (TextView) convertView.findViewById(R.id.classification);
				holder.department = (TextView) convertView.findViewById(R.id.department);
				holder.picture = (ImageView) convertView.findViewById(R.id.picture);

				convertView.setTag(holder);
			}
			else
			{
				holder = (ViewHolder) convertView.getTag();
			}

			try
			{
				holder.picture.setImageBitmap(m_bitmaps.get(position));

				JSONObject resultJSON = m_results.getJSONObject(position);

				String firstName = resultJSON.getString("FirstName");
				String preferredName = resultJSON.getString("PrefFirstName");
				String middleName = resultJSON.getString("MiddleName");
				String lastName = resultJSON.getString("LastName");

				StringBuilder builder = new StringBuilder();
				if (!isNullOrEmpty(firstName))
					builder.append(firstName);
				if (!isNullOrEmpty(preferredName))
				{
					builder.append(" \"");
					builder.append(preferredName);
					builder.append("\"");
				}
				if (!isNullOrEmpty(middleName))
				{
					builder.append(' ');
					builder.append(middleName);
				}
				if (!isNullOrEmpty(lastName))
				{
					builder.append(' ');
					builder.append(lastName);
				}
				holder.name.setText(builder.toString());

				setTextOrHideTextView("CPO ", resultJSON.getString("CPOBox"), holder.cpo);
				setTextOrHideTextView("", resultJSON.getString("Classification"), holder.classification);
				if (resultJSON.getInt("Type") == 1)
					holder.type.setText("Faculty/Staff");
				else
					holder.type.setText("Student");
				setTextOrHideTextView("Department: ", resultJSON.getString("Dept"), holder.department);
			}
			catch (Exception e)
			{
				Log.e(TAG, "getView(position=" + position + ")", e);
			}

			return convertView;
		}

		private void setTextOrHideTextView(String prefix, String text, TextView view)
		{
			if (!isNullOrEmpty(text))
				view.setText(prefix + text);
			else
				view.setVisibility(View.GONE);
		}

		private boolean isNullOrEmpty(String string)
		{
			return string == null || string.equals("") || string.equals("null");
		}
	}

	private static class ViewHolder
	{
		TextView name;
		TextView cpo;
		TextView type;
		TextView classification;
		TextView department;
		ImageView picture;
	}

	private class LoadBitmapTask extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected Void doInBackground(Void ... voids)
		{
			m_progressBar.setMax(m_results.length());

			for (int i = 0; i < m_results.length(); i++)
			{
				try
				{
					m_bitmaps.add(getBitmapFromURL(m_results.getJSONObject(i).getString("PhotoUrl")));
				}
				catch (JSONException e)
				{
					Log.e("Thread.run()", e.getLocalizedMessage());
				}

				// Make sure we're still running
				if (isCancelled())
					return null;

				// Update the progress bar
				m_handler.post(new Runnable()
				{
					@Override
					public void run()
					{
						m_progressBar.incrementProgressBy(1);
					}
				});
			}
			runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					m_progressBar.setVisibility(View.GONE);
					getListView().setVisibility(View.VISIBLE);
				}
			});

			m_loadBitmapTask = null;
			return null;
		}

	}

	private static final String TAG = WhosWhoResults.class.toString();
	private static final BitmapFactory.Options BITMAP_OPTIONS = new BitmapFactory.Options();
	private static final int IN_SAMPLE_SIZE = 2;

	private final Handler m_handler = new Handler();
	private final OnItemClickListener m_sendEmailClickListener = new OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			try
			{
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("message/rfc822");
				intent.putExtra(Intent.EXTRA_EMAIL, new String[] { m_results.getJSONObject(position).getString("Email") } );
				startActivity(Intent.createChooser(intent, getString(R.string.send_mail)));
			}
			catch (ActivityNotFoundException e)
			{
				Toast.makeText(WhosWhoResults.this, R.string.no_email_clients, Toast.LENGTH_SHORT).show();
			}
			catch (JSONException e)
			{
				Log.e(TAG, "position=" + position, e);
			}
		}
	};
	
	private AsyncTask<Void, Void, Void> m_loadBitmapTask;
	private ProgressBar m_progressBar;
	private JSONArray m_results;
	private List<Bitmap> m_bitmaps;
}

package com.wheaton.app;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.Overlay;

/*
 * Class to display campus map with pins at each building.
 * @author Drew Hannay, Andrew Wolfe
 * December 28, 2010
 */
public class MapScreen extends MapActivity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);

		m_mapView = (TapControlledMapView) findViewById(R.id.map);
		m_mapView.setBuiltInZoomControls(true);

		// dismiss pop up with single tap
		m_mapView.setOnSingleTapListener(new TapControlledMapView.OnSingleTapListener()
		{
			@Override
			public boolean onSingleTap(MotionEvent e)
			{
				m_itemizedOverlay.hideAllBalloons();
				return true;
			}
		});

		m_overlays = m_mapView.getOverlays();
		m_pinDrawable = getResources().getDrawable(R.drawable.map_pin_orange);
		new LoadJSONTask(MainScreen.MAP_PINS_URL).execute();
	}

	@Override
	protected boolean isRouteDisplayed()
	{
		return false;
	}

	private void onLoadJSONSucceeded(String jsonString)
	{
		JSONArray jsonArray = null;
		GeoPoint point;
		try
		{
			JSONObject jsonObject = new JSONObject(jsonString);
			jsonArray = jsonObject.getJSONArray("pins");

			m_itemizedOverlay = new SimpleItemizedOverlay(m_pinDrawable, m_mapView);
			m_itemizedOverlay.setShowClose(false);
			m_itemizedOverlay.setShowDisclosure(true);

			m_purpleOverlay = new SimpleItemizedOverlay(getResources().getDrawable(R.drawable.map_pin_blue), m_mapView);
			m_purpleOverlay.setShowClose(false);
			m_purpleOverlay.setShowDisclosure(true);

			int latitude;
			int longitude;
			WheatonOverlayItem overlayItem;
			for (int i = 0; i < jsonArray.length(); i++)
			{
				JSONObject pin = jsonArray.getJSONObject(i);
				latitude = (int) (Double.parseDouble(pin.getString("latitude")) * 1E6);
				longitude = (int) (Double.parseDouble(pin.getString("longitude")) * 1E6);
				point = new GeoPoint(latitude, longitude);

				overlayItem = new WheatonOverlayItem(point, pin.getString("name"), null);

				String url = pin.optString("url");
				if (!url.equals(""))
					overlayItem.setURL(url);

				overlayItem.setIsPurple(pin.optBoolean("isPurple", false));

				if (overlayItem.isPurple())
					m_purpleOverlay.addOverlay(overlayItem);
				else
					m_itemizedOverlay.addOverlay(overlayItem);
			}

			m_overlays.add(m_itemizedOverlay);
			if (m_purpleOverlay.size() > 0)
				m_overlays.add(m_purpleOverlay);

			m_mapView.invalidate();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		point = new GeoPoint(41870024, -88098384);

		MapController mc = m_mapView.getController();
		mc.animateTo(point);
		mc.setZoom(20);

		m_mapView.setSatellite(true);
	}

	private class LoadJSONTask extends AsyncTask<Void, Void, String>
	{
		public LoadJSONTask(String url)
		{
			m_url = url;
		}

		@Override
		protected String doInBackground(Void... params)
		{
			StringBuilder builder = new StringBuilder();
			HttpClient client = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(m_url);
			try
			{
				HttpResponse response = client.execute(httpGet);
				StatusLine statusLine = response.getStatusLine();
				int statusCode = statusLine.getStatusCode();
				if (statusCode == 200)
				{
					HttpEntity entity = response.getEntity();
					InputStream content = entity.getContent();
					BufferedReader reader = new BufferedReader(new InputStreamReader(content));
					String line;
					while ((line = reader.readLine()) != null)
					{
						builder.append(line);
					}
				}
				else
				{
					Log.e(MapScreen.class.toString(), "Failed to download file:" + statusCode);
				}
			}
			catch (Exception e)
			{
				Log.e("readURL", e.getMessage());
			}
			return builder.toString();
		}

		@Override
		protected void onPostExecute(String result)
		{
			if (isCancelled())
				return;
			if (result != null)
				onLoadJSONSucceeded(result);
		}

		private final String m_url;
	}

	private TapControlledMapView m_mapView;
	private SimpleItemizedOverlay m_itemizedOverlay;
	private SimpleItemizedOverlay m_purpleOverlay;
	private List<Overlay> m_overlays;
	private Drawable m_pinDrawable;
}

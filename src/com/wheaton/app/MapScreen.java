package com.wheaton.app;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.Overlay;
import com.wheaton.utility.LoadURLTask;

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
		new LoadURLTask(MainScreen.MAP_PINS_URL, new LoadURLTask.RunnableOfT<String>()
		{
			@Override
			public void run(String result)
			{
				onLoadURLSucceeded(result);
			}
		}).execute();
	}

	@Override
	protected boolean isRouteDisplayed()
	{
		return false;
	}

	private void onLoadURLSucceeded(String jsonString)
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

	private TapControlledMapView m_mapView;
	private SimpleItemizedOverlay m_itemizedOverlay;
	private SimpleItemizedOverlay m_purpleOverlay;
	private List<Overlay> m_overlays;
	private Drawable m_pinDrawable;
}

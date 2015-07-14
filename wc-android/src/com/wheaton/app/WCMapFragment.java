package com.wheaton.app;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class WCMapFragment extends android.support.v4.app.Fragment
{
	static final LatLng QUAD = new LatLng(41.870016, -88.098362);

	public WCMapFragment() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		View mRootView = inflater.inflate(R.layout.fragment_map, container);


/*		MapView mapView = (MapView) mRootView.findViewById(R.id.mapView);

		mapView.onCreate(savedInstanceState);
		mapView.onResume();		

		setUpMapIfNeeded(mapView);*/

		return mRootView;
	}	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (mMap != null)
			mMap.setMyLocationEnabled(true);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if (mMap != null)
			mMap.setMyLocationEnabled(false);
	}

	private void setUpMapIfNeeded(MapView mapView) {
		if (mMap == null) {
			
			mMap = mapView.getMap();
			mMap.getUiSettings().setMyLocationButtonEnabled(false);
			mMap.setMyLocationEnabled(true);

			try {
				MapsInitializer.initialize(this.getActivity());
			} catch (GooglePlayServicesNotAvailableException e) {
				e.printStackTrace();
			}

			if (mMap != null) {
				setUpMap();
			}
		}
	}
	private void setUpMap() {
		mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(QUAD, 19));
		mMap.setMapType(2);

		/*
		new LoadURLTask(MainScreen.MAP_PINS_URL, new LoadURLTask.RunnableOfT<String>() {
			@Override
			public void run(String result) {
				onLoadURLSucceeded(result);
			}
		}).execute();
		*/
	}

	private void onLoadURLSucceeded(String jsonString) {
		try {
			JSONArray jsonArray = new JSONArray(jsonString);

			Double latitude;
			Double longitude;
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject pin = jsonArray.getJSONObject(i);
				latitude = Double.parseDouble(pin.getJSONObject("location").getString("latitude"));
				longitude = Double.parseDouble(pin.getJSONObject("location").getString("longitude"));

				mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(pin.getString("name")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private GoogleMap mMap;
}

package com.wheaton.app;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wheaton.utility.LoadURLTask;

public class MapScreen extends Fragment
{
	static final LatLng QUAD = new LatLng(41.870016, -88.098362);
	
	public MapScreen() {
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		
		View mRootView = inflater.inflate(R.layout.map, container, false);
		
		setUpMapIfNeeded();
		
		return mRootView;
	}	
	
	private void setUpMapIfNeeded() {
        if (mMap == null) {

            mMap = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            if (mMap != null) {
                setUpMap();
            }
        }
    }
	private void setUpMap() {
		mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(QUAD, 19));
		mMap.setMapType(2);
        
		new LoadURLTask(MainScreen.MAP_PINS_URL, new LoadURLTask.RunnableOfT<String>() {
			@Override
			public void run(String result) {
				onLoadURLSucceeded(result);
			}
		}).execute();
    }
	
	private void onLoadURLSucceeded(String jsonString) {
		JSONArray jsonArray = null;
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			jsonArray = jsonObject.getJSONArray("locations");

			Double latitude;
			Double longitude;
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject pin = jsonArray.getJSONObject(i);
				latitude = Double.parseDouble(pin.getString("latitude"));
				longitude = Double.parseDouble(pin.getString("longitude"));
				
				mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(pin.getString("name")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private GoogleMap mMap;
}

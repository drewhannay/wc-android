package com.wheaton.app;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class SimpleItemizedOverlay extends BalloonItemizedOverlay<OverlayItem>
{
	public SimpleItemizedOverlay(Drawable defaultMarker, MapView mapView)
	{
		super(boundCenter(defaultMarker), mapView);
		m_context = mapView.getContext();
	}

	public void addOverlay(OverlayItem overlay)
	{
		m_overlays.add(overlay);
		populate();
	}

	@Override
	protected OverlayItem createItem(int i)
	{
		return m_overlays.get(i);
	}

	@Override
	public int size()
	{
		return m_overlays.size();
	}

	@Override
	protected boolean onBalloonTap(int index, OverlayItem item)
	{
		if (!(item instanceof WheatonOverlayItem))
			return false;

		WheatonOverlayItem overlayItem = (WheatonOverlayItem) item;
		if (overlayItem.getURL() == null)
			return false;

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(overlayItem.getURL()));
		m_context.startActivity(intent);
		return true;
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow)
	{
		super.draw(canvas, mapView, false);
	}

	private Context m_context;
	private List<OverlayItem> m_overlays = new ArrayList<OverlayItem>();
}

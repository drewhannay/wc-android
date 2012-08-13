package com.wheaton.app;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class WheatonOverlayItem extends OverlayItem
{
	public WheatonOverlayItem(GeoPoint point, String title, String snippet)
	{
		super(point, title, snippet);
	}

	public void setURL(String url)
	{
		m_url = url;
	}

	public void setIsPurple(boolean isPurple)
	{
		m_isPurple = isPurple;
	}

	public String getURL()
	{
		return m_url;
	}

	public boolean isPurple()
	{
		return m_isPurple;
	}
	
	private String m_url;
	private boolean m_isPurple;
}

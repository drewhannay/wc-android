package com.wheaton.app;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public abstract class BalloonItemizedOverlay<Item extends OverlayItem> extends ItemizedOverlay<Item>
{
	/**
	 * Create a new BalloonItemizedOverlay
	 * 
	 * @param defaultMarker - A bounded Drawable to be drawn on the map for each
	 * item in the overlay.
	 * @param mapView - The view upon which the overlay items are to be drawn.
	 */
	public BalloonItemizedOverlay(Drawable defaultMarker, MapView mapView)
	{
		super(defaultMarker);
		m_mapView = mapView;
		m_viewOffset = 0;
		m_mapController = mapView.getController();
	}

	/**
	 * Set the horizontal distance between the marker and the bottom of the
	 * information balloon. The default is 0 which works well for center bounded
	 * markers. If your marker is center-bottom bounded, call this before adding
	 * overlay items to ensure the balloon hovers exactly above the marker.
	 * 
	 * @param pixels - The padding between the center point and the bottom of
	 * the information balloon.
	 */
	public void setBalloonBottomOffset(int pixels)
	{
		m_viewOffset = pixels;
	}

	public int getBalloonBottomOffset()
	{
		return m_viewOffset;
	}

	/**
	 * Override this method to handle a "tap" on a balloon. By default, does
	 * nothing and returns false.
	 * 
	 * @param index - The index of the item whose balloon is tapped.
	 * @param item - The item whose balloon is tapped.
	 * @return true if you handled the tap, otherwise false.
	 */
	protected boolean onBalloonTap(int index, Item item)
	{
		return false;
	}

	/**
	 * Override this method to perform actions upon an item being tapped before
	 * its balloon is displayed.
	 * 
	 * @param index - The index of the item tapped.
	 */
	protected void onBalloonOpen(int index)
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.android.maps.ItemizedOverlay#onTap(int)
	 */
	@Override
	// protected final boolean onTap(int index) {
	public final boolean onTap(int index)
	{
		m_handler.removeCallbacks(finishBalloonInflation);
		isInflating = true;
		m_handler.postDelayed(finishBalloonInflation, BALLOON_INFLATION_TIME);

		hideBalloon();

		m_currentFocusedIndex = index;
		m_currentFocusedItem = createItem(index);
		setLastFocusedIndex(index);

		onBalloonOpen(index);
		createAndDisplayBalloonOverlay();

		if (m_snapToCenter)
			animateTo(index, m_currentFocusedItem.getPoint());

		return true;
	}

	/**
	 * Animates to the given center point. Override to customize how the MapView
	 * is animated to the given center point
	 * 
	 * @param index The index of the item to center
	 * @param center The center point of the item
	 */
	protected void animateTo(int index, GeoPoint center)
	{
		m_mapController.animateTo(center);
	}

	/**
	 * Creates the balloon view. Override to create a sub-classed view that can
	 * populate additional sub-views.
	 */
	protected BalloonOverlayView<Item> createBalloonOverlayView()
	{
		return new BalloonOverlayView<Item>(getMapView().getContext(), getBalloonBottomOffset());
	}

	/**
	 * Expose map view to subclasses. Helps with creation of balloon views.
	 */
	protected MapView getMapView()
	{
		return m_mapView;
	}

	/**
	 * Makes the balloon the topmost item by calling View.bringToFront().
	 */
	public void bringBalloonToFront()
	{
		if (m_balloonView != null)
			m_balloonView.bringToFront();
	}

	/**
	 * Sets the visibility of this overlay's balloon view to GONE and unfocus
	 * the item.
	 */
	public void hideBalloon()
	{
		if (m_balloonView != null)
			m_balloonView.setVisibility(View.GONE);
		m_currentFocusedItem = null;
	}

	/**
	 * Hides the balloon view for any other BalloonItemizedOverlay instances
	 * that might be present on the MapView.
	 * 
	 * @param overlays - list of overlays (including this) on the MapView.
	 */
	private void hideOtherBalloons(List<Overlay> overlays)
	{
		for (Overlay overlay : overlays)
		{
			if (overlay instanceof BalloonItemizedOverlay<?> && overlay != this)
				((BalloonItemizedOverlay<?>) overlay).hideBalloon();
		}
	}

	public void hideAllBalloons()
	{
		if (!isInflating)
		{
			List<Overlay> mapOverlays = m_mapView.getOverlays();
			if (mapOverlays.size() > 1)
				hideOtherBalloons(mapOverlays);

			hideBalloon();
		}
	}

	/**
	 * Sets the onTouchListener for the balloon being displayed, calling the
	 * overridden {@link #onBalloonTap} method.
	 */
	private OnTouchListener createBalloonTouchListener()
	{
		return new OnTouchListener()
		{
			float startX;
			float startY;

			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				View l = ((View) v.getParent()).findViewById(R.id.balloon_main_layout);
				Drawable d = l.getBackground();

				if (event.getAction() == MotionEvent.ACTION_DOWN)
				{
					if (d != null)
					{
						int[] states = { android.R.attr.state_pressed };
						if (d.setState(states))
						{
							d.invalidateSelf();
						}
					}
					startX = event.getX();
					startY = event.getY();
					return true;
				}
				else if (event.getAction() == MotionEvent.ACTION_UP)
				{
					if (d != null)
					{
						int newStates[] = {};
						if (d.setState(newStates))
						{
							d.invalidateSelf();
						}
					}
					if (Math.abs(startX - event.getX()) < 40 && Math.abs(startY - event.getY()) < 40)
					{
						// call overridden method
						onBalloonTap(m_currentFocusedIndex, m_currentFocusedItem);
					}
					return true;
				}
				else
				{
					return false;
				}

			}
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.android.maps.ItemizedOverlay#getFocus()
	 */
	@Override
	public Item getFocus()
	{
		return m_currentFocusedItem;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.android.maps.ItemizedOverlay#setFocus(Item)
	 */
	@Override
	public void setFocus(Item item)
	{
		super.setFocus(item);
		m_currentFocusedIndex = getLastFocusedIndex();
		m_currentFocusedItem = item;
		if (m_currentFocusedItem == null)
		{
			hideBalloon();
		}
		else
		{
			if (m_balloonView != null)
			{
				m_balloonView.setVisibility(View.GONE);
			}
			createAndDisplayBalloonOverlay();
		}
	}

	/**
	 * Creates and displays the balloon overlay by recycling the current balloon
	 * or by inflating it from xml.
	 * 
	 * @return true if the balloon was recycled false otherwise
	 */
	private boolean createAndDisplayBalloonOverlay()
	{
		m_balloonView = createBalloonOverlayView();
		m_clickRegion = m_balloonView.findViewById(R.id.balloon_inner_layout);
		m_clickRegion.setOnTouchListener(createBalloonTouchListener());
		m_closeRegion = m_balloonView.findViewById(R.id.balloon_close);
		if (m_closeRegion != null)
		{
			if (!m_showClose)
			{
				m_closeRegion.setVisibility(View.GONE);
			}
			else
			{
				m_closeRegion.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						hideBalloon();
					}
				});
			}
		}
		if (m_showDisclosure && !m_showClose)
		{
			View v = m_balloonView.findViewById(R.id.balloon_disclosure);
			if (v != null && m_currentFocusedItem instanceof WheatonOverlayItem
					&& ((WheatonOverlayItem) m_currentFocusedItem).getURL() != null)
			{
				v.setVisibility(View.VISIBLE);
			}
		}

		m_balloonView.setVisibility(View.GONE);

		List<Overlay> mapOverlays = m_mapView.getOverlays();
		if (mapOverlays.size() > 1)
		{
			hideOtherBalloons(mapOverlays);
		}

		if (m_currentFocusedItem != null)
			m_balloonView.setData(m_currentFocusedItem);

		GeoPoint point = m_currentFocusedItem.getPoint();
		MapView.LayoutParams params = new MapView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, point,
				MapView.LayoutParams.BOTTOM_CENTER);
		params.mode = MapView.LayoutParams.MODE_MAP;

		m_balloonView.setVisibility(View.VISIBLE);

		m_mapView.addView(m_balloonView, params);

		return false;
	}

	public void setShowClose(boolean showClose)
	{
		m_showClose = showClose;
	}

	public void setShowDisclosure(boolean showDisclosure)
	{
		m_showDisclosure = showDisclosure;
	}

	public void setSnapToCenter(boolean snapToCenter)
	{
		m_snapToCenter = snapToCenter;
	}

	public static boolean isInflating()
	{
		return isInflating;
	}

	private static Runnable finishBalloonInflation = new Runnable()
	{
		@Override
		public void run()
		{
			isInflating = false;
		}
	};

	private static final long BALLOON_INFLATION_TIME = 300;

	private static Handler m_handler = new Handler();
	private static boolean isInflating = false;

	private final MapController m_mapController;

	private MapView m_mapView;
	private BalloonOverlayView<Item> m_balloonView;
	private View m_clickRegion;
	private View m_closeRegion;
	private int m_viewOffset;
	private Item m_currentFocusedItem;
	private int m_currentFocusedIndex;
	private boolean m_showClose = true;
	private boolean m_showDisclosure = false;
	private boolean m_snapToCenter = true;
}

package com.wheaton.app;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

import com.google.android.maps.MapView;

public class TapControlledMapView extends MapView implements OnGestureListener
{
	public interface OnSingleTapListener
	{
		public boolean onSingleTap(MotionEvent e);
	}

	public TapControlledMapView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		setupGestures();
	}

	public TapControlledMapView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		setupGestures();
	}

	public TapControlledMapView(Context context, String apiKey)
	{
		super(context, apiKey);
		setupGestures();
	}

	private void setupGestures()
	{
		m_gestureDetector = new GestureDetector(this);

		// set the on Double tap listener
		m_gestureDetector.setOnDoubleTapListener(new OnDoubleTapListener()
		{

			@Override
			public boolean onSingleTapConfirmed(MotionEvent e)
			{
				if (m_singleTapListener != null)
				{
					return m_singleTapListener.onSingleTap(e);
				}
				else
				{
					return false;
				}
			}

			@Override
			public boolean onDoubleTap(MotionEvent e)
			{
				TapControlledMapView.this.getController().zoomInFixing((int) e.getX(), (int) e.getY());
				return false;
			}

			@Override
			public boolean onDoubleTapEvent(MotionEvent e)
			{
				return false;
			}

		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		if (m_gestureDetector.onTouchEvent(ev))
		{
			return true;
		}
		else
		{
			return super.onTouchEvent(ev);
		}
	}

	public void setOnSingleTapListener(OnSingleTapListener singleTapListener)
	{
		m_singleTapListener = singleTapListener;
	}

	@Override
	public boolean onDown(MotionEvent e)
	{
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e)
	{
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e)
	{
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
	{
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e)
	{
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
	{
		return false;
	}

	private GestureDetector m_gestureDetector;
	private OnSingleTapListener m_singleTapListener;
}

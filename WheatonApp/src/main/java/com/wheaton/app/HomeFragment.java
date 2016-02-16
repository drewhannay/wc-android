package com.wheaton.app;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wheaton.app.List.Item;
import com.wheaton.app.List.ListItem;
import com.wheaton.app.List.TwoTextArrayAdapter;
import com.wheaton.utility.LoadURLTask;
import com.wheaton.utility.Utils;


public class HomeFragment extends TrackedFragment {

	public HomeFragment() {
	    // Empty constructor required for fragment subclasses
    }

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		super.onCreateView(inflater, container, savedInstanceState);
		
		mRootView = inflater.inflate(R.layout.fragment_home, container, false);


        /*ViewPager viewPager = (ViewPager) mRootView.findViewById(R.id.view_pager);
        ImagePagerAdapter adapter = new ImagePagerAdapter();
        viewPager.setAdapter(adapter);*/
/*
        WindowManager wm = (WindowManager) myContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics myMetrics = new DisplayMetrics();
        display.getMetrics(myMetrics);*/
        //realViewSwitcher.setLayoutParams(new ViewGroup.LayoutParams(myMetrics.widthPixels, myMetrics.widthPixels/2));



        mLoadEventsURLTask = new LoadURLTask(MainScreen.EVENTS_CALENDAR, new LoadURLTask.RunnableOfT<String>() {
			@Override
			public void run(String result) {
				mLoadEventsURLTask = null;
				if (getActivity() != null)
					onLoadEventsURLSucceeded(result);
			}
		});
		
		mLoadSportsURLTask = new LoadURLTask(MainScreen.SPORTS_URL, new LoadURLTask.RunnableOfT<String>() {
			@Override
			public void run(String result) {
				mLoadSportsURLTask = null;
				if (getActivity() != null)
					onLoadSportsURLSucceeded(result);
			}
		});
		
		mLoadEventsURLTask.execute();
		mLoadSportsURLTask.execute();
		
		return mRootView;
	}


	private void onLoadSportsURLSucceeded(String data) {
		try {
			TextView title = (TextView)getView().findViewById(R.id.sports_title);
			title.setTypeface(null, Typeface.BOLD);
			title.setText("Sports");

			ListView lv = (ListView)getView().findViewById(R.id.sports);
			lv.setAdapter(new SportsAdapter(getActivity(), new JSONArray(data), 6));
		} catch (JSONException e) {
			Log.e(TAG, "onLoadURLSucceeded", e);
		}
	}
	
	private void onLoadEventsURLSucceeded(String data) {
		TextView title = (TextView)getView().findViewById(R.id.events_title);
		title.setTypeface(null, Typeface.BOLD);

		title.setText("Upcoming");
			List<Item> items = new ArrayList<Item>();
			List<String> titles = new ArrayList<String>();
			HashMap<String, String> day = new HashMap<String, String>();

			try {
				JSONArray events = new JSONArray(data);
				//Log.v("TAG", "LENGTH: " + events.length());
				Calendar calendar = Calendar.getInstance();

				for (int i = 0; i < events.length(); i++) {
					if(!titles.contains(events.getJSONObject(i).getString("title"))) {
						day = new HashMap<String, String>();
						day.put("item_header", events.getJSONObject(i).getString("title"));
						titles.add(events.getJSONObject(i).getString("title"));

						Calendar sessionDate = Utils.dateFromString(events.getJSONObject(i).getString("timeStamp"));
						SimpleDateFormat format1 = new SimpleDateFormat("MM/dd");
						SimpleDateFormat format2 = new SimpleDateFormat("h:mm a");
						String dateString = format1.format(sessionDate.getTime());
						String timeString = format2.format(sessionDate.getTime());

						day.put("item_date", dateString);
						day.put("item_time", timeString);

						items.add(new ListItem(day, R.layout.item_calendar_event));
					}
				}
			}
			catch (JSONException e){

			}
			ListView lv = (ListView)getView().findViewById(R.id.events);
			lv.setAdapter(new TwoTextArrayAdapter(getActivity(), items, 6));


	}




/*
//In development: banner scroll view

    private class ImagePagerAdapter extends PagerAdapter {
		public ImagePagerAdapter(){
			//obtain list of images

			mLoadImagesURLTask = new LoadURLTask(MainScreen.CHAPEL_URL, new LoadURLTask.RunnableOfT<String>() {
				@Override
				public void run(String result) {
					mLoadImagesURLTask = null;
					try {
						JSONArray images = new JSONArray(result);
						mImages = new int[images.length()];

						for(int i = 0; i < images.length(); i++){
							//first field is image url

							mImages[i] = ImageIO.read(images[i]);
							//second field is link url
						}
					}
					catch (JSONException e) {
						Log.e(TAG, "onLoadURLSucceeded", e);
					}

				}
			});
			mLoadImagesURLTask.execute();
			//determine count of images

			//create array

		}
        private int[] mImages = new int[] {
                R.drawable.chiang_mai,
                R.drawable.himeji,
        };

        @Override
        public int getCount() {
            return mImages.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((ImageView) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Context context = myContext;
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setImageResource(mImages[position]);
            ((ViewPager) container).addView(imageView, 0);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((ImageView) object);
        }
    }

*/

	private static final String TAG = ChapelFragment.class.toString();

	private LoadURLTask mLoadEventsURLTask;
	private LoadURLTask mLoadSportsURLTask;
	private LoadURLTask mLoadImagesURLTask;
	private View mRootView;
}
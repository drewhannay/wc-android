package com.wheaton.app;

import com.wheaton.utility.Utils;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.mixpanel.android.mpmetrics.MixpanelAPI;


public class MainScreen extends ActionBarActivity
{
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	public static final String CHAPEL_URL = "http://mobileapps.wheaton.edu/api/chapel";
	public static final String MAP_PINS_URL = "http://mobileapps.wheaton.edu/api/locations";
	//public static final String MAP_PINS_URL = "http://23.21.107.65/locations?contentType=json&limit=300";
	public static final String MENU_URL = "http://mobileapps.wheaton.edu/api/menu";
	//public static final String SPORTS_URL = "http://23.21.107.65/events/type/sport?contentType=json";
	public static final String SPORTS_URL = "http://mobileapps.wheaton.edu/api/sports";
	//public static final String WHOS_WHO_PREFIX = "https://www.isoncamp.us/wheaton/person";
	public static final String WHOS_WHO_PREFIX = "https://intra.wheaton.edu/whoswho/person/searchJson";
	//public static final String ACADEMIC_CALENDAR = "http://www.25livepub.collegenet.com/calendars/event-collections-general_calendar_wp.rss";
	public static final String ACADEMIC_CALENDAR = "http://mobileapps.wheaton.edu/api/academic";
	//public static final String EVENTS_CALENDAR = "http://www.25livepub.collegenet.com/calendars/intra-campus-calendar.rss";
	public static final String EVENTS_CALENDAR = "http://mobileapps.wheaton.edu/api/events";
	public static final String BANNER_URL = "http://mobileapps.wheaton.edu/api/banners";
	public static final String INTRA_URL = "http://intra.wheaton.edu";

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mPageTitles;
	private MixpanelAPI mixpanel;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		String projectToken = "ba1c3c53b3cd538357b7f85ff033c648"; // e.g.: "1ef7e30d2a58d27f4b90c42e31d6d7ad"
		mixpanel = MixpanelAPI.getInstance(this, projectToken);


		mTitle = mDrawerTitle = getTitle();
		mPageTitles = new String[]{"Home", "Campus Map", "Chapel Schedule", "Who's Who", "Academic Calendar", "Sports", "Meal Menu"};

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		// set up the drawer's list view with items and click listener
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.item_drawer, mPageTitles));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(
				this,
				mDrawerLayout,
				R.drawable.ic_drawer,
				R.string.drawer_open,
				R.string.drawer_close
				) {
			@Override
			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(mTitle);
//				invalidateOptionsMenu();
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(mDrawerTitle);
//				invalidateOptionsMenu();
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);


		if (savedInstanceState == null) {
			selectItem(0);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		mDrawerLayout.isDrawerOpen(mDrawerList);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		switch(item.getItemId()) {
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* The click listener for ListView in the navigation drawer */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			selectItem(position);
		}
	}

	private void selectItem(final int position) {
		Fragment fragment = new ChapelFragment();
		switch (position) {
		case 0:
			fragment = new HomeFragment();
			mixpanel.track("Opened Home");
			break;
		case 1:
			mixpanel.track("Opened Map");
			try {
				fragment = new WCMapFragment();
			}catch(Exception e) {
				e.getStackTrace();
			}
			break;
		case 2:
			mixpanel.track("Opened Chapel");
			fragment = new ChapelFragment();
			break;
		case 3:
			mixpanel.track("Whos Who");
			Handler h = new Handler() {
			    @Override
			    public void handleMessage(Message msg) {
			        if (msg.what != 1) {
			        	Toast.makeText(getApplicationContext(), R.string.connect_to_wheaton, Toast.LENGTH_SHORT).show();
			        } else { // code if connected
			        	Fragment fragment = new WhosWhoFragment();
						FragmentManager fragmentManager = getSupportFragmentManager();
				        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
				        mDrawerList.setItemChecked(position, true);
						setTitle(mPageTitles[position]);
						mDrawerLayout.closeDrawer(mDrawerList);
			        }
			    }
			};
			Utils.isNetworkAvailable(INTRA_URL, h, 2000);
			return;
		case 4:
			mixpanel.track("Opened Academic");
			fragment = new AcademicCalendarFragment();
			break;
		case 5:
			mixpanel.track("Opened Sports");
			fragment = new SportsFragment();
			break;
		case 6:
			mixpanel.track("Opened Menu");
			fragment = new BonAppMenu();
			break;
		}

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();


		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(mPageTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggle
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onDestroy() {
		mixpanel.flush();
		super.onDestroy();
	}
}

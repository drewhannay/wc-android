package com.wheaton.app;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainScreen extends ActionBarActivity
{
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	public static final String CHAPEL_URL = "https://s3.amazonaws.com/wcstatic/chapel.json";
	public static final String MAP_PINS_URL = "http://23.21.107.65/locations?contentType=json&limit=300";
	public static final String MENU_URL = "http://wheatonorientation.herokuapp.com/menu";
	public static final String SPORTS_URL = "http://23.21.107.65/events/type/sport?contentType=json";
	public static final String WHOS_WHO_PREFIX = "http://23.21.107.65/people?contentType=json&limit=200&name=";
	public static final String ACADEMIC_CALENDAR = "http://25livepub.collegenet.com/calendars/event-collections-general_calendar_wp.rss";
	public static final String EVENTS_CALENDAR = "http://25livepub.collegenet.com/calendars/intra-campus-calendar.rss";
	public static final String BANNER_URL = "https://s3.amazonaws.com/wcstatic/banners.json";

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mPageTitles;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
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

	private void selectItem(int position) {
		Fragment fragment = new ChapelFragment();
		
		switch (position) {
		case 0:
			fragment = new HomeFragment();
			break;
		case 1:
			try {
				fragment = new MapFragment();
			}catch(Exception e) {
				e.getStackTrace();
			}
			break;
		case 2:
			fragment = new ChapelFragment();
			break;
		case 3:
			fragment = new WhosWhoFragment();
			break;
		case 4:
			fragment = new AcademicCalendarFragment();
			break;
		case 5:
			fragment = new SportsFragment();
			break;
		case 6:
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
}

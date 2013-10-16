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
	public static final String MAP_PINS_URL = "https://s3.amazonaws.com/wcstatic/location.json";
	public static final String MENU_URL = "http://wheatonorientation.herokuapp.com/menu";
	public static final String WHOS_WHO_PREFIX = "https://webapp.wheaton.edu/whoswho/person/searchJson?page_size=100&q=";
	public static final String ACADEMIC_CALENDAR = "http://25livepub.collegenet.com/calendars/event-collections-general_calendar_wp.rss";

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
				R.layout.drawer_list_item, mPageTitles));
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
			selectItem(2);
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
		Fragment fragment = new ChapelSchedule();
		
		switch (position) {
		case 1:
			try {
				fragment = new MapScreen();
			}catch(Exception e) {
				e.getStackTrace();
			}
			break;
		case 2:
			fragment = new ChapelSchedule();
			break;
		case 3:
			fragment = new WhosWhoSearch();
			break;
		case 4:
			fragment = new AcademicCalendar();
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
//
//	private View m_whosWhoLauncher;
//	private View m_menuLauncher;
//	private View m_openFloorLauncher;
//	private View m_mapLauncher;
//	private View m_linksLauncher;
//	private View m_aboutLauncher;
//	private View m_chapelLauncher;
}

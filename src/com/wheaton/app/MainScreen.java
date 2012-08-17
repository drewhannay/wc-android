package com.wheaton.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class MainScreen extends Activity
{
	public static final String CHAPEL_URL = "http://dl.dropbox.com/u/36045671/chapel.json";
	public static final String MAP_PINS_URL = "http://dl.dropbox.com/u/36045671/mapPins.json";
	public static final String MENU_URL = "http://www.cafebonappetit.com/print-menu/cafe/339/menu/13292/days/not-today/pgbrks/0/";
	public static final String OPEN_FLOOR_URL = "https://dl-web.dropbox.com/u/36045671/openFloors.json";
	public static final String WHOS_WHO_PREFIX = "https://webapp.wheaton.edu/whoswho-dev/person/searchJson?page_size=100&q=";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		m_whosWhoLauncher = findViewById(R.id.whos_who);
		m_whosWhoLauncher.setOnClickListener(m_buttonClickListener);
		m_menuLauncher = findViewById(R.id.menu);
		m_menuLauncher.setOnClickListener(m_buttonClickListener);
		m_openFloorLauncher = findViewById(R.id.open_floor);
		m_openFloorLauncher.setOnClickListener(m_buttonClickListener);
		m_mapLauncher = findViewById(R.id.map);
		m_mapLauncher.setOnClickListener(m_buttonClickListener);
		m_linksLauncher = findViewById(R.id.links);
		m_linksLauncher.setOnClickListener(m_buttonClickListener);
		m_aboutLauncher = findViewById(R.id.about);
		m_aboutLauncher.setOnClickListener(m_buttonClickListener);
		m_chapelLauncher = findViewById(R.id.chapel);
		m_chapelLauncher.setOnClickListener(m_buttonClickListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.contact:
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(CONTACT_URL)));
			return true;
		}
		return false;
	}

	private static final String CONTACT_URL = "https://spreadsheets.google.com/viewform?formkey=dDNFamI5UGJqRDZmNFRkZW96ZHEybXc6MQ";

	private final OnClickListener m_buttonClickListener = new OnClickListener()
	{
		@Override
		public void onClick(View view)
		{
			switch (view.getId())
			{
			case R.id.whos_who:
				startActivity(new Intent(MainScreen.this, WhosWhoSearch.class));
				break;
			case R.id.menu:
				startActivity(new Intent(MainScreen.this, BonAppMenu.class));
				break;
			case R.id.open_floor:
				startActivity(new Intent(MainScreen.this, OpenFloor.class));
				break;
			case R.id.map:
				startActivity(new Intent(MainScreen.this, MapScreen.class));
				break;
			case R.id.links:
				startActivity(new Intent(MainScreen.this, Links.class));
				break;
			case R.id.about:
				startActivity(new Intent(MainScreen.this, About.class));
				break;
			case R.id.chapel:
				startActivity(new Intent(MainScreen.this, ChapelSchedule.class));
				break;
			}
		}
	};

	private View m_whosWhoLauncher;
	private View m_menuLauncher;
	private View m_openFloorLauncher;
	private View m_mapLauncher;
	private View m_linksLauncher;
	private View m_aboutLauncher;
	private View m_chapelLauncher;
}

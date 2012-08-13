package com.wheaton.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Class to run the main screen of the application. Get references to the
 * different Views in the Activity and use them to let the user launch the
 * individual sections of the application.
 * 
 * @author Drew Hannay
 * 
 */
public class MainScreen extends Activity
{
	public static final String MAP_PINS_URL = "http://dl.dropbox.com/u/36045671/mapPins.json";

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
		case R.id.clear_cache:
			for (String string : fileList())
				deleteFile(string);
			return true;
		}
		return false;
	}

	private void launchMenu()
	{
		m_progressDialog.dismiss();
		startActivity(new Intent(this, MenuHome.class));
	}

	private void launchOpenFloor()
	{
		m_progressDialog.dismiss();
		startActivity(new Intent(this, OpenFloorHome.class));
	}

	private void launchChapel()
	{
		m_progressDialog.dismiss();
		startActivity(new Intent(this, ChapelHome.class));
	}

	private static final String CONTACT_URL = "https://spreadsheets.google.com/viewform?formkey=dDNFamI5UGJqRDZmNFRkZW96ZHEybXc6MQ";

	private final OnClickListener m_buttonClickListener = new OnClickListener()
	{
		public void onClick(View view)
		{
			Intent intent;
			switch (view.getId())
			{
			case R.id.whos_who:
				intent = new Intent(MainScreen.this, StalkernetHome.class);
				startActivity(intent);
				break;
			case R.id.menu:
				// Preemptively grab the Menu information.
				Thread thread = new Thread()
				{
					@Override
					public void run()
					{
						MenuParser.parse(MainScreen.this);
						m_handler.post(m_launchMenu);
					}
				};
				thread.start();
				m_progressDialog = ProgressDialog.show(MainScreen.this, "Loading", "Please wait while menus are loaded", true, false);
				break;
			case R.id.open_floor:
				thread = new Thread()
				{
					@Override
					public void run()
					{
						OpenFloorParser.parse(MainScreen.this);
						m_handler.post(m_launchOpenFloor);
					}
				};
				thread.start();
				m_progressDialog = ProgressDialog.show(MainScreen.this, "Loading", "Please wait while schedules are loaded", true,
						false);
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
				thread = new Thread()
				{
					@Override
					public void run()
					{
						ChapelParser.parse(MainScreen.this);
						m_handler.post(m_launchChapel);
					}
				};
				thread.start();
				m_progressDialog = ProgressDialog.show(MainScreen.this, "Loading", "Please wait while the chapel schedule is loaded.",
						true, false);
				break;
			}
		}
	};

	private final Runnable m_launchMenu = new Runnable()
	{
		public void run()
		{
			launchMenu();
		}
	};

	private final Runnable m_launchOpenFloor = new Runnable()
	{
		public void run()
		{
			launchOpenFloor();
		}
	};

	private final Runnable m_launchChapel = new Runnable()
	{
		public void run()
		{
			launchChapel();
		}
	};

	// Need handler for callbacks to the UI thread
	private final Handler m_handler = new Handler();

	private ProgressDialog m_progressDialog;
	private View m_whosWhoLauncher;
	private View m_menuLauncher;
	private View m_openFloorLauncher;
	private View m_mapLauncher;
	private View m_linksLauncher;
	private View m_aboutLauncher;
	private View m_chapelLauncher;
}

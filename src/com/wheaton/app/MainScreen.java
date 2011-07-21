package com.wheaton.app;

import com.google.ads.AdRequest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Class to run the main screen of the application.
 * Get references to the different Views in the Activity and use
 * them to let the user launch the individual sections of the
 * application.
 * @author Drew Hannay
 *
 */
public class MainScreen extends Activity implements OnClickListener {
	
    // Need handler for callbacks to the UI thread
    final Handler mHandler = new Handler();
    
    private ProgressDialog pd;
    
    final Runnable launchMenu = new Runnable() {
        public void run() {
            launchMenu();
        }
    };
    final Runnable launchOpenFloor = new Runnable() {
        public void run() {
            launchOpenFloor();
        }
    };
    final Runnable launchChapel = new Runnable(){
    	public void run(){
    		launchChapel();
    	}
    };
	
	/**
	 * The Views used to control the on screen buttons and images.
	 */
	private View stalkernetLauncher, menuLauncher, openFloorLauncher, 
	mapLauncher, linksLauncher, aboutLauncher, chapelLauncher;
	
	/**
	 * Method to override the default onCreate method for an Activity.
	 * Set the layout for this activity, set up the buttons and
	 * launch Threads to preemptively parse through the Menu and
	 * Open Floor information.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		
//		AdRequest request = new AdRequest();
//
//		request.addTestDevice(AdRequest.TEST_EMULATOR);

		
		
		stalkernetLauncher = findViewById(R.id.stalkernet);
		stalkernetLauncher.setOnClickListener(this);
		menuLauncher = findViewById(R.id.menu);
		menuLauncher.setOnClickListener(this);
		//openFloorLauncher = findViewById(R.id.openFloors);
		//openFloorLauncher.setOnClickListener(this);
		mapLauncher = findViewById(R.id.map);
		mapLauncher.setOnClickListener(this);
		linksLauncher = findViewById(R.id.links);
		linksLauncher.setOnClickListener(this);
		aboutLauncher = findViewById(R.id.about);
		aboutLauncher.setOnClickListener(this);
		chapelLauncher = findViewById(R.id.chapel);
		chapelLauncher.setOnClickListener(this);
	}

	/**
	 * Method to determine what to do when a button is clicked.
	 * Use a switch statement on the given View's ID to launch
	 * the correct activity.
	 */
	public void onClick(View v) {
		Intent i;
		switch(v.getId()){
		case R.id.stalkernet:
			i = new Intent(this,StalkernetHome.class);
			startActivity(i);
			break;
		case R.id.menu:
			//Preemptively grab the Menu information.
	        Thread t = new Thread() {
	            public void run() {
	            	MenuParser.parse(MainScreen.this);
	            	mHandler.post(launchMenu);
	            }
	        };
	        t.start();
	        pd = ProgressDialog.show(this, "Loading", "Please wait while menus are loaded", true, false);
			break;
//		case R.id.openFloors:
//	        t = new Thread() {
//	        	public void run() {
//	        		OpenFloorParser.parse(MainScreen.this);
//					mHandler.post(launchOpenFloor);
//	        	}
//	        };
//	        t.start();
//	        pd = ProgressDialog.show(this, "Loading", "Please wait while schedules are loaded", true, false);
//			break;
		case R.id.map:
			i = new Intent(this, Map.class);
			startActivity(i);
			break;
		case R.id.links:
			i = new Intent(this, Links.class);
			startActivity(i);
			break;
		case R.id.about:
			i = new Intent(this, About.class);
			startActivity(i);
			break;
		case R.id.chapel:
			t = new Thread() {
				public void run() {
					ChapelParser.parse(MainScreen.this);
					mHandler.post(launchChapel);
		        }
			};
		    t.start();
		    pd = ProgressDialog.show(this, "Loading", "Please wait while the chapel schedule is loaded.", true, false);
			break;
		}
	}
	
	/**
	 * Method to override the creation of the Options Menu.
	 * Get the MenuInflater and inflate it with the appropriate
	 * XML file containing the options menu.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}
	
	/**
	 * Method to determine what to do when a menu button is pressed.
	 * Use a switch statement on the ID of the selected menu item 
	 * and launch the correct activity for that menu item.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case R.id.contact:
			//Launch the contact form in the user's web browser.
			Intent i = new Intent(Intent.ACTION_VIEW, 
					Uri.parse("https://spreadsheets.google.com/viewform?formkey=dDNFamI5UGJqRDZmNFRkZW96ZHEybXc6MQ")); 
			startActivity(i);
			return true;
		case R.id.clear_cache:
			//Delete any cached data
			for(String a:this.fileList())
				deleteFile(a);
			return true;
		}
		return false;
	}
	
    private void launchMenu() {
		pd.dismiss();
		Intent i = new Intent(this,MenuHome.class);
		startActivity(i);
    }
    private void launchOpenFloor()  {
    	pd.dismiss();
    	Intent i = new Intent(this,OpenFloorHome.class);
		startActivity(i);
    }
    private void launchChapel(){
    	pd.dismiss();
    	Intent i = new Intent(this,ChapelHome.class);
    	startActivity(i);
    }
}

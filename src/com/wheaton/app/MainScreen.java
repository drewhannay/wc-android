package com.wheaton.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class MainScreen extends Activity implements OnClickListener {
	
	private View stalkernetLauncher;
	private View menuLauncher;
	private View openFloorLauncher;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		stalkernetLauncher = findViewById(R.id.stalkernet);
		stalkernetLauncher.setOnClickListener(this);
		menuLauncher = findViewById(R.id.menu);
		menuLauncher.setOnClickListener(this);
		openFloorLauncher = findViewById(R.id.openFloors);
		openFloorLauncher.setOnClickListener(this);
		
		//Preemptively grab the Menu information.
        Thread t = new Thread() {
            public void run() {
            	MenuParser.parse(MainScreen.this);
            }
        };
        t.start();
		
	}

	public void onClick(View v) {
		Intent i;
		switch(v.getId()){
		case R.id.stalkernet:
			i = new Intent(this,StalkernetHome.class);
			startActivity(i);
			break;
		case R.id.menu:
			i = new Intent(this,MenuHome.class);
			startActivity(i);
			break;
		case R.id.openFloors:
			i = new Intent(this,OpenFloorHome.class);
			startActivity(i);
			break;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case R.id.contact:
			//Launch the contact form in the user's web browser.
			Intent i = new Intent(Intent.ACTION_VIEW, 
					Uri.parse("https://spreadsheets.google.com/viewform?formkey=dDNFamI5UGJqRDZmNFRkZW96ZHEybXc6MQ")); 
			startActivity(i);
			return true;
		}
		return false;
		
	}
}

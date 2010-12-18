package com.wheaton.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MainScreen extends Activity implements OnClickListener {
	
	View stalkernetLauncher;
	View menuLauncher;
	View openFloorLauncher;
	
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
		
	}

	public void onClick(View arg0) {
		
		
		
	}
}

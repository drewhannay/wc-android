package com.wheaton.app;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ViewAnimator;

public class ChapelHome extends Activity {
	
	/**
	 * The display through which the different day's schedules will be displayed.
	 */
	ViewAnimator display;
	
	/**
	 * Method to override the default onCreate method for an Activity.
	 * Set the layout for this Activity, then set up the buttons and 
	 * the ViewAnimator. If necessary, make the "Next" display button
	 * invisible.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chapel_main);
		
		display = (ViewAnimator) findViewById(R.id.view_area);
		
		ArrayList<View> days = ChapelParser.toArrayList(getLayoutInflater());  /*toArrayList(getLayoutInflater());*/
		for(View day:days){
			display.addView(day);
		}
		
	}

}

package com.wheaton.app;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ViewAnimator;

/**
 * Class to run the Menu section of the application.
 * Get references to the different buttons and ViewAnimator,
 * fill the ViewAnimator, and let the user scroll through 
 * the different day's menus.
 * @author Drew Hannay
 *
 */
public class MenuHome extends Activity implements OnClickListener {

	/**
	 * Buttons to control movement throughout the different menu screens.
	 */
	Button next,previous,today;
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
		setContentView(R.layout.menu_main);
		
		next = (Button) findViewById(R.id.rightButton);
		next.setOnClickListener(this);
		previous = (Button) findViewById(R.id.leftButton);
		previous.setOnClickListener(this);
		previous.setVisibility(View.INVISIBLE);//No need for a previous button at the beginning.
		today = (Button) findViewById(R.id.todayButton);
		today.setOnClickListener(this);
		display = (ViewAnimator) findViewById(R.id.view_area);
		
		ArrayList<View> days = MenuParser.toArrayList(getLayoutInflater());  /*toArrayList(getLayoutInflater());*/
		for(View day:days){
			display.addView(day);
		}
		
		//If there's only one Child View, we don't need a next button.
		if(display.getChildCount()==1)
			next.setVisibility(View.INVISIBLE);
		
	}

	/**
	 * Method to determine what to do when a button is clicked.
	 * Set in and out animations correctly and set the 
	 * visibility of the buttons correctly. Button visibility is 
	 * guaranteed to be correct at the start of any case; animations 
	 * are not.
	 */
	public void onClick(View v) {
		switch (v.getId()){
		case R.id.rightButton:
			display.showNext();
			
			//If this view is the last one in the group, don't let the
			//user click the next button.
			if(display.getDisplayedChild()==display.getChildCount()-1)
				next.setVisibility(View.INVISIBLE);
			//We just came from somewhere, so we must be able to go back there.
			previous.setVisibility(View.VISIBLE);
			break;
		case R.id.leftButton:
			display.showPrevious();
			
			//If this is the first view, there's nothing to go backwards to.
			if(display.getDisplayedChild()==0)
				previous.setVisibility(View.INVISIBLE);
			//We just came from somewhere, so we must be able to get back there.
			next.setVisibility(View.VISIBLE);
			
			break;
		case R.id.todayButton:
			
			//Don't jump if we're already at the beginning.
			if(display.getDisplayedChild()!=0){
				display.setDisplayedChild(0);
			
				//If there's only one child, we can't go forward. Otherwise we can.
				if(display.getChildCount()==1)
					next.setVisibility(View.INVISIBLE);
				else
					next.setVisibility(View.VISIBLE);
	
				//We just jumped to the first Child, so there's no previous.
				previous.setVisibility(View.INVISIBLE);
			}
			break;
		}
	}

}

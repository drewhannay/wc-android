package com.wheaton.app;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ViewAnimator;

public class OpenFloorHome extends Activity implements OnClickListener{
	
	Button next,previous,today;
	ViewAnimator display;
	public static int todayIndex = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.openfloor_main);
		
		next = (Button) findViewById(R.id.rightButton);
		next.setOnClickListener(this);
		previous = (Button) findViewById(R.id.leftButton);
		previous.setOnClickListener(this);
		if(todayIndex == 0)
			previous.setVisibility(View.INVISIBLE);//No need for a previous button at the beginning.
		else
			previous.setVisibility(View.VISIBLE);
		today = (Button) findViewById(R.id.todayButton);
		today.setOnClickListener(this);
		display = (ViewAnimator) findViewById(R.id.view_area);
		ArrayList<View> days = OpenFloorParser.toArrayList(getLayoutInflater());
		for(View day:days){
			display.addView(day);
		}
		display.setDisplayedChild(todayIndex);
		
		
		//If there's only one Child View, we don't need a next button.
		if(display.getChildAt(todayIndex+1) == null)
			next.setVisibility(View.INVISIBLE);
		else
			next.setVisibility(View.VISIBLE);
	}
	
	
	/**
	 * Method to determine what to do when a button is clicked.
	 * Set in and out animations correctly and set the 
	 * visibility of the buttons correctly. Button visibility is 
	 * guaranteed to be correct at the start of any case; animations 
	 * are not.
	 */
	public void onClick(View v) {
		if(todayIndex <0){
			todayIndex = 0;
		}
		switch (v.getId()){
		case R.id.rightButton:
			display.setOutAnimation(this, android.R.anim.slide_out_right);
			display.setInAnimation(this, android.R.anim.slide_in_left);
			display.showNext();
			
			//If this view is the last one in the group, don't let the
			//user click the next button.
			if(display.getDisplayedChild()==display.getChildCount()-1)
				next.setVisibility(View.INVISIBLE);
			//We just came from somewhere, so we must be able to go back there.
			previous.setVisibility(View.VISIBLE);
			break;
		case R.id.leftButton:
			display.setInAnimation(this, android.R.anim.fade_in);
			display.setOutAnimation(this, android.R.anim.fade_out);
			display.showPrevious();
			
			//If this is the first view, there's nothing to go backwards to.
			if(display.getDisplayedChild()==0)
				previous.setVisibility(View.INVISIBLE);
			//We just came from somewhere, so we must be able to get back there.
			next.setVisibility(View.VISIBLE);
			
			break;
		case R.id.todayButton:			
			//Don't jump if we're already at the beginning.
			if(display.getDisplayedChild()!=todayIndex){
				display.setInAnimation(this, android.R.anim.fade_in);
				display.setOutAnimation(this, android.R.anim.fade_out);
				display.setDisplayedChild(todayIndex);
			
				//If there's only one child, we can't go forward. Otherwise we can.
				if(display.getChildAt(todayIndex+1) == null)
					next.setVisibility(View.INVISIBLE);
				else
					next.setVisibility(View.VISIBLE);
	
				//We just jumped to the first Child, so there's no previous.
				if(todayIndex == 0)
					previous.setVisibility(View.INVISIBLE);
				else
					previous.setVisibility(View.VISIBLE);
			}
			break;
		}
	}
}

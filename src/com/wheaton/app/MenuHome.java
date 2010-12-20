package com.wheaton.app;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ViewAnimator;
//superfluous comment -AW
public class MenuHome extends Activity implements OnClickListener {

	Button next,previous;
	ViewAnimator display;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_main);
		
		next = (Button) findViewById(R.id.rightButton);
		next.setOnClickListener(this);
		previous = (Button) findViewById(R.id.leftButton);
		previous.setOnClickListener(this);
		display = (ViewAnimator) findViewById(R.id.view_area);
		
		ArrayList<View> days = fakeViews();//TODO Set days to the actual ArrayList of Days
		for(View day:days){
			display.addView(day);
		}
			
		
		
	}

	private ArrayList<View> fakeViews() {
		ArrayList<View> toReturn = new ArrayList<View>();
		View v1 = new View(this);
		v1.setBackgroundColor(Color.RED);
		View v2 = new View(this);
		v2.setBackgroundColor(Color.BLUE);
		View v3 = new View(this);
		v3.setBackgroundColor(Color.GREEN);
		
		toReturn.add(v1);
		toReturn.add(v2);
		toReturn.add(v3);
		
		return toReturn;
	}

	public void onClick(View v) {
		switch (v.getId()){
		case R.id.rightButton:
			display.showNext();
			break;
		case R.id.leftButton:
			display.showPrevious();
			break;
		}
		
		
	}

}

package com.wheaton.app;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Class to display links to various resources on the web.
 * Keep static, final arrays of URLs and the text that should display
 * in the ListActivity. Display the text and set the onClickListeners
 * to launch an Intent to the browser when the list items are clicked.
 * @author Drew Hannay, Andrew Wolfe
 * December 29, 2010
 *
 */
public class Links extends ListActivity {
	
	//To hold the text to be displayed for each list item.
	//Each line is numbered as a sanity check when matching to the URL array.
	private static final String[] TEXT = {
		"Academic Calendar",				//1
		"Arena Theater",					//2
		"Athletics",						//3
		"Bookstore",						//4
		"Chapel Schedule",					//5
		"Conservatory",						//6
		"Events Calendar",					//7
		"General Education Requirements",	//8
		"HoneyRock",						//9
		"Housing Calendar",					//10
		"Library",							//11
		"Long Term Calendar",				//12
		"Metra Schedule",					//13
		"My Wheaton Portal",				//14
		"Registrar's Catalog",				//15
		"WETN"								//16
		};
	//To hold the URL to launch when an item is clicked.
	private static final String[] URLS = {
		"http://wheaton.edu/Calendars/academic.html",												//1
		"http://www.wheatonarena.com",																//2
		"http://athletics.wheaton.edu",																//3
		"http://www.wheatonbooks.com",																//4
		"http://www.wheaton.edu/chaplain/Program/schedule.html",									//5
		"http://www.wheaton.edu/Conservatory",														//6
		"http://wheaton.edu/Calendars/events.html",													//7
		"http://wheaton.edu/Registrar/catalog/ug_acad_policies.htm#General_Education_Requirements",	//8
		"http://www.honeyrockcamp.org",																//9
		"http://www.wheaton.edu/reslife/dates.htm",													//10
		"http://library.wheaton.edu",																//11
		"http://www.wheaton.edu/Registrar/schedules/future_calendar.pdf",	 						//12
		"http://www.metrarail.com",																	//13
		"http://my.wheaton.edu",																	//14
		"http://www.wheaton.edu/Registrar/catalog",													//15
		"http://www.wheaton.edu/wetn"																//16
		};
	
	/**
	 * Override the default onCreate method for a ListActivity.
	 * Set the Activity's ListAdapter, enable the Text Filter,
	 * and set the onItemClickListener.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Set the ListAdapter to read in the TEXT array, using the layout link_item.xml
		setListAdapter(new ArrayAdapter<String>(this, R.layout.link_item, TEXT));

		//After getting the view, enable the text filter, so the user can type to narrow the search results.
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
	
		//Set the OnItemClickListener.
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//Launch an Intent to the browser using the URL in the same position as the TEXT that was clicked.
				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(URLS[position]));
		    	startActivity(i);
		    }
		  });
	}	

}

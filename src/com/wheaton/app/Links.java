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
		"Events Calendar",					//1
		"Academic Calendar",				//2
		"Athletics",						//3
		"Housing Calendar",					//4
		"Long Term Calendar",				//5
		"General Education Requirements",	//6
		"My Wheaton Portal",				//7
		"Chapel Schedule",					//8
		"Arena Theater",					//9
		"Bookstore",						//10
		"Registrar's Catalog",				//11
		"Conservatory",						//12
		"HoneyRock",						//13
		"Library",							//14
		"WETN",								//15
		"Metra Schedule"					//16
		};
	//To hold the URL to launch when an item is clicked.
	private static final String[] URLS = {
		"http://wheaton.edu/Calendars/events.html",													//1
		"http://wheaton.edu/Calendars/academic.html",												//2
		"http://athletics.wheaton.edu",																//3
		"http://www.wheaton.edu/reslife/dates.htm",													//4
		"http://www.wheaton.edu/Registrar/schedules/future_calendar.pdf",	 						//5
		"http://wheaton.edu/Registrar/catalog/ug_acad_policies.htm#General_Education_Requirements",	//6
		"http://my.wheaton.edu",																	//7
		"http://www.wheaton.edu/chaplain/Program/schedule.html",									//8
		"http://www.wheatonarena.com",																//9
		"http://www.wheatonbooks.com",																//10
		"http://www.wheaton.edu/Registrar/catalog",													//11
		"http://www.wheaton.edu/Conservatory",														//12
		"http://www.honeyrockcamp.org",																//13
		"http://library.wheaton.edu",																//14
		"http://www.wheaton.edu/wetn",																//15
		"http://www.metrarail.com"																	//16
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

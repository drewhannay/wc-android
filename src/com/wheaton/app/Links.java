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
		"Google",		//1
		"Facebook",		//2
		"Arena Theater",//3
		"Athletics",	//4
		"BannerWeb",	//5
		"Bookstore",	//6
		"Bon Appétit",	//7
		"Catalog",		//8
		"Conservatory",	//9
		"HoneyRock",	//10
		"Library",		//11
		"myWheaton",	//12
		"WETN"			//13
		};
	//To hold the URL to launch when an item is clicked.
	private static final String[] URLS = {
		"http://www.google.com",					//1
		"http://www.facebook.com",					//2
		"http://www.wheatonarena.com",				//3
		"http://athletics.wheaton.edu",				//4
		"https://bannerweb.wheaton.edu",			//5
		"http://www.wheatonbooks.com",				//6
		"http://www.cafebonappetit.com/wheaton",	//7
		"http://www.wheaton.edu/Registrar/catalog",	//8
		"http://www.wheaton.edu/Conservatory",		//9
		"http://www.honeyrockcamp.org",				//10
		"http://library.wheaton.edu",				//11
		"https://my.wheaton.edu",					//12
		"http://www.wheaton.edu/wetn"				//13
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

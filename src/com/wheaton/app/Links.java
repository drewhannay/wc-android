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
 * @author Drew Hannay
 * December 29, 2010
 *
 */
public class Links extends ListActivity {
	
	//To hold the text to be displayed for each list item.
	//Each line is numbered as a sanity check when matching to the URL array.
	private static final String[] TEXT = {
		"Google",		//1
		"Facebook"		//2
		};
	//To hold the URL to launch when an item is clicked.
	private static final String[] URLS = {
		"http://www.google.com",		//1
		"http://www.facebook.com"		//2
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

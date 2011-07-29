package com.wheaton.app;

import java.util.TreeMap;

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
	
	private static TreeMap<String, String> t = new TreeMap<String, String>();
	private static ArrayAdapter<String> adapter;
	
	/**
	 * Override the default onCreate method for a ListActivity.
	 * Set the Activity's ListAdapter, enable the Text Filter,
	 * and set the onItemClickListener.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		t.put("Academic Calendar","http://wheaton.edu/Academics/Academic-Calendar");
		t.put("Arena Theater","http://www.wheatonarena.com");
		t.put("Athletics","http://athletics.wheaton.edu");
		t.put("Bookstore","http://www.wheatonbooks.com");
		t.put("Chapel Schedule","http://www.wheaton.edu/Student-Life/Spiritual-Life/Chapel/Schedule");
		t.put("Conservatory","http://www.wheaton.edu/academics/departments/conservatory");
		t.put("Events Calendar","http://wheaton.edu/events");
		//TODO The gen-ed link works for now but redirects to a www2.wheaton.edu site...test later.
		t.put("General Education Requirements","http://wheaton.edu/Registrar/catalog/ug_acad_policies.htm#General_Education_Requirements");
		t.put("HoneyRock","http://www.honeyrockcamp.org");
		t.put("Housing Calendar","http://www.wheaton.edu/Student-Life/Living-at-Wheaton/Calendar");
		t.put("Library","http://library.wheaton.edu");
		t.put("Long Term Calendar","http://www.wheaton.edu/Academics/Services/Registrar/~/media/Files/Academics/Academic%20Services/Office%20of%20the%20Registrar/Schedule/future_calendar.pdf");
		t.put("Metra Schedule","http://www.metrarail.com");
		t.put("My Wheaton Portal","http://my.wheaton.edu");
		//TODO Redirects to a www2.wheaton.edu site also - but that's also what I get when following
		//a link from the new Wheaton site. Check this later in case they update the link.
		t.put("Registrar's Catalog","http://www2.wheaton.edu/Registrar/catalog/index.htm");
		t.put("WETN","http://www.wheaton.edu/wetn");
		
		//Set the ListAdapter to read in the TEXT array, using the layout link_item.xml
		String[] TEXT = new String[t.size()];
		t.keySet().toArray(TEXT);
		adapter = new ArrayAdapter<String>(this, R.layout.link_item, TEXT);
		setListAdapter(adapter);

//		registerForContextMenu(getListView());
		
		//After getting the view, enable the text filter, so the user can type to narrow the search results.
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
	
		//Set the OnItemClickListener.
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//Launch an Intent to the browser using the URL in the same position as the TEXT that was clicked.
				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse((String)t.values().toArray()[position]));
		    	startActivity(i);
		    }
		  });
	}	
}

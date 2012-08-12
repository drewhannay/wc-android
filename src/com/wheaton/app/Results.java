package com.wheaton.app;

import java.io.InputStream;
import java.net.URL;

import android.app.ListActivity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Class to display the results of a user's search.
 * Use arrays of Strings and Matches to fill the ListView
 * and set the onClickListener for each item to display 
 * the image associated with that item.
 * @author Drew Hannay, Alisa Maas, & Andrew Wolfe
 *
 */
public class Results extends ListActivity {
	
	/**
	 * Array holding matches for the user's search. To be filled by StalkernetHome.java
	 */
	public static Match[] matches;
	/**
	 * Array holding textual results of the user's search. To be filled by StalkernetHome.java
	 */
	public static String[] params;
	
	/**
	 * Override the default version of onCreate for an Activity.
	 * 
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.results);
		
		//Get the ImageView from the layout.
		final ImageView curPic = (ImageView) findViewById(R.id.curPic);
		
		//Set the ListAdapter for this ListActivity, using the params array of String results.
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, params));

		//Get a reference to the ListView and set the OnItemClickListener.
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//Set the displayed image to the image from the selected result
				curPic.setImageDrawable(LoadImageFromWebOperations(
						matches[position].getImage()));
			}
		});
	}
	
	/**
	 * Method to retrieve image from URL
	 * Open an InputStream from the URL and then create
	 * the Drawable from that InputStream.
	 * @param url The URL of the image to retrieve.
	 * @return The image, in the form of a Drawable object.
	 */
	private Drawable LoadImageFromWebOperations(String url){
	        try{
	            InputStream is = (InputStream) new URL(url).getContent();
	            Drawable d = Drawable.createFromStream(is, "src name");
	            return d;
	        }catch (Exception e) {
	        	Log.e("LoadImageFromWebOperations", e.toString());
	        	return null;
	        }
    }

	

}

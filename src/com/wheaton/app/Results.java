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

public class Results extends ListActivity {
	
	public static Match[] matches;
	public static String[] params;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.results);
		//Get the ImageView from the layout.
		final ImageView curPic = (ImageView) findViewById(R.id.curPic);

		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, params));

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
					curPic.setImageDrawable(LoadImageFromWebOperations(
							"http://intra.wheaton.edu/directory/whosnew/" + matches[position].getImage()));
			}
		});
	}
	
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

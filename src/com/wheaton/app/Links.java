package com.wheaton.app;

import java.util.TreeMap;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;
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
		
		t.put("Academic Calendar","http://wheaton.edu/Calendars/academic.html");
		t.put("Arena Theater","http://www.wheatonarena.com");
		t.put("Athletics","http://athletics.wheaton.edu");
		t.put("Bookstore","http://www.wheatonbooks.com");
		t.put("Chapel Schedule","http://www.wheaton.edu/chaplain/Program/schedule.html");
		t.put("Conservatory","http://www.wheaton.edu/Conservatory");
		t.put("Events Calendar","http://wheaton.edu/Calendars/events.html");
		t.put("General Education Requirements","http://wheaton.edu/Registrar/catalog/ug_acad_policies.htm#General_Education_Requirements");
		t.put("HoneyRock","http://www.honeyrockcamp.org");
		t.put("Housing Calendar","http://www.wheaton.edu/reslife/dates.htm");
		t.put("Library","http://library.wheaton.edu");
		t.put("Long Term Calendar","http://www.wheaton.edu/Registrar/schedules/future_calendar.pdf");
		t.put("Metra Schedule","http://www.metrarail.com");
		t.put("My Wheaton Portal","http://my.wheaton.edu");
		t.put("Registrar's Catalog","http://www.wheaton.edu/Registrar/catalog");
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
	
//	@Override
//	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
//	  super.onCreateContextMenu(menu, v, menuInfo);
//	  MenuInflater inflater = getMenuInflater();
//	  inflater.inflate(R.menu.context_menu, menu);
//	}
//	
//	@Override
//	public boolean onContextItemSelected(MenuItem item) {
//	  AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
//	  switch (item.getItemId()) {
//	  case R.id.edit:
//	    editLink(info.position);
//	    return true;
//	  case R.id.delete:
//	    deleteLink(info.position);
//	    return true;
//	  default:
//	    return super.onContextItemSelected(item);
//	  }
//	}
//
//	private void refreshScreen(){
//		String[] TEXT = new String[t.size()];
//		t.keySet().toArray(TEXT);
//		adapter = new ArrayAdapter<String>(this, R.layout.link_item, TEXT);
//		setListAdapter(adapter);
//	}
//	
//	private void deleteLink(int pos) {
//		String[] temp = new String[t.size()];
//		t.keySet().toArray(temp);
//		String toRemove = temp[pos];
//		t.remove(toRemove);
//		refreshScreen();
//	}
//
//	private void editLink(int pos) {
//		String tempName = "";
//		String tempURL = "";
//		
//		LayoutInflater factory = LayoutInflater.from(this);
//        final View textEntryView = factory.inflate(R.layout.enter_url, null);
//        AlertDialog alert = new AlertDialog.Builder(Links.this)
//        	.setTitle(R.string.edit_title)
//            .setView(textEntryView)
//            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int whichButton) {
//
//                    /* User clicked OK so do some stuff */
//                }
//            })
//            .setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int whichButton) {
//
//                    /* User clicked cancel so do some stuff */
//                }
//            }).show();
//        
//		
//		
//		
//		String[] temp = new String[t.size()];
//		t.keySet().toArray(temp);
//		String toRemove = temp[pos];
//		t.remove(toRemove);
//		t.put(tempName, tempURL);
//		refreshScreen();
//		
//	}
//	private void addLink(){
//		AlertDialog.Builder alert = new AlertDialog.Builder(Links.this);  
//		alert.setTitle("Please enter a name for the link.");  
//		final EditText input = new EditText(Links.this);  
//		alert.setView(input); 
//		final String[] hack = new String[2];
//		  
//		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {  
//			public void onClick(DialogInterface dialog, int whichButton) {
//				hack[0] = input.getText().toString();
//			}
//		
//		});
//		alert.show();
//		AlertDialog.Builder alert2 = new AlertDialog.Builder(Links.this);  
//		alert2.setTitle("Please enter a name for the link.");  
//		final EditText input2 = new EditText(Links.this);  
//		alert2.setView(input2); 
//		  
//		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {  
//			public void onClick(DialogInterface dialog, int whichButton) {
//				hack[1] = input.getText().toString();
//			}
//		
//		});
//		alert2.show();
//		t.put(hack[0], hack[1]);
//	}
}

package com.wheaton.app;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Class to display the home screen for the Stalkernet Activity.
 * Display a text box and search button to the user, collect their
 * input, and use that input to put together arrays of results
 * from the Wheaton College intranet directory.
 * @author Drew Hannay, Alisa Maas, & Andrew Wolfe
 *
 */
public class StalkernetHome extends Activity {

	/**
	 * Handler for callbacks to the UI thread.
	 */
	final Handler mHandler = new Handler();

	/**
	 * ProgressDialog to display while results are being calculated.
	 */
	private ProgressDialog pd;

	/**
	 * Runnable to launch Results Activity after calculations have been completed.
	 */
	final Runnable launchResults = new Runnable() {
		public void run() {
			//Dismiss the ProgressDialog, then create and launch the Intent.
			pd.dismiss();
			Intent i = new Intent(StalkernetHome.this, Results.class);
			startActivity(i);
		}
	};

	/**
	 * Method to override the default onCreate method for an Activity.
	 * Get references to the UI elements and set the onClickListener
	 * to start searching for the results when the user clicks the
	 * search button.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stalkernet_main);

		//Get references to the text field and search button.
		m_searchField = (EditText) findViewById(R.id.text_box);
		final Button searchButton = (Button) findViewById(R.id.search_button);

		m_searchField.setOnEditorActionListener(new TextView.OnEditorActionListener()
		{
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
			{
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
		            performSearch();
		            return true;
		        }
		        return false;
			}
		});

		//Define what happens when the user clicks the search button.
		searchButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				performSearch();
			}
		});
	}

	private void performSearch()
	{
		//Grab the text from the search box.
		String s = m_searchField.getText().toString();

		//If no text was entered, offer a helpful hint, by way of a Toast.
		if(s.equals("")){
			Toast.makeText(StalkernetHome.this, "Please enter a search term.", Toast.LENGTH_SHORT).show();
			return;
		}

		//Otherwise, replace any spaces in the query with plus signs.
		while(s.contains(" ")){
			s = s.substring(0,s.indexOf(' ')) + "+" + s.substring(s.indexOf(' ') + 1);
		}
		//Make a final variable to hold the finished string so it can be accessed from within the Thread.
		final String q  = s;

		//Run a quick check to make sure they're connected to the Wheaton Network.
		try{
			URL stalkernet = new URL("https://intra.wheaton.edu/whoswho/");
			new Scanner((InputStream) stalkernet.getContent());
		}catch(Exception e){
			Toast.makeText(StalkernetHome.this, "Please connect to the Wheaton " +
					"College network to use this app.", Toast.LENGTH_SHORT).show();
			Log.e("URL","Error!", e);
			return;
		}
		//Launch our expensive calculations in a new Thread to prevent the UI from freezing.
		Thread t = new Thread() {
			public void run() {
				Match[] matches = makeMatches(q);
				Results.params = makeStrings(matches);
				Results.matches = matches;
				mHandler.post(launchResults);
			}
		};
		t.start();
		//And show them a ProgressDialog while they wait.
		pd = ProgressDialog.show(StalkernetHome.this, "Loading", "Please wait while results are loaded", true, false);
	}

	/**
	 * Given the Matches, compile the results into
	 * a readable format.
	 * @param matches
	 * @return The String versions of all matches found.
	 */
	private static String[] makeStrings(Match[] matches) {
		//If nothing was found, return a message informing the user
		if(matches.length==0){
			String[] temp = {"No results found."};
			return temp;
		}
		//Other case, make an appropriate array of Strings
		String[] strings = new String[matches.length];

		//Go through the Matches and get the String version, compiling the results
		for(int i = 0; i<matches.length;i++)
			strings[i] = matches[i].toString();

		return strings;
	}

	/**
	 * Parse the html code after searching and compile the results 
	 * into Matches.
	 * @param param
	 * @return
	 */
	private static Match[] makeMatches(String param){
		//The URL to retrieve results from
		URL stalkernet;
		try {
			stalkernet = new URL("https://intra.wheaton.edu/whoswho/person/search?q=" + param);
			Scanner in = new Scanner((InputStream) stalkernet.getContent());

			String intraResults = "";
			while(in.hasNext())
				intraResults += in.nextLine();
			//To contain the Matches.
			ArrayList<Match> matches = new ArrayList<Match>();

			boolean noResults = intraResults.contains("<span class=\"empty\">No results found.</span></div>");
			if(noResults) throw new Exception("No results");
			intraResults = intraResults.substring(intraResults.indexOf("div class=\"span-24\"")+20);
			//as long as there are more...
			while(!(intraResults.charAt(intraResults.indexOf("div id=\"")+8)=='f')){
				intraResults = intraResults.substring(intraResults.indexOf("src"));
				//read in the address of the photo.
				String photo_file = intraResults.substring(4,intraResults.indexOf("/>")).trim();
				intraResults = intraResults.substring(intraResults.indexOf("<div class=\"name bold\">"));

				//once to get past the div class, once for the a href.
				intraResults = intraResults.substring(intraResults.indexOf(">")+1);
				intraResults = intraResults.substring(intraResults.indexOf(">")+1);

				//find the first name of the current match.
				String first_name = intraResults.substring(0,intraResults.indexOf(' ')).trim();

				//and the last name.
				String last_name = intraResults.substring(intraResults.indexOf(' ')+1,intraResults.indexOf("<")).trim();

				//and preferred first name, if listed.
				String preferred_first_name = "";
				int prefIndex = intraResults.indexOf("Preferred");
				if(prefIndex!=-1&&prefIndex<intraResults.indexOf("Type")){
					intraResults = intraResults.substring(intraResults.indexOf("</span>")+7);
					preferred_first_name = intraResults.substring(0,intraResults.indexOf("<br")).trim();
					intraResults = intraResults.substring(intraResults.indexOf("<br")+6).trim();
				}

				//find the CPO, if listed.
				String cpo = "";
				int cpoIndex = intraResults.indexOf("CPO");
				if(cpoIndex!=-1&&cpoIndex<intraResults.indexOf("Type")){
					intraResults = intraResults.substring(intraResults.indexOf("</span>")+7);
					cpo = intraResults.substring(0,intraResults.indexOf("<br")).trim();
					intraResults = intraResults.substring(intraResults.indexOf("<br")+6).trim();
				}

				//check if current person is student or employee.
				boolean student_type = false;
				int student = intraResults.indexOf("Student");
				//student case
				int staffIndex = intraResults.indexOf("Faculty/Staff");
				if((student != -1 && student < staffIndex)||
						staffIndex == -1){
					student_type = true;
				}
				String klazz = "";
				int klazzIndex = intraResults.indexOf("Class");
				if(klazzIndex!=-1&&klazzIndex<intraResults.indexOf("Type")){
					intraResults = intraResults.substring(intraResults.indexOf("</span>")+7);
					klazz = intraResults.substring(0,intraResults.indexOf("<br")).trim();
					intraResults = intraResults.substring(intraResults.indexOf("<br")+6).trim();
				}
				String department = "";
				int departmentIndex = intraResults.indexOf("Department");
				if(departmentIndex!=-1&&departmentIndex<intraResults.indexOf("Type")){
					intraResults = intraResults.substring(intraResults.indexOf("</span>")+7);
					department = intraResults.substring(0,intraResults.indexOf("<br")).trim();
					intraResults = intraResults.substring(intraResults.indexOf("<br")+6).trim();
				}

				//We have all the necessary information for the match, so compile it.
				matches.add(new Match(first_name,last_name,preferred_first_name,
						student_type,klazz,cpo,department,photo_file));

				//Get rid of the trailing </match> tag to move on to the next match.
				intraResults = intraResults.substring(intraResults.indexOf("<div id=\""));
			}

			//Move the results from an ArrayList to an array.
			Match[] finalmatches = new Match[matches.size()];
			for(int i = 0;i<finalmatches.length;i++){
				finalmatches[i] = matches.get(i);
			}
			return finalmatches;
		} catch (Exception e) {

			//TODO Do something to explain to the user why this didn't work.
		}

		//If there are no results, return an array with an empty match.
		return new Match[]{};
	}

	private EditText m_searchField;
}

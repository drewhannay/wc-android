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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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
		final EditText textBox = (EditText) findViewById(R.id.text_box);
		final Button searchButton = (Button) findViewById(R.id.search_button);
		
		//Define what happens when the user clicks the search button.
		searchButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//Grab the text from the search box.
				String s = textBox.getText().toString();

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
					URL stalkernet = new URL("http://intra.wheaton.edu/directory/whosnew/index.php/");
					new Scanner((InputStream) stalkernet.getContent());
				}catch(Exception e){
					Toast.makeText(StalkernetHome.this, "Please connect to the Wheaton " +
							"College network to use this app.", Toast.LENGTH_SHORT).show();
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
		});
	}

	/**
	 * Given the Matches, compile the results into
	 * a readable format.
	 * @param matches
	 * @return The String versions of all matches found.
	 */
	public static String[] makeStrings(Match[] matches) {
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
	public static Match[] makeMatches(String param){
		//The URL to retrieve results from
		URL stalkernet;
		try {
			stalkernet = new URL("http://intra.wheaton.edu/directory/whosnew/index.php?search_text=" + param);
			Scanner in = new Scanner((InputStream) stalkernet.getContent());
			
			String s = "";
			//Add more to the String until the entire file has been
			//read in.
			while(in.hasNext())
				s += in.next();
			//To contain the Matches.
			ArrayList<Match> matches = new ArrayList<Match>();
			
			//as long as there are more...
			while(s.contains("<match>")){
				//Find the first name of the current match
				String first_name = s.substring(s.indexOf("<first_name>")+12,s.indexOf("</first_name>"));
				if(first_name.contains("Extraresults")) //special case - too many results to process.
					break;
				//Get the last name of the current match
				String last_name = s.substring(s.indexOf("<last_name>")+11,s.indexOf("</last_name>"));
				
				//Get the middle name of the current match.
				String middle_name = s.substring(s.indexOf("<middle_name>")+13,s.indexOf("</middle_name>"));
				
				//Get the preferred first name of the match
				String preferred_first_name = s.substring(s.indexOf("<preferred_first_name>")+22,s.indexOf("</preferred_first_name>"));
				
				//Get the student type
				String student_type = s.substring(s.indexOf("<student_type>")+14,s.indexOf("</student_type>"));
				
				//Add a space, since for some reason parsing the html removes the space between the new <student type>
				student_type = student_type.substring(0,3) + " " + student_type.substring(3,student_type.length());
				
				//Get the year entered
				String year_entered = s.substring(s.indexOf("<year_entered>")+14,s.indexOf("</year_entered>"));
				
				//Get the address of the photo
				String photo_file = s.substring(s.indexOf("<photo_file>")+12,s.indexOf("</photo_file>"));
				
				//We have all the necessary information for the match, so compile it.
				matches.add(new Match(first_name,last_name,middle_name,preferred_first_name,
						student_type,year_entered,photo_file));
				
				//Get rid of the trailing </match> tag to move on to the next match.
				s = s.substring(s.indexOf("</match>")+1,s.length());
			}
			
			//Move the results from an ArrayList to an array.
			Match[] finalmatches = new Match[matches.size()];
			for(int i = 0;i<finalmatches.length;i++){
				finalmatches[i] = matches.get(i);
			}
			return finalmatches;
			
			
		} catch (Exception e) {
			Log.e("URL", e.toString());
			//TODO Do something to explain to the user why this didn't work.
		}
		
		//If there are no results, return an array with an empty match.
		return new Match[]{new Match("", "", "", "", "", "", "")};
	}

}

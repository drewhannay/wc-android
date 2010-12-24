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


public class StalkernetHome extends Activity {
	
	private EditText textBox;
	private Button searchButton;
	private String s2 = "";
    
	// Need handler for callbacks to the UI thread
    final Handler mHandler = new Handler();
    
    private ProgressDialog pd;
    
    final Runnable launchResults = new Runnable() {
        public void run() {
    		pd.dismiss();
    		Intent i = new Intent(StalkernetHome.this, Results.class);
    		startActivity(i);
        }
    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stalkernet_main);
		
		textBox = (EditText) findViewById(R.id.text_box);
		searchButton = (Button) findViewById(R.id.search_button);
		
		searchButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				String s = textBox.getText().toString();
				
				if(s.equals("")){
					Toast.makeText(StalkernetHome.this, "Please enter a search term.", Toast.LENGTH_SHORT).show();
					return;
				}
				for(int i = 0; i<s.length(); i++){
					if(s.charAt(i)==' ')
						s2+="+";
					else s2+=s.charAt(i);
				}
				try{
					URL stalkernet = new URL("http://intra.wheaton.edu/directory/whosnew/index.php/");
					new Scanner((InputStream) stalkernet.getContent());
				}catch(Exception e){
					Toast.makeText(StalkernetHome.this, "Please connect to the Wheaton College network to use this app.", Toast.LENGTH_SHORT).show();
					return;
				}
		        Thread t = new Thread() {
		            public void run() {
		            	Match[] matches = makeMatches(s2);
		            	Results.params = makeStrings(matches);
		            	Results.matches = matches;
		            	mHandler.post(launchResults);
		            }
		        };
		        t.start();
		        pd = ProgressDialog.show(StalkernetHome.this, "Loading", "Please wait while results are loaded", true, false);
			}
		});
	}
	
	public static String[] makeStrings(Match[] matches) {
		if(matches.length==0){
			String[] temp = {"No results found."};
			return temp;
		}
		String[] strings = new String[matches.length];
		
		for(int i = 0; i<matches.length;i++)
			strings[i] = matches[i].toString();
		
		return strings;
	}

	public static Match[] makeMatches(String param){
		URL stalkernet;
		try {
			stalkernet = new URL("http://intra.wheaton.edu/directory/whosnew/index.php?search_text=" + param);
			Scanner in = new Scanner((InputStream) stalkernet.getContent());
			
			String s = "";
			while(in.hasNext())
				s += in.next();
			ArrayList<Match> matches = new ArrayList<Match>();
			while(s.contains("<match>")){
				String first_name = s.substring(s.indexOf("<first_name>")+12,s.indexOf("</first_name>"));
				if(first_name.contains("Extraresults"))
					break;
				String last_name = s.substring(s.indexOf("<last_name>")+11,s.indexOf("</last_name>"));
				String middle_name = s.substring(s.indexOf("<middle_name>")+13,s.indexOf("</middle_name>"));
				String preferred_first_name = s.substring(s.indexOf("<preferred_first_name>")+22,s.indexOf("</preferred_first_name>"));
				String student_type = s.substring(s.indexOf("<student_type>")+14,s.indexOf("</student_type>"));
				student_type = student_type.substring(0,3) + " " + student_type.substring(3,student_type.length());
				String year_entered = s.substring(s.indexOf("<year_entered>")+14,s.indexOf("</year_entered>"));
				String photo_file = s.substring(s.indexOf("<photo_file>")+12,s.indexOf("</photo_file>"));
				matches.add(new Match(first_name,last_name,middle_name,preferred_first_name,
						student_type,year_entered,photo_file));
				s = s.substring(s.indexOf("</match>")+1,s.length());
			}
			Match[] finalmatches = new Match[matches.size()];
			for(int i = 0;i<finalmatches.length;i++){
				finalmatches[i] = matches.get(i);
			}
			return finalmatches;
			
			
		} catch (Exception e) {
			Log.e("URL", e.toString());
			//TODO Do something to explain to the user why this didn't work.
		}
		
		
		return new Match[]{new Match("", "", "", "", "", "", "")};
	}

}

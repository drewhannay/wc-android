package com.wheaton.app;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

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
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.results);
		//Get the ImageView from the layout.
		final ImageView curPic = (ImageView) findViewById(R.id.curPic);

		String param = getIntent().getExtras().getString("param");
		
		final Match[] matches = makeMatches(param);
		final String[] params = makeStrings(matches);
		
		
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, params));

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
	
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				try{curPic.setImageDrawable(LoadImageFromWebOperations("http://intra.wheaton.edu/directory/whosnew/" + matches[position].getImage()));}
				catch(ArrayIndexOutOfBoundsException aioobe){}
			}
		});
		
	}
	
	private String[] makeStrings(Match[] matches) {
		if(matches.length==0){
			String[] temp = {"No results found."};
			return temp;
		}
		String[] strings = new String[matches.length];
		
		for(int i = 0; i<matches.length;i++)
			strings[i] = matches[i].toString();
		
		return strings;
	}

	private Match[] makeMatches(String param){
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
	
	private Drawable LoadImageFromWebOperations(String url){
	        try
	        {
	            InputStream is = (InputStream) new URL(url).getContent();
	            Drawable d = Drawable.createFromStream(is, "src name");
	            return d;
	        }catch (Exception e) {
	            System.out.println("Exc="+e);
	            return null;
	        }
    }

	

}

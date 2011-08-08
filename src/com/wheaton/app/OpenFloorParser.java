package com.wheaton.app;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;

public class OpenFloorParser {
	
	private static boolean ERROR = false;

//	private static Calendar calendar = Calendar.getInstance(); //used to get current date
//	private static int currentMonth = calendar.get(Calendar.MONTH); 
//	private static int currentYear =(calendar.get(Calendar.YEAR));

	private static ArrayList<String> schedule;

	
	public static void parse(Context con){
		URL openFloor;
		try {
			openFloor = new URL("http://dl.dropbox.com/u/36045671/openfloor.txt");

			Scanner openFloorIn = new Scanner((InputStream) openFloor.getContent());
			String line = openFloorIn.nextLine();

			//Skip past the example text
			while(openFloorIn.hasNext()&&(!(line.contains("-----"))))
				line = openFloorIn.nextLine();
			line = openFloorIn.nextLine();//And skip the line of dashes
			line = openFloorIn.nextLine();//And the "END EXAMPLE!" line
			line = openFloorIn.nextLine();//And the next line of dashes
			

			schedule = new ArrayList<String>();

			while(openFloorIn.hasNext()){
				String text = "<head><style type=\"text/css\"> h1 { font-size: 1.1em; font-weight: bold; " +
					"text-align: center; color:#CC6600; } h2 { font-size: 0.9em; } h3 { font-style:italic; font-size: 0.8em;}" +
					"p { font-size: 0.7em; } </style></head>";
				while((line.equals("") || line.contains("-----"))&& openFloorIn.hasNext())
					line = openFloorIn.nextLine();
				text += "<h1>" + line.trim() + "</h1>";
				while(openFloorIn.hasNext() && !line.contains("Open Fischer Floors:"))
					line = openFloorIn.nextLine();
				text += "<h2>" + line.trim() + "</h2>";
				line = openFloorIn.nextLine();
				while(openFloorIn.hasNext()&&(!(line.contains("Open Smith/Traber Floors:")))){			
					text += line.trim() + "<br />";
					line = openFloorIn.nextLine();
				}
				text += "<h2>" + line.trim() + "</h2>";
				line = openFloorIn.nextLine();
				while(openFloorIn.hasNext()&&(!(line.contains("-----")))){			
					text += line.trim() + "<br />";
					line = openFloorIn.nextLine();
				}
				text += "</body></html>";
				schedule.add(text);
				
				
				
			}

		} catch (Exception e) {
			Log.e("OpenFloorParser.parse()",e.toString());
			for(StackTraceElement s: e.getStackTrace()){
				Log.e("OpenFloorParser.parse()",s.toString()); //Print the WHOLE stack trace so we can tell
				//where this error actually came from
			}

			ERROR = true;
		}
	}
	
	public static ArrayList<View> toArrayList(LayoutInflater l){
		ArrayList<View> toReturn = new ArrayList<View>();
		WebView v;
		String webCode;
		if(ERROR){
			v = (WebView) l.inflate(R.layout.food_menu, null).findViewById(R.id.web);
			webCode = "<html><head><style type=\"text/css\"> h1 { font-size: 1.2em; font-weight: bold; " +
			"text-align: center; }</style></head><body><br/><br/><br/><br/><h1>The open floor schedule is not yet available. Check back soon!</h1></body></html>";
			v.loadData(webCode, "text/html", "utf-8");
			v.setBackgroundColor(0);
			toReturn.add(v);
			return toReturn;
		}
		for(String day:schedule){
			v = (WebView) l.inflate(R.layout.food_menu, null).findViewById(R.id.web);
			v.loadData(day, "text/html", "utf-8");
			v.setBackgroundColor(0);
			toReturn.add(v);
		}
		return toReturn;
	}

}

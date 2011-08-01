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

public class ChapelParser{

	private static boolean ERROR = false;

//	private static Calendar calendar = Calendar.getInstance(); //used to get current date
//	private static int currentMonth = calendar.get(Calendar.MONTH); 
//	private static int currentYear =(calendar.get(Calendar.YEAR));

	private static ArrayList<ChapelWeek> schedule;


	public static void parse(Context con){
		URL chapel;
		try {
			chapel = new URL("http://dl.dropbox.com/u/35618101/Chapel%20Schedule.txt");

			Scanner chapelin = new Scanner((InputStream) chapel.getContent());
			String line = chapelin.nextLine();

			//Skip past the example text
			while(chapelin.hasNext()&&(!(line.contains("-----"))))
				line = chapelin.nextLine();
			line = chapelin.nextLine();//And skip the line of dashes


			ChapelWeek week;
			String[] info;

			week = new ChapelWeek();
			schedule = new ArrayList<ChapelWeek>();

			while(chapelin.hasNext()&&(!(line.contains("-----")))){
				String text = "";
				while(chapelin.hasNext()&&(!(line.contains("-----")))){
					text += line;
					line = chapelin.nextLine();
				}

				if(chapelin.hasNext()) //If we're not already at the end
					line = chapelin.nextLine();//Skip over the line of dashes
				info = parseDay(text);
				if(week.addDay(info))
					continue;
				else{
					schedule.add(week);
					week = new ChapelWeek();
					week.addDay(info);
				}
			}
			schedule.add(week);

		} catch (Exception e) {
			Log.e("ChapelParser.parse()",e.toString());
			for(StackTraceElement s: e.getStackTrace()){
				Log.e("ChapelParser.parse()",s.toString()); //Print the WHOLE stack trace so we can tell
				//where this error actually came from
			}

			ERROR = true;
		}
	}

	public static String[] parseDay(String text){
		try{
			String[] toReturn = new String[5];

			text = text.substring(text.indexOf("Date:")+5);
			toReturn[0] = text.substring(0,text.indexOf("Title:")).trim();
			text = text.substring(text.indexOf("Title:")+6);
			toReturn[1] = text.substring(0,text.indexOf("Speaker(s) [Optional]:")).trim();
			text = text.substring(text.indexOf("Speaker(s) [Optional]:")+22);
			toReturn[2] = text.substring(0,text.indexOf("Special Series? (Y/N):")).trim();
			text = text.substring(text.indexOf("Special Series? (Y/N):")+22);
			toReturn[3] = text.substring(0,text.indexOf("Description:")).trim();
			text = text.substring(text.indexOf("Description:")+12);
			toReturn[4] = text.trim();

			return toReturn;

		}catch(Exception e){
			Log.e("parseDay", e.toString());
			return new String[]{"","","","",""};
		}
	}

	public static ArrayList<View> toArrayList(LayoutInflater l){
		ArrayList<View> toReturn = new ArrayList<View>();
		WebView v;
		String webCode;
		if(ERROR){
			v = (WebView) l.inflate(R.layout.food_menu, null).findViewById(R.id.web);
			webCode = "<html><head><style type=\"text/css\"> h1 { font-size: 1.2em; font-weight: bold; " +
			"text-align: center; }</style></head><body><br/><br/><br/><br/><h1>The chapel schedule is not yet available. Check back soon!</h1></body></html>";
			v.loadData(webCode, "text/html", "utf-8");
			v.setBackgroundColor(0);
			toReturn.add(v);
			return toReturn;
		}
		for(ChapelWeek week:schedule){
			v = (WebView) l.inflate(R.layout.food_menu, null).findViewById(R.id.web);
			v.loadData(week.toString(), "text/html", "utf-8");
			v.setBackgroundColor(0);
			toReturn.add(v);
		}
		return toReturn;
	}
}
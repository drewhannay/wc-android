package com.wheaton.app;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Scanner;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;

public class ChapelParser {
	private static HashMap<String,Integer> monthToInt = new HashMap<String,Integer>();
	private static Calendar calendar = Calendar.getInstance(); //used to get current date
	private static int currentMonth = calendar.get(Calendar.MONTH); 
	private static int currentYear =(calendar.get(Calendar.YEAR));
	static{
		monthToInt.put("January",0);
		monthToInt.put("February",1);
		monthToInt.put("March", 2);
		monthToInt.put("April",3);
		monthToInt.put("May",4);
		monthToInt.put("June",5);
		monthToInt.put("July",6);
		monthToInt.put("August",7);
		monthToInt.put("September", 8);
		monthToInt.put("October",9);
		monthToInt.put("November",10);
		monthToInt.put("December",11);
		}
	public static ChapelSchedule schedule;
	
	public static void parse(Context con){
		try {
			URL chapel = new URL("http://www.wheaton.edu/chaplain/Program/schedule.html");
			Scanner chapelin = new Scanner((InputStream) chapel.getContent());
			String line = chapelin.nextLine();
			String text = "";

			while(chapelin.hasNext()&&(!(line.contains("Chapel Schedule"))))
				line = chapelin.nextLine();
			
			line = chapelin.nextLine();
			
			while(chapelin.hasNext()){
				text += line;
				if(line.contains("</table>"))
					break;
				line = chapelin.nextLine();
			}
			 schedule = new ChapelSchedule(currentMonth<8,text);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void crop(Context con){
		
	}
	
	public static ArrayList<View> toArrayList(LayoutInflater l){
		ArrayList<View> toReturn = new ArrayList<View>();
		WebView v = (WebView) l.inflate(R.layout.food_menu, null).findViewById(R.id.web);
		
		String webCode = "<html><head><style type=\"text/css\"> h1 { font-size: 1.2em; font-weight: bold; " +
					"text-align: center; }</style></head><body>";
		webCode += schedule.html;
		
		webCode += "</body></html>";
		
		v.loadData(webCode, "text/html", "utf-8");
		v.setBackgroundColor(0);
		toReturn.add(v);
		return toReturn;
	}
}

package com.wheaton.app;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;

public class ChapelParser{

	private static boolean ERROR = false;
	private static String date = "";
	private static ArrayList<ChapelWeek> schedule;


	public static void parse(Context con){
		URL chapel;
		
		Calendar calendar = Calendar.getInstance(); //used to get current date
	    int currentMonth = calendar.get(Calendar.MONTH);
	    int currentDay = (calendar.get(Calendar.DAY_OF_MONTH));
	    int currentYear =(calendar.get(Calendar.YEAR));
	    boolean todaySet = false;
	    
		 
		
		try {
			chapel = new URL("http://dl.dropbox.com/u/36045671/Chapel%20Schedule.txt");

			Scanner chapelin = new Scanner((InputStream) chapel.getContent());
			String line = chapelin.nextLine();
			int i = 0;
			ChapelHome.todayIndex = -1;
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
					StringTokenizer st = new StringTokenizer(date); //Grab the date from the Day.
		            int tempMonth = Integer.parseInt(st.nextToken());
		            boolean notToday = false;
		            if(tempMonth<currentMonth){ //If the month is less than the current month, it must be old
		                    notToday = true;
		            }
		            int tempDay = Integer.parseInt(st.nextToken());
		            if(tempMonth==currentMonth&&tempDay<currentDay){ //If the day is less than the current day, it must be old                          
		                            notToday = true;
		                    }
		            int tempYear = Integer.parseInt(st.nextToken());
		            if(tempYear<currentYear){ //If the year is less than the current year, it must be old.
		                    notToday = true;
		            }
		            if(!todaySet&&!notToday){
		            	ChapelHome.todayIndex = i;
		            	todaySet = true;
		            }
					i++;
				}
			}
			
			schedule.add(week);
			StringTokenizer st = new StringTokenizer(date); //Grab the date from the Day.
            int tempMonth = Integer.parseInt(st.nextToken());
            boolean notToday = false;
            if(tempMonth<currentMonth){ //If the month is less than the current month, it must be old
                    notToday = true;
            }
            int tempDay = Integer.parseInt(st.nextToken());
            if(tempMonth==currentMonth&&tempDay<currentDay){ //If the day is less than the current day, it must be old                          
                            notToday = true;
                    }
            int tempYear = Integer.parseInt(st.nextToken());
            if(tempYear<currentYear){ //If the year is less than the current year, it must be old.
                    notToday = true;
            }
            if(!todaySet&&!notToday){
            	ChapelHome.todayIndex = i;
            	todaySet = true;
            }
            
            if(!todaySet){
            	ERROR = true;
            	ChapelHome.todayIndex = 0;
            }
            	

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
			HashMap<String,Integer> monthToInt = new HashMap<String,Integer>();
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
			text = text.substring(text.indexOf("Date:")+5);
			toReturn[0] = text.substring(0,text.indexOf("Title:")).trim();
			String line = toReturn[0];
            line = line.substring(line.indexOf(" ")+1);
            
            int month = monthToInt.get(line.substring(0,line.indexOf(" ")));
            line = line.substring(line.indexOf(" ") +1);
            int day_n = Integer.parseInt(line.substring(0,line.indexOf(",")));
            line = line.substring(line.indexOf(" ")+1); //reliant on the fact that there IS a space
            //between the comma and year.
            int year = Integer.parseInt(line); //this means no space at end of the line, try to 
            //fix this later.
            date = month + " " + day_n + " " + year;
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
			v = (WebView) l.inflate(R.layout.webview, null).findViewById(R.id.web);
			webCode = "<html><head><style type=\"text/css\"> h1 { font-size: 1.2em; font-weight: bold; " +
			"text-align: center; }</style></head><body><br/><br/><br/><br/><h1>The chapel schedule is not yet available. Check back soon!</h1></body></html>";
			v.loadData(webCode, "text/html", "utf-8");
			v.setBackgroundColor(0);
			toReturn.add(v);
			return toReturn;
		}
		for(ChapelWeek week:schedule){
			v = (WebView) l.inflate(R.layout.webview, null).findViewById(R.id.web);
			v.loadData(week.toString(), "text/html", "utf-8");
			v.setBackgroundColor(0);
			toReturn.add(v);
		}
		return toReturn;
	}
}
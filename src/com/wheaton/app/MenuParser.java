
package com.wheaton.app;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
/**
 * A class to do the html parsing and set the appropriate instance variable, which
 * is made public. Currently, it also saves and reads the saved days stack.
 * @author Alisa Maas
 *
 */
public class MenuParser {
	
	public static ArrayList<String> dates; //TODO move inside method after done testing
	
	private static HashMap<String,Integer> monthToInt = new HashMap<String,Integer>();
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
		
	/*
	 * A Object to contain the days. It's ordered in a
	 * stack for maximum efficiency when removing expired
	 * days. Oldest days are at the top, which also happens 
	 * to be convenient for processing the days and displaying
	 * the next recent one.
	 */
	public static Stack<Day> days = new Stack<Day>();
	
	@SuppressWarnings("unchecked")
	public static void parse(Context con){
		
		//TODO Fix the saving/reading of the file, then remove this line.
		con.deleteFile("days_cache");
		
		
		if(days.empty()){
			if(con.fileList().length != 0){ 
			
        		FileInputStream f_in = null;
				try {
					f_in = con.openFileInput("days_cache");
					
					// Read object using ObjectInputStream
					ObjectInputStream obj_in = new ObjectInputStream (f_in);
					days = (Stack<Day>) obj_in.readObject(); //read in the stack, if there.
					parse(con); //This will hopefully crop any old days.
					return;
				}catch(Exception e){
					//this should never, ever happen since
					//as of now days_cache is our ONLY file.
				}
			}
			//TODO if no file, parse the html
			Stack<Day> stack = new Stack<Day>();
			//TODO actually parse here, placing the data in the stack 
			try{
				URL lunchmenu = new URL("http://www.cafebonappetit.com/wheaton/cafes/anderson/weekly_menu.html");
				URL dinnermenu = new URL("http://www.cafebonappetit.com/wheaton/cafes/anderson/weekly_menu2.html");
				Scanner lunchin = new Scanner((InputStream) lunchmenu.getContent());
				Scanner dinnerin = new Scanner((InputStream) dinnermenu.getContent());
				String lunchline = "";
				String dinnerline = "";
				ArrayList<ArrayList<String>> allLunchStations = new ArrayList<ArrayList<String>>();
				ArrayList<ArrayList<String>> allLunchItems = new ArrayList<ArrayList<String>>();
				ArrayList<ArrayList<String>> allDinnerStations = new ArrayList<ArrayList<String>>();
				ArrayList<ArrayList<String>> allDinnerItems = new ArrayList<ArrayList<String>>();
				dates = new ArrayList<String>();
				ArrayList<String> printableDates = new ArrayList<String>();
				while(lunchin.hasNext()){
					while(lunchin.hasNext()&&!lunchline.contains("align=\"center\"><strong><br>")&&!(lunchline = lunchin.nextLine()).contains("align=\"center\"><strong><br>"));
					if(!lunchin.hasNext()){
						break;
					}
					ArrayList<String> lunchstations = new ArrayList<String>();
					ArrayList<String> lunchitems = new ArrayList<String>(); 
					lunchline = lunchline.substring(lunchline.indexOf("\"center\"><strong><br>"));
					if(lunchline.indexOf(' ')==21){
						lunchline = lunchline.substring(22); //in case they have a random space
						//before the day of the week - for some reason they do for the first day, but not
						//the rest..
					}
					else lunchline = lunchline.substring(21);
					lunchline = lunchline.substring(0,lunchline.indexOf("<"));
					StringTokenizer lunch_token = new StringTokenizer(lunchline);
					//if(lunchline.contains("\"center\"><strong><br>"))
					//	lunchline = lunchline.substring(21);
					printableDates.add(lunchline);
					//StringTokenizer dinner_token = new StringTokenizer(dinnerline);
					lunch_token.nextToken();
					//dinner_token.nextToken();
					String date = "";
					date += monthToInt.get(lunch_token.nextToken())+ " ";
					String temp = lunch_token.nextToken();
					temp = (temp.contains(",")?temp.substring(0,temp.length()-1):temp);
					date+=temp + " " + lunch_token.nextToken();
					date = date.contains("<")?date.substring(0,date.indexOf("<")):date;
					dates.add(date);
					while(lunchin.hasNext()&&!lunchline.contains("align=\"center\"><strong><br>")){
					while(lunchin.hasNext()&&!lunchline.contains("<strong>"))
						lunchline = lunchin.nextLine();
					if(!lunchin.hasNext())
						break;
					if(lunchline.contains("align=\"center\"><strong><br>"))
						break;
					//lunchline = lunchin.nextLine();
					lunchline = lunchline.substring(lunchline.indexOf("ng>")+3);
					lunchline = lunchline.substring(0,lunchline.indexOf("<"));
					//Log.e("s",lunchline);
					if(lunchline.equals(""))
						continue;
					lunchstations.add(lunchline);
//					Log.e("lunch",lunchline);
					lunchline = lunchin.nextLine();
					lunchline = lunchline.substring(lunchline.indexOf(">")+1);
					while(lunchline.contains("&amp;")){
						lunchline = lunchline.substring(0,lunchline.indexOf("&amp;")+1) + lunchline.substring(lunchline.indexOf("&amp;")+5);
					}
					lunchitems.add(lunchline);
//					Log.e("lunch",lunchline);
					
					}
					allLunchStations.add(lunchstations);	
					allLunchItems.add(lunchitems);
//					Log.e("Next Day (lunch)","----------");
				}
					while(dinnerin.hasNext()){
						while(dinnerin.hasNext()&&!dinnerline.contains("align=\"center\"><strong><br>")&&!(dinnerline = dinnerin.nextLine()).contains("align=\"center\"><strong><br>"));
						if(!dinnerin.hasNext()){
							break;
						}
						ArrayList<String> dinnerstations = new ArrayList<String>();
						ArrayList<String> dinneritems = new ArrayList<String>(); 
						dinnerline = "";
						while(dinnerin.hasNext()&&!dinnerline.contains("align=\"center\"><strong><br>")){
						while(dinnerin.hasNext()&&!dinnerline.contains("<strong>"))
							dinnerline = dinnerin.nextLine();
						if(!dinnerin.hasNext())
							break;
						if(dinnerline.contains("align=\"center\"><strong><br>"))
							break;
						dinnerline = dinnerline.substring(dinnerline.indexOf("ng>")+3);
						dinnerline = dinnerline.substring(0,dinnerline.indexOf("<"));
						if(dinnerline.equals(""))
							continue;
						dinnerstations.add(dinnerline);
//						Log.e("d",dinnerline);
						dinnerline = dinnerin.nextLine();
						dinnerline = dinnerline.substring(dinnerline.indexOf(">")+1);
						while(dinnerline.contains("&amp;")){
							dinnerline = dinnerline.substring(0,dinnerline.indexOf("&amp;")+1) + dinnerline.substring(dinnerline.indexOf("&amp;")+5);
						}
						dinneritems.add(dinnerline);
//						Log.e("l",dinnerline);
						
						}
						allDinnerStations.add(dinnerstations);	
						allDinnerItems.add(dinneritems);
//						Log.e("next day","-----------");
					}
					//TODO create stack of Days here!
//					Log.e("d","making stack");
					for(int i = 0;i<=(allLunchStations.size()>allDinnerStations.size()?allLunchStations.size()-1:allDinnerStations.size()-1);i++){
						ArrayList<String> dinnerstations = (allDinnerStations.size()<=i?null:allDinnerStations.get(i));
						ArrayList<String> dinneritems = (allDinnerItems.size()<=i?null:allDinnerItems.get(i));
						ArrayList<String> lunchstations = (allLunchStations.size()<=i?null:allLunchStations.get(i));
						ArrayList<String> lunchitems = (allLunchItems.size()<=i?null:allLunchItems.get(i));
						String printableDate = printableDates.get(i);
						String date = dates.get(i);
						days.push(new Day(date,printableDate,lunchstations,lunchitems,dinnerstations,dinneritems));
						
					}
//					Log.e("d","before printing");
//					for(Iterator<Day> it = days.iterator();it.hasNext();){
//						it.next().print();
//					}
					}catch(Exception e){
						Log.e("MenuParser",e.getMessage());
					}
			if(stack.empty()){
				stack.push(new Day("Sorry, but Saga has not yet published the menu. Check back later for more details."));
			}
			else{
				
			}
		}
		else{
			//crop expired days by comparing to the current time/date
			
			Calendar calendar = Calendar.getInstance();
			int currentMonth = calendar.get(Calendar.MONTH);
			int currentDay = (calendar.get(Calendar.DAY_OF_MONTH));
			int currentYear =(calendar.get(Calendar.YEAR));
			while(!days.empty()){
				StringTokenizer st = new StringTokenizer((days.peek()).date);
				int tempMonth = Integer.parseInt(st.nextToken());
				if(tempMonth!=currentMonth){
					days.pop();
					continue;
				}
				int tempDay = Integer.parseInt(st.nextToken());
					if(tempDay<currentDay){
						days.pop();
						continue;
					}
				int tempYear = Integer.parseInt(st.nextToken());
				if(tempYear<currentYear){
					days.pop();
					continue;
				}
				break;
			}
			//if we've emptied the stack, fill it again. This will
			//perform checks again and determine to parse the html.
			//Here would be a good use for a goto if java still had one
			//to avoid extra checking. But this will never happen more than
			//once a day, at the most.
			if(days.empty())
				parse(con);
			
		}
		//Either way, at the end of the method, write the new object
		//to data in case. TODO - put in the main menu class at some point?
		//onPause? Seems like a good use for it...
		try{FileOutputStream f_out = con.openFileOutput("days_cache", Context.MODE_PRIVATE);

		// Write object with ObjectOutputStream
		ObjectOutputStream obj_out = new ObjectOutputStream(f_out);
		// Write object out to disk
		obj_out.writeObject (days);}catch(Exception e){}
	}
	
	public static ArrayList<View> toArrayList(LayoutInflater l){
		ArrayList<View> toReturn = new ArrayList<View>();
		View v;
		Day d;
		TextView t;
		for(Iterator<Day> it = days.iterator();it.hasNext();){
			d = it.next();
			v = l.inflate(R.layout.food_menu, null);
			t = (TextView) v.findViewById(R.id.date);
			t.setText(d.printableDate);
			t = (TextView) v.findViewById(R.id.lunch_items);
			t.setText(d.lunchPrint());
			t = (TextView) v.findViewById(R.id.dinner_items);
			t.setText(d.dinnerPrint());
			toReturn.add(v);
		}
		
		return toReturn;
	}
	
}

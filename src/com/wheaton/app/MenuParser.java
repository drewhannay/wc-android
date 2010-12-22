
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
import android.webkit.WebView;
/**
 * A class to do the html parsing and set the appropriate instance variable, which
 * is made public. Currently, it also saves and reads the saved days stack.
 * @author Alisa Maas
 *
 */
public class MenuParser {
	
	/**
	 * To convert from the name of the month to the number (counting
	 * from 0). Used for determining old meals.
	 */
	private static HashMap<String,Integer> monthToInt = new HashMap<String,Integer>();
	/*
	 * Set up the monthToInt hashmap. Should only be done once, upon 
	 * instatiation of the class.
	 */
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
	
	
	/**
	 * Trim the stack of Days, if it exists, and read in
	 * the file, if it exists and the stack does not. If 
	 * necessary, parse the html file for new Days, and
	 * crop any old ones. The purpose of this method
	 * is to insure the Days stack is created and populated
	 * with up to date information.
	 * @param con Used for file IO. 
	 */
	public static void parse(Context con){
		
		//TODO Fix the saving/reading of the file, then remove this line.
		con.deleteFile("days_cache");
		
		
		if(days.empty()){
			if(con.fileList().length != 0){  //in the case of an empty days stack, try to
				//read in more days from the file (so we don't have to parse)
			
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
			//if no file, parse the html
			
			try{
				URL lunchmenu = new URL("http://www.cafebonappetit.com/wheaton/cafes/anderson/weekly_menu.html"); //read in from the lunch menu
				URL dinnermenu = new URL("http://www.cafebonappetit.com/wheaton/cafes/anderson/weekly_menu2.html"); //read in from the dinner menu
				Scanner lunchin = new Scanner((InputStream) lunchmenu.getContent());
				Scanner dinnerin = new Scanner((InputStream) dinnermenu.getContent());
				String lunchline = "";
				String dinnerline = "";
				ArrayList<ArrayList<String>> allLunchStations = new ArrayList<ArrayList<String>>(); //Since each day needs an ArrayList<String> 
				//for lunch stations, this will hold the ArrayLists for each of the Days parsed.
				ArrayList<ArrayList<String>> allLunchItems = new ArrayList<ArrayList<String>>();//Since each day needs an ArrayList<String> 
				//for lunch items, this will hold the ArrayLists for each of the Days parsed.
				ArrayList<ArrayList<String>> allDinnerStations = new ArrayList<ArrayList<String>>();//Since each day needs an ArrayList<String> 
				//for dinner stations, this will hold the ArrayLists for each of the Days parsed.
				ArrayList<ArrayList<String>> allDinnerItems = new ArrayList<ArrayList<String>>();//Since each day needs an ArrayList<String> 
				//for dinner items, this will hold the ArrayLists for each of the Days parsed.
				ArrayList<String> dates = new ArrayList<String>(); //An ArrayList of all the dates found via parsing.
				ArrayList<String> printableDates = new ArrayList<String>(); //A more well formatted display of the dates found via
				//parsing, to display to the user.
				while(lunchin.hasNext()){ //if there's more things in the lunch menu to read.
					while(lunchin.hasNext()&&!lunchline.contains("align=\"center\"><strong><br>")&&!(lunchline = lunchin.nextLine()).contains("align=\"center\"><strong><br>"));
					//The above line checks for a particular line that begins each Day and, as near as I can tell, ONLY proceeds a date.
					if(!lunchin.hasNext()){
						break;
					}
					ArrayList<String> lunchstations = new ArrayList<String>(); //to hold the lunch stations for this day
					ArrayList<String> lunchitems = new ArrayList<String>();  //to hold the lunch items for this day
					
					//cut off unnecessary heading tags
					lunchline = lunchline.substring(lunchline.indexOf("\"center\"><strong><br>")); 
					if(lunchline.indexOf(' ')==21){
						lunchline = lunchline.substring(22); //in case they have a random space
						//before the day of the week - for some reason they do for the first day, but not
						//the rest..
					}
					else lunchline = lunchline.substring(21); //normal case
					
					//Get rid of trailing tags
					lunchline = lunchline.substring(0,lunchline.indexOf("<"));
					
					//To process date information
					StringTokenizer lunch_token = new StringTokenizer(lunchline);
					
					lunch_token.nextToken();
					String date = "";
					
					//Formatting stuff to get the date in correctly in the form month day year (ie, 11 20 2010)
					date += monthToInt.get(lunch_token.nextToken())+ " ";
					String temp = lunch_token.nextToken();
					temp = (temp.contains(",")?temp.substring(0,temp.length()-1):temp);
					date+=temp + " " + lunch_token.nextToken();
					date = date.contains("<")?date.substring(0,date.indexOf("<")):date;
					dates.add(date); //Adds this to the ArrayList of dates used for stack sorting
					lunchline = lunchline.substring(0,(lunchline.length()-6)); //Gets rid of the year. Remove this line
					//if we decide we want to display the date after all. This is used for the printable date.
					printableDates.add(lunchline);
					
					while(lunchin.hasNext()&&!lunchline.contains("align=\"center\"><strong><br>")){
					while(lunchin.hasNext()&&!lunchline.contains("<strong>"))
						lunchline = lunchin.nextLine();
					//The above code moves lunchline along until it contains the string with the name of the next
					//lunch station.
					if(!lunchin.hasNext())
						break;
					if(lunchline.contains("align=\"center\"><strong><br>"))
						break;
					lunchline = lunchline.substring(lunchline.indexOf("ng>")+3);
					lunchline = lunchline.substring(0,lunchline.indexOf("<"));
					//The above crops the line so it contains only the wanted station name
					if(lunchline.equals("")) //To account for a bizarre case in which it seems to grab an empty lunch station
						//name. This does actually happen.
						continue;
					
					lunchstations.add(lunchline);
					lunchline = lunchin.nextLine();
					lunchline = lunchline.substring(lunchline.indexOf(">")+1);
					//Finds and crops the items in that station
					while(lunchline.contains("&amp;")){ //gets rid of a problem where any & is replaced by "&amp;" for all incidences in the line
						lunchline = lunchline.substring(0,lunchline.indexOf("&amp;")+1) + lunchline.substring(lunchline.indexOf("&amp;")+5);
					}
					lunchitems.add(lunchline);
					
					}
					//When done with the day, add the ArrayLists to the appropriate ArrayList of ArrayList<String>s.
					allLunchStations.add(lunchstations); 
					allLunchItems.add(lunchitems);
				}
					while(dinnerin.hasNext()){
						while(dinnerin.hasNext()&&!dinnerline.contains("align=\"center\"><strong><br>")&&!(dinnerline = dinnerin.nextLine()).contains("align=\"center\"><strong><br>"));
						if(!dinnerin.hasNext()){
							break;
						}
						//Like before, that code moves to the section containing the date, but
						//since the dates have already been added, adding the dates again is unnecessary.
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
						//Get the name of the station
						dinnerline = dinnerline.substring(dinnerline.indexOf("ng>")+3);
						dinnerline = dinnerline.substring(0,dinnerline.indexOf("<"));
						
						if(dinnerline.equals(""))
							continue;
						dinnerstations.add(dinnerline);
						//Get the name of the menu items at this station
						dinnerline = dinnerin.nextLine();
						dinnerline = dinnerline.substring(dinnerline.indexOf(">")+1);
						while(dinnerline.contains("&amp;")){ //fix the "&amp;" problem.
							dinnerline = dinnerline.substring(0,dinnerline.indexOf("&amp;")+1) + dinnerline.substring(dinnerline.indexOf("&amp;")+5);
						}
						dinneritems.add(dinnerline);
						
						}
						allDinnerStations.add(dinnerstations);	
						allDinnerItems.add(dinneritems);
					}
					//create stack of Days
					for(int i = 0;i<=(allLunchStations.size()>allDinnerStations.size()?allLunchStations.size()-1:allDinnerStations.size()-1);i++){
						ArrayList<String> dinnerstations = (allDinnerStations.size()<=i?null:allDinnerStations.get(i));
						ArrayList<String> dinneritems = (allDinnerItems.size()<=i?null:allDinnerItems.get(i));
						ArrayList<String> lunchstations = (allLunchStations.size()<=i?null:allLunchStations.get(i));
						ArrayList<String> lunchitems = (allLunchItems.size()<=i?null:allLunchItems.get(i));
						String printableDate = printableDates.get(i);
						String date = dates.get(i);
						days.push(new Day(date,printableDate,lunchstations,lunchitems,dinnerstations,dinneritems));
						
					}
					}catch(Exception e){
						Log.e("MenuParser",e.getMessage());
					}
		}
		//NOTE - Everything above this line is tested and working code. It looks like the code below doesn't yet
		//work, but I haven't tested yet.
		
			//crop expired days by comparing to the current time/date
		else{	
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
//		View v;
//		Day d;
//		TextView t;
//		for(Iterator<Day> it = days.iterator();it.hasNext();){
//			d = it.next();
//			v = l.inflate(R.layout.food_menu, null);
//			t = (TextView) v.findViewById(R.id.date);
//			t.setText(d.printableDate);
//			t = (TextView) v.findViewById(R.id.lunch_items);
//			t.setText(d.lunchPrint());
//			t = (TextView) v.findViewById(R.id.dinner_items);
//			t.setText(d.dinnerPrint());
//			toReturn.add(v);
//		}
		String example = "<html><body><h1>Monday, December 20, 2010</h1><h2><strong>Lunch</strong></h2><h3><u>Kettles</u></h3><p>Tomato Soup</p>" +
				"<h3><u>Classics</u></h3><p>Ham & Grilled Cheese, Zuchini & Squash, Roasted Wedge Potatoes, " +
				"House Salad. Vegetarian: Grilled Cheese</p><h2>Dinner</h2><p>No dinner listed</p></body></html>";
		WebView v = (WebView) l.inflate(R.layout.food_menu, null).findViewById(R.id.web);
		v.loadData(example, "text/html", "utf-8");
		Log.e("WEBVIEW","Finished making webview");
		toReturn.add(v);
		
		return toReturn;
	}
	
}

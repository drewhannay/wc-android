
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
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
/**
 * A class to do the HTML parsing and set the appropriate instance variable, which
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
	
	private static Calendar calendar = Calendar.getInstance(); //used to get current date
	private static int currentMonth = calendar.get(Calendar.MONTH); 
	private static int currentDay = (calendar.get(Calendar.DAY_OF_MONTH));
	private static int currentYear =(calendar.get(Calendar.YEAR));
	/*
	 * Set up the monthToInt HashMap. Should only be done once, upon 
	 * instantiation of the class.
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
	 * necessary, parse the HTML file for new Days, and
	 * crop any old ones. The purpose of this method
	 * is to insure the Days stack is created and populated
	 * with up to date information.
	 * @param con Used for file IO. 
	 */
	public static void parse(Context con){
		
		if(days.empty()){
			if(con.fileList().length != 0){  //in the case of an empty days stack, try to
				//read in more days from the file (so we don't have to parse)
			
        		FileInputStream f_in = null;
				try {
					Log.e("reading in","Reading file...");
					f_in = con.openFileInput("days_cache");
					
					// Read object using ObjectInputStream
					ObjectInputStream obj_in = new ObjectInputStream (f_in);
					days = (Stack<Day>) obj_in.readObject(); //read in the stack, if there.
					crop(con,true); //This will hopefully crop any old days.
					Log.e("IO","Finished reading in");
					return;
				}catch(Exception e){
					Log.e("MenuParserOpenFile", e.toString());
				}
			}
			//if no file, parse the HTML
			
			try{
				//Create URL Objects to read in the lunch and dinner menus from the Internet.
				URL lunchmenu = new URL("http://www.cafebonappetit.com/wheaton/cafes/anderson/weekly_menu.html"); 
				URL dinnermenu = new URL("http://www.cafebonappetit.com/wheaton/cafes/anderson/weekly_menu2.html");
				//Make Scanners from the URLs.
				Scanner lunchin = new Scanner((InputStream) lunchmenu.getContent());
				Scanner dinnerin = new Scanner((InputStream) dinnermenu.getContent());
				//Create Lunch and Dinner line strings.
				String lunchline = "", dinnerline = "";
				
				//Hold all the ArrayList<String>s for all the Days of the week, for lunch and dinner.
				ArrayList<ArrayList<String>> allLunchStations = new ArrayList<ArrayList<String>>(); 
				ArrayList<ArrayList<String>> allLunchItems = new ArrayList<ArrayList<String>>();
				ArrayList<ArrayList<String>> allDinnerStations = new ArrayList<ArrayList<String>>();
				ArrayList<ArrayList<String>> allDinnerItems = new ArrayList<ArrayList<String>>();
				
				//Keep track of all the dates found and store them in two forms.
				ArrayList<String> dates = new ArrayList<String>(); 
				ArrayList<String> printableDates = new ArrayList<String>();
				
				//While there's more things in the lunch menu to read...
				while(lunchin.hasNext()){ 
					//Cut off everything before the first date by looking for specific HTML.
					while(lunchin.hasNext()&&!lunchline.contains("align=\"center\"><strong><br>")
							&&!(lunchline = lunchin.nextLine()).contains("align=\"center\"><strong><br>")){
						//Do nothing. Loop is only used for getting to the next date in the HTML code.
					}
					
					//If we're out of code, just stop.
					if(!lunchin.hasNext()){
						break;
					}
					//ArrayList<String>s to hold the lunch stations and items for this day.
					ArrayList<String> lunchstations = new ArrayList<String>();
					ArrayList<String> lunchitems = new ArrayList<String>();
					
					//Cut off unnecessary heading tags
					lunchline = lunchline.substring(lunchline.indexOf("\"center\"><strong><br>"));
					
					//Take care of random space that occasionally occurs before the day of the week.
					if(lunchline.indexOf(' ')==21){
						lunchline = lunchline.substring(22);
					}
					//Normal case
					else {
						lunchline = lunchline.substring(21); 
					}
					
					//Get rid of trailing tags
					lunchline = lunchline.substring(0,lunchline.indexOf("<"));
					
					//To process date information
					StringTokenizer lunch_token = new StringTokenizer(lunchline);
					
					lunch_token.nextToken();
					String date = "";
					
					//Formatting stuff to get the date in correctly in the form month day year (IE, 11 20 2010)
					date += monthToInt.get(lunch_token.nextToken())+ " ";
					String temp = lunch_token.nextToken();
					temp = (temp.contains(",")?temp.substring(0,temp.length()-1):temp);
					date+=temp + " " + lunch_token.nextToken();
					date = date.contains("<")?date.substring(0,date.indexOf("<")):date;
					StringTokenizer st = new StringTokenizer(date);
					
					//Optimization: If the date we're reading in is less than the current date, keep going.
					if(Integer.parseInt(st.nextToken())<currentMonth || Integer.parseInt(st.nextToken()) < currentDay
							|| Integer.parseInt(st.nextToken()) < currentYear)
						continue;
					
					dates.add(date); //Adds this to the ArrayList of dates used for stack sorting
					
					//Remove the year from the printableDate and add to the list.
					lunchline = lunchline.substring(0,(lunchline.length()-6));
					printableDates.add(lunchline);
					
					while(lunchin.hasNext()&&!lunchline.contains("align=\"center\"><strong><br>")){
						//Move to the name of the next lunch station.
						while(lunchin.hasNext()&&!lunchline.contains("<strong>")){
							lunchline = lunchin.nextLine();
						}

						//If we're out of code, stop.
						if(!lunchin.hasNext()){
							break;
						}
						//If we're at the right spot, stop.
						if(lunchline.contains("align=\"center\"><strong><br>")){
							break;
						}
						//Crop the line to contain only the wanted station name.
						lunchline = lunchline.substring(lunchline.indexOf("ng>")+3);
						lunchline = lunchline.substring(0,lunchline.indexOf("<"));

						//Cover case of grabbing an empty lunch station name.
						if(lunchline.equals("")){
							continue;
						}
						
						//Find and crop the items in that station
						lunchstations.add(lunchline);
						lunchline = lunchin.nextLine();
						lunchline = lunchline.substring(lunchline.indexOf(">")+1);
						
						//Convert any HTML "&amp"s to simply "&"
						while(lunchline.contains("&amp;")){ 
							lunchline = lunchline.substring(0,lunchline.indexOf("&amp;")+1) + 
									lunchline.substring(lunchline.indexOf("&amp;")+5);
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
				for(int i = (allLunchStations.size()>allDinnerStations.size()?allLunchStations.size()-1:allDinnerStations.size()-1);i>-1;i--){
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
		else {
			crop(con, false); //If the crop code is causing crashing, comment out this line.
		}
		
		//Either way, at the end of the method, write the new object to data.
		try{
			FileOutputStream f_out = con.openFileOutput("days_cache", Context.MODE_PRIVATE);
			// Write object with ObjectOutputStream
			ObjectOutputStream obj_out = new ObjectOutputStream(f_out);
			// Write object out to disk
			obj_out.writeObject (days);
		}catch(Exception e){
			Log.e("MenuParserFileIO", e.toString());
		}
	}

	
	/**
	 * Crop the old days from the Days stack.
	 * @param con Used to pass back in the call to parse,
	 * in the event that we have emptied the stack by cropping.
	 */
	public static void crop(Context con, boolean reparse){
		while(!days.empty()){ //as long as there's still Days in the stack keep looking
			StringTokenizer st = new StringTokenizer((days.peek()).date); //Grab the date from the Day.
			int tempMonth = Integer.parseInt(st.nextToken());
			if(tempMonth<currentMonth){ //If the month is less than the current month, it must be old. 
				days.pop(); //get rid of the old Day.
				continue;
			}
			int tempDay = Integer.parseInt(st.nextToken());
				if(tempDay<currentDay){ //If the day is less than the current day, it must be old.
					days.pop(); //get rid of the old Day.
					continue;
				}
			int tempYear = Integer.parseInt(st.nextToken());
			if(tempYear<currentYear){ //If the year is less than the current year, it must be old.
				days.pop(); //get rid of the old Day.
				continue;
			}
			break;
		}
		//If we've emptied the stack, fill it again. This will
		//perform checks again and determine to parse the HTML.
		//Here would be a good use for a goto if java still had one
		//to avoid extra checking. But this will never happen more than
		//once a day, at the most.
		if(reparse && days.empty()){
			con.deleteFile("days_cache");
			parse(con);
		}
	}
	
	
	
	public static ArrayList<View> toArrayList(LayoutInflater l){
		ArrayList<View> toReturn = new ArrayList<View>();
		WebView v;
		Day d;
		for(Stack<Day> st = days;!st.empty();){
			d = st.pop();
			String webCode = "<html><head><style type=\"text/css\"> h1 { font-size: 1.2em; font-weight: bold; " +
					"text-align: center; }</style></head><body>";
				webCode +="<h1>" + d.printableDate + "</h1><h2><center><strong><u>Lunch</u></strong>" +
						"</center></h2>";
				webCode += d.lunchPrint();
				webCode += "<h2><center><strong><u>Dinner</u></strong></center></h2>";
				webCode += d.dinnerPrint();
				webCode += "</body></html>";
				v = (WebView) l.inflate(R.layout.food_menu, null).findViewById(R.id.web);
				v.loadData(webCode, "text/html", "utf-8");
				v.setBackgroundColor(0);
				toReturn.add(v);
		}
		return toReturn;
	}
	
}

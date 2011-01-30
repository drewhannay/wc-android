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
	
	private static HashMap<Integer,String> dayofweek = new HashMap<Integer,String>();
	
	private static HashMap<Integer,String> intToMonth = new HashMap<Integer,String>();
	
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
		
		intToMonth.put(0,"January");
		intToMonth.put(1,"February");
		intToMonth.put(2,"March");
		intToMonth.put(3,"April");
		intToMonth.put(4,"May");
		intToMonth.put(5,"June");
		intToMonth.put(6,"July");
		intToMonth.put(7,"August");
		intToMonth.put(8,"September");
		intToMonth.put(9,"October");
		intToMonth.put(10,"November");
		intToMonth.put(11,"December");
		dayofweek.put(Calendar.SUNDAY,"Sunday");
		dayofweek.put(Calendar.MONDAY,"Monday");
		dayofweek.put(Calendar.TUESDAY,"Tuesday");
		dayofweek.put(Calendar.WEDNESDAY,"Wednesday");
		dayofweek.put(Calendar.THURSDAY,"Thursday");
		dayofweek.put(Calendar.FRIDAY, "Friday");
		dayofweek.put(Calendar.SATURDAY, "Saturday");
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
				boolean readin = false;
				for(String a: con.fileList()){
					if(a.equals("days_cache1"))
						readin = true;
					else if(a.equals("days_cache"))
						con.deleteFile("days_cache");
				} 
				if(readin){
        		FileInputStream f_in = null;
				try {
					Log.e("reading in","Reading file...");
					f_in = con.openFileInput("days_cache1");
					
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
					int tempMonth = Integer.parseInt(st.nextToken());
					int tempDay = Integer.parseInt(st.nextToken());
					int tempYear = Integer.parseInt(st.nextToken());
					//Optimization: If the date we're reading in is less than the current date, keep going.
					if(tempMonth<currentMonth || (tempMonth==currentMonth&&tempDay<currentDay)
							|| tempYear<currentYear)
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
						

						lunchitems.add(lunchline);
					
					}
					//When done with the day, add the ArrayLists to the appropriate ArrayList of ArrayList<String>s.
					allLunchStations.add(lunchstations); 
					allLunchItems.add(lunchitems);
				}
				ArrayList<String> datehack = new ArrayList<String>();
				ArrayList<String> printableDinnerDates = new ArrayList<String>();
				//Do almost the same thing for dinner.
				while(dinnerin.hasNext()){
					//Cut off everything before the first date by looking for specific HTML.
					while(dinnerin.hasNext()&&!dinnerline.contains("align=\"center\"><strong><br>")&&
							!(dinnerline = dinnerin.nextLine()).contains("align=\"center\"><strong><br>")){
						//Do nothing. Loop is only used for getting to the next date in the HTML code.
					}
					//If we're out of code, just stop.
					if(!dinnerin.hasNext()){
						break;
					}
					//ArrayList<String>s to hold the dinner stations and items for this day.
					ArrayList<String> dinnerstations = new ArrayList<String>();
					ArrayList<String> dinneritems = new ArrayList<String>(); 
					//Cut off unnecessary heading tags
					dinnerline = dinnerline.substring(dinnerline.indexOf("\"center\"><strong><br>"));
					
					//Take care of random space that occasionally occurs before the day of the week.
					if(dinnerline.indexOf(' ')==21){
						dinnerline = dinnerline.substring(22);
					}
					//Normal case
					else {
						dinnerline = dinnerline.substring(21); 
					}
					
					//Get rid of trailing tags
					dinnerline =dinnerline.substring(0,dinnerline.indexOf("<"));
					
					//To process date information
					StringTokenizer dinner_token = new StringTokenizer(dinnerline);
					
					dinner_token.nextToken();
					String date = "";
					
					//Formatting stuff to get the date in correctly in the form month day year (IE, 11 20 2010)
					date += monthToInt.get(dinner_token.nextToken())+ " ";
					String temp = dinner_token.nextToken();
					temp = (temp.contains(",")?temp.substring(0,temp.length()-1):temp);
					date+=temp + " " + dinner_token.nextToken();
					date = date.contains("<")?date.substring(0,date.indexOf("<")):date;
					StringTokenizer st = new StringTokenizer(date);
					int tempMonth = Integer.parseInt(st.nextToken());
					int tempDay = Integer.parseInt(st.nextToken());
					int tempYear = Integer.parseInt(st.nextToken());
					//Optimization: If the date we're reading in is less than the current date, keep going.
					if(tempMonth<currentMonth || (tempMonth==currentMonth&&tempDay<currentDay)
							|| tempYear<currentYear)
						continue;
					
					datehack.add(date);
					dinnerline = dinnerline.substring(0,(dinnerline.length()-6));
					printableDinnerDates.add(dinnerline);
					while(dinnerin.hasNext()&&!dinnerline.contains("align=\"center\"><strong><br>")){
						//Move to the name of the next dinner station.
						while(dinnerin.hasNext()&&!dinnerline.contains("<strong>")){
							dinnerline = dinnerin.nextLine();
						}
						
						//If we're out of code, stop.
						if(!dinnerin.hasNext()){
							break;
						}
						//If we're at the right spot, stop.
						if(dinnerline.contains("align=\"center\"><strong><br>")){
							break;
						}
						
						//Crop the line to contain only the wanted station name.
						dinnerline = dinnerline.substring(dinnerline.indexOf("ng>")+3);
						dinnerline = dinnerline.substring(0,dinnerline.indexOf("<"));
						
						
						//Cover case of grabbing an empty dinner station name.
						if(dinnerline.equals("")){
							continue;
						}
						
						//Find and crop the items in that station
						dinnerstations.add(dinnerline);
						dinnerline = dinnerin.nextLine();
						dinnerline = dinnerline.substring(dinnerline.indexOf(">")+1);
						

						dinneritems.add(dinnerline);
					
					}
					//When done with the day, add the ArrayLists to the appropriate ArrayList of ArrayList<String>s.
					allDinnerStations.add(dinnerstations);	
					allDinnerItems.add(dinneritems);
				}
				
				
				//Create the stack of Days
				for(int i = allLunchStations.size()-1,j = allDinnerStations.size()-1;i>-1;){
					ArrayList<String> dinnerstations = (allDinnerStations.size()<=j||j<0?null:allDinnerStations.get(j));
					ArrayList<String> dinneritems = (allDinnerItems.size()<=j||j<0?null:allDinnerItems.get(j));
					ArrayList<String> lunchstations = (allLunchStations.size()<=i?null:allLunchStations.get(i));
					ArrayList<String> lunchitems = (allLunchItems.size()<=i?null:allLunchItems.get(i));
					String printableDate = printableDates.get(i);
					String printableDinnerDate = (printableDinnerDates.size()<j||j<0?"a":printableDinnerDates.get(j));
					String date = dates.get(i);
					String dateTest = (datehack.size()<j||j<0?"a":datehack.get(j));
					Day toAdd = null;
					if(printableDinnerDate.equals("a")){
						toAdd = new Day(date,printableDate,lunchstations,lunchitems,null,null);
						i--;
					}
					else if(!date.equals(dateTest)){
						StringTokenizer st = new StringTokenizer(date);
						StringTokenizer st2 = new StringTokenizer(dateTest);
						int lunchmonth = Integer.parseInt(st.nextToken());
						int dinnermonth = Integer.parseInt(st2.nextToken());
						int lunchday = Integer.parseInt(st.nextToken());
						int dinnerday = Integer.parseInt(st2.nextToken());
						int lunchyear = Integer.parseInt(st.nextToken());
						int dinneryear = Integer.parseInt(st.nextToken());
						
						if(lunchyear!=dinneryear){
							if(lunchyear<dinneryear){
								toAdd = new Day(date,printableDate,lunchstations,lunchitems,null,null);
								i--;
							}
							else{
								toAdd = new Day(dateTest,printableDinnerDate,null,null,dinnerstations,dinneritems);
								j--;
							}
							
						}
						else if(lunchmonth!=dinnermonth){
							if(lunchmonth<dinneryear){
								toAdd = new Day(date,printableDate,lunchstations,lunchitems,null,null);
								i--;
							}
							else{
								toAdd = new Day(dateTest,printableDinnerDate,null,null,dinnerstations,dinneritems);
								j--;
							}
							
						}
						else if(lunchday!=dinnerday){
							if(lunchday<dinnerday){
								toAdd = new Day(date,printableDate,lunchstations,lunchitems,null,null);
								i--;
							}
							else{
								toAdd = new Day(dateTest,printableDinnerDate,null,null,dinnerstations,dinneritems);
								j--;
							}
							
						}
					}
					else{
						toAdd = new Day(date,printableDate,lunchstations,lunchitems,dinnerstations,dinneritems);
						i--;
						j--;
					}
					days.push(toAdd);
				}
				
				if(!days.empty()){
				Day temp = days.peek();
				String date = temp.date;
				StringTokenizer st = new StringTokenizer(date);
				int month = Integer.parseInt(st.nextToken());
				int day = Integer.parseInt(st.nextToken());
				int year = Integer.parseInt(st.nextToken());
				if(currentMonth!=month||currentDay!=day||currentYear!=year){
				
				String printabledate = dayofweek.get(Calendar.DAY_OF_WEEK) + ", " + intToMonth.get(month) + " " + day;
					days.push(new Day((-1 + " " + -1 + " " + 1880),printabledate,null,null,null,null));
				}
				
				}
				else{
					Log.e("test",Calendar.DAY_OF_WEEK+"");
					days.push(new Day((currentMonth + " " + currentDay + " " + currentYear),(intToMonth.get(currentMonth) + " " + currentDay),null,null,null,null));
				}
				}catch(Exception e){
					e.printStackTrace();
					Log.e("MenuParser",e.getMessage());
				}
		}
		else {
			//The Day Stack was not empty, so we need to remove any old days.
			crop(con, false);
		}
		
		//Either way, at the end of the method, write the new object to data.
		try{
			FileOutputStream f_out = con.openFileOutput("days_cache1", Context.MODE_PRIVATE);
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
				if(tempMonth==currentMonth&&tempDay<currentDay){ //If the day is less than the current day, it must be old.
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
	
	
	/**
	 * Method to convert Day Stack to ArrayList<View>
	 * For each Day in the stack, assemble a String with the appropriate HTML code and
	 * the data from the Day and load that String as the data for a WebView, then put
	 * that WebView in the ArrayList to return.
	 * @param l LayoutInflator, which is needed to create Views from the food_menu.xml file.
	 * @return The completed ArrayList<View> containing the info from the Day Stack.
	 */
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
		if(toReturn.size()==0){
			v = (WebView) l.inflate(R.layout.food_menu, null).findViewById(R.id.web);
			String webCode = "<html><head><style type=\"text/css\"> h1 { font-size: 1.2em; font-weight: bold; " +
			"text-align: center; }</style></head><body><br/><br/><br/><br/><h1>No current cafe menu available</h1></body></html>";
			v.loadData(webCode, "text/html", "utf-8");
			v.setBackgroundColor(0);
			toReturn.add(v);
		}
		return toReturn;
	}
	
}
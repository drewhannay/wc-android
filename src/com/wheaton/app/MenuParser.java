
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
/**
 * A class to do the html parsing and set the appropriate instance variable, which
 * is made public. Currently, it also saves and reads the saved days stack.
 * @author Alisa Maas
 *
 */
public class MenuParser {
	
	/*
	 * A Object to contain the days. It's ordered in a
	 * stack for maximum efficiency when removing expired
	 * days. Oldest days are at the top, which also happens 
	 * to be convenient for processing the days and displaying
	 * the next recent one.
	 */
	public static Stack<Day> days = new Stack<Day>();

	public static void parse(Context con){
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
					ArrayList<String> dates = new ArrayList<String>();
					while(lunchin.hasNext()){
					while(lunchin.hasNext()&&!(lunchline = lunchin.nextLine()).contains("align=\"center\"><strong><br>"));
					if(!lunchin.hasNext())
						break;
					lunchline = lunchline.substring(lunchline.indexOf("\"center\"><strong><br>"));
					dinnerline = dinnerline.substring(dinnerline.indexOf("\"center\"><strong><br>"));
					StringTokenizer lunch_token = new StringTokenizer(lunchline);
					StringTokenizer dinner_token = new StringTokenizer(dinnerline);
					lunch_token.nextToken();
					dinner_token.nextToken();
					String date = "";
					date += monthToInt.get(lunch_token.nextToken())+ " ";
					String temp = lunch_token.nextToken();
					temp = (temp.contains(",")?temp.substring(0,temp.length()-1):temp);
					date+=temp + " " + lunch_token.nextToken();
					dates.add(date);
					}
					while(!(dinnerline = dinnerin.nextLine()).contains("align=\"center\"><strong><br>"));

					
				}catch(Exception e){}
				if(stack.empty())
					stack.push(new Day("Sorry, but saga has not yet published the menu. Check back later for more details."));
			
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
	
}

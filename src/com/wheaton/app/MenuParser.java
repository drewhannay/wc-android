package com.wheaton.app;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Stack;
import java.util.StringTokenizer;

import android.content.Context;

public class MenuParser {
public static Stack<Day> days = new Stack<Day>();

	public static void parse(Context con){
		if(days.empty()){
			if(con.fileList().length != 0){ 
			
        		FileInputStream f_in = null;
				try {
					f_in = con.openFileInput("days_cache");
				
        		// Read object using ObjectInputStream
				ObjectInputStream obj_in;
					obj_in = new ObjectInputStream (f_in);
				days = (Stack<Day>) obj_in.readObject();
				}catch(Exception e){
					//this should never, ever happen since
					//as of now days_cache is our ONLY file.
				}
			//TODO if no file, parse the html
		}
		else{
			//crop expired days by comparing to the current time/date
			
			Calendar calendar = Calendar.getInstance();
			int currentMonth = calendar.get(Calendar.MONTH)+1;
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
	}
}

package com.wheaton.app;

import java.util.Calendar;
import java.util.Stack;
import java.util.StringTokenizer;
public class MenuParser {
public static Stack<Day> days = new Stack<Day>();

	public static void parse(){
		if(days.empty()){
			//TODO try to read from the file
	
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
				parse();
		}
	}
}

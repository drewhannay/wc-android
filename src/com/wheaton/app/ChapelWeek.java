package com.wheaton.app;

import java.io.Serializable;
import java.util.ArrayList;

public class ChapelWeek implements Serializable{

	private static final long serialVersionUID = 3559824920871726320L;
	private static final String[] weekdays;
	
	static{
		weekdays = new String[]{"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
	}
	
	
	private ArrayList<String[]> schedule;
	
	
	public ChapelWeek(){
		schedule = new ArrayList<String[]>();
	}
	
	public boolean addDay(String date, String title, String speakers, String specialSeries, String description){
		return addDay(new String[]{date,title,speakers,specialSeries,description});
	}
	
	public boolean addDay(String[] info){
		if(schedule.size()!=0){
			String lastDate = schedule.get(schedule.size()-1)[0];
			String oldDay = lastDate.substring(0,lastDate.indexOf(","));
			String newDay = info[0].substring(0, info[0].indexOf(","));
			for(String s:weekdays){
				if(s.equals(newDay))//If we find the Day we're trying to add before the previous day, it must be a new week.
					return false;
				if(s.equals(oldDay))
					break;
			}
		}
		schedule.add(info);
		return true;
	}
		
	public String toString(){
		//TODO Implement this method.
		return "";
	}
	
	

}

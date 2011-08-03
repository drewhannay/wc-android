package com.wheaton.app;

import java.io.Serializable;
import java.util.ArrayList;

public class OpenFloorDay implements Serializable {

	private static final long serialVersionUID = -5664251875919714947L;
	private static final String[] weekdays = new String[]{"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
	
	
	private ArrayList<String[]> schedule;
	
	
	public OpenFloorDay(){
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
		String toReturn = "<html><head><style type=\"text/css\"> h1 { font-size: 1.1em; font-weight: bold; " +
        "text-align: center; color:#CC6600; } h2 { font-size: 0.9em; } h3 { font-style:italic; font-size: 0.8em;}" +
        "p { font-size: 0.7em; } </style></head><body>";
		
		for(String[] day:schedule){
			toReturn += "<h1>"+day[0]+"</h1>";
			if(day[3].equals("Y"))
				toReturn += "<span style=\"color:#000066;\">";
			toReturn += "<h2>"+day[1]+"</h2>";
			toReturn += "<h3>"+day[2]+"</h3>";
			toReturn += "<p>"+day[4]+"</p>";
			if(day[3].equals("Y"))
				toReturn += "</span>";
			
			toReturn += "<hr />";
			
		}
		
		
		return toReturn;
	}
	
	

}
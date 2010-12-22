package com.wheaton.app;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A class to hold the day, including the lunch and
 * dinner schedule, Serializable so it can be stored in
 * a temp file and retrieved later (at least until it's 
 * expired)
 * @author Alisa Maas
 *
 */
public class Day implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/*
 * To be in the form month day year (space separated)
 * Year is necessary, because it checks to see
 * that the month and day are not the same. When
 * the year changes, both change as well - but, if
 * they don't use the app for a year (unlikely but 
 * possible), this would mess things up and they'd get
 * an old schedule.
 */
public String date = "1 1 1800";

/*
 * Names of lunch stations.
 */
public ArrayList<String> lunchStations;;
/*
 * The entrees offered at lunch, organized by station.
 */
public ArrayList<String> lunchEntrees;
/*
 * Names of dinner stations
 */
public ArrayList<String> dinnerStations;
/*
 * The entrees offered at dinner, organized by station.
 */
public ArrayList<String> dinnerEntrees;

/*
 * In case instead of meals, saga decides to post a message - for example, "Merry Christmas!"
 */
public String specialMessage = "";

public String printableDate = "";

	/**
	 * Most common constructor, sets up usual information.
	 * @param date to set the date
	 * @param lunchStations to set the names of stations (lunch)
	 * @param lunchEntrees to set the dishes offered (lunch)
	 * @param dinnerStations to set the names of stations (dinner)
	 * @param dinnerEntrees to set the dishes offered (dinner)
	 */
	public Day(String date, String printableDate,ArrayList<String> lunchStations, ArrayList<String> lunchEntrees, ArrayList<String> dinnerStations, ArrayList<String> dinnerEntrees){
		this.date = date;
		this.lunchStations = lunchStations;
		this.lunchEntrees = lunchEntrees;
		this.dinnerStations = dinnerStations;
		this.dinnerEntrees = dinnerEntrees;
		this.printableDate = printableDate;
	
	}
	/**
	 * Special case constructor to account for unusual circumstances.
	 * @param message The message to display.
	 */
	public Day(String message){
		this.specialMessage = message;
	}

	public String lunchPrint(){
		String toReturn = "";
		if(lunchStations!=null){
			for(int i = 0;i<lunchStations.size();i++){
				toReturn += "<h3><em>"+ lunchStations.get(i) + "</em></h3><p>" + lunchEntrees.get(i) + "</p>";
			}
		}
		else{
			toReturn += "<p>No Lunch Listed.</p>";
		}
		return toReturn;
	}
	
	public String dinnerPrint(){
		String toReturn = "";
		if(dinnerStations!=null){
			for(int i = 0;i<dinnerStations.size();i++){
				toReturn += "<h3><em>" + dinnerStations.get(i) + "</em></h3><p>" + dinnerEntrees.get(i) + "</p>";
			}
		}
		else{
			toReturn += "<p>No Dinner Listed.</p>";
		}
		return toReturn;
	}
}

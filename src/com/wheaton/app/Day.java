package com.wheaton.app;

import java.io.Serializable;
import java.util.ArrayList;

import android.util.Log;

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
	
	
//	public void print(){
//		Log.e("Date: ", date);
//		if(lunchStations!=null)
//		for(int i = 0;i<lunchStations.size();i++){
//			Log.e(lunchStations.get(i) + ": ", lunchEntrees.get(i));
//		}
//		Log.e("Dinner","is served");
//		if(dinnerStations!=null)
//		for(int i = 0;i<dinnerStations.size();i++){
//			Log.e(dinnerStations.get(i) + ": ", dinnerEntrees.get(i));
//		}
//		Log.e("Next day.","----------");
//	}
	public String print(){
		String toReturn = "Lunch\n";
		if(lunchStations!=null){
			for(int i = 0;i<lunchStations.size();i++){
				toReturn += lunchStations.get(i) + ": " + lunchEntrees.get(i) + "\n";
			}
		}
		else{
			toReturn += "No Lunch Listed.\n";
		}
		toReturn += "\nDinner\n";
		if(dinnerStations!=null){
			for(int i = 0;i<dinnerStations.size();i++){
				toReturn += dinnerStations.get(i) + ": " + dinnerEntrees.get(i) + "\n";
			}
		}
		else{
			toReturn += "No Dinner Listed.\n";
		}
		return toReturn;
	}
}

package com.wheaton.app;

import java.io.Serializable;

/**
 * A class to hold the day, including the lunch and
 * dinner schedule, Serializable so it can be stored in
 * a temp file and retrieved later (at least until it's 
 * expired)
 * @author Alisa Maas
 *
 */
public class Day implements Serializable {
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
 * To be in the form station1name station2name station3name...etc. The
 * names should be separated by spaces so we can use StringTokenizers,
 * and this should account for days when they have random stations.
 */
public String lunchStations = "";
/*
 * The entrees offered at lunch, organized by station, separated by
 * spaces.
 */
public String lunchEntrees = "";
/*
 * To be in the form station1name station2name station3name...etc. The
 * names should be separated by spaces so we can use StringTokenizers,
 * and this should account for days when they have random stations.
 */
public String dinnerStations = "";
/*
 * The entrees offered at dinner, organized by station, separated by
 * spaces.
 */
public String dinnerEntrees = "";

/*
 * In case instead of meals, saga decides to post a message - for example, "Merry Christmas!"
 */
public String specialMessage = "";

	/**
	 * Most common constructor, sets up usual information.
	 * @param date to set the date
	 * @param lunchStations to set the names of stations (lunch)
	 * @param lunchEntrees to set the dishes offered (lunch)
	 * @param dinnerStations to set the names of stations (dinner)
	 * @param dinnerEntrees to set the dishes offered (dinner)
	 */
	public Day(String date, String lunchStations, String lunchEntrees, String dinnerStations, String dinnerEntrees){
		this.date = date;
		this.lunchStations = lunchStations;
		this.lunchEntrees = lunchEntrees;
		this.dinnerStations = dinnerStations;
		this.dinnerEntrees = dinnerEntrees;
		
	
	}
	/**
	 * Special case constructor to account for unusual circumstances.
	 * @param message The message to display.
	 */
	public Day(String message){
		this.specialMessage = message;
	}

}

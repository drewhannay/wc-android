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
public String cucinaLunch = "";
public String spiceLunch = "";
public String classicsLunch = "";
public String cucinaDinner = "";
public String spiceDinner = "";
public String classicsDinner = "";
public String specialMessage = "";

	public Day(String date, String cucinaLunch, String spiceLunch, String classicsLunch,String cucinaDinner, String spiceDinner, String classicsDinner){
		this.date = date;
		this.cucinaLunch = cucinaLunch;
		this.spiceLunch = spiceLunch;
		this.classicsLunch = classicsLunch;
		this.cucinaDinner = cucinaDinner;
		this.spiceDinner = spiceDinner;
		this.classicsDinner = classicsDinner;
	
	}
	public Day(String message){
		this.specialMessage = message;
	}

}

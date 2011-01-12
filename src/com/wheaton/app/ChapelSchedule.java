package com.wheaton.app;

import java.io.Serializable;

public class ChapelSchedule implements Serializable{
	public static boolean firstSemester;
	public static String html;
	public static int year;
	
	public ChapelSchedule(boolean firstSemester,String html,int year){
		this.firstSemester = firstSemester;
		this.html = html;
		this.year =year;
	}
}

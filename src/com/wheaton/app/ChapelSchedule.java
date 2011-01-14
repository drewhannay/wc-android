package com.wheaton.app;

import java.io.Serializable;

public class ChapelSchedule implements Serializable{

	private static final long serialVersionUID = 1L;
	public boolean firstSemester;
	public String html;
	public int year;
	
	public ChapelSchedule(boolean firstSemester,String html,int year){
		this.firstSemester = firstSemester;
		this.html = html;
		this.year =year;
	}
}

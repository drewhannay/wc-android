package com.wheaton.app;


public class Match {
	
	private String firstName;
	private String lastName;
	private String preferredFirst;
	private boolean studentType;
	private String klazz;
	private String imgURL;
	private String cpo;
	private String department;
	
	public Match(String firstName, String lastName, String preferredFirst, boolean studentType, String klazz, String cpo, String department, String imgURL){
		this.firstName = firstName;
		this.lastName = lastName;
		this.preferredFirst = preferredFirst;
		this.studentType = studentType;
		this.klazz = klazz;
		this.imgURL = imgURL;
		this.cpo = cpo;
		this.department = department;
	}
	
	public String toString(){
		String s = firstName;
		if(!preferredFirst.equals(""))
			s += " (" + preferredFirst + ")";
		s += " " + lastName + "";
		if(studentType){
			s+= ""+(!cpo.equals("")?(",\nCPO: " + cpo):("")) + (!klazz.equals("")?
					", " +klazz: "") + " Student";
			}
		else
			s+=(!department.equals("")?", \n"+department+" Department":"");
		
		return s;
	}
	
	public String getImage(){
		return this.imgURL;
	}
	
}

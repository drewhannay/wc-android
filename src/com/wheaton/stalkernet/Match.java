package com.wheaton.stalkernet;

/*
 * adding comments to test if hg is working. -a
 */
public class Match {
 
 private String firstName;
 private String lastName;
 private String middleName;
 private String preferredFirst;
 private String studentType;
 private String yearEntered;
 private String imgURL;
 
 public Match(String firstName, String lastName, String middleName, String preferredFirst, String studentType, String yearEntered, String imgURL){
  this.firstName = firstName;
  this.lastName = lastName;
  this.middleName = middleName;
  this.preferredFirst = preferredFirst;
  this.studentType = studentType;
  this.yearEntered = yearEntered;
  this.imgURL = imgURL;
 }
 
 public String toString(){
  String s = firstName;
  if(!preferredFirst.equals(""))
   s += " (" + preferredFirst + ")";
  s += " " + middleName + " " + lastName + ", " + studentType + ", " + yearEntered;
  return s;
 }
 
 public String getImage(){
  return this.imgURL;
 }
 
}

package com.wheaton.app;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
/**
 * A class to do the HTML parsing and set the appropriate instance variable, which
 * is made public. Currently, it also saves and reads the saved days stack.
 * @author Alisa Maas
 *
 */
public class MenuParser {
        
        /**
         * To convert from the name of the month to the number (counting
         * from 0). Used for determining old meals.
         */
        private static HashMap<String,Integer> monthToInt = new HashMap<String,Integer>();
        
        private static HashMap<Integer,String> dayofweek = new HashMap<Integer,String>();
        
        private static HashMap<Integer,String> intToMonth = new HashMap<Integer,String>();
        
        private static Calendar calendar = Calendar.getInstance(); //used to get current date
        private static int currentMonth = calendar.get(Calendar.MONTH); 
        private static int currentDay = (calendar.get(Calendar.DAY_OF_MONTH));
        private static int currentYear =(calendar.get(Calendar.YEAR));
        /*
         * Set up the monthToInt HashMap. Should only be done once, upon 
         * instantiation of the class.
         */
        static{
                monthToInt.put("January",0);
                monthToInt.put("February",1);
                monthToInt.put("March", 2);
                monthToInt.put("April",3);
                monthToInt.put("May",4);
                monthToInt.put("June",5);
                monthToInt.put("July",6);
                monthToInt.put("August",7);
                monthToInt.put("September", 8);
                monthToInt.put("October",9);
                monthToInt.put("November",10);
                monthToInt.put("December",11);
                
                intToMonth.put(0,"January");
                intToMonth.put(1,"February");
                intToMonth.put(2,"March");
                intToMonth.put(3,"April");
                intToMonth.put(4,"May");
                intToMonth.put(5,"June");
                intToMonth.put(6,"July");
                intToMonth.put(7,"August");
                intToMonth.put(8,"September");
                intToMonth.put(9,"October");
                intToMonth.put(10,"November");
                intToMonth.put(11,"December");
                dayofweek.put(Calendar.SUNDAY,"Sunday");
                dayofweek.put(Calendar.MONDAY,"Monday");
                dayofweek.put(Calendar.TUESDAY,"Tuesday");
                dayofweek.put(Calendar.WEDNESDAY,"Wednesday");
                dayofweek.put(Calendar.THURSDAY,"Thursday");
                dayofweek.put(Calendar.FRIDAY, "Friday");
                dayofweek.put(Calendar.SATURDAY, "Saturday");
        }
                
        /*
         * A Object to contain the days. It's ordered in a
         * stack for maximum efficiency when removing expired
         * days. Oldest days are at the top, which also happens 
         * to be convenient for processing the days and displaying
         * the next recent one.
         */
        public static Stack<MenuDay> days = new Stack<MenuDay>();
        
        /**
         * Trim the stack of Days, if it exists, and read in
         * the file, if it exists and the stack does not. If 
         * necessary, parse the HTML file for new Days, and
         * crop any old ones. The purpose of this method
         * is to insure the Days stack is created and populated
         * with up to date information.
         * @param con Used for file IO. 
         */
        public static void parse(Context con){
                
                        //if no file, parse the html
                        try{
                                Scanner menu = new Scanner((InputStream)(new URL("http://dl.dropbox.com/u/36045671/menu.txt")).getContent());
                                String line = menu.nextLine();
                                MenuDay[] parsedDays = new MenuDay[7];
                                //main loop, until EOF
                                for(int i = 0;i<7;i++){
                                        while(!line.contains("Date:")){
                                                line = menu.nextLine();
                                        }
                                        MenuDay next = new MenuDay();
                                        ArrayList<String> lunchStations = new ArrayList<String>();
                                        ArrayList<String> dinnerStations = new ArrayList<String>();
                                        ArrayList<String> lunchEntrees = new ArrayList<String>();
                                        ArrayList<String> dinnerEntrees = new ArrayList<String>();
                                        line = line.substring(line.indexOf(" ")+1);
                                        next.printableDate = line; // get the printable version of the date.
                                        
                                        line = line.substring(line.indexOf(" ")+1);
                                        int month = monthToInt.get(line.substring(0,line.indexOf(" ")));
                                        line = line.substring(line.indexOf(" ") +1);
                                        int day_n = Integer.parseInt(line.substring(0,line.indexOf(",")));
                                        line = line.substring(line.indexOf(" ")+1); //reliant on the fact that there IS a space
                                        //between the comma and year.
                                        int year = Integer.parseInt(line); //this means no space at end of the line, try to 
                                        //fix this later.
                                        next.date = month + " " + day_n + " " + year;
                                        
                                while(!line.contains("Hours:")){
                                        line = menu.nextLine();
                                }
                                int offset = 1;
                                if(line.contains(": "))
                                        offset++;
                                line = line.substring(line.indexOf(":")+offset);
                                next.lunchHours = line;
                                
                                if(!line.contains("Closed")){
                                	parseMeal(line,lunchStations,lunchEntrees,menu,"---Dinner---");
                                }
                                else{
                                        
                                        lunchStations = null;
                                }
                                
                                while(!line.contains("Hours:")){
                                        line = menu.nextLine();
                                }
                                offset = 1;
                                if(line.contains(": "))
                                        offset++;
                                line = line.substring(line.indexOf(":")+offset);
                                next.dinnerHours = line;
                                if(!line.contains("Closed")){
                                	parseMeal(line,dinnerStations,dinnerEntrees,menu,"------");
                                }
                                else{
                                        dinnerStations = null;
                                }
                                next.lunchEntrees = lunchEntrees;
                                next.dinnerEntrees = dinnerEntrees;
                                next.lunchStations = lunchStations;
                                next.dinnerStations = dinnerStations;
                                parsedDays[i] = next;
                                }
                                for(int i = 6;i>-1;i--){
                                        days.push(parsedDays[i]);
                                }
                                
                                crop(con);
                        }catch(Exception e){
                                e.printStackTrace();
                                
                        }
                        
                
                
        }
        /**
         * Parses either a lunch or dinner from the document.
         * @param line The current line
         * @param mealStations The current array list of meal station names
         * @param mealEntrees The current array list of meal entrees
         * @param menu The scanner providing access to the file
         * @param endToken What string marks the end of the meal? "---Dinner---" for lunch
         * and "------" for dinner. 
         */
        public static void parseMeal(String line,ArrayList<String> mealStations,ArrayList<String> mealEntrees,Scanner menu,String endToken){
        	while(!line.contains(endToken)){
                while(!line.contains("Station:")&&!line.contains(endToken)){
                        line = menu.nextLine();
                        }
               int offset = 1;
                if(line.contains(": "))
                        offset++;
                line = line.substring(line.indexOf(":")+offset);
                mealStations.add(line);
                line = menu.nextLine();
                offset = 1;
                if(line.contains(": "))
                        offset++;
                line = line.substring(line.indexOf(":")+offset);
                String string = "";
                
                while(!line.contains("Station:")&&!line.contains(endToken)){
                        string+=line;
                        line = menu.nextLine();
                }
                mealEntrees.add(string);
                
                }
        }
        
        
        /**
         * Crop the old days from the Days stack.
         * @param con Used to pass back in the call to parse,
         * in the event that we have emptied the stack by cropping.
         */
        public static void crop(Context con){
                while(!days.empty()){ //as long as there's still Days in the stack keep looking
                        StringTokenizer st = new StringTokenizer((days.peek()).date); //Grab the date from the Day.
                        int tempMonth = Integer.parseInt(st.nextToken());
                        if(tempMonth<currentMonth){ //If the month is less than the current month, it must be old. 
                                days.pop(); //get rid of the old Day.
                                continue;
                        }
                        int tempDay = Integer.parseInt(st.nextToken());
                                if(tempMonth==currentMonth&&tempDay<currentDay){ //If the day is less than the current day, it must be old.
                                        days.pop(); //get rid of the old Day.
                                        continue;
                                }
                        int tempYear = Integer.parseInt(st.nextToken());
                        if(tempYear<currentYear){ //If the year is less than the current year, it must be old.
                                days.pop(); //get rid of the old Day.
                                continue;
                        }
                        break;
                }
        }
        
        
        /**
         * Method to convert Day Stack to ArrayList<View>
         * For each Day in the stack, assemble a String with the appropriate HTML code and
         * the data from the Day and load that String as the data for a WebView, then put
         * that WebView in the ArrayList to return.
         * @param l LayoutInflator, which is needed to create Views from the food_menu.xml file.
         * @return The completed ArrayList<View> containing the info from the Day Stack.
         */
        public static ArrayList<View> toArrayList(LayoutInflater l){
                ArrayList<View> toReturn = new ArrayList<View>();
                WebView v;
                MenuDay d;
                for(Stack<MenuDay> st = days;!st.empty();){
                        d = st.pop();
                        String webCode = "<html><head><style type=\"text/css\"> h1 { font-size: 1.2em; font-weight: bold; " +
                                        "text-align: center; }</style></head><body>";
                                webCode +="<h1>" + d.printableDate + "</h1><h2><center><strong><u>Lunch</u></strong>" +
                                                "</center></h2>";
                                webCode += d.lunchPrint();
                                webCode += "<h2><center><strong><u>Dinner</u></strong></center></h2>";
                                webCode += d.dinnerPrint();
                                webCode += "</body></html>";
                                v = (WebView) l.inflate(R.layout.food_menu, null).findViewById(R.id.web);
                                v.loadData(webCode, "text/html", "utf-8");
                                v.setBackgroundColor(0);
                                toReturn.add(v);
                }
                if(toReturn.size()==0){
                        v = (WebView) l.inflate(R.layout.food_menu, null).findViewById(R.id.web);
                        String webCode = "<html><head><style type=\"text/css\"> h1 { font-size: 1.2em; font-weight: bold; " +
                        "text-align: center; }</style></head><body><br/><br/><br/><br/><h1>No current cafe menu available</h1></body></html>";
                        v.loadData(webCode, "text/html", "utf-8");
                        v.setBackgroundColor(0);
                        toReturn.add(v);
                }
                return toReturn;
        }
        
}
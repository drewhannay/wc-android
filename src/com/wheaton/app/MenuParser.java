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
 * is made public.
 * @author Alisa Maas
 *
 */
public class MenuParser {
        
       
                
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
        	Calendar calendar = Calendar.getInstance(); //used to get current date
           int currentMonth = calendar.get(Calendar.MONTH); 
           int currentDay = (calendar.get(Calendar.DAY_OF_MONTH));
           int currentYear =(calendar.get(Calendar.YEAR));
           HashMap<String,Integer> monthToInt = new HashMap<String,Integer>();
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
     
                		boolean todaySet = false;
                        //if no file, parse the html
                        try{
                                Scanner menu = new Scanner((InputStream)(new URL("http://dl.dropbox.com/u/36045671/menu.txt")).getContent());
                                String line = menu.nextLine();
                                MenuDay[] parsedDays = new MenuDay[7];
                                MenuHome.todayIndex = -1;
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
                                        line = line.substring(line.indexOf(":")+1).trim();
                                       
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
                                        StringTokenizer st = new StringTokenizer(next.date); //Grab the date from the Day.
                                        int tempMonth = Integer.parseInt(st.nextToken());
                                        boolean notToday = false;
                                        if(tempMonth!=currentMonth){ //If the month is less than the current month, it must be old
                                                notToday = true;
                                        }
                                        int tempDay = Integer.parseInt(st.nextToken());
                                        if(tempDay!=currentDay){ //If the day is less than the current day, it must be old                          
                                                        notToday = true;
                                                }
                                        int tempYear = Integer.parseInt(st.nextToken());
                                        if(tempYear!=currentYear){ //If the year is less than the current year, it must be old.
                                                notToday = true;
                                        }
                                        if(!todaySet&&!notToday){
                                        	MenuHome.todayIndex = i;
                                        	todaySet = true;
                                        }
                                        
                                
                                while(!line.contains("Hours:")){
                                        line = menu.nextLine();
                                }

                                line = line.substring(line.indexOf(":")+1).trim();
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
                                
                       
                                line = line.substring(line.indexOf(":")+1).trim();
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
                                
                                if(todaySet){
                                	for(int i = 6;i>-1;i--){
                                		days.push(parsedDays[i]);
                                	}
                               
                                }
                                else{
                                	MenuHome.todayIndex = 0;
                                }
                                
                                
                                //crop(con);
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

                line = line.substring(line.indexOf(":")+1).trim();
                mealStations.add(line);
                line = menu.nextLine();

                line = line.substring(line.indexOf(":")+1).trim();
                String string = "";
                
                while(!line.contains("Station:")&&!line.contains(endToken)){
                        string+=line;
                        line = menu.nextLine();
                }
                mealEntrees.add(string);
                
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
                        String webCode = "<html><head><style type=\"text/css\"> h1 { font-size: 1.1em; font-weight: bold; " +
					"text-align: center; color:#CC6600; } h2 { font-size: 0.9em; } h3 { font-style:italic; font-size: 0.8em;}" +
					"p { font-size: 0.7em; } </style></head><body>";
                                webCode +="<h1>" + d.printableDate + "</h1><h2><center><strong><u>Lunch</u></strong>" +
                                                "</center></h2>";
                                webCode += d.lunchPrint();
                                webCode += "<h2><center><strong><u>Dinner</u></strong></center></h2>";
                                webCode += d.dinnerPrint();
                                webCode += "</body></html>";
                                v = (WebView) l.inflate(R.layout.webview, null).findViewById(R.id.web);
                                v.loadData(webCode, "text/html", "utf-8");
                                v.setBackgroundColor(0);
                                toReturn.add(v);
                }
                if(toReturn.size()==0){
                        v = (WebView) l.inflate(R.layout.webview, null).findViewById(R.id.web);
                        String webCode = "<html><head><style type=\"text/css\"> h1 { font-size: 1.2em; font-weight: bold; " +
                        "text-align: center; }</style></head><body><br/><br/><br/><br/><h1>No current cafe menu available</h1></body></html>";
                        v.loadData(webCode, "text/html", "utf-8");
                        v.setBackgroundColor(0);
                        toReturn.add(v);
                }
                return toReturn;
        }
        
}
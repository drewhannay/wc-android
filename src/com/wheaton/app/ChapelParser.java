package com.wheaton.app;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

public class ChapelParser{

	private static boolean ERROR = false;

	private static Calendar calendar = Calendar.getInstance(); //used to get current date
	private static int currentMonth = calendar.get(Calendar.MONTH); 
	private static int currentYear =(calendar.get(Calendar.YEAR));
	private static ChapelSchedule scheduleOld;

	private static ArrayList<ChapelWeek> schedule;
	
	
	public static void parse(Context con){
//		try {
//
//			boolean readin = false;
//			for(String a: con.fileList()){
//				if(a.equals("chapel_cache"))
//					readin = true;
//			}
//
//
//			if(readin){
//				Log.e("R","Reading in");
//				FileInputStream f_in = con.openFileInput("chapel_cache");
//
//				// Read object using ObjectInputStream
//				ObjectInputStream obj_in = new ObjectInputStream (f_in);
//				scheduleOld = (ChapelSchedule) obj_in.readObject(); //read in the stack, if there.
//				if(scheduleOld.year==currentYear||(scheduleOld.firstSemester&&currentMonth>7)||(!scheduleOld.firstSemester&&currentMonth<8))
//					return;
//			}
//
//		}catch (Exception e) {
//			e.printStackTrace();
//			ERROR = true;
//		}
		URL chapel;
		try {
			chapel = new URL("http://dl.dropbox.com/u/35618101/Chapel%20Schedule.txt");

			Scanner chapelin = new Scanner((InputStream) chapel.getContent());
			String line = chapelin.nextLine();
			
			//Skip past the example text
			while(chapelin.hasNext()&&(!(line.contains("-----"))))
				line = chapelin.nextLine();
			line = chapelin.nextLine();//And skip the line of dashes

			
			ChapelWeek week;
			String[] info;

			week = new ChapelWeek();
			
			while(chapelin.hasNext()&&(!(line.contains("-----")))){
				String text = "";
				while(chapelin.hasNext()&&(!(line.contains("-----")))){
					line = chapelin.nextLine();
					text += line;
				}
				Log.e("HERE",text);
				if(chapelin.hasNext()) //line I put in to fix NoSuchElementException <--Alisa
					line = chapelin.nextLine();//Skip over the line of dashes
//				info = parseDay(text);
//				if(week.addDay(info))
//					continue;
//				else{
////					schedule.add(week);
//					week = new ChapelWeek();
//					week.addDay(info);
//				}
			}
//			schedule.add(week);
			
//			try{
//				FileOutputStream f_out = con.openFileOutput("chapel_cache", Context.MODE_PRIVATE);
//				// Write object with ObjectOutputStream
//				ObjectOutputStream obj_out = new ObjectOutputStream(f_out);
//				// Write object out to disk
//				obj_out.writeObject (scheduleOld);
//			}catch(Exception a){
//				Log.e("ChapelParserFileIO", a.toString());
//				ERROR = true;
//			}
		} catch (Exception e1) {
			for(StackTraceElement s: e1.getStackTrace()){
				Log.e("ChapelParser1",s.toString()); //Print the WHOLE stack trace so we can tell
				//where this error actually came from
			}
			
			ERROR = true;
		}
	}

	public static String[] parseDay(String text){
		String temp = "";
		text = text.substring(text.indexOf("Date: "+6));
		Log.e("ERRORRRRRR", text);
		
		
		return null;
	}
	
	
	
	
	public static void crop(Context con){

	}

	public static ArrayList<View> toArrayList(LayoutInflater l){
		ArrayList<View> toReturn = new ArrayList<View>();
		WebView v = (WebView) l.inflate(R.layout.food_menu, null).findViewById(R.id.web);
		String webCode;
		//if(ERROR){
		if(true){
			webCode = "<html><head><style type=\"text/css\"> h1 { font-size: 1.2em; font-weight: bold; " +
			"text-align: center; }</style></head><body><br/><br/><br/><br/><h1>The chapel schedule is not yet available. Check back soon!</h1></body></html>";
			v.loadData(webCode, "text/html", "utf-8");
			v.setBackgroundColor(0);
			toReturn.add(v);
			return toReturn;
		}
		webCode = "<html><head><style type=\"text/css\"> h1 { font-size: 1.2em; font-weight: bold; " +
		"text-align: center; }</style></head><body>";
		webCode += scheduleOld.html;

		webCode += "</body></html>";

		v.loadData(webCode, "text/html", "utf-8");
		v.setBackgroundColor(0);
		toReturn.add(v);
		return toReturn;
	}
}
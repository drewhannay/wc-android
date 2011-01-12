package com.wheaton.app;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
import java.util.Stack;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;

public class ChapelParser{
	
	private static Calendar calendar = Calendar.getInstance(); //used to get current date
	private static int currentMonth = calendar.get(Calendar.MONTH); 
	private static int currentYear =(calendar.get(Calendar.YEAR));
	private static ChapelSchedule schedule;
	
	public static void parse(Context con){
		try {
			
				boolean readin = false;
				for(String a: con.fileList()){
					if(a.equals("chapel_cache"))
						readin = true;
				}
				
					
			if(readin){
				Log.e("R","Reading in");
			FileInputStream f_in = con.openFileInput("chapel_cache");
			
			// Read object using ObjectInputStream
			ObjectInputStream obj_in = new ObjectInputStream (f_in);
			schedule = (ChapelSchedule) obj_in.readObject(); //read in the stack, if there.
			if(schedule.year==currentYear||(schedule.firstSemester&&currentMonth>7)||(!schedule.firstSemester&&currentMonth<8))
				return;
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		URL chapel;
		try {
			chapel = new URL("http://www.wheaton.edu/chaplain/Program/schedule.html");
		
		Scanner chapelin = new Scanner((InputStream) chapel.getContent());
		String line = chapelin.nextLine();
		String text = "";
		while(chapelin.hasNext()&&(!(line.contains("Chapel Schedule"))))
			line = chapelin.nextLine();
		
		line = chapelin.nextLine();
		
		while(chapelin.hasNext()){
			text += line;
			if(line.contains("</table>"))
				break;
			line = chapelin.nextLine();
		}
		boolean firstSemester = currentMonth>7;
		int year = currentYear;
		String html = text;
		schedule = new ChapelSchedule(firstSemester,html,year);
		try{
			FileOutputStream f_out = con.openFileOutput("chapel_cache", Context.MODE_PRIVATE);
			// Write object with ObjectOutputStream
			ObjectOutputStream obj_out = new ObjectOutputStream(f_out);
			// Write object out to disk
			obj_out.writeObject (schedule);
		}catch(Exception a){
			Log.e("MenuParserFileIO", a.toString());
		}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public static void crop(Context con){
		
	}
	
	public static ArrayList<View> toArrayList(LayoutInflater l){
		ArrayList<View> toReturn = new ArrayList<View>();
		WebView v = (WebView) l.inflate(R.layout.food_menu, null).findViewById(R.id.web);
		
		String webCode = "<html><head><style type=\"text/css\"> h1 { font-size: 1.2em; font-weight: bold; " +
					"text-align: center; }</style></head><body>";
		webCode += schedule.html;
		
		webCode += "</body></html>";
		
		v.loadData(webCode, "text/html", "utf-8");
		v.setBackgroundColor(0);
		toReturn.add(v);
		return toReturn;
	}
}

package com.wheaton.app;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class OpenFloorParser {
	
	public static void parse(Context con){
		
	}
	
	public static ArrayList<View> toArrayList(LayoutInflater l){
		ArrayList<View> toReturn = new ArrayList<View>();
//		WebView v;
//		String webCode;
//		if(ERROR){
//			v = (WebView) l.inflate(R.layout.food_menu, null).findViewById(R.id.web);
//			webCode = "<html><head><style type=\"text/css\"> h1 { font-size: 1.2em; font-weight: bold; " +
//			"text-align: center; }</style></head><body><br/><br/><br/><br/><h1>The chapel schedule is not yet available. Check back soon!</h1></body></html>";
//			v.loadData(webCode, "text/html", "utf-8");
//			v.setBackgroundColor(0);
//			toReturn.add(v);
//			return toReturn;
//		}
//		for(ChapelWeek week:schedule){
//			v = (WebView) l.inflate(R.layout.food_menu, null).findViewById(R.id.web);
//			v.loadData(week.toString(), "text/html", "utf-8");
//			v.setBackgroundColor(0);
//			toReturn.add(v);
//		}
		return toReturn;
	}

}

package com.wheaton.app;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class Map extends MapActivity {
	
	GeoPoint p;
	
	class MyItemizedOverlay extends ItemizedOverlay {

		private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
		Context mContext;
		
		public MyItemizedOverlay(Drawable defaultMarker, Context context) {
			super(boundCenterBottom(defaultMarker));
			mContext = context;
		}
		
		public void addOverlay(OverlayItem overlay) {
		    mOverlays.add(overlay);
		    populate();
		}

		@Override
		protected OverlayItem createItem(int i) {
			return mOverlays.get(i);
		}

		@Override
		public int size() {
			return mOverlays.size();
		}
		
		@Override
		protected boolean onTap(int index) {
		  OverlayItem item = mOverlays.get(index);
		  AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		  dialog.setTitle(item.getTitle());
		  dialog.setMessage(item.getSnippet());
		  dialog.setNegativeButton("Close", null);
		  dialog.show();
		  return true;
		}
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.map);
	    
	    MapView mapView = (MapView) findViewById(R.id.map);
	    mapView.setBuiltInZoomControls(true);
	    
	    MapController mc = mapView.getController();
        String coordinates[] = {"41.8691", "-88.0970"};
        double lat = Double.parseDouble(coordinates[0]);
        double lng = Double.parseDouble(coordinates[1]);
 
        p = new GeoPoint(
            (int) (lat * 1E6), 
            (int) (lng * 1E6));
 
        mc.animateTo(p);
        mc.setZoom(17);  
        mapView.setSatellite(true);
        
        List<Overlay> mapOverlays = mapView.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.push_pin);
        MyItemizedOverlay itemizedoverlay = new MyItemizedOverlay(drawable, this);
        
        GeoPoint point = new GeoPoint(41868334,-88094650);
        OverlayItem overlayitem = new OverlayItem(point, "Corinthian Co-op", "Hours:\nMonday: 1pm - 3pm\n" +
        		"Tuesday: 10:30am - 12pm\nWednesday: 3pm - 5pm\nThursday: 11:30am - 12:30pm\nSaturday 10am -12pm");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
        
        point = new GeoPoint(41870643,-88096887);
        overlayitem = new OverlayItem(point, "Sports & Recreation Complex", "Hours:\nMonday-Friday: 6am - 11pm\n" +
        		"Saturday: 8am - 11pm\nSunday: 2pm - 5pm");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
        
        point = new GeoPoint(41872681,-88096774);
        overlayitem = new OverlayItem(point, "Fischer Hall", "");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);

        point = new GeoPoint(41870120,-88099526);
        overlayitem = new OverlayItem(point, "Buswell Memorial Library", "Hours:\nMonday-Thursday: 7:30am - 12am\n" +
        	 "Friday: 7:30am - 10pm\n" + "Saturday: 8:30am - 10pm\nSunday: Closed");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
        
        point = new GeoPoint(41870707,-88094805);
        overlayitem = new OverlayItem(point, "Smith/Traber Halls", "");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
        
        point = new GeoPoint(41871774,-88098899);
        overlayitem = new OverlayItem(point, "Hearth House", "");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
        
        point = new GeoPoint(41871436,-88098936);
        overlayitem = new OverlayItem(point, "Phoenix House", "");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
        
        point = new GeoPoint(41871253,-88098917);
        overlayitem = new OverlayItem(point, "Irving House", "");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
        
        point = new GeoPoint(41871073,-88098917);
        overlayitem = new OverlayItem(point, "Hunter House", "");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);

        point = new GeoPoint(41871097,-88098542);
        overlayitem = new OverlayItem(point, "Fine Arts House", "");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
        
        point = new GeoPoint(41871448,-88095245);
        overlayitem = new OverlayItem(point, "LeBar House", "");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
        
        point = new GeoPoint(41871265,-88095259);
        overlayitem = new OverlayItem(point, "Country House", "");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
        
        point = new GeoPoint(41870909,-88095852);
        overlayitem = new OverlayItem(point, "International House", "");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
        
        point = new GeoPoint(41870685,-88095860);
        overlayitem = new OverlayItem(point, "Kay House", "");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
        
        point = new GeoPoint(41870518,-88095862);
        overlayitem = new OverlayItem(point, "Fellowship House", "");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
        
        point = new GeoPoint(41870364,-88095892);
        overlayitem = new OverlayItem(point, "Mathetai House", "");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);

        point = new GeoPoint(41868790,-88094875);
        overlayitem = new OverlayItem(point, "White House", "");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);

        point = new GeoPoint(41868263,-88096831);
        overlayitem = new OverlayItem(point, "Chase House", "");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);

        point = new GeoPoint(41866892,-88100932);
        overlayitem = new OverlayItem(point, "Graham House", "");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);

        point = new GeoPoint(41870026,-88101527);
        overlayitem = new OverlayItem(point, "Harbor House", "");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);

        point = new GeoPoint(41870849,-88101111);
        overlayitem = new OverlayItem(point, "Teresa House", "");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
        
        point = new GeoPoint(41871085,-88101135);
        overlayitem = new OverlayItem(point, "Kilby House", "");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
        
        point = new GeoPoint(41869888,-88100621);
        overlayitem = new OverlayItem(point, "Edman Memorial Chapel", "");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
        
        point = new GeoPoint(41869053,-88100626);
        overlayitem = new OverlayItem(point, "Pierce Memorial Chapel", "");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
        
        point = new GeoPoint(41868746,-88100669);
        overlayitem = new OverlayItem(point, "McAlister Conservatory", "");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
        
        point = new GeoPoint(41869173,-88099875);
        overlayitem = new OverlayItem(point, "Adams Hall", "");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
        
        point = new GeoPoint(41868498,-88099617);
        overlayitem = new OverlayItem(point, "Blanchard Hall", "");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
        
        point = new GeoPoint(41869145,-88098738);
        overlayitem = new OverlayItem(point, "Memorial Student Center", "");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
        
        point = new GeoPoint(41868966,-88098196);
        overlayitem = new OverlayItem(point, "Williston Hall", "");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
        
        point = new GeoPoint(4186889,-88097933);
        overlayitem = new OverlayItem(point, "Student Services Building", "Hours:\nMonday - Thursday: 9am - 6pm\n"+
"Friday: 9am - 5pm\nSaturday: 10am - 4pm");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
        
        point = new GeoPoint(41869685,-88098952);
        overlayitem = new OverlayItem(point, "Schell Hall", "");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
        
        point = new GeoPoint(41870056,-88098877);
        overlayitem = new OverlayItem(point, "Wyngarden Health Center", "Clinic Hours:\nMonday - Thursday: 7:30am - 6:30pm\n"+
        	"Friday: 7:30am - 5pm\nSaturday: 11am - 1pm");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
        
        point = new GeoPoint(41870611,-88098453);
        overlayitem = new OverlayItem(point, "Armerding Hall", "");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
        
        point = new GeoPoint(41870256,-8809789);
        overlayitem = new OverlayItem(point, "Evans Hall", "");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
        
        point = new GeoPoint(41870256,-8809789);
        overlayitem = new OverlayItem(point, "McManis Hall", "");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
        
        point = new GeoPoint(41869165,-88097139);
        overlayitem = new OverlayItem(point, "Todd M. Beamer Student Center", "");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
        
        point = new GeoPoint(41869733,-88096152);
        overlayitem = new OverlayItem(point, "Wheaton Science Center", "");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
        
        point = new GeoPoint(41869549,-88094972);
        overlayitem = new OverlayItem(point, "Jenks Hall", "");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
        
        point = new GeoPoint(41866737,-88099376);
        overlayitem = new OverlayItem(point, "Billy Graham Center", "Museum Hours:\nMonday-Saturday: 9:30am - 5:30pm\n"+
        		"Sunday: 1pm - 5pm");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
        
        point = new GeoPoint(41867396,-88095986);
        overlayitem = new OverlayItem(point, "McCully Stadium", "");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
        
        point = new GeoPoint(41869549,-88094972);
        overlayitem = new OverlayItem(point, "Bean Stadium", "");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
        
        point = new GeoPoint(41867787,-88092837);
        overlayitem = new OverlayItem(point, "Chase Service Center", "");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
        
        point = new GeoPoint(41872525,-88097633);
        overlayitem = new OverlayItem(point, "Grammar School", "");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
        
        point = new GeoPoint(41870617,-88101181);
        overlayitem = new OverlayItem(point, "Marion E. Wade Center", "Hours:\nMonday - Friday: 9am - 4pm\n"
        		+ "Saturdays 9am - 12pm");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
        
        point = new GeoPoint(41868298,-88101487);
        overlayitem = new OverlayItem(point, "Westgate", "");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
        
        point = new GeoPoint(41866175,-88099907);
        overlayitem = new OverlayItem(point, "Campus Utility", "");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
       
        mapView.invalidate();
        
        
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}

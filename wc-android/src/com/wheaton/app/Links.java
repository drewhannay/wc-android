package com.wheaton.app;

import java.util.Map;
import java.util.TreeMap;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Links extends ListActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.links);

		final Map<String, String> linkMap = new TreeMap<String, String>();

		linkMap.put(getString(R.string.academic_calendar), "http://wheaton.edu/Academics/Academic-Calendar");
		linkMap.put(getString(R.string.admissions), "http://wheaton.edu/Admissions-and-aid");
		linkMap.put(getString(R.string.arena_theater), "http://www.wheatonarena.com");
		linkMap.put(getString(R.string.athletics), "http://athletics.wheaton.edu");
		linkMap.put(getString(R.string.bookstore), "http://www.wheatonbooks.com");
		linkMap.put(getString(R.string.chapel_schedule), "http://www.wheaton.edu/Student-Life/Spiritual-Life/Chapel/Schedule");
		linkMap.put(getString(R.string.conservatory), "http://www.wheaton.edu/academics/departments/conservatory");
		linkMap.put(getString(R.string.counseling_center), "http://www.wheaton.edu/student-life/student-care/counseling-center");
		linkMap.put(getString(R.string.course_schedules), "http://wheaton.edu/Academics/Services/Registrar/Schedules");
		linkMap.put(getString(R.string.events_calendar), "http://wheaton.edu/events");
		linkMap.put(getString(R.string.general_education_requirements),
				"http://www2.wheaton.edu/Registrar/catalog/ug_acad_policies.htm#General_Education_Requirements");
		linkMap.put(getString(R.string.honeyrock), "http://www.honeyrockcamp.org");
		linkMap.put(getString(R.string.housing_calendar), "http://www.wheaton.edu/Student-Life/Living-at-Wheaton/Calendar");
		linkMap.put(getString(R.string.library), "http://library.wheaton.edu");
		linkMap.put(getString(R.string.long_term_calendar),
						"http://www.wheaton.edu/Academics/Services/Registrar/~/media/Files/Academics/Academic%20Services/Office%20of%20the%20Registrar/Schedule/future_calendar.pdf");
		linkMap.put(getString(R.string.metra_schedule), "http://www.metrarail.com");
		linkMap.put(getString(R.string.my_wheaton_portal), "http://my.wheaton.edu");
		linkMap.put(getString(R.string.registrar_s_catalog), "http://www2.wheaton.edu/Registrar/catalog/index.htm");
		linkMap.put(getString(R.string.wetn), "http://www.wheaton.edu/wetn");

		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, linkMap.keySet().toArray(new String[linkMap.size()])));

		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
		listView.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse((String) linkMap.values().toArray()[position])));
			}
		});
	}
}

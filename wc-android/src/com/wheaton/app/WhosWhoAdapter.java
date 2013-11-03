package com.wheaton.app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wheaton.utility.ImageLoader;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

class WhosWhoAdapter extends BaseAdapter {
	
	public WhosWhoAdapter(Context context, JSONArray results) {
		mResults = results;
		mContext = context;
		mImageLoader = new ImageLoader(context);
	}
	
	@Override
	public int getCount() {
		return mResults.length();
	}

	@Override
	public Object getItem(int position) {
		try {
			return mResults.get(position);
		} catch (JSONException e) {
			Log.e(TAG, "getItem(position=" + position + ")", e);
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.whos_who_search_result, parent, false);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.cpo = (TextView) convertView.findViewById(R.id.cpo);
			holder.type = (TextView) convertView.findViewById(R.id.type);
			holder.classification = (TextView) convertView.findViewById(R.id.classification);
			holder.picture = (ImageView) convertView.findViewById(R.id.picture);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		try {
			JSONObject resultJSON = mResults.getJSONObject(position);
			
			if (holder.picture != null) {
				mImageLoader.DisplayImage(resultJSON.getJSONObject("image").getJSONObject("url").getString("medium"), holder.picture); 
			}

			JSONObject name = resultJSON.getJSONObject("name");
			
			String firstName = name.getString("first");
			String preferredName = name.getString("preferred");
			String middleName = name.getString("middle");
			String lastName = name.getString("last");

			StringBuilder builder = new StringBuilder();
			if (!isNullOrEmpty(firstName))
				builder.append(firstName);
			if (!isNullOrEmpty(preferredName)) {
				builder.append(" \"");
				builder.append(preferredName);
				builder.append("\"");
			}
			if (!isNullOrEmpty(middleName)) {
				builder.append(' ');
				builder.append(middleName);
			}
			if (!isNullOrEmpty(lastName)) {
				builder.append(' ');
				builder.append(lastName);
			}
			holder.name.setText(builder.toString());

			setTextOrHideTextView("CPO ", resultJSON.getString("address"), holder.cpo);
			setTextOrHideTextView("", resultJSON.getString("classification"), holder.classification);
			if (resultJSON.getInt("type") == 1)
				holder.type.setText("Faculty/Staff");
			else
				holder.type.setText("Student");
		}
		catch (Exception e) {
			Log.e(TAG, "getView(position=" + position + ")", e);
		}

		return convertView;
	}

	private void setTextOrHideTextView(String prefix, String text, TextView view) {
		if (!isNullOrEmpty(text))
			view.setText(prefix + text);
		else
			view.setVisibility(View.GONE);
	}

	private boolean isNullOrEmpty(String string) {
		return string == null || string.equals("") || string.equals("null");
	}
	
	private static class ViewHolder {
		TextView name;
		TextView cpo;
		TextView type;
		TextView classification;
		ImageView picture;
	}
	
	private static final String TAG = WhosWhoAdapter.class.toString();
	
	private JSONArray mResults;
	private Context mContext;
	private ImageLoader mImageLoader;
}
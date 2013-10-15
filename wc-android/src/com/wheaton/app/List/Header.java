package com.wheaton.app.List;

import com.wheaton.app.R;
import com.wheaton.app.List.TwoTextArrayAdapter.RowType;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class Header implements Item {
    private final String name;

    public Header(String name) {
        this.name = name;
    }

    @Override
    public int getViewType() {
        return RowType.HEADER_ITEM.ordinal();
    }

    @Override
    public View getView(Context context, View convertView) {
        View view;
        if (convertView == null) {
            view = (View) LayoutInflater.from(context).inflate(R.layout.header_layout, null);
            // Do some initialization
        } else {
            view = convertView;
        }

        TextView text = (TextView) view.findViewById(R.id.list_hdr);
        text.setText(name);

        return view;
    }

}

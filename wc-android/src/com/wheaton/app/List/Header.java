package com.wheaton.app.List;

import com.wheaton.app.R;
import com.wheaton.app.List.TwoTextArrayAdapter.RowType;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    public View getView(Context context, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_header, parent, false);
        } else {
            view = convertView;
        }

        TextView text = (TextView) view.findViewById(R.id.list_hdr);
        text.setText(name);

        return view;
    }

}

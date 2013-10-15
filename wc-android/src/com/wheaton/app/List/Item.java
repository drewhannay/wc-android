package com.wheaton.app.List;

import android.content.Context;
import android.view.View;

public interface Item {
    public int getViewType();
    public View getView(Context context, View convertView);
}
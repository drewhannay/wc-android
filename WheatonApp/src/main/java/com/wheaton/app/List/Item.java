package com.wheaton.app.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public interface Item {
    public int getViewType();
    public View getView(Context context, View convertView, ViewGroup parent);
}
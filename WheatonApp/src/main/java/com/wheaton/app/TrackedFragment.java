package com.wheaton.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;

public class TrackedFragment extends Fragment {
    private Tracker tracker;
    private String activityId;
    private String fragmentId;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EasyTracker.getInstance().setContext(getActivity().getApplicationContext());
//        this.tracker = EasyTracker.getTracker();
//        this.fragmentId = getClass().getSimpleName();
//        this.activityId = getActivity().getClass().getSimpleName();
    }

    @Override
    public void onResume() {
        super.onResume();
//        this.tracker.sendView("/" + this.activityId + "/" + this.fragmentId);
    }
}
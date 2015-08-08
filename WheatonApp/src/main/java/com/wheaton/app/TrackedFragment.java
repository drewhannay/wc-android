package com.wheaton.app;

import android.support.v4.app.Fragment;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class TrackedFragment extends Fragment {
    @Override
    public void onResume() {
        super.onResume();
        Tracker tracker = ((WheatonApplication) getActivity().getApplicationContext()).getTracker();
        tracker.setScreenName(getClass().getSimpleName());
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
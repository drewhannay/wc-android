package com.wheaton.app;

import android.app.Application;
import android.support.annotation.NonNull;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

public class WheatonApplication extends Application {

    private Tracker tracker;

    @Override
    public void onCreate() {
        super.onCreate();

        GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
        analytics.setLocalDispatchPeriod(1800);
//        analytics.setDryRun(BuildConfig.DEBUG);

        tracker = analytics.newTracker("UA-8708870-7");
        tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(true);
    }

    @NonNull
    public Tracker getTracker() {
        return tracker;
    }
}

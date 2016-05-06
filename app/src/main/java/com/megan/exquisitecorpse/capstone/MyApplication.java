package com.megan.exquisitecorpse.capstone;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.Logger;

public class MyApplication extends com.activeandroid.app.Application {
    public Tracker mTracker;

    public void startTracking(){
        if(mTracker == null){
            GoogleAnalytics ga = GoogleAnalytics.getInstance(this);

            mTracker = ga.newTracker(R.xml.track_app);

            ga.enableAutoActivityReports(this);

            ga.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
        }
    }

    public Tracker getTracker(){
        startTracking();
        return mTracker;
    }

}

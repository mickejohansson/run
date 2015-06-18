package org.mikaeljohansson.run.business;

import android.location.Location;

import java.util.ArrayList;


public class WorkoutLog {
    private static ArrayList<Location> mLocationList;

    public static void add(Location location) {
        if (mLocationList == null) {
            mLocationList = new ArrayList<>();
        }

        mLocationList.add(location);
    }

    public static void clear() {
        mLocationList.clear();
    }

    public static ArrayList<Location> getLog() {
        return mLocationList;
    }
}

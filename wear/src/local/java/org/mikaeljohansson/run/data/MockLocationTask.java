package org.mikaeljohansson.run.data;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.SystemClock;

import org.mikaeljohansson.run.RunApplication;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MockLocationTask extends AsyncTask {

    private static final String TRKPT = "trkpt";
    private static final String LAT = "lat";
    private static final String LON = "lon";
    private static final String MY_MOCK_PROVIDER = "MyMockProvider";
    private static final String TIME = "time";

    private boolean mKeepGoing = true;
    private ArrayList<Location> mLocations;

    public MockLocationTask() {
        mLocations = new ArrayList<>();
        InputStream inputStream = null;

        try {
            inputStream = RunApplication.getAppContext().getAssets().open("run.gpx");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(inputStream, null);
            int eventType = parser.getEventType();
            Location location = null;
            String text = null;
            Date date = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (parser.getName().equals(TRKPT)) {
                        location = new Location(MY_MOCK_PROVIDER);
                        for (int i = 0; i < parser.getAttributeCount(); i++) {
                            if (parser.getAttributeName(i).equals(LAT)) {
                                location.setLatitude(Double.parseDouble(parser.getAttributeValue(i)));
                            } else if (parser.getAttributeName(i).equals(LON)) {
                                location.setLongitude(Double.parseDouble(parser.getAttributeValue(i)));
                            }

                        }
                    }
                } else if (eventType == XmlPullParser.TEXT) {
                    text = parser.getText();
                } else if (eventType == XmlPullParser.END_TAG) {
                    if (parser.getName().equals(TRKPT)) {
                        location.setTime(date.getTime());
                        mLocations.add(location);
                        location = null;
                    } else if (parser.getName().equals(TIME)) {
                        String dateString = text;
                        date = dateFormat.parse(dateString);
                    }
                }

                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        LocationManager locationManager = (LocationManager) RunApplication.getAppContext().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.getProvider(MY_MOCK_PROVIDER) != null) {
            locationManager.removeTestProvider(MY_MOCK_PROVIDER);
        }
        locationManager.addTestProvider(MY_MOCK_PROVIDER, false, false, false, false, false, false, false, Criteria.POWER_LOW, Criteria.ACCURACY_FINE);
        locationManager.setTestProviderEnabled(MY_MOCK_PROVIDER, true);

        int i = 0;
        while (mKeepGoing) {
            Location mockLocation = mLocations.get(i % (mLocations.size() - 1));

            mockLocation.setAccuracy(Criteria.ACCURACY_FINE);
            mockLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
            locationManager.setTestProviderLocation(MY_MOCK_PROVIDER, mockLocation);
            System.out.println("setting location: " + mockLocation);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            i++;
        }
        return null;
    }
}

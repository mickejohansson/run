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
import java.util.ArrayList;
import java.util.Date;

public class MockLocationTask extends AsyncTask {

    private static final String TRKPT = "trkpt";
    private static final String LAT = "lat";
    private static final String LON = "lon";
    private static final String MY_MOCK_PROVIDER = "MyMockProvider";

    private boolean mKeepGoing = true;
    private ArrayList<GPSPoint> mGPSPoints;

    public MockLocationTask() {
        mGPSPoints = new ArrayList<>();
        InputStream inputStream = null;

        try {
            inputStream = RunApplication.getAppContext().getAssets().open("run.gpx");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(inputStream, null);
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (parser.getName().equals(TRKPT)) {
                        GPSPoint point = new GPSPoint();
                        for (int i = 0; i < parser.getAttributeCount(); i++) {
                            if (parser.getAttributeName(i).equals(LAT)) {
                                point.latitude = Double.parseDouble(parser.getAttributeValue(i));
                            } else if (parser.getAttributeName(i).equals(LON)) {
                                point.longitude = Double.parseDouble(parser.getAttributeValue(i));
                            }

                        }
                        mGPSPoints.add(point);
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
            GPSPoint point = mGPSPoints.get(i % (mGPSPoints.size() - 1));

            Location mockLocation = new Location(MY_MOCK_PROVIDER);
            mockLocation.setLatitude(point.latitude);
            mockLocation.setLongitude(point.longitude);
            mockLocation.setAltitude(0);
            mockLocation.setTime(System.currentTimeMillis());
            mockLocation.setAccuracy(Criteria.ACCURACY_FINE);
            mockLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
            locationManager.setTestProviderLocation(MY_MOCK_PROVIDER, mockLocation);
            System.out.println("settingTestProviderLocation: " + mockLocation);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            i++;
        }
        return null;
    }

    private class GPSPoint {
        double latitude;
        double longitude;
        double altitude;
        Date date;
    }
}

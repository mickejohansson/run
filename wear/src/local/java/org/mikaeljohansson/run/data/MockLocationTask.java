package org.mikaeljohansson.run.data;

import android.os.AsyncTask;

import org.mikaeljohansson.run.RunApplication;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

public class MockLocationTask extends AsyncTask {

    private static final String TRKPT = "trkpt";
    private static final String LAT = "lat";
    private static final String LON = "lon";

    private boolean mKeepGoing = true;
    private Collection<GPSPoint> mGPSPoints;

    public MockLocationTask() {
        mGPSPoints = new LinkedList<>();
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
                if (eventType == XmlPullParser.END_TAG) {
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
        while (mKeepGoing) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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

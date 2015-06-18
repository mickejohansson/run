package org.mikaeljohansson.run.data;

import android.location.Location;

import org.mikaeljohansson.run.RunApplication;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GpxParser {

    private static final String TRKPT = "trkpt";
    private static final String LAT = "lat";
    private static final String LON = "lon";
    private static final String MY_MOCK_PROVIDER = "MyMockProvider";
    private static final String TIME = "time";

    public static ArrayList<Location> parse(String gpxFilename) {
        ArrayList<Location> locations = new ArrayList<>();
        InputStream inputStream = null;

        try {
            inputStream = RunApplication.getAppContext().getAssets().open(gpxFilename);
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
                        locations.add(location);
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

        return locations;
    }

    public static void writeGpxFile(String fileName, ArrayList<Location> locations) {
        FileWriter
        for (Location location : locations) {
            System.out.println(location.toString());
        }
    }
}

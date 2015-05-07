package org.mikaeljohansson.run;

import android.app.Application;
import android.content.Context;

public class RunApplication extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();

        sContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return sContext;
    }
}

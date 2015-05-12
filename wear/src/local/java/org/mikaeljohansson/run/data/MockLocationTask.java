package org.mikaeljohansson.run.data;

import android.os.AsyncTask;

public class MockLocationTask extends AsyncTask {

    private boolean mKeepGoing = true;

    @Override
    protected Object doInBackground(Object[] objects) {
        while (mKeepGoing) {
            System.out.println("doInBackground");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

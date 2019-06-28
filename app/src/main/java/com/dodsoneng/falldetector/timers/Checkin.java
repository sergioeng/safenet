package com.dodsoneng.falldetector.timers;

import android.util.Log;

import com.dodsoneng.falldetector.MainActivity;

import java.util.TimerTask;

/**
 * Created by sergio.eng on 10/23/17.
 */

public class Checkin extends TimerTask {

    private static String TAG = "FD.timers.Checkin......";
    private static Checkin singleton = null;

    public Checkin () {
        Log.d (TAG, "constructor()");
    }

    public static Checkin initiate() {

        Log.d (TAG, "initiate():");

        if (null == singleton) {
            singleton = new Checkin ();
        }

        return singleton;

    }


    public void run () {
        MainActivity.startFlashCheckinButton();
    }
}

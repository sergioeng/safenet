package com.dodsoneng.falldetector.timers;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;

import com.dodsoneng.falldetector.MainActivity;
import com.dodsoneng.falldetector.Positioning;
import com.dodsoneng.falldetector.Telephony;

import java.util.TimerTask;

import static com.dodsoneng.falldetector.models.EventInfo.MAX_EVENTS;

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

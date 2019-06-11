/*
The MIT License (MIT)

Copyright (c) 2016

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.dodsoneng.falldetector;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class Guardian extends Service {

    private static String TAG = "FD.Guardian............";

    private static int NOTIFICATION_ID = 1;
    private static Intent intent = null;
    private Context context;
    private NotificationManager gNotificationManager;
    private Positioning positioning;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate()");
        context = getApplicationContext();
        positioning = Positioning.initiate(context);
        Detector.initiate(context);
        Log.d(TAG, "onCreate() ... end");
    }

    public static void initiate(Context context) {
        Log.d(TAG, "initiate()");
        intent = new Intent(context, Guardian.class);
        context.startService(intent);
    }

    public static void terminate (Context context) {
        Log.d(TAG, "terminate()");
        //Intent intent = new Intent(context, Guardian.class);
        //this.stopService(intent);

        context.stopService(intent);
        Telephony.handsfree(context, false);
                        /*
                Intent intentMyService = new Intent(this, Telephony.class);
                stopService(intentMyService);
                */
        ComponentName component = new ComponentName(context, Telephony.class);
        int status = context.getPackageManager().getComponentEnabledSetting(component);
        if(status == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
            Log.d(TAG, "receiver is enabled");
        } else if(status == PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
            Log.d(TAG, "receiver is disabled");
        }
        else {
            Log.d(TAG, "receiver is ["+status+"]");
        }

    }

    public static void trigger () {
        Positioning.trigger();
    }

    public static void stop () {
        Positioning.stop ();
    }


    //    @SuppressWarnings("deprecation")
    @Override
    public int onStartCommand(Intent intent, int flags, int startID) {

        Log.d (TAG, "onStartCommand(): startID="+startID + " flags="+flags);

        /** SERGIO ENG - Previous old code, which is deprecated
        long now = System.currentTimeMillis();
        Notification notification = new Notification(
                android.R.drawable.stat_sys_warning, "Guardian is active.", now);
        Intent about = new Intent(this, MainActivity.class);
        PendingIntent pending = PendingIntent.getActivity(this, 0, about, 0);
        notification.setLatestEventInfo(this, "Guardian", "Guardian is active", pending);
        startForeground(1, notification);
        **/


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(android.R.drawable.stat_sys_warning)
                        .setContentTitle("Fall Detector Guardian")
                        .setContentText("Guardian is active");



// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        gNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.

        gNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());


        return (START_STICKY);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d (TAG, "onBind()");
        return (null);
    }

    @Override
    public void onDestroy() {

        Log.d (TAG, "onDestroy()");
        gNotificationManager.cancel(NOTIFICATION_ID);
        positioning.terminate(context);
        positioning = null;
        Detector.terminate();
    }

}

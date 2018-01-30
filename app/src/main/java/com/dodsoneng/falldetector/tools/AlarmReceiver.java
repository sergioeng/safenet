package com.dodsoneng.falldetector.tools;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;



/*

ATENCAO NAO ESTA EM USO, esta aqui apenas para conservar o conhecimento


 */
/**
 * Created by sergio.eng on 8/28/17.
 */

public class AlarmReceiver extends BroadcastReceiver {

    private static String TAG = "FD.TOOLS.AlarmRec";

    private static final int PERIOD_UPDATE_DATA    = 1; // unit: minutes

    public  static final int REQUEST_CODE = 12345;
    public  static final String ACTION = "com.codepath.example.servicesdemo.alarm";

    // Triggered by the Phone periodically (starts the service to run task)
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, MyUpdateService.class);
        i.putExtra("foo", "bar");
        context.startService(i);


    }

    public void schedule (Context context) {

        Log.d(TAG,"schedule()");

        // Construct an intent that will execute the AlarmReceiver
        Intent intent = new Intent(context, AlarmReceiver.class);

        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast (context,
                                                                  AlarmReceiver.REQUEST_CODE,
                                                                  intent,
                                                                   PendingIntent.FLAG_UPDATE_CURRENT);

        // Setup periodic alarm every every xxx minutes from this point onwards
        long firstMillis = System.currentTimeMillis(); // alarm is set right away
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, firstMillis,1000 * 60 * PERIOD_UPDATE_DATA, pIntent);
    }

    public void cancel (Context context) {

        Log.d(TAG,"cancel()");

        Intent intent = new Intent(context, AlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(context,
                                                                 AlarmReceiver.REQUEST_CODE,
                                                                 intent,
                                                                 PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pIntent);
    }

}

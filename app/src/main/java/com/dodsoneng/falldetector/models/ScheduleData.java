package com.dodsoneng.falldetector.models;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sergio.eng on 10/19/17.
 */

public class ScheduleData {

    private static String           TAG = "FD.models.ScheduleData";


    String  mTime;
    Date    mDate;


    public Date getDate () {
        return mDate;
    }

    public void setTime (String _time) {

        mTime = _time;

        //Date date = new Date();
        DateFormat df = new SimpleDateFormat("HH:mm");

        try {
            Calendar today = Calendar.getInstance();
            today.setTime(new Date());

            Calendar cal = Calendar.getInstance();
            Date date = df.parse (mTime);
            cal.setTime(date);
            cal.set (Calendar.DAY_OF_MONTH, today.get (Calendar.DAY_OF_MONTH));
            cal.set (Calendar.MONTH, today.get (Calendar.MONTH));
            cal.set (Calendar.YEAR, today.get (Calendar.YEAR));

            if (cal.getTimeInMillis() < today.getTimeInMillis() )
                cal.setTimeInMillis (cal.getTimeInMillis() + 24*3600*1000);

            mDate = cal.getTime();
//            Log.d (TAG, "setTime(): schedule=" + mTime + " today=" + today.getTime() +  " date=" + mDate);
        }
        catch (Exception e) {
            Log.d (TAG, "setTime(): Time parsing failed. " + e.getMessage());
        }

    }

}

package com.dodsoneng.falldetector.timers;

import android.content.Context;
import android.util.Log;

import com.dodsoneng.falldetector.models.ScheduleData;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;

/**
 * Created by sergio.eng on 10/20/17.
 */

public class Scheduler {

    private static String           TAG = "FD.timers.Scheduler....";

    private static final int PERIOD_CONFIGURATION    = 1 * (1000 * 60); // 1 minuto em milliseconds

    private Context                 mContext;
    private ArrayList<Timer>        mTimersList;
    private ArrayList<ScheduleData> mScheduleList;

    public Scheduler (Context context) {
        mContext = context;
        mTimersList = new ArrayList<Timer>();
    }
    public void start () {

        Log.d (TAG, "start()");

        Timer timer = new Timer();

        timer.schedule(Configuration.initiate(mContext), 0, PERIOD_CONFIGURATION);
        mTimersList.add (timer);

        timer = new Timer();
        timer.schedule(Event.initiate(mContext), 0, PERIOD_CONFIGURATION);
        mTimersList.add (timer);

        setSchedule();

        if (mScheduleList == null) {
            Log.d (TAG, "start(): no schedule list configured");
            return;
        }

        Log.d (TAG, "start(): scheduler list has=" +mScheduleList.size() + " entries");

        for (int i = 0; i < mScheduleList.size(); i++) {
            ScheduleData schedule = mScheduleList.get(i);
            Log.d (TAG, "start(): schedule=" + schedule.getDate() );

            timer = new Timer();
            timer.schedule(new Checkin(), schedule.getDate(), 24*3600*1000 );
            mTimersList.add (timer);

        }


    }

    public void stop () {
        Log.d (TAG, "stop()");

        for (int i=0; i < mTimersList.size(); i++) {
            Timer timer = mTimersList.get(i);
            timer.purge();
            timer.cancel();
        }
    }

    private void setSchedule () {
        mScheduleList = Configuration.getScheduleList();
    }
}

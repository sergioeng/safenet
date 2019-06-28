package com.dodsoneng.falldetector.timers;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.dodsoneng.falldetector.Battery;
import com.dodsoneng.falldetector.Positioning;
import com.dodsoneng.falldetector.Telephony;
import com.dodsoneng.falldetector.models.EventInfo;
import com.dodsoneng.falldetector.tools.PostJSON;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimerTask;

import static com.dodsoneng.falldetector.models.EventInfo.*;

/**
 * Created by sergio.eng on 4/6/17.
 */

public class Event extends TimerTask {

    private static String TAG = "FD.timers.Event.......";

    private static Event singleton = null;

    private static Context      mContext;
    private static Positioning mPositioning;
    private static String       mDeviceId;
    private static long []      mEventTime ;
    private static String       mURL; // = "http://10.10.11.231/safenet/event.php";


    /*
     * Methods
     */

    private Event (Context context) {
        Log.d (TAG, "constructor()");

        mContext = context;
        mEventTime = new long [MAX_EVENTS];
    }

    public static Event initiate(Context context) {
        Log.d (TAG, "initiate():");

        if (null == singleton) {
            singleton = new Event (context);
        }

        mPositioning = Positioning.initiate(context);
        mDeviceId = Telephony.getDeviceId(context);

        Log.d (TAG, "initiate(): mPositioning: " + ((mPositioning == null) ? mPositioning.getLastUpdateTime():"null"));
        Log.d (TAG, "initiate(): mDeviceId=" + mDeviceId);

        return singleton;
    }

    private int getEventIndex (String _eventType) {
        Integer type = new Integer(_eventType.toString());

        if (type.intValue() > MAX_EVENTS) {
            Log.d (TAG, "Invalid event="+type );
            return -1;
        }

        return (type.intValue() - 1);
    }

    public void setTime (String _type) {
        int     eveId = getEventIndex (_type);
        long time = System.currentTimeMillis();

//        Log.d (TAG, "_type=" + _type + "event="+ eveId + " time="+time + " mEventTime" + mEventTime);

        mEventTime [eveId] = time;
    }


    public void sendDataToServer () {
/*
        if (! isDeviceOnline()) {

            Log.d (TAG, "No network connection available");
            return;
        }
*/
        Log.d (TAG, "sendDataToServer ()");

        setTime(EventInfo.getType (BATTERY));
        try {
            PostJSON post = new PostJSON(mURL);
            List<JSONObject> jsonList = new ArrayList<>();

            for (int i = 0; i < MAX_EVENTS; i++) {
                JSONObject obj = getJSONObject(i);
                if (! obj.get("timestamp").equals("0")) {
                    Log.d (TAG, "evento: " + obj.get("name"));
                    jsonList.add (obj);
                }
            }

            if (jsonList.size() > 0) {
                JSONObject[] array = jsonList.toArray(new JSONObject[jsonList.size()]);
                post.execute(array);
            }
            else
                Log.d (TAG, "nothing to send " );


        }

        catch (Exception e) {
            Log.d(TAG, "sendDataToServer(): failed to post JSON: " +e.getCause());
            Log.d(TAG, e.toString());
        }

    }

    public void setURL (String url) {
        mURL = url;
    }


    private JSONObject getJSONObject (int _type) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put ("userid", mDeviceId);
            jsonObj.put ("latitude", getLatitude());
            jsonObj.put ("longitude", getLongitude());
            jsonObj.put ("battery", getBattery());
            jsonObj.put ("timestamp", String.valueOf (mEventTime [getEventIndex (EventInfo.getType (_type))]));
            jsonObj.put ("name", EventInfo.getName (_type));
            jsonObj.put ("type", EventInfo.getType (_type));
        } catch (JSONException e) {
            Log.d(TAG, "getJSONObject(): failed to update object: [" +e.getCause() + "]");

            return null;
        }

        return jsonObj;
    }


    private static String getBattery () {
        int         battery = Battery.level(mContext);
//        status = String.format(Locale.US, "%d", battery);
        return Integer.toString(battery);
    }

    private double getLatitude () {
      if (mPositioning == null) {
            return 0.0;
        }

        Location location = mPositioning.getLocation();

        if (location == null) {
            return 0.0;
        }

        return (location.getLatitude());
    }
    private double getLongitude () {

        if (mPositioning == null) {
            return 0.0;
        }

        Location location = mPositioning.getLocation();

        if (location == null) {
            return 0.0;
        }

        return (location.getLongitude());
    }

    public String getStatusText () {

        int         battery = Battery.level(mContext);
        String      status ;

        status = String.format(Locale.US, "%d", battery);

        if (mPositioning == null) {
            status = "0.0; 0.0; " + status;
            return status;
        }

        Location location = mPositioning.getLocation();

        if (location == null) {
            status = "0.0; 0.0; " + status;
            return status;
        }

        status = location.getLatitude() + "; " + location.getLongitude() + "; " + status;
        return status;
    }

    public void run () {
        sendDataToServer ();
    }
}

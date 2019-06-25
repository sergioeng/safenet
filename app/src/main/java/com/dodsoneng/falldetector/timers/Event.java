package com.dodsoneng.falldetector.timers;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.dodsoneng.falldetector.Battery;
import com.dodsoneng.falldetector.Positioning;
import com.dodsoneng.falldetector.Telephony;
import com.dodsoneng.falldetector.models.EventInfo;
import com.dodsoneng.falldetector.tools.PostJSON;

import org.json.JSONObject;

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
    private static long         mEventTime [];
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

    private static int getEventIndex (String _eventType) {
        Integer type = new Integer(_eventType.toString());

        if (type.intValue() > MAX_EVENTS) {
            Log.d (TAG, "Invalid event="+type );
            return -1;
        }

        return (type.intValue() - 1);
    }

    public static void setTime (String _type) {

        int     eveId = getEventIndex (_type);

        long time = (long) (System.currentTimeMillis());

//        Log.d (TAG, "_type=" + _type + "event="+ eveId + " time="+time + " mEventTime" + mEventTime);

        mEventTime [eveId] = time;

    }


    public static void sendDataToServer () {
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

            JSONObject jsonObj = new JSONObject();

            jsonObj.put ("userid", mDeviceId);
            jsonObj.put ("latitude", getLatitude());
            jsonObj.put ("longitude", getLongitude());
            jsonObj.put ("battery", getBattery());
            jsonObj.put ("timestamp", String.valueOf (mEventTime [getEventIndex (EventInfo.getType (CHECKIN))]));
            jsonObj.put ("name", EventInfo.getName (CHECKIN));
            jsonObj.put ("type", EventInfo.getType (CHECKIN));

            Log.d (TAG, "---------------jsonObj: " + jsonObj);

            post.execute(jsonObj);
            /*
            {
	"userid": "3128309218",
	"type": "CheckIn",
	"timestamp": "2019-06-15T00:10:53.772Z",
	"latitude": 23.55,
	"longitude": 46.63
            }
             */
            /*   a partir daqui
            post = new PostJSON(mURL);
            post.execute("userid", mDeviceId,
                    "timestamp", String.valueOf (mEventTime [getEventIndex (EventInfo.getType (EMERGENCY))]),
                    "name", EventInfo.getName (EMERGENCY),
                    "type", EventInfo.getType (EMERGENCY),
                    "latitude", getLatitude(),
                    "longitude", getLongitude(),
                    "battery", getBattery()
            );
            post = new PostJSON(mURL);
            post.execute("userid", mDeviceId,
                    "timestamp", String.valueOf (mEventTime [getEventIndex (EventInfo.getType (SOS))]),
                    "name", EventInfo.getName (SOS),
                    "type", EventInfo.getType (SOS),
                    "latitude", getLatitude(),
                    "longitude", getLongitude(),
                    "battery", getBattery()
            );
            post = new PostJSON(mURL);
            post.execute("userid", mDeviceId,
                    "timestamp", String.valueOf (mEventTime [getEventIndex (EventInfo.getType (BATTERY))]),
                    "name", EventInfo.getName (BATTERY),
                    "type", EventInfo.getType (BATTERY),
                    "latitude", getLatitude(),
                    "longitude", getLongitude(),
                    "battery", getBattery()
            );
            post = new PostJSON(mURL);
            post.execute("userid", mDeviceId,
                    "timestamp", String.valueOf (mEventTime [getEventIndex (EventInfo.getType (FALL))]),
                    "name", EventInfo.getName (FALL),
                    "type", EventInfo.getType (FALL),
                    "latitude", getLatitude(),
                    "longitude", getLongitude(),
                    "battery", getBattery()
            );
            ****/
        }

        catch (Exception e) {
            Log.d(TAG, "sendDataToServer(): failed to post JSON: " +e.getCause());
            Log.d(TAG, e.toString());
        }

    }

    public static void setURL (String url) {
        mURL = url;
    }

    private static String getBattery () {
        int         battery = Battery.level(mContext);
//        status = String.format(Locale.US, "%d", battery);
        return Integer.toString(battery);
    }

    private static double getLatitude () {
      if (mPositioning == null) {
            return 0.0;
        }

        Location location = mPositioning.getLocation();

        if (location == null) {
            return 0.0;
        }

        return (location.getLatitude());
    }
    private static double getLongitude () {

        if (mPositioning == null) {
            return 0.0;
        }

        Location location = mPositioning.getLocation();

        if (location == null) {
            return 0.0;
        }

        return (location.getLongitude());
    }

    public static String getStatusText () {

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

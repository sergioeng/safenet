package com.dodsoneng.falldetector.timers;

import android.content.Context;
import android.util.Log;

import com.dodsoneng.falldetector.Contact;
import com.dodsoneng.falldetector.Telephony;
import com.dodsoneng.falldetector.models.EventInfo;
import com.dodsoneng.falldetector.models.ScheduleData;
import com.dodsoneng.falldetector.models.SymptomData;
import com.dodsoneng.falldetector.tools.GetJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.TimerTask;

/**
 * Created by sergio.eng on 2/8/17.
 */

public class Configuration extends TimerTask{

    private static String           TAG = "FD.timers.Configuration";
    private static Context          mContext = null;
    private static Configuration    singleton = null;
    private static String           mURL;
    private static int              mCheckinType = EventInfo.CHECKIN_BUTTON;
    private static ArrayList        mSymptomList;
    private static ArrayList<ScheduleData>        mScheduleList;

     public static final class CheckinType {
        public static final String BUTTON_ONLY              = "BTONLY";
        public static final String BUTTON_AND_TEXT          = "BTTEXT";
        public static final String BUTTON_AND_TEXT_AND_LIST = "BTYMP";
    }

    private Configuration(Context context) {
        mContext = context;
    }

    public static Configuration initiate(Context context) {

        Log.d(TAG, "initiate():");

        if (null == singleton) {
            singleton = new Configuration(context);
        }

        return singleton;
    }



    public void setURL(String url) {
        String deviceId = Telephony.getDeviceId(mContext);
        mURL = url + "/users/" + deviceId + "/configuration";
    }

    public String getURL() {
        return mURL;
    }

    public void getDataFromServer() {

        Log.d (TAG, "URL=" + mURL);
        int    ret;

        /// Getting CONFIGURATION
        GetJSON json = new GetJSON();
        json.execute(mURL);

        try {
            Log.d(TAG, "waiting ....");
            ret = json.get();
            Log.d(TAG, "finished. status=" + json.getStatus() + " ret="+ret);

            if (ret == -1)
                return;

            JSONObject jsoConfig = json.getConfig();

            Contact.set (mContext, jsoConfig.getString("guardiannum"));
            String checkinType = jsoConfig.getString("checkintype");
            if (checkinType.equals(CheckinType.BUTTON_ONLY))
                mCheckinType = EventInfo.CHECKIN_BUTTON;
            else if (checkinType.equals(CheckinType.BUTTON_AND_TEXT))
                mCheckinType = EventInfo.CHECKIN_BUTTON_TEXT;
            else if (checkinType.equals(CheckinType.BUTTON_AND_TEXT_AND_LIST))
                mCheckinType = EventInfo.CHECKIN_BUTTON_TEXT_SYMPTOM;

            mScheduleList = new ArrayList();
            mSymptomList = new ArrayList();
            JSONArray jsonArray = jsoConfig.getJSONArray("schedules");
            for (int j = 0; j < jsonArray.length(); j++) {
                ScheduleData scheduleData = new ScheduleData();

                String time = jsonArray.getString(j);

                Log.d(TAG, "setData(): CHECKIN schedule=" + time);

                scheduleData.setTime(time);

                mScheduleList.add(scheduleData);
            }
            jsonArray = jsoConfig.getJSONArray("symptoms");
            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject jsonObj = jsonArray.getJSONObject(j);
                SymptomData symptomData = new SymptomData();
                String symptom = jsonObj.getString("name");
                Boolean hasEntry = jsonObj.getBoolean("has_entry");
                String unit = jsonObj.getString("unit");
                Log.d(TAG, "setData(): CHECKIN symptom=" + symptom + " has_entry=" + hasEntry + " unit=" + unit);

                symptomData.setName(symptom);
                symptomData.setHasEntry(hasEntry);
                symptomData.setUnit(unit);

                mSymptomList.add(symptomData);
            }
        }
        catch (Exception e) {
            Log.e (TAG, "Exeception: [" + e.getMessage());
        }
    }

    public int getCheckinType () {
        return mCheckinType;
    }

    public ArrayList getSymptomList() {
        return mSymptomList;
    }
    public ArrayList getScheduleList() {
        return mScheduleList;
    }

    public void run () {
        getDataFromServer();
    }
}
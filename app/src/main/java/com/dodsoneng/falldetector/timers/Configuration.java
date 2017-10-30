package com.dodsoneng.falldetector.timers;

import android.content.Context;
import android.util.Log;

import com.dodsoneng.falldetector.Contact;
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
    private JSONArray               jsonArray = null;
    private static Context          mContext = null;
    private static Configuration    singleton = null;
    private static String           mURL; // = "http://10.10.11.231/safenet/event.php";
    private static int              mCheckinType = EventInfo.CHECKIN_BUTTON;
    private static ArrayList        mSymptomList;
    private static ArrayList<ScheduleData>        mScheduleList;


    public static final class CheckinType {
        public static final String BUTTON_ONLY              = "A";
        public static final String BUTTON_AND_TEXT          = "B";
        public static final String BUTTON_AND_TEXT_AND_LIST = "C";
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



    public static void setURL(String url) {
        mURL = url;
    }


    public static void getDataFromServer() {

        Log.d (TAG, "URL=" + mURL);
        int    ret;

        /// Getting CONFIGURATION
        GetJSON json = new GetJSON();
//                json.execute("http://10.10.11.231/safenet/config.php");
        json.execute(mURL);

        try {
            Log.d(TAG, "waiting ....");
            ret = json.get();
            Log.d(TAG, "finished. status=" + json.getStatus() + " ret="+ret);
        } catch (Exception e) {
            Log.d(TAG, "failed to get settings: " + e.getCause());
            Log.d(TAG, e.toString());
            return;
        }

        if (ret == -1)
            return;

        JSONArray jsa = json.getJsonArray();

        try {
            for (int i = 0; i < jsa.length(); i++) {
                JSONObject jso = jsa.getJSONObject(i);
                String name = jso.getString("name");
                String value = jso.getString("value");

                if (name != null && name.equals("GUARDIANNUM")) {
                    Log.d(TAG, "setData(): name=" + name + " value=" + value );
                    Contact.set(mContext, value);
                }
                if (name != null && name.equals("CHECKINTYPE")) {
;
                    if (value.equals(CheckinType.BUTTON_ONLY) == true) {
                        mCheckinType = EventInfo.CHECKIN_BUTTON;
                        continue;
                    }
                    else if (value.equals(CheckinType.BUTTON_AND_TEXT) == true)
                        mCheckinType = EventInfo.CHECKIN_BUTTON_TEXT;
                    else
                        mCheckinType = EventInfo.CHECKIN_BUTTON_TEXT_SYMPTOM;

                        /// Obtem do JSON a lista de sintomas a serem apresentados no dialogo extra do CHECK IN
                    try {
                        mSymptomList = new ArrayList();

                        //// Envolver em um try catch
                        JSONArray jsonArray = jso.getJSONArray("symptom");
                        Log.d(TAG, "setData(): name=" + name + " value=" + value + " entries in symptom table=" + jsonArray.length());

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
                    catch (JSONException e) {
                        Log.d(TAG, "setData(): apesar da configuracao do CHECKIN definir extensao, nao exsitem sintomas definidos");
                    }

                    /// Obtem do JSON a lista dos schedules a serem para notificar a necessidade de CHECK IN
                    try {
                        mScheduleList = new ArrayList();

                        //// Envolver em um try catch
                        JSONArray jsonArray = jso.getJSONArray("schedule");
                        Log.d(TAG, "setData(): name=" + name + " value=" + value + " entries in schedule table=" + jsonArray.length());

                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject jsonObj = jsonArray.getJSONObject(j);
                            ScheduleData scheduleData = new ScheduleData();

                            String time = jsonObj.getString("time");

                            Log.d(TAG, "setData(): CHECKIN schedule=" + time );

                            scheduleData.setTime(time);

                            mScheduleList.add(scheduleData);

                        }
                    }
                    catch (JSONException e) {
                        Log.d(TAG, "setData(): apesar da configuracao do CHECKIN definir extensao, nao exsitem schedules definidos");
                    }
                }
            }
        }
        catch (JSONException e) {
            Log.d(TAG, "setData(): === Exception =========================================");
            Log.d(TAG, "setData(): " + e.getLocalizedMessage());
            Log.d(TAG, "setData(): =======================================================");
        }
    }

    public int getCheckinType () {
        return mCheckinType;
    }

    public static ArrayList getSymptomList() {
        return mSymptomList;
    }
    public static ArrayList getScheduleList() {
        return mScheduleList;
    }

    public void run () {
        getDataFromServer();
    }
}
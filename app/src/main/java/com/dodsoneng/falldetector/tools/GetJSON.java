package com.dodsoneng.falldetector.tools;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sergio.eng on 2/8/17.
 */

public class GetJSON extends AsyncTask<String, Void, Integer> {

    private static String TAG = "FD.TOOLS.GetJSON.......";

    private JSONObject mJSONObj = null;

    @Override
    protected void onPreExecute() {
//        Log.d (TAG, "onPreExecute()");
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(String... params) {
        Log.d (TAG, "doInBackground():  in");
        String uri = params[0];

        BufferedReader bufferedReader = null;
        try {
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            StringBuilder sb = new StringBuilder();

            bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String json;
            while((json = bufferedReader.readLine())!= null){
                sb.append(json+"\n");
            }

            mJSONObj = new JSONObject(sb.toString().trim());

            return 0;

        }
        catch(Exception e) {
            Log.d (TAG, "doInBackground(): Exception: [" + e.getLocalizedMessage() + "]");
            return -1;
        }

    }

    @Override
    protected void onPostExecute(Integer ret) {
        super.onPostExecute(ret);
        Log.d (TAG, "onPostExecute(): "+ret);
    }

    public JSONObject getConfig () {
//        Log.d(TAG, "getJsonArray(). status=" + getStatus() );
      return mJSONObj;
    }
}

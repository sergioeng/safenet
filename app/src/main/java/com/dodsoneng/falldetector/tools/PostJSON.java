package com.dodsoneng.falldetector.tools;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.dodsoneng.falldetector.models.EventInfo.MAX_EVENTS;

/**
 * Created by sergio.eng on 4/6/17.
 */
    /*================================================================================
     * Calling JSON to send data to database in the main server
     *
     *
     */
/**
 * An asynchronous task that handles the Google Sheets API call.
 * Placing the API calls in their own task ensures the UI stays responsive.
 */

public class PostJSON extends AsyncTask<JSONObject, Void, String> {
    private static String TAG = "FD.TOOLS.PostJSON......";
    private static String mURL;

    private Exception mLastError = null;


    public PostJSON (String _url) {
        mURL = _url;
    }

    /**
     * Background task to call Google Sheets API.
     */
    protected  String doInBackground(JSONObject ... _jsonObjects) {
        try {

            for (int i = 0; i < _jsonObjects.length; i++)
                post (_jsonObjects[i]);
        }
        catch (Exception e) {
            mLastError = e;
            cancel(true);
            return null;
        }
        return "SUCCESS";
    }

    /**
     * Fetch a list of names and majors of students in a sample spreadsheet:
     * https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
     * @return List of names and majors
     * @throws IOException
     */
    protected  String post (JSONObject _jsonObj) throws IOException {
        StringBuffer response = null;
        try {
            URL url = new URL(mURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(_jsonObj.toString());
            Log.d (TAG,"POST request to URL : " + url);
            Log.d (TAG,"JASON PARAM         : " + _jsonObj.toString());


            writer.close();
            out.close();

            int responseCode = conn.getResponseCode();
            Log.d (TAG,"RESPONSE CODE       : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            response = null;
            response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
//                in.reset();
            Log.d (TAG,"Response in universal: " + response.toString());

        }
        catch (java.net.MalformedURLException ex) {
            Log.d (TAG,"java.net.MalformedURLException: [" + mURL + "]");
            return "FAILED";
        }
        catch (Exception exception) {
            Log.d (TAG,"Exception: " + exception);
            return "FAILED";
        }

        return "SUCCESS";
    }
    @Override
    protected void onPreExecute() {
//        Log.d (TAG, "onPreExecute: " );
    }

    @Override
    protected void onPostExecute(String output) {
        Log.d (TAG, "onPostExecute: " + output);
    }

    @Override
    protected void onCancelled() {
        //Log.d (TAG, "onCancelled() ");

        if (mLastError != null) {
                /*
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            MainActivity.REQUEST_AUTHORIZATION);
                } else {
                    Log.d (TAG, "The following error occurred: " + mLastError.getMessage());
                }
                */
        } else {
            Log.d (TAG, "Request cancelled.");
        }
    }

}


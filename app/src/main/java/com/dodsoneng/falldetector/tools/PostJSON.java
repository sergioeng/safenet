package com.dodsoneng.falldetector.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.dodsoneng.falldetector.Telephony;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

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

public class PostJSON extends AsyncTask<String, Void, String> {
    private static String TAG = "FD.TOOLS.PostJSON......";
    private static String mURL;

    private Exception mLastError = null;


    public PostJSON (String _url) {
        mURL = _url;
    }

    /**
     * Background task to call Google Sheets API.
     */
    @Override
    protected  String doInBackground(String... value) {

//        Log.d (TAG, "doInBackground: arguments lenght=" + value.length);
//        for (int i = 0; i < value.length ; i += 2) Log.d (TAG, "doInBackground: arguments pair=("+value [i]+","+value[i+1]+")");
        try {
            return post (value );
        }
        catch (Exception e) {
            mLastError = e;
            cancel(true);
            return null;
        }
    }

    /**
     * Fetch a list of names and majors of students in a sample spreadsheet:
     * https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
     * @return List of names and majors
     * @throws IOException
     */
    protected  String post (String... value) throws IOException {
        StringBuffer response = null;
        try {

            JSONObject parameters = new JSONObject();

            for (int i = 0; i < value.length; i += 2) {
                parameters.put(value [i], value [i+1]);
            }


            URL url = new URL(mURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(parameters.toString());
            Log.d (TAG,"POST request to URL : " + url);
            Log.d (TAG,"JASON PARAM         : " + parameters.toString());


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

        } catch (Exception exception) {
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

        if (output == null)  {
            Log.d (TAG, "onPostExecute: No results returned");
        } else {
            Log.d (TAG, "onPostExecute: Data updated using the Google Sheets API");
        }
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


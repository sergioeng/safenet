/*
The MIT License (MIT)

Copyright (c) 2016

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.dodsoneng.falldetector;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.R.attr.action;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.LocationListener;


public class Positioning implements
        GoogleApiClient.ConnectionCallbacks, OnConnectionFailedListener, LocationListener,  ResultCallback<LocationSettingsResult> {
    private static String TAG = "FD.Positioning.........";

    private static Positioning singleton = null;
    private Object lock = new Object();
    private final Context mContext;
    private Location gps;
    private Location network;
    private long once = 0;
    private boolean replied = true;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    protected GoogleApiClient           mGoogleApiClient;
    protected Location                  mCurrentLocation;
    protected Location                  mLastLocation;
    protected LocationRequest           mLocationRequest;
    protected LocationSettingsRequest   mLocationSettingsRequest;
    protected String                    mLastUpdateTime;


    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;


    private Positioning(Context context) {
        Log.d (TAG, "Positioning(): constructor");
        mContext = context;

/* OLD CODE
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        try {
            gps = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            network = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        catch (SecurityException e) {
            e.printStackTrace();
            Log.d (TAG, "constructor: failed to getLastKnownLocation");
        }

        reset();
*/
        //
        // REFERENCE:
        // https://github.com/googlesamples/android-play-location/blob/master/LocationSettings/app/src/main/java/com/google/android/gms/location/sample/locationsettings/MainActivity.java
        //
        // Create an instance of GoogleAPIClient.

        buildGoogleApiClient();
        createLocationRequest();
        buildLocationSettingsRequest();

        mGoogleApiClient.connect();

    }

    public static Positioning initiate(Context context) {

        Log.d (TAG, "initiate():");

        if (null == singleton) {
            singleton = new Positioning(context);
        }

        return singleton;
    }

    public void terminate(Context ctx) {

        Log.d (TAG, "terminate():");

        /* OLD CODE
        LocationManager manager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        manager.removeUpdates(this);
        */

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        singleton = null;
    }

    public static void trigger() {

        Log.d (TAG, "trigger()" );

        if (null != singleton) {
            singleton.run();
        }
    }

    public static void stop () {
        //singleton.
        if (null != singleton) {
            singleton.stopLocationUpdates();
        }
    }

    public String getLastUpdateTime () {
        return mLastUpdateTime;
    }
    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.d (TAG, "onConnected(): ");
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                Log.d(TAG, "onConnected(): mLastLocation=("+mLastLocation.getLatitude()+","+mLastLocation.getLongitude()+")");
            }
            else {
                Log.d(TAG, "onConnected(): mLastLocation=null");
            }

        }
        catch (SecurityException e) {
            Log.d (TAG, "onConnected(): failed");
            Log.d (TAG, "onConnected(): " + e.getMessage());
        }
    }

    /**
     * Callback that fires when the location changes.
     */
    @Override
    public void onLocationChanged(Location location) {

        Log.d (TAG, "onLocationChanged():");

        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        int battery = Battery.level(mContext);
        String message;

        if (null != location && location.hasAccuracy()) {
            double lat = location.getLatitude();
            double lon = location.getLongitude();
            int accuracy = (int) location.getAccuracy();
            int altitude = (int) location.getAltitude();
            int bearing = (int) location.getBearing();
            int speed = (int) (location.getSpeed() * 60.0 * 60.0 / 1000.0);
            String time = format.format(new Date(location.getTime()));
            message = "Battery: %d%% Location: %s %.5f %.5f ~%dm ^%dm %ddeg %dkm/h http://maps.google.com/?q=%.5f,%.5f";
            message = String.format(Locale.US, message, battery, time, lat, lon, accuracy, altitude, bearing, speed, lat, lon);
        }
        else {
            message = "Battery: %d%%; Location unknown";
            message = String.format(Locale.US, message, battery);
        }
        Log.d (TAG, "onLocationChanged(): sending SMS: ["+message+"]");
        Messenger.sms(Contact.get(mContext), message);
    }

    @Override
    public void onConnectionSuspended (int cause ) {
        Log.i(TAG, "onConnectionSuspended(): cause="+cause);
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed (ConnectionResult result ) {
        Log.i(TAG, "onConnectionFailed(): ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    /**
     * The callback invoked when
     * {@link com.google.android.gms.location.SettingsApi#checkLocationSettings(GoogleApiClient,
     * LocationSettingsRequest)} is called. Examines the
     * {@link com.google.android.gms.location.LocationSettingsResult} object and determines if
     * location settings are adequate. If they are not, begins the process of presenting a location
     * settings dialog to the user.
     */
    public void onResult(LocationSettingsResult locationSettingsResult) {
        Log.i(TAG, "onResult(): ");

        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                Log.i(TAG, "onResult(): All location settings are satisfied.");
                startLocationUpdates();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                Log.i(TAG, "onResult(): Location settings are not satisfied. Show the user a dialog to" +
                        "upgrade location settings ");

                /*
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    // status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    Log.i(TAG, "PendingIntent unable to execute request.");
                }
                */
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.i(TAG, "onResult(): Location settings are inadequate, and cannot be fixed here. Dialog " +
                        "not created.");
                break;
        }
    }

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "buildGoogleApiClient(): ");
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * Uses a {@link com.google.android.gms.location.LocationSettingsRequest.Builder} to build
     * a {@link com.google.android.gms.location.LocationSettingsRequest} that is used for checking
     * if a device has the needed location settings.
     */
    protected void buildLocationSettingsRequest() {
        Log.i(TAG, "buildLocationSettingsRequest():");

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    protected void createLocationRequest() {
        Log.i(TAG, "createLocationRequest():");

        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
    * Check if the device's location settings are adequate for the app's needs using the
    * {@link com.google.android.gms.location.SettingsApi#checkLocationSettings(GoogleApiClient,
    * LocationSettingsRequest)} method, with the results provided through a {@code PendingResult}.
    */
    protected void checkLocationSettings() {
        Log.i(TAG, "checkLocationSettings():");

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        mLocationSettingsRequest
                );
        result.setResultCallback(this);
    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void startLocationUpdates() {
        Log.i(TAG, "startLocationUpdates(): ");

        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient,
                    mLocationRequest,
                    this
            ).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(Status status) {
                    Log.i(TAG, "ResultCallback.onResult(): status="+status );

                }
            });
        }
        catch (SecurityException e) {
            Log.i(TAG, "startLocationUpdates(): failed");
            Log.i(TAG, "startLocationUpdates(): " + e.getMessage());
        }

    }
    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void stopLocationUpdates() {
        Log.i(TAG, "stopLocationUpdates(): ");

        try {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
        catch (SecurityException e) {
            Log.i(TAG, "stopLocationUpdates(): failed");
            Log.i(TAG, "stopLocationUpdates(): " + e.getMessage());
        }

    }


    private void run() {

        Log.d (TAG, "run(): requesting location updates");
/** OLD CODE
        enforce(context);

        synchronized (lock) {
            LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            try {
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                once = System.currentTimeMillis();
                replied = false;
            }
            catch (SecurityException e) {
                e.printStackTrace();
                Log.d (TAG, "constructor: failed to requestLocationUpdates");
            }

        }
 **/
        startLocationUpdates();
    }

    public Location getLocation () {
        Location location;

        location = mCurrentLocation;
        if (mCurrentLocation == null)
            location = mLastLocation;

        return location;
    }

    /** OLD CODE

    private void reset() {

        Log.d (TAG, "reset(): removing location updates");

        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        manager.removeUpdates(this);
        int meters10 = 10;
        int minutes10 = 10 * 60 * 1000;
        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minutes10, meters10, this);
    }

    private static void enforce(Context context) {

        Log.d (TAG, ".enforce(): in");

        enforceWiFi(context);
        enforceGPS(context);
    }

    private static void enforceWiFi(Context context) {

        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        Log.d (TAG, "enforceWiFi(): BEF wifi state=" + wifi.getWifiState());
        wifi.setWifiEnabled(true);
        Log.d (TAG, "enforceWiFi(): AFT wifi state=" + wifi.getWifiState());
    }

    @SuppressWarnings("deprecation")
    private static void enforceGPS(Context context) {

        Log.d (TAG, "enforceGPS():");

        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Log.d (TAG, "enforceGPS(): provider is enabled="+ manager.isProviderEnabled(LocationManager.GPS_PROVIDER));

        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return;
        }
        boolean stealth = false;
        try {
            PackageManager packages = context.getPackageManager();
            PackageInfo info = packages.getPackageInfo("com.android.settings", PackageManager.GET_RECEIVERS);
            if (info != null) {
                for (ActivityInfo receiver : info.receivers) {
                    if (receiver.name.equals("com.android.settings.widget.SettingsAppWidgetProvider") && receiver.exported) {
                        stealth = true;
                    }
                }
            }
        } catch (NameNotFoundException ignored) {
        }
        if (stealth) {
            String provider = Secure.getString(context.getContentResolver(), Secure.LOCATION_PROVIDERS_ALLOWED);
            if (!provider.contains("gps")) {
                Intent poke = new Intent();
                poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                context.sendBroadcast(poke);
            }
        } else {
            Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    private double accuracy(Location location) {
//        Log.d (TAG, ".accuracy(): in");

        if (null != location && location.hasAccuracy()) {
            return (location.getAccuracy());
        } else {
            return (Double.POSITIVE_INFINITY);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        Log.d (TAG, "onLocationChanged():");

        enforce(context);
        synchronized (lock) {
            if (LocationManager.GPS_PROVIDER.equals(location.getProvider())) {

                Log.d (TAG, "onLocationChanged(): GPS accuracy: current("+gps.hasAccuracy()+ ") = "+gps.getAccuracy());
                Log.d (TAG, "onLocationChanged(): GPS accuracy: lastknw("+location.hasAccuracy()+ ") = "+location.getAccuracy());

                if (accuracy(location) <= accuracy(gps)) {
                    Log.d (TAG, ".onLocationChanged(): GPS provider ... good accuracy ... using new GPS location");
                    gps = location;
                }
                else {
                    Log.d (TAG, ".onLocationChanged(): GPS provider ... bad  accuracy ... using old GPS location");
                }
            }
            if (LocationManager.NETWORK_PROVIDER.equals(location.getProvider())) {
                Log.d (TAG, "onLocationChanged(): NET accuracy: current("+network.hasAccuracy()+ ") = "+network.getAccuracy());
                Log.d (TAG, "onLocationChanged(): NET accuracy: lastknw("+location.hasAccuracy()+ ") = "+location.getAccuracy());
                if (accuracy(location) <= accuracy(network)) {
                    Log.d (TAG, "onLocationChanged(): NET provider ... good accuracy ... using new NET location");
                    /// BUG FIX by Sergio Eng: it was  =>  gps = location;
                    network = location;
                }
                else {
                    Log.d (TAG, "onLocationChanged(): NET provider ... bad  accuracy ... using old NET location");
                }
            }

            long deadline = once + 120000;
            long now = System.currentTimeMillis();
            if (deadline <= now && !replied) {
                int battery = Battery.level(context);
                String message;
                if (Double.isInfinite(accuracy(gps)) && Double.isInfinite(accuracy(network))) {
                    message = "Battery: %d%%; Location unknown";
                    message = String.format(Locale.US, message, battery);
                } else {

                    if (accuracy(gps) <= accuracy(network)) {
                        Log.d (TAG, "onLocationChanged(): Using GPS location. Accuracy="+accuracy(gps));
                        location = gps;
                    } else {
                        Log.d (TAG, "onLocationChanged(): Using NET location. Accuracy="+accuracy(network));
                        location = network;
                    }
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();
                    int accuracy = (int) location.getAccuracy();
                    int altitude = (int) location.getAltitude();
                    int bearing = (int) location.getBearing();
                    int speed = (int) (location.getSpeed() * 60.0 * 60.0 / 1000.0);
                    String time = format.format(new Date(location.getTime()));
                    message = "Battery: %d%% Location: %s %.5f %.5f ~%dm ^%dm %ddeg %dkm/h http://maps.google.com/?q=%.5f,%.5f";
                    message = String.format(Locale.US, message, battery, time, lat, lon, accuracy, altitude, bearing, speed, lat, lon);
                }
                Messenger.(Contact.get(context), message);
                reset();
                replied = true;
            }
        }
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d (TAG, ".onStatusChanged(): provider="+provider+" status="+status);
        enforce(context);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d (TAG, ".onProviderEnabled(): provider="+provider);
        enforce(context);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d (TAG, ".onProviderDisabled(): provider="+provider);
        enforce(context);
    }
     **/
}

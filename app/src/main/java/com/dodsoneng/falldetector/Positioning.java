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
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;



public class Positioning {
    // talvez algo como implements Runnable {
    //implements
    // GoogleApiClient.ConnectionCallbacks,
    // OnConnectionFailedListener,
    // LocationListener,
    // ResultCallback<LocationSettingsResult> {

    private static String       TAG = "FD.Positioning.........";
    private static Positioning  mSingleton = null;
    private final Context       mContext;
    private SimpleDateFormat    format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    private Location            mCurrentLocation;
    private LocationRequest     mLocationRequest;
    private String              mLastUpdateTime;
    private Location            mLastLocation = null;

    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Callback for Location events.
     */
    private LocationCallback mLocationCallback;

    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    private Positioning(Context context) {
        Log.d(TAG, "Positioning(): constructor");
        mContext = context;
/*
==========================================
SERGIO ENG 26JUN19 REFERENCIA
https://github.com/googlesamples/android-play-location/blob/master/LocationUpdatesForegroundService/app/src/main/java/com/google/android/gms/location/sample/locationupdatesforegroundservice/LocationUpdatesService.java
==========================================
 */
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                onNewLocation(locationResult.getLastLocation());

                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            }
        };

        createLocationRequest();
        getLastLocation();
    }

    public static Positioning initiate(Context context) {
        Log.d(TAG, "initiate():");

        if (null == mSingleton) {
            mSingleton = new Positioning(context);
        }

        return mSingleton;
    }

    void terminate() {
        Log.d(TAG, "terminate():");
        mSingleton = null;
    }

    void trigger() {
        Log.d(TAG, "trigger()");

        if (null != mSingleton) {
            mSingleton.run();
        }
    }

    void stop() {
        if (null != mSingleton) {
            mSingleton.removeLocationUpdates();
        }
    }

    public String getLastUpdateTime() {
        return mLastUpdateTime;
    }


    /**
     * Callback that fires when the location changes.
     */
    private void onNewLocation (Location location) {
        Log.d(TAG, "onLocationChanged():");

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
        } else {
            message = "Battery: %d%%; Location unknown";
            message = String.format(Locale.US, message, battery);
        }
        Log.d(TAG, "onLocationChanged(): sending SMS: [" + message + "]");
        Messenger.sms(Contact.get(mContext), message);
    }

    /**
     * Uses a {@link com.google.android.gms.location.LocationSettingsRequest.Builder} to build
     * a {@link com.google.android.gms.location.LocationSettingsRequest} that is used for checking
     * if a device has the needed location settings.
     */
    private void getLastLocation() {
        Log.i(TAG, "getLastLocation():");

        try {
            mFusedLocationClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                mLastLocation = task.getResult();
                            } else {
                                Log.w(TAG, "Failed to get location.");
                            }
                        }
                    });
        } catch (SecurityException unlikely) {
            Log.e(TAG, "Lost location permission." + unlikely);
        }
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
    private void createLocationRequest() {
        Log.i(TAG, "createLocationRequest():");

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
//        mLocationRequest.setSmallestDisplacement(Constants.SMALLEST_DISTANCE);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    /**
     * Requests location updates from the FusedLocationApi. Note: we don't call this unless location
     * runtime permission has been granted.
     */
    private void requestLocationUpdates() {
        Log.i(TAG, "requestLocationUpdates(): Requesting location updates");
        try {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback, Looper.myLooper());
        } catch (SecurityException unlikely) {
            Log.e(TAG, "Lost location permission. Could not request updates. " + unlikely);
        }
    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    private void removeLocationUpdates() {
        Log.i(TAG, "removeLocationUpdates(): Removing location updates");
        try {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        } catch (SecurityException unlikely) {
            Log.e(TAG, "Lost location permission. Could not remove updates. " + unlikely);
        }
    }

    private void run() {
        Log.d(TAG, "run(): requesting location updates");
        requestLocationUpdates();
    }

    public Location getLocation() {
        if (mCurrentLocation == null)
            return mLastLocation;
        return mCurrentLocation;
    }
}
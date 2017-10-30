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

import android.Manifest;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.dodsoneng.falldetector.fragments.SymptomFragment;
import com.dodsoneng.falldetector.models.EventInfo;
import com.dodsoneng.falldetector.timers.Event;
import com.dodsoneng.falldetector.timers.Scheduler;
import com.dodsoneng.falldetector.timers.Configuration;
import com.dodsoneng.falldetector.tools.Phone;

import java.util.ArrayList;
import java.util.List;

import static com.dodsoneng.falldetector.models.EventInfo.*;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private static String TAG = "FD.MainActivity........";

    private static final String[] INITIAL_PERMS={
            Manifest.permission.CALL_PHONE,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_CONTACTS

    };
/* TOBEDELETED 05.10.2017
    private static final String[] CAMERA_PERMS={
            Manifest.permission.CAMERA
    };
    private static final String[] CONTACTS_PERMS={
            Manifest.permission.READ_CONTACTS
    };
    private static final String[] LOCATION_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION
    };
*/
    private Context             mContext;
    private Dialog              mDialog;
    private Configuration       mConfig;
    private Event               mEvent;
    private static Handler      mHandler;
    private int                 mTargetSdkVersion = 0;
    private boolean             flag = false; /// Checar se eu preciso disso mesmo !!!
    private Scheduler           mScheduler;
    private static ImageButton  mCheckinButton;

    private static final int INITIAL_REQUEST     = 1337;
    private static final int CAMERA_REQUEST      = INITIAL_REQUEST+1;
    private static final int CONTACTS_REQUEST    = CAMERA_REQUEST+1;
    private static final int LOCATION_REQUEST    = CONTACTS_REQUEST+1;
    private static final int CALL_PHONE_REQUEST  = LOCATION_REQUEST+1;
    private static final int RECEIVE_SMS_REQUEST = CALL_PHONE_REQUEST+1;


    private void eula(Context context) {
        Log.d(TAG, "eula()");

        // Load the EULA
        mDialog = new Dialog(context);
        mDialog.setContentView(R.layout.eula);
        mDialog.setTitle("EULA");

        WebView web = (WebView) mDialog.findViewById(R.id.eula);
        web.loadUrl("file:///android_asset/eula.html");

        Button accept = (Button) mDialog.findViewById(R.id.accept);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });

        mDialog.show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");

        super.onCreate(savedInstanceState);

        mContext = this;
        mScheduler = new Scheduler(mContext);

        try {
            final PackageInfo info = mContext.getPackageManager().getPackageInfo(
                    mContext.getPackageName(), 0);
            mTargetSdkVersion = info.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "onCreate(): mTargetSdkVersion: "+mTargetSdkVersion+" Build.VERSION.SDK_INT="+Build.VERSION.SDK_INT );


        if (checkPermissions () == true) {
            String eveURL = getPrefValue(getString (R.string.key_url_event));
            String cfgURL = getPrefValue(getString (R.string.key_url_configuration));

            Log.d (TAG, "onCreate(): event url:" +  eveURL);
            Log.d (TAG, "onCreate(): configuration url: " +  cfgURL);

            mEvent= Event.initiate(mContext);
            mEvent.setURL(eveURL);

            mConfig = Configuration.initiate(mContext);
            mConfig.setURL(cfgURL);
            mConfig.getDataFromServer();

            initiateApp ();
            mScheduler.start ();
        }


    }



    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick()");
        Intent intent;

        switch (view.getId()) {
            case R.id.emergency:
                Log.d (TAG, "Emergency button pressed");
                if (Contact.isConfigured(mContext)) {
                    Phone.call(this);
                    mEvent.setTime(EventInfo.getType(EMERGENCY));
                }else {
                    Toast.makeText(mContext, "Failed, check phone configuration", Toast.LENGTH_LONG).show();
                }

                return;
            case R.id.sos:
                Log.d (TAG, "SOS button pressed: flag="+flag);
                ToggleButton btnSOS = (ToggleButton) findViewById(R.id.sos);
                Log.d (TAG, "SOS button pressed: flag="+flag+" isChecked="+btnSOS.isChecked());

                if (btnSOS.isChecked()) {
                    flag = true;
                    mEvent.setTime(EventInfo.getType(SOS));
                    Guardian.trigger();
                }
                else {
                    flag = false;
                    Guardian.stop();
                }
                return;
            case R.id.checkin:
                Log.d (TAG, "Checkin button pressed");

                showSymptomDialog();

                if (Messenger.sms(Contact.get(this), mEvent.getStatusText())) {
                    mEvent.setTime(EventInfo.getType(CHECKIN));
                } else {
                    Toast.makeText(mContext, "Failed, check phone configuration", Toast.LENGTH_LONG).show();
                }

                stopFlashCheckinButton();

                return;
            case R.id.signals:
                Log.d (TAG, "Signals button pressed");
                intent = new Intent(this, Signals.class);
                startActivity(intent);
                return;
            case R.id.turnoff:
                Log.d (TAG, "Turn app off button pressed");
                Guardian.terminate(this);
                if (mDialog != null)  mDialog.cancel();
                this.finish();
                mScheduler.stop();
                break;
            case R.id.getconfig:
                Log.d (TAG, "GET CONFIGURATION FROM SERVER");

                //mConfig.getDataFromServer();
                startFlashCheckinButton();

                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected(): begin");

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent();
            intent.setClassName(this, "com.dodsoneng.falldetector.SettingsActivity");
            startActivity(intent);
            Log.d(TAG, "onOptionsItemSelected(): end 1");

            return true;
        }

        Log.d(TAG, "onOptionsItemSelected(): end 2");
        return super.onOptionsItemSelected(item);

    }


    public void onResume () {

        super.onResume();

        String eveURL = getPrefValue(getString (R.string.key_url_event));
        String cfgURL = getPrefValue(getString (R.string.key_url_configuration));

        if (eveURL.equals("")) {
            eveURL = getString (R.string.url_event);
        }
        mEvent.setURL(eveURL);


        if (cfgURL.equals("")) {
            cfgURL = getString(R.string.url_configuration);
        }
        mConfig.setURL(cfgURL);

        Log.d (TAG, "onResume(): event url        :" +  eveURL);
        Log.d (TAG, "onResume(): configuration url:" +  cfgURL);


    }

    private String getPrefValue (String key) {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        return SP.getString(key, "");
    }


    private void initiateApp () {

//-----------------------------------------------------------------------------------------
// SERGIO ENG: why two calls to Detecttor.inititate ???, So I commented the following one.
//->>        Detector.initiate(this);
//-----------------------------------------------------------------------------------------
        Log.d(TAG, "initiateApp()");
        setContentView(R.layout.actitvity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        WebView web = (WebView) findViewById(R.id.about);
        web.loadUrl("file:///android_asset/about.html");

        ImageButton btnEmergency = (ImageButton) findViewById(R.id.emergency);
        btnEmergency.setOnClickListener(this);
        btnEmergency.setOnTouchListener(new ButtonHighlighterOnTouchListener(btnEmergency));
/*
        ImageButton btnSOS = (ImageButton) findViewById(R.id.sos);
        btnSOS.setOnClickListener(this);
        btnSOS.setOnTouchListener(new ButtonHighlighterOnTouchListener(btnSOS));
*/
        ToggleButton btnSOS = (ToggleButton) findViewById(R.id.sos);
        btnSOS.setOnClickListener(this);

//        btnSOS.setOnTouchListener(new ButtonHighlighterOnTouchListener(btnSOS));

        ImageButton btnCheckin = (ImageButton) findViewById(R.id.checkin);
        btnCheckin.setOnClickListener(this);
        btnCheckin.setOnTouchListener(new ButtonHighlighterOnTouchListener(btnCheckin));
        mCheckinButton = btnCheckin;
        /*
        Button signals = (Button) findViewById(R.id.signals);
        signals.setOnClickListener(this);
        */

        Button turnoff = (Button) findViewById(R.id.turnoff);
        turnoff.setOnClickListener(this);

        Button getconfig = (Button) findViewById(R.id.getconfig);
        getconfig.setOnClickListener(this);

        mHandler = new Handler();

        // Run the guardian
        Guardian.initiate(this);

//        eula(this);

    }


    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    private boolean checkPermissions () {

        /*
        boolean permStatus = true;

        // Here, thisActivity is the current activity
        if (canCallPhone() == false) {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, CALL_PHONE_REQUEST);
            permStatus = false;
        }
        if (canReceiveSMS() == false) {
            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, RECEIVE_SMS_REQUEST);
            permStatus = false;
        }
        if (canAccessContacts() == false) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, CONTACTS_REQUEST);
            permStatus = false;
        }
        if (canAccessLocation() == false ) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST);
            permStatus = false;
        }

        Log.d (TAG, "checkPermissions: return("+permStatus+")");

        return permStatus;
        */

        List<String> listPermissionsNeeded = new ArrayList<>();

        // Here, thisActivity is the current activity
        if (canCallPhone() == false) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
        }
        if (canReceiveSMS() == false) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_SMS);
        }
        if (canAccessContacts() == false) {
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
        }
        if (canAccessLocation() == false ) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }


        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            Log.d (TAG, "checkPermissions: return(false)");
            return false;
        }
        Log.d (TAG, "checkPermissions: return(true)");
        return true;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult(): mTargetSdkVersion: "+mTargetSdkVersion+" Build.VERSION.SDK_INT="+Build.VERSION.SDK_INT );
        Log.d(TAG, "Permission callback called------- grantResults.length="+grantResults.length);

/*
        /// Check if all permissions are granted
        if (checkPermissions ()) {
            initiateApp ();
        }

*/
        List<String> listPermissionsNeeded = new ArrayList<>();

        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:

                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            Log.d(TAG, "Permission: " + permissions[i] + " GRANTED");
                        } else {
                            Log.d(TAG, "Permission: " + permissions[i] + " not granted ask again ");
                            listPermissionsNeeded.add(permissions[i]);
                        }
                    }
                }
                break;
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            Log.d (TAG, "onRequestPermissionsResult: requesting permissions not granted yet");
            return;
        }

        initiateApp ();

        return;


/*
            Map<String, Integer> perms = new HashMap<>();
                    // Initialize the map with both permissions
                    perms.put(Manifest.permission.SEND_SMS, PackageManager.PERMISSION_GRANTED);
                    perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                    // Fill with actual results from user
                    if (grantResults.length > 0) {
                        for (int i = 0; i < permissions.length; i++)
                            perms.put(permissions[i], grantResults[i]);
                        // Check for both permissions
                        if (perms.get(Manifest.permission.SEND_SMS) == c
                                && perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            // process the normal flow
                            //else any one or both the permissions are not granted
                        } else {
                            Log.d(TAG, "Some permissions are not granted ask again ");
                            //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                            //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                                showDialogOK("SMS and Location Services Permission required for this app",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                switch (which) {
                                                    case DialogInterface.BUTTON_POSITIVE:
                                                        checkAndRequestPermissions();
                                                        break;
                                                    case DialogInterface.BUTTON_NEGATIVE:
                                                        // proceed with logic by disabling the related features or quit the app.
                                                        break;
                                                }
                                            }
                                        });
                            }
                            //permission is denied (and never ask again is  checked)
                            //shouldShowRequestPermissionRationale will return false
                            else {
                                Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                        .show();
                                //                            //proceed with logic by disabling the related features or quit the app.
                            }
                        }
                    }
                }
            }
*/

    }

    private boolean canAccessLocation() {       return(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));}
    private boolean canAccessCamera()   {       return(hasPermission(Manifest.permission.CAMERA));              }
    private boolean canAccessContacts() {       return(hasPermission(Manifest.permission.READ_CONTACTS));       }
    private boolean canCallPhone()      {       return(hasPermission(Manifest.permission.CALL_PHONE));          }
    private boolean canReceiveSMS()     {       return(hasPermission(Manifest.permission.RECEIVE_SMS));         }

    private boolean hasPermission(String permission) {

        // For Android < Android M, self permissions are always granted.
        /*
        boolean result = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (mTargetSdkVersion >= Build.VERSION_CODES.M) {
                // targetSdkVersion >= Android M, we can
                // use Context#checkSelfPermission
                result = (mContext.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
            else {
                // targetSdkVersion < Android M, we have to use PermissionChecker
                result = PermissionChecker.checkSelfPermission(mContext, permission)
                        == PermissionChecker.PERMISSION_GRANTED;
            }
        }

        return result;

        */
        return (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, permission));
    }

    /*
    Inner Class ButtonHighlighterOnTouchListener
    Used to give HighLight efect when hoovering over the image Button
    */
    public class ButtonHighlighterOnTouchListener implements View.OnTouchListener {

        private final int TRANSPARENT_GREY = Color.argb(0, 185, 185, 185);
        private final int FILTERED_GREY = Color.argb(155, 185, 185, 185);

//        View view = null;

        ImageView imageView = null;
        TextView textView = null;

        public ButtonHighlighterOnTouchListener(final ImageView imageView) {
            super();
            this.imageView = imageView;
        }
/*
        public ButtonHighlighterOnTouchListener(final View view) {
            super();
            this.view = view;
        }
*/
        public ButtonHighlighterOnTouchListener(final TextView textView) {
            super();
            this.textView = textView;
        }

        public boolean onTouch(final View view, final MotionEvent motionEvent) {
            Log.d (TAG, "onTouch(): testView=" + (textView != null)) ;

            if (imageView != null) {
                Log.d (TAG, "onTouch(): isPressed="+imageView.isPressed()
                        + " isFocused=" +imageView.isFocused()
                        + " isActivated=" +imageView.isActivated()
                        + " isEnabled=" +imageView.isEnabled()
                        + " isHovered=" +imageView.isHovered()
                        + " isFocusable=" +imageView.isFocusable()
                        + " isFocusableInTouchMode=" +imageView.isFocusableInTouchMode()
                        + " isInTouchMode=" +imageView.isInTouchMode()
                );

                Drawable myDrawable = imageView.getDrawable();

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.d (TAG, "onTouch(): ACTION_DOWN: alpha="+myDrawable.getAlpha());
//                    myDrawable.setColorFilter (FILTERED_GREY, PorterDuff.Mode.LIGHTEN);
                    myDrawable.setAlpha ((int)(0.6*255));

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    Log.d (TAG, "onTouch(): ACTION_UP: alpha="+myDrawable.getAlpha());
//                    myDrawable.setColorFilter(TRANSPARENT_GREY, PorterDuff.Mode.SRC_ATOP); // or null
                    myDrawable.setAlpha ((int)(1.0*255));
                }
                else {
                    Log.d (TAG, "onTouch(): ACTION_?? = ["+motionEvent.getAction() +"]");
                }
            } else {

                Log.d (TAG, "onTouch(): isPressed="+textView.isPressed()
                        + " isFocused=" +textView.isFocused()
                        + " isActivated=" +textView.isActivated()
                        + " isEnabled=" +textView.isEnabled()
                        + " isHovered=" +textView.isHovered()
                        + " isFocusable=" +textView.isFocusable()
                        + " isFocusableInTouchMode=" +textView.isFocusableInTouchMode()
                        + " isInTouchMode=" +textView.isInTouchMode()
                );

                Log.d (TAG, "onTouch():  "+textView.getCompoundDrawables() [0]);

                for (final Drawable compoundDrawable : textView.getCompoundDrawables()) {
                    if (compoundDrawable != null) {
                        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                            Log.d (TAG, "onTouch(): ACTION_DOWN: alpha"+compoundDrawable.getAlpha());
                            compoundDrawable.setAlpha(128);

                            // we use PorterDuff.Mode. SRC_ATOP as our filter color is already transparent
                            // we should have use PorterDuff.Mode.LIGHTEN with a non transparent color
//                            compoundDrawable.setColorFilter(FILTERED_GREY, PorterDuff.Mode.LIGHTEN);
                        }
                        else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                           Log.d (TAG, "onTouch(): ACTION_UP: alpha"+compoundDrawable.getAlpha());
                           compoundDrawable.setAlpha(255);
//                            compoundDrawable.setColorFilter(TRANSPARENT_GREY, PorterDuff.Mode.SRC_ATOP); // or null
                        }
                        else {
                            Log.d (TAG, "onTouch(): ACTION_???: alpha"+compoundDrawable.getAlpha());
                        }
                    }
                }
            }
            return false;
        }

    }

    public static void startFlashCheckinButton () {
        mHandler.post(new Runnable() {
            public void run() {
                final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
                animation.setDuration(500); // duration - half a second
                animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
                animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
                animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in

                final ImageButton btn = mCheckinButton;
                btn.startAnimation(animation);
            }
        });
    }

    public static void stopFlashCheckinButton () {

        mCheckinButton.clearAnimation();
    }


    /// DIALOGS SESSION

    public void showSymptomDialog() {

        if (mConfig.getCheckinType() == EventInfo.CHECKIN_BUTTON )
            return;

        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new SymptomFragment();
        dialog.show(getFragmentManager(), "NoticeDialogFragment");
    }


}



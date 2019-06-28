package com.dodsoneng.falldetector.tools;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.dodsoneng.falldetector.timers.Configuration;
import com.dodsoneng.falldetector.timers.Event;

/*




ATENCAO NAO ESTA EM USO, esta aqui apenas para conservar o conhecimento






*/
/**
 * Created by sergio.eng on 8/28/17.
 */

public class MyUpdateService extends IntentService {

    private static String TAG = "FD.TOOLS.MyUpdateService....";

    public MyUpdateService() {
        super("MyUpdateService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Do the task here

        Log.i(TAG, "Service running");
        Event event = Event.initiate(getApplicationContext());
        Configuration config = Configuration.initiate(getApplicationContext());
        event.sendDataToServer();
        config.getDataFromServer();

    }
}



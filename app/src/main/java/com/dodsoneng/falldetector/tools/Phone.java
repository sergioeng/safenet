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
package com.dodsoneng.falldetector.tools;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.support.annotation.Keep;
import android.util.Log;
import android.widget.Toast;

import com.dodsoneng.falldetector.Contact;
import com.dodsoneng.falldetector.timers.Event;
import com.dodsoneng.falldetector.R;
import com.dodsoneng.falldetector.Telephony;
import com.dodsoneng.falldetector.models.EventInfo;

import static com.dodsoneng.falldetector.models.EventInfo.FALL;
@Keep
public class Phone {

    private static String TAG = "FD.PHONE";

    private static SoundPool pool = null;
    private static int id = -1;
    private static int volume=0;

    @SuppressWarnings("deprecation")
    public static void siren(Context context) {
        Log.d(TAG, "siren()");
        if (null == pool) {
            pool = new SoundPool(5, AudioManager.STREAM_ALARM, 0);
        }
        if (-1 == id) {
            id = pool.load(context.getApplicationContext(), R.raw.alarm, 1);
        }
        loudest(context, AudioManager.STREAM_ALARM);
        pool.play(id, 1.0f, 1.0f, 1, 3, 1.0f);
    }

    public static void loudest(Context context, int stream) {
        Log.d(TAG, "loudest()");
        AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int loudest = manager.getStreamMaxVolume(stream);
        volume = manager.getStreamVolume(stream);
        manager.setStreamVolume(stream, loudest, 0);
    }

    public static void setNormalVolume (Context context, int stream) {
        Log.d(TAG, "normalVolume()");
        AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        manager.setStreamVolume(stream, volume, 0);
    }


    public static void call(Context context) {
        String contact = Contact.get(context);
        Log.d (TAG, "call(): contact=["+contact+"]");
        try {
            if (contact != null && !"".equals(contact)) {
                Toast.makeText(context, "Calling guardian's phone number for help", Toast.LENGTH_SHORT).show();
                Intent call = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contact));
                call.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(call);
                Telephony.handsfree(context, true);
            } else {
                Toast.makeText(context, "Please enter guardian's phone number in the settings", Toast.LENGTH_SHORT).show();
                siren(context);
            }
        }
        catch (SecurityException e) {
            Log.d (TAG, "call(): exception: " + e.getMessage());
        }
    }

    public static void fall (Context _context) {

        Event eve = Event.initiate(_context);
        eve.setTime(EventInfo.getType(FALL));
    }


}

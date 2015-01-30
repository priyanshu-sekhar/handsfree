package com.example.android.handsfree;

/**
 * Created by priyanshu on 16-Jan-15.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by priyanshu on 13-Jan-15.
 */

public class HeadphoneListener extends BroadcastReceiver {
    public HeadphoneListener(){

    }
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, BackgroundService.class);
        //service.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY);
        //context.startService(service);
        SharedPreferences hPrefs = context.getSharedPreferences("headphone", Context.MODE_PRIVATE);
        if (!hPrefs.getBoolean("headphone", false)) {
//            CallActionListener.isToggleOn = false;
            context.startService(service);
//
//        }
//        else {
//            CallActionListener.isToggleOn = true;
//            context.startService(service);
//        }
            if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
                int state = intent.getIntExtra("state", -1);
                String TAG = "Plugged_State";
                switch (state) {
                    case 0:
                        Log.d(TAG, "Headset is unplugged");
                        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                        audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        CallActionListener.isHeadsetPlugged = false;
                        break;
                    case 1:
                        Log.d(TAG, "Headset is plugged");
                        AudioManager resetAudio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                        resetAudio.setRingerMode(0);
                        CallActionListener.isHeadsetPlugged = true;
                        break;
                    default:
                        Log.d(TAG, "I have no idea what the headset state is");
                }
            }
        }
    }
}

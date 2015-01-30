    package com.example.android.handsfree;
    import android.app.Activity;
    import android.app.Service;
    import android.content.BroadcastReceiver;
    import android.content.Context;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.database.Cursor;
    import android.media.AudioManager;
    import android.speech.tts.TextToSpeech;
    import android.telephony.TelephonyManager;
    import android.util.Log;
    import android.widget.Toast;

    import com.example.android.handsfree.Blacklist_main;
    import com.example.android.handsfree.DBHandler;
    import com.example.android.handsfree.DBReader;

    import java.util.Locale;

    import static java.lang.Thread.sleep;

    public class ServiceReceiver extends BroadcastReceiver {
        DBHandler mHandler;
        AudioManager audio;
        private TextToSpeech tts;
        public static Context context;
        public ServiceReceiver() {

        }

        @Override
        public void onReceive(Context context, Intent intent) {
            this.context = context;
            audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            Intent service = new Intent(context, BackgroundService.class);
            service = new Intent(context, BackgroundService.class);
            context.startService(service);
            SharedPreferences hPrefs=context.getSharedPreferences("headphone",Context.MODE_PRIVATE);
            if(!hPrefs.getBoolean("headphone",false)){
                //context.stopService(service);


           // else {

                context.startService(service);



                switch (tm.getCallState()) {

                    case TelephonyManager.CALL_STATE_RINGING:
                        String text;
                        audio.setRingerMode(0);
                        String phoneNr = intent.getStringExtra("incoming_number");
                        mHandler = new DBHandler(context);
                        Cursor pointer = mHandler.getQuery(DBReader.DBEntry.BLACKLIST_TABLE, phoneNr);
                        pointer.moveToFirst();
                        if (pointer.getCount() == 0) {
                            Cursor pointer1 = mHandler.getQuery(DBReader.DBEntry.TABLE_NAME, phoneNr);
                            pointer1.moveToFirst();
                            if (pointer1.getCount() != 0)
                                text = new String(pointer1.getString(pointer1.getColumnIndex(DBReader.DBEntry.COLUMN_NAME)));
                            else {
                                if (phoneNr != null)
                                    text = phoneNr.replace("", " ").substring(6).trim();
                                else
                                    text = "Unknown person";
                            }
                            Log.i("debug test", text);
                            audio.setRingerMode(0);

                        } else {
                            text = new String(pointer.getString(pointer.getColumnIndex(DBReader.DBEntry.COLUMN_NAME)));
                            audio.setRingerMode(0);
                        }
                        CallActionListener.TextToSpeak = text;
                        service.putExtra("Name", text);
                        context.startService(service);

                        context.startActivity(new Intent(context, CallActionListener.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:

                        audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        android.os.Process.killProcess(android.os.Process.myPid());


                        break;

                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        break;


                }
            }
        }


    }

package com.example.android.handsfree;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;


public class CallActionListener extends ActionBarActivity {

    /*Variables for TextToSpeak Settings*/
    private TextToSpeech tts;
    public static String TextToSpeak = "";
    public static boolean isBlacklisted = false;
    public static boolean isHeadsetPlugged = false;
    public static boolean isToggleOn=false;
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    HeadphoneListener receiver;
    /**
     * *******************************
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //check if handsfree is activated or deactivated

            if (!isBlacklisted) {
                initiate(getApplicationContext());
            } else {
                disconnectCallAndroid();
            }

        /*Intent for HeadphoneListener*/
        IntentFilter receiverFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
         receiver = new HeadphoneListener();
        registerReceiver( receiver, receiverFilter );
        /******************************/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
        return true;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == ActionBarActivity.RESULT_OK) {
            // Fill the list view with the strings the recognizer thought it could have heard
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);

            Log.d("Entered reply", matches.toString());
            if (matches.toString().equals("[yes]"))
                connectCallAndroid();
            else
                disconnectCallAndroid();
            setResult(resultCode);

        }

//                ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//                am.restartPackage("com.example.android.handsfree");
        super.onActivityResult(requestCode, resultCode, data);
        //LaunchTutorial.closeActivity();
        super.finish();


    }

    public int disconnectCallAndroid() {
        Runtime runtime = Runtime.getRuntime();
        int nResp = 0;
        try {
            Log.d("discon", "service call phone 5 \n");
            runtime.exec("service call phone 5 \n");
        } catch (Exception exc) {
            Log.e("discon", exc.getMessage());
            exc.printStackTrace();
        }
        return nResp;
    }

    public void connectCallAndroid() {
        Log.d("connect", "InSecond Method Ans Call");
        // froyo and beyond trigger on buttonUp instead of buttonDown
        Intent buttonUp = new Intent(Intent.ACTION_MEDIA_BUTTON);
        buttonUp.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(
                KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
        sendOrderedBroadcast(buttonUp, "android.permission.CALL_PRIVILEGED");
        Intent headSetUnPluggedintent = new Intent(Intent.ACTION_HEADSET_PLUG);
        headSetUnPluggedintent.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY);
        headSetUnPluggedintent.putExtra("state", 0);
        headSetUnPluggedintent.putExtra("name", "Headset");
        try {
            sendOrderedBroadcast(headSetUnPluggedintent, null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    public void speakOut(String text) {
        text+="is Calling you. Say YES to receive or NO to reject";
        Log.i("tts", tts.toString());
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "UniqueID");
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, map);
        while (tts.isSpeaking()) ;
    }

    public void initiate(final Context context) {

        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {

                    int result = tts.setLanguage(Locale.US);

                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.i("TTS", "This Language is not supported");
                    } else {
                        speakOut(TextToSpeak);

                    }

                } else {
                    Log.i("TTS", "Initilization Failed!");
                }
            }
        });

        tts.setOnUtteranceProgressListener(utteranceProgressListener);
    }

    String TAG = "lauda";
    UtteranceProgressListener utteranceProgressListener = new UtteranceProgressListener() {

        @Override
        public void onStart(String utteranceId) {
            Log.d(TAG, "onStart ( utteranceId :" + utteranceId + " ) ");
        }

        @Override
        public void onError(String utteranceId) {
            Log.d(TAG, "onError ( utteranceId :" + utteranceId + " ) ");
        }

        @Override
        public void onDone(String utteranceId) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech recognition demo");
            startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
        }
    };


}


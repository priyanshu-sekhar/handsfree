package com.example.android.handsfree;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;

import java.util.ArrayList;


public class SpeechListener extends Activity{



    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    //public SpeechListener(){}
//    public static void startVoiceRecognitionActivity(Activity activity) {
//
//        final Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
//                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech recognition");
////        intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
////        intent.setFlags(Intent.FLAG_RECEIVER_FOREGROUND);
//        //intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//        //intent.addFlags(Intent.FLAG_RECEIVER_NO_ABORT);
//       // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        //activity.startActivity(intent);
//        Log.i("p","k");
//
//        activity.startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
//
//
//    }





//    @Override
// protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//     Log.i("onAct","entered");
//        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//
//            // Fill the list view with the strings the recognizer thought it could have heard
//            ArrayList<String> matches = data.getStringArrayListExtra(
//                    RecognizerIntent.EXTRA_RESULTS);
//            Log.i("onAct2","OK2");
//            //list.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
//                    //matches));
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//
//    }


}

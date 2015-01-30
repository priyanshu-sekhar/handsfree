package com.example.android.handsfree;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

/**
 * Created by priyanshu on 15-Jan-15.
 */
public class TexttoSpeech implements TextToSpeech.OnInitListener {
   private TextToSpeech tts;

    public void init(Context context) {
        if (tts == null) {
            tts = new TextToSpeech(context, this);
        }
   }

    public void speakOut(String sayThis) {
        tts.speak(sayThis, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.i("TTS", "This Language is not supported");
            }

        } else {
            Log.i("TTS", "Initilization Failed!");
        }
    }

    public void shutdown() {
        tts.shutdown();
    }
}

package com.example.android.handsfree;

/**
 * Created by Rakesh Sarangi on 30-Jan-15.
 */
import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;

/**
 * Created by Rakesh Sarangi on 20-Jan-15.
 */
public class VolumeDialogPreference extends DialogPreference{

    public VolumeDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.slider_layout);
    }


}

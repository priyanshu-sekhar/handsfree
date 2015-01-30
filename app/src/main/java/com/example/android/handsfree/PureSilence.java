package com.example.android.handsfree;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

/**
 * Created by priyanshu on 11-Jan-15.
 */
public class PureSilence extends Fragment implements View.OnClickListener {
    private RadioButton test;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View rootView = inflater.inflate(R.layout.navigate_pure_silence, container, false);
        //test=(RadioButton)getView().findViewById(R.id.radioButton);
        //test.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {

    }
}


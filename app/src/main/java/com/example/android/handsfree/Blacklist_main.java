package com.example.android.handsfree;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.lang.Thread.sleep;

/**
 * Created by priyanshu on 12-Jan-15.
 */

public class Blacklist_main extends ListFragment implements View.OnClickListener {
    public ListView myListView;
    Cursor pointer;
    public String[] Contacts = {};
    static View rootView;
    private FragmentActivity myContext;
    public int[] to = {};
    private ImageButton add, remove;
    DBHandler mHandler;
    ListAdapter adapter;
    PackageManager pm;
    static List<ResolveInfo> activities;
    static FragmentActivity activity = null;
    ActionBarDrawerToggle mToggle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.navigate_blacklist, container, false);

        return rootView;
    }

    @Override
    public void onResume() {

//        ((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        ((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);



        super.onResume();
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pm = getActivity().getPackageManager();
        activities = pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);

        // add reference to views
        add = (ImageButton) getView().findViewById(R.id.add);
        remove = (ImageButton) getView().findViewById(R.id.remove);

        // set listeners to image buttons
        add.setOnClickListener(this);
        remove.setOnClickListener(this);

        mHandler = new DBHandler(getActivity().getApplicationContext());
        pointer = mHandler.getTablePointer(DBReader.DBEntry.BLACKLIST_TABLE);

        pointer.moveToFirst();
        adapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.custom_textview,
                pointer,
                Contacts = new String[]{DBReader.DBEntry.COLUMN_NAME,
                        DBReader.DBEntry.COLUMN_PHONE
                        // ContactsContract.CommonDataKinds.Phone.NUMBER
                },
                to = new int[]{android.R.id.text1});

        setListAdapter(adapter);
        myListView = getListView();
        myListView.setItemsCanFocus(false);
        //myListView.setFastScrollEnabled(true);
        myListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mToggle = new ActionBarDrawerToggle(
                getActivity(),
                (DrawerLayout) getView().findViewById(R.id.drawer_layout),
                0,
                0
        );
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add: // the add button

                mToggle.setDrawerIndicatorEnabled(false);
                FragmentManager fragmentManager = myContext.getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.container, new ContactsListFragment()).addToBackStack(null).commit();
                break;

            case R.id.remove: // the remove button
                SQLiteDatabase db = mHandler.getWritableDatabase();
                SparseBooleanArray checkedPositions = myListView
                        .getCheckedItemPositions();


                if (checkedPositions != null) {
                    for (int k = 0; k < checkedPositions.size(); k++) {
//                        Log.i("value->", checkedPositions.keyAt(k) + "");
                        if (checkedPositions.valueAt(k)) {
                            int position = checkedPositions.keyAt(k);
                            pointer.moveToPosition(position);
                            //db.delete(DBReader.DBEntry.BLACKLIST_TABLE, DBReader.DBEntry._ID + "='" + pointer.getString(pointer.getColumnIndex(DBReader.DBEntry._ID)) + "'", null);
                            mHandler.deleteRowFromDatabase(DBReader.DBEntry.BLACKLIST_TABLE,
                                    DBReader.DBEntry._ID,
                                    pointer.getString(pointer.getColumnIndex(DBReader.DBEntry._ID))

                            );

                        }


                    }
                    FragmentManager fragmentManager2 = myContext.getSupportFragmentManager();
                    FragmentTransaction ft2 = fragmentManager2.beginTransaction();
                    ft2.replace(R.id.container, new Blacklist_main()).commit();
                }
                break;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }





}
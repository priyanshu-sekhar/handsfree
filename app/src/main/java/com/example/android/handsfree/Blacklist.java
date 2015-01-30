package com.example.android.handsfree;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.database.Cursor;
import android.database.sqlite.*;
import android.widget.Toast;

import java.util.zip.Inflater;

/**
 * Created by priyanshu on 11-Jan-15.
 */
public class Blacklist extends ListFragment implements View.OnClickListener {

    // List variables

    public int[] to = {};



    private ImageButton save, cancel;
    private TextView phone;
    private String phoneNumber;
    private Cursor cursor;
    ContentValues values;
    String ContactNumber,ContactName,ContactID;
    DBHandler mHandler;
    Cursor mCursor;
    SQLiteDatabase db;
    public ListView myListView;
    Cursor pointer;
    public String[] Contacts = {};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //super.onCreateView(inflater,container,savedInstanceState);
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.contacts, container, false);

       return rootView;
    }



    @Override
    public void onViewCreated(View view,Bundle savedInstanceState){
        mHandler=new DBHandler(getActivity().getApplicationContext());

        pointer=mHandler.getTablePointer(DBReader.DBEntry.TABLE_NAME);
        pointer.moveToFirst();

        // set references to views
        save = (ImageButton) getView().findViewById(R.id.save);
        cancel = (ImageButton) getView().findViewById(R.id.cancel);

        // Defines listeners for the buttons
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);

        ListAdapter adapter = new SimpleCursorAdapter(
                getActivity(),

                R.layout.custom_textview,



                pointer,
                Contacts = new String[] {DBReader.DBEntry.COLUMN_NAME,
                        DBReader.DBEntry.COLUMN_PHONE
                // ContactsContract.CommonDataKinds.Phone.NUMBER
                   },
                to = new int[] { android.R.id.text1 });

        setListAdapter(adapter);
        myListView = getListView();
        myListView.setItemsCanFocus(true);
        //myListView.setFastScrollEnabled(true);
        myListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                // TODO Auto-generated method stub

                Log.v("long clicked", "pos: " + pos);

                return true;
            }
        });

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        super.onListItemClick(l, v, position, id);
        SQLiteDatabase contacts= mHandler.getReadableDatabase();

        String[] projection={
                DBReader.DBEntry._ID,
                //DBReader.DBEntry.KEY_ID,
                DBReader.DBEntry.COLUMN_NAME,
                DBReader.DBEntry.COLUMN_PHONE,
                //DBReader.DBEntry.COLUMN_BLACKLIST,
                //DBReader.DBEntry.COLUMN_WHITELIST
        };
        String selection =null;// ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER + " = '1'";
//        String[] condition = {DBReader.DBEntry._ID+"="+position};
        String[] selectionArgs = null;
        String sortOrder = DBReader.DBEntry.COLUMN_NAME;

        pointer = contacts.query(
                DBReader.DBEntry.TABLE_NAME,              // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                null,                                // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        pointer.moveToPosition(position);


    }
    @Override
    public void onClick(View src) {
        Intent i;
        switch (src.getId()) {

            case R.id.cancel:
                getActivity().getSupportFragmentManager().popBackStackImmediate();
                break;

            case R.id.save:

               SparseBooleanArray checkedPositions = myListView
                        .getCheckedItemPositions();
                if (checkedPositions != null) {
                     for (int k = 0; k < checkedPositions.size(); k++) {
                        if (checkedPositions.valueAt(k)) {
                            int position=checkedPositions.keyAt(k);
                            pointer.moveToPosition(position);
                            mHandler.SaveTable(DBReader.DBEntry.BLACKLIST_TABLE,
                                    pointer.getString(pointer.getColumnIndex(DBReader.DBEntry.COLUMN_NAME)),
                                    pointer.getString(pointer.getColumnIndex(DBReader.DBEntry.COLUMN_PHONE))
                                    );

                        }
                    }

                }

                getActivity().getSupportFragmentManager().popBackStackImmediate();
                break;
        }

    }


}
package com.example.android.handsfree;

/**
 * Created by priyanshu on 09-Jan-15.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import static java.lang.Thread.sleep;
//import android.os.SystemProperties;


public class Splash extends Activity {   /*Changes for Headset-Plugin listener*/
    private static final String LOG_TAG = Splash.class.getSimpleName();
    private View mDecorView;
    public static final String MyPrefs = "MyPrefs";
    private static Splash instance;
    HeadphoneListener receiver;
    private static final String APP_ID = "applicationID";
    private static final String APP_SECRET = "applicationSecret";
    private static final String APP_ROUTE = "applicationRoute";
    private static final String PROPS_FILE = "bluelist.properties";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mDecorView = getWindow().getDecorView();
        hideSystemUI();
        // Runs for 2 secs then goes to next activity
        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {

                    SharedPreferences sp = getSharedPreferences(MyPrefs, Context.MODE_PRIVATE);
                    if (!sp.getBoolean("First", false)) {
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putBoolean("First", true);
                        editor.commit();
                        Intent intent = new Intent(Splash.this, LaunchTutorial.class);
                        startActivity(intent);
                        addContacts();
                    } else {
                        Intent intent = new Intent(Splash.this, MainPage.class);
                        startActivity(intent);
                    }


                }
            }
        };
        timer.start();



        /*Intent for HeadphoneListener*/
        IntentFilter receiverFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        receiver = new HeadphoneListener();
        registerReceiver( receiver, receiverFilter );
        /******************************/



    }

    public void addContacts() {
        DBHandler mHandler = new DBHandler(getApplicationContext());
        Cursor mCursor = getContacts();
        startManagingCursor(mCursor);
        mCursor.moveToFirst();
        do {
            String names = mCursor.getString(mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String number = mCursor.getString(mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            mHandler.SaveTable(DBReader.DBEntry.TABLE_NAME, names, number);
        } while (mCursor.moveToNext());

    }

    private Cursor getContacts() {
        // Run query
        String ContactNumber, ContactName, ContactID;
        ContactNumber = ContactsContract.CommonDataKinds.Phone.NUMBER;
        ContactName = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;
        ContactID = ContactsContract.CommonDataKinds.Phone._ID;
        String[] projection;
        projection = new String[]{ContactID,
                ContactName,
                ContactNumber
        };
        String selection = ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER + " = '1'";

        String[] selectionArgs = null;
        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                + " COLLATE LOCALIZED ASC";
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        return managedQuery(uri, projection, selection, selectionArgs,
                sortOrder);
    }

    private void hideSystemUI() {
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    protected void onResume() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
//        registerReceiver(myHeadsetReceiver, filter);
        super.onResume();

    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();

    }


}




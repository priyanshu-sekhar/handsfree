package com.example.android.handsfree;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TimePicker;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.lang.Object.*;
//import com.google.android.gcm.server.Constants;
public class MainPage extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks, View.OnClickListener {

    private static Button bUnplug;
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;
    private ListView mDrawerList;
    FragmentManager myContext;
    SwitchCompat masterButton;
    private boolean isSearchResultView = false;
    ActionBarDrawerToggle mToggle;
    DrawerLayout drawerLayout;
    SwitchCompat headphoneToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
//        masterButton=(SwitchCompat)findViewById(R.id.switchForActionBar);
//        masterButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                masterButton.isChecked();
//                Toast.makeText(getApplicationContext(),"Activated",Toast.LENGTH_SHORT).show();
//            }
//        });
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                drawerLayout);


        mToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                0,
                0
        );


    }




    /**
     * To do when button is clicked
     */


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        Fragment selectedFrag = null;
        switch (position + 1) {
            case 1:
                selectedFrag = new Blacklist_main();
                break;
            case 2:
                selectedFrag = new Whitelist_main();
                break;
            default:
                break;
        }
        if (selectedFrag != null)
            ft.replace(R.id.container, selectedFrag).commit();

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mToggle.onConfigurationChanged(newConfig);
    }
    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;

        }
    }
    public void SavePreferences(){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       final SharedPreferences hToggle=getSharedPreferences("headphone", Context.MODE_PRIVATE);
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main, menu);
            MenuItem actionSwitch=menu.findItem(R.id.Switch);
            //actionSwitch.setActionView(R.layout.actionbar_switch);
            headphoneToggle=(SwitchCompat) MenuItemCompat.getActionView(actionSwitch);
            if(hToggle.getBoolean("headphone",false))
                headphoneToggle.setChecked(false);//set headphone toggle off
            else
                headphoneToggle.setChecked(true);//set headphone toggle on
            (headphoneToggle).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    String toggle=null;
                    SharedPreferences.Editor editor=hToggle.edit();
                    editor.putBoolean("headphone",isChecked);
                    if(hToggle.getBoolean("headphone",false)) {
                        editor.putBoolean("headphone", false);
                        toggle="HandsFree activated";
                    }
                    else {
                        editor.putBoolean("headphone", true);
                        toggle="HandsFree deactivated";
                    }
                    editor.commit();
                    Toast.makeText(getApplicationContext(),toggle,Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getApplicationContext(),BackgroundService.class);
                    if(isChecked)
                        startService(intent);
                    else
                        stopService(intent);

                }
            });


                // Only show items in the action bar relevant to this screen
                // if the drawer is not showing. Otherwise, let the drawer
                // decide what to show in the action bar.



                boolean fragmentOpened=false;
                restoreActionBar();
                resetActionBar(fragmentOpened, DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                return super.onCreateOptionsMenu(menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#848482")));
    }

    public void resetActionBar(boolean childAction,int drawerMode){
        if (childAction) {
            // [Undocumented?] trick to get up button icon to show
            mToggle.setDrawerIndicatorEnabled(false);
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        } else {
            mToggle.setDrawerIndicatorEnabled(true);
        }

        drawerLayout.setDrawerLockMode(drawerMode);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        DBHandler mHandler = new DBHandler(getApplicationContext());
        SQLiteDatabase db = mHandler.getWritableDatabase();
        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {

            case R.id.home:// the back arrow
                Log.i("mp home","called");
                break;

            case R.id.action_settings: // the settings option
                Intent intent = new Intent(MainPage.this, Settings.class);
                startActivity(intent);
                break;

            case R.id.Switch: // the toggle switch
               if(BackgroundService.startApp)
                   BackgroundService.startApp=false;
               else
                   BackgroundService.startApp=true;
                Log.i("Switch",""+BackgroundService.startApp);
                Toast.makeText(getApplicationContext(),""+BackgroundService.startApp,Toast.LENGTH_SHORT).show();
               break;
        }
        db.close();
        return false;
    }

    private Cursor getContacts() {
        // Run query
        String ContactNumber, ContactName, ContactID;
        ContactNumber = ContactsContract.CommonDataKinds.Phone.NUMBER;
        Log.v("number:", ContactNumber);
        ContactName = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;
        Log.v("names:", ContactName);
        ContactID = ContactsContract.CommonDataKinds.Phone._ID;

        String[] projection;
        projection = new String[]{ContactID,
                ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
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
    public InputStream openPhoto(long contactId) {
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        Cursor cursor = getContentResolver().query(photoUri,
                new String[] {ContactsContract.Contacts.Photo.PHOTO}, null, null, null);
        if (cursor == null) {
            return null;
        }
        try {
            if (cursor.moveToFirst()) {
                byte[] data = cursor.getBlob(0);
                if (data != null) {
                    return new ByteArrayInputStream(data);
                }
            }
        } finally {
            cursor.close();
        }
        return null;
    }

    @Override
    public void onClick(View v) {

    }


    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainPage) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    @SuppressLint("ValidFragment")
    public class BlacklistFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";
        //protected RadioButton test=(RadioButton)findViewById(R.id.radioButton);

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public BlacklistFragment newInstance(int sectionNumber) {
            BlacklistFragment blackFrag = new BlacklistFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            blackFrag.setArguments(args);
            return blackFrag;
        }

        public BlacklistFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            View rootView = inflater.inflate(R.layout.navigate_blacklist, container, false);

            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
//            ((MainPage) activity).onSectionAttached(
//                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    public static class WhitelistFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static WhitelistFragment newInstance(int sectionNumber) {
            WhitelistFragment whiteFrag = new WhitelistFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            whiteFrag.setArguments(args);
            return whiteFrag;
        }

        public WhitelistFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.navigation_whitelist, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainPage) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    public static class PureSilenceFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PureSilenceFragment newInstance(int sectionNumber) {
            PureSilenceFragment pureFrag = new PureSilenceFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            pureFrag.setArguments(args);
            return pureFrag;
        }



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.navigate_pure_silence, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainPage) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }


}


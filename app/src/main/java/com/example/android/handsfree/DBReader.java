package com.example.android.handsfree;

import android.provider.BaseColumns;

/**
 * Created by priyanshu on 12-Jan-15.
 */
public final class DBReader {
    public DBReader(){};
    public static abstract class DBEntry implements BaseColumns{
        public static final String TABLE_NAME="Contacts";
        public static final String BLACKLIST_TABLE="Blacklist";

        public static final String KEY_ID="Id";
        public static final String COLUMN_NAME="Name";
        public static final String COLUMN_PHONE="Number";

        //public static final String COLUMN_BLACKLIST="isBlackList";
        //public static final String COLUMN_WHITELIST="isWhiteList";
    }
    private static final String TEXT_TYPE=" TEXT unique";
    private static final String COM_SEP=",";
    public static final String SQL_CREATE_ENTRIES=
            "Create Table "
            +DBEntry.TABLE_NAME+" ("+
            DBEntry._ID+" INTEGER PRIMARY KEY,"+
            DBEntry.COLUMN_NAME+TEXT_TYPE+COM_SEP+
            DBEntry.COLUMN_PHONE+TEXT_TYPE+
            ")";
    public static final String SQL_BLACKLIST_ENTRIES=
            "Create Table "
                    +DBEntry.BLACKLIST_TABLE+" ("+
                    DBEntry._ID+" INTEGER PRIMARY KEY,"+
                    DBEntry.COLUMN_NAME+TEXT_TYPE+COM_SEP+
                    DBEntry.COLUMN_PHONE+TEXT_TYPE+
                    ")";
    /**
     *"CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
     FeedEntry._ID + " INTEGER PRIMARY KEY," +
     FeedEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
     FeedEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
     ... // Any other options for the CREATE command
     " )";

     */

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DBEntry.TABLE_NAME;
}

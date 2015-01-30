package com.example.android.handsfree;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.ContactsContract;

import com.example.android.handsfree.DBReader;
/**
 * Created by priyanshu on 12-Jan-15.
 */


public class DBHandler extends SQLiteOpenHelper{
    private static final String DATABASE_NAME="Contact5.db";
    private static final int DATABASE_VERSION=1;
    DBHandler mHandler;
    Context context;
    Cursor pointer;
    String[] projection={
            DBReader.DBEntry._ID,
            DBReader.DBEntry.COLUMN_NAME,
            DBReader.DBEntry.COLUMN_PHONE,

    };
    String selection = null;//ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER + " = '1'";

    String[] selectionArgs = null;
    String sortOrder = DBReader.DBEntry.COLUMN_NAME
            + " ASC";
    SQLiteDatabase contacts;
    DBHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBReader.SQL_CREATE_ENTRIES);
        db.execSQL(DBReader.SQL_BLACKLIST_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       db.execSQL(DBReader.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public Cursor getQuery(String tableName, String phone){

        contacts= this.getReadableDatabase();
//        pointer = contacts.rawQuery("SELECT count(*) FROM "+tableName, null);
//        pointer.moveToFirst();
//        int count = pointer.getInt(0);
//        if(count>0)
        String select = DBReader.DBEntry.COLUMN_PHONE +"='"+phone+"'";
        pointer = contacts.query(
                tableName,                                // The table to query
                projection,                               // The columns to return
                select,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        return pointer;
    }

    public Cursor getNumber(String tableName,String name){
        contacts=this.getReadableDatabase();
        String select=DBReader.DBEntry.COLUMN_NAME+"='"+name+"'";
        pointer=contacts.query(
                tableName,
                projection,
                select,
                selectionArgs,
                null,
                null,
                sortOrder

        );

        return pointer;

    }

    public Cursor getTablePointer(String tableName){

        contacts= this.getReadableDatabase();
//        pointer = contacts.rawQuery("SELECT count(*) FROM "+tableName, null);
//        pointer.moveToFirst();
//        int count = pointer.getInt(0);
//        if(count>0)
            pointer = contacts.query(
                    tableName,                                // The table to query
                    projection,                               // The columns to return
                    selection,                                // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );
        return pointer;
    }
    public boolean deleteRowFromDatabase(String tableName,String Key,String KeyValue){
        return this.getWritableDatabase().delete(tableName,Key + "='" + KeyValue + "'", null) > 0;

    }

    public boolean deleteAll(String tableName){
        return this.getWritableDatabase().delete(tableName, null, null) > 0;

    }

    public boolean SaveTable(String tableName,String name, String number){
        contacts= this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBReader.DBEntry.COLUMN_NAME,name);
        values.put(DBReader.DBEntry.COLUMN_PHONE,number);
        long val=contacts.insert(
                tableName,
                null,
                values);
        contacts.close();
        return val>0;
    }

}

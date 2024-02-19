package com.example.contactlist2;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ContactDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mycontacts.db";
    private static final int DATABASE_VERSION = 2;


    //Database creation sql statement
    private static final String Create_Table_Contact =
            "create table contact (_id integer primary key autoincrement, " +
                    "contactname text not null, streetaddress text, " +
                    "city text, state text, zipcode text, " +
                    "phonenumber text, cellnumber text, " +
                    "email text, birthday text, contactphoto blob);";

    public ContactDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate (SQLiteDatabase db) {
        db.execSQL(Create_Table_Contact);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Log.w(ContactDBHelper.class.getName(), "upgrading database from version " + oldVersion + " to " + newVersion
                //+ ", which will destroy all old data");
        //db.execSQL("DROP TABLE IF EXISTS contact");
        //onCreate(db);
        try {
            db.execSQL("ALTER TABLE contact ADD COLUMN contactphoto blob");
        } catch (Exception e) {
            //do nothing
        }
    }



}

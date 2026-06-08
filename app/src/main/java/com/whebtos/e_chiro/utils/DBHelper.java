package com.whebtos.e_chiro.utils;

//public class DBHelper {
//}


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.whebtos.e_chiro.ui.notifications.Notification;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = DefaultSettings.DATABASE_NAME;
    private static final int DATABASE_VERSION = 4;

    private String id,title,content,dateCreated,serviceProvideID,serviceProviderEmail;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + DefaultSettings.ALERTS_TABLE +
                " (id TEXT PRIMARY KEY, title TEXT, content TEXT, dateCreated TEXT,serviceproviderid TEXT, serviceprovideremail TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades if needed
        db.execSQL("DROP TABLE IF EXISTS " + DefaultSettings.ALERTS_TABLE);
        onCreate(db);
    }

    // Define a method to retrieve data from the table
    public Cursor getAllAlerts() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + DefaultSettings.ALERTS_TABLE, null);
    }

    public void insertNotification(String title, String content,
                                   String serviceProviderID, String serviceProviderEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("content", content);
        values.put("serviceproviderid", serviceProviderID);
        values.put("serviceprovideremail", serviceProviderEmail);

        db.insert(DefaultSettings.ALERTS_TABLE, null, values);
        db.close();
    }

    public List<Notification> getAllNotifications() {
        List<Notification> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DefaultSettings.ALERTS_TABLE, null);


        if (cursor.moveToFirst()) {
            do {

                Notification n = new Notification(id,title,content,dateCreated,serviceProvideID,serviceProviderEmail);
                n.setId(cursor.getString(cursor.getColumnIndexOrThrow("id")));
                n.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
                n.setContent(cursor.getString(cursor.getColumnIndexOrThrow("content")));
                n.setDateCreated(cursor.getString(cursor.getColumnIndexOrThrow("dateCreated")));
                n.setServiceProviderID(cursor.getString(cursor.getColumnIndexOrThrow("serviceproviderid")));
                n.setServiceProviderEmail(cursor.getString(cursor.getColumnIndexOrThrow("serviceprovideremail")));
                list.add(n);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    public void deleteNotification(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DefaultSettings.ALERTS_TABLE, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }
}


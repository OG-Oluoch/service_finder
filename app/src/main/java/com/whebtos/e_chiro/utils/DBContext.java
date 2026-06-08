package com.whebtos.e_chiro.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.whebtos.e_chiro.ui.notifications.Notification;

import java.util.ArrayList;
import java.util.List;

public class DBContext extends SQLiteOpenHelper {

    private static final int VER = 4;

    public DBContext(@Nullable Context context) {
        super(context, DefaultSettings.DATABASE_NAME, null, VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       // String query = "create table " + DefaultSettings.ALERTS_TABLE + "(id text primary key, title text,content text,serviceproviderid text,serviceprovideremail text)";
        String query2 = "create table " + DefaultSettings.LOGINS_TABLE + "(id integer primary key, user_id text,user_name text,password text,token text,token_expiry text)";

        //db.execSQL(query);
        db.execSQL(query2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "drop table if exists " + DefaultSettings.ALERTS_TABLE + "";
        String query2 = "drop table if exists " + DefaultSettings.LOGINS_TABLE + "";
        db.execSQL(query);
        db.execSQL(query2);

        onCreate(db);
    }

//    public List<Notification> getAllAlerts() {
//        List<Notification> list = new ArrayList<>();
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT * FROM " + DefaultSettings.ALERTS_TABLE, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                Notification n = new Notification();
//                n.setId(cursor.getString(cursor.getColumnIndexOrThrow("id")));
//                n.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
//                n.setContent(cursor.getString(cursor.getColumnIndexOrThrow("content")));
//                n.setServiceProviderID(cursor.getString(cursor.getColumnIndexOrThrow("serviceproviderid")));
//                n.setServiceProviderEmail(cursor.getString(cursor.getColumnIndexOrThrow("serviceprovideremail")));
//                list.add(n);
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        db.close();
//        return list;
//
//    }
}

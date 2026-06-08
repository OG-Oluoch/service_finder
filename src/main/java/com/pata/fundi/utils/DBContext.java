package com.whebtos.e_chiro.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBContext extends SQLiteOpenHelper {

    private static final int VER = 4;

    public DBContext(@Nullable Context context) {
        super(context, DefaultSettings.DATABASE_NAME, null, VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "create table " + DefaultSettings.ALERTS_TABLE + "(id integer primary key, title text,content text,date_created text)";
        String query2 = "create table " + DefaultSettings.LOGINS_TABLE + "(id integer primary key, user_id text,user_name text,password text,token text,token_expiry text)";
        db.execSQL(query);
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
}

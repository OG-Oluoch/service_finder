package com.whebtos.e_chiro.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "user_token.db";
    private static final int DATABASE_VERSION = 4;

    public static final String TABLE_USER_TOKEN = "User";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "user_name";
    public static final String COLUMN_USERID = "user_id";

    public UserDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //String createTableSQL = "CREATE TABLE "+TABLE_USER_TOKEN+"(" +COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+COLUMN_USERNAME + "TEXT"+COLUMN_USERID+"TEXT)";
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + TABLE_USER_TOKEN + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_USERID + " TEXT)";
        db.execSQL(createTableSQL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_TOKEN);
        onCreate(db);
    }

}

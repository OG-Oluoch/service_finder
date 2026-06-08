package com.whebtos.e_chiro.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "client.db";
    private static final int DATABASE_VERSION = 4;

    public static final String TABLE_CLIENT = "client";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CLIENT_TYPE = "client_type";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableSQL = "CREATE TABLE " + TABLE_CLIENT + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CLIENT_TYPE + " TEXT)";
        db.execSQL(createTableSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLIENT);
        onCreate(db);
    }

    public String getClientType() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CLIENT, new String[]{COLUMN_CLIENT_TYPE},
                null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String type = cursor.getString(0);
            cursor.close();
            return type;
        }
        return null;
    }

}

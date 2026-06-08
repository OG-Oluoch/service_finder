package com.whebtos.e_chiro.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.whebtos.e_chiro.models.ApiResponse;
import com.whebtos.e_chiro.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TokenUtil {

    private SecureHttpRequest secureHttpRequest;

    public TokenUtil(SecureHttpRequest secureHttpRequest){
        this.secureHttpRequest=secureHttpRequest;
    }

    public void secureHttpRequest(Context context) {

        User user = new User();

        try {

            DBContext dbContext = new DBContext(context);

            SQLiteDatabase db = dbContext.getReadableDatabase();

            Cursor cursor = db.rawQuery("SELECT * FROM " + DefaultSettings.LOGINS_TABLE, null);

            if (cursor.moveToFirst()) {

                user.setId(cursor.getString(1));

                user.setUserName(cursor.getString(2));

                user.setPassword(cursor.getString(3));

                user.setAccessToken(cursor.getString(4));

                String expiry = cursor.getString(5);

                user.setAccessTokenExpiryDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(expiry));

                if ((new Date()).compareTo(user.getAccessTokenExpiryDate()) > 0) {

                    JSONObject jsonObject = new JSONObject();

                    jsonObject.put("user_name", user.getUserName());

                    jsonObject.put("password", user.getPassword());

                    final String requestBody = jsonObject.toString();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, DefaultSettings.URL + DefaultSettings.URL_USER_LOGIN, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                JSONObject responseObject = new JSONObject(response);

                                ApiResponse apiResponse = new ApiResponse();

                                user.setAccessToken(responseObject.getString("accessToken"));

                                if (apiResponse.getAccessToken() != null && !user.getAccessToken().isEmpty()) {

                                    DBContext dbContext = new DBContext(context);

                                    SQLiteDatabase sqLiteDatabase = dbContext.getWritableDatabase();

                                    sqLiteDatabase.delete(DefaultSettings.LOGINS_TABLE, null, null);

                                    ContentValues contentValues = new ContentValues();

                                    contentValues.put("user_id", user.getId());

                                    contentValues.put("user_name", user.getUserName());

                                    contentValues.put("password", user.getPassword());

                                    contentValues.put("token", user.getAccessToken());

                                    contentValues.put("token_expiry", apiResponse.getAccessTokenExpiryDate());

                                    sqLiteDatabase.insert(DefaultSettings.LOGINS_TABLE, null, contentValues);

                                    secureHttpRequest.request(user);

                                } else {

                                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }) {
                        @Override
                        public String getBodyContentType() {
                            return "application/json; charset=utf-8";
                        }

                        @Override
                        public byte[] getBody() throws AuthFailureError {
                            try {
                                return requestBody == null ? null : requestBody.getBytes("utf-8");
                            } catch (UnsupportedEncodingException uee) {
                                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                                return null;

                            }
                        }

                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(context);

                    requestQueue.add(stringRequest);

                } else {
                    secureHttpRequest.request(user);
                }

            }

        } catch (Exception ex) {

        }
    }

    public void secureHttpRequest2(Context context) {

        User user = new User();

        try {

            DBContext dbContext = new DBContext(context);

            SQLiteDatabase db = dbContext.getReadableDatabase();

            Cursor cursor = db.rawQuery("SELECT * FROM " + DefaultSettings.LOGINS_TABLE, null);

            if (cursor.moveToFirst()) {

                user.setId(cursor.getString(1));

                user.setUserName(cursor.getString(2));

                user.setPassword(cursor.getString(3));

                user.setAccessToken(cursor.getString(4));

                String expiry = cursor.getString(5);

                user.setAccessTokenExpiryDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(expiry));

                if ((new Date()).compareTo(user.getAccessTokenExpiryDate()) > 0) {

                    JSONObject jsonObject = new JSONObject();

                    jsonObject.put("user_name", user.getUserName());

                    jsonObject.put("password", user.getPassword());

                    final String requestBody = jsonObject.toString();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, DefaultSettings.URL + DefaultSettings.URL_USER_LOGIN, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                JSONObject responseObject = new JSONObject(response);

                                ApiResponse apiResponse = new ApiResponse();

                                user.setAccessToken(responseObject.getString("accessToken"));

                                if (apiResponse.getAccessToken() != null && !user.getAccessToken().isEmpty()) {

                                    DBContext dbContext = new DBContext(context);

                                    SQLiteDatabase sqLiteDatabase = dbContext.getWritableDatabase();

                                    sqLiteDatabase.delete(DefaultSettings.LOGINS_TABLE, null, null);

                                    ContentValues contentValues = new ContentValues();

                                    contentValues.put("user_id", user.getId());

                                    contentValues.put("user_name", user.getUserName());

                                    contentValues.put("password", user.getPassword());

                                    contentValues.put("token", user.getAccessToken());

                                    contentValues.put("token_expiry", apiResponse.getAccessTokenExpiryDate());

                                    sqLiteDatabase.insert(DefaultSettings.LOGINS_TABLE, null, contentValues);

                                    secureHttpRequest.request2(user);

                                } else {

                                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }) {
                        @Override
                        public String getBodyContentType() {
                            return "application/json; charset=utf-8";
                        }

                        @Override
                        public byte[] getBody() throws AuthFailureError {
                            try {
                                return requestBody == null ? null : requestBody.getBytes("utf-8");
                            } catch (UnsupportedEncodingException uee) {
                                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                                return null;

                            }
                        }

                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(context);

                    requestQueue.add(stringRequest);

                } else {
                    secureHttpRequest.request2(user);
                }

            }

        } catch (Exception ex) {

        }
    }
}

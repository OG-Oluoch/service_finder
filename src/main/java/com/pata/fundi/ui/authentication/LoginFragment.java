package com.whebtos.e_chiro.ui.authentication;

import static android.content.Context.MODE_PRIVATE;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.shobhitpuri.custombuttons.GoogleSignInButton;
import com.whebtos.e_chiro.MainActivity;
import com.whebtos.e_chiro.R;
import com.whebtos.e_chiro.models.ApiResponse;
import com.whebtos.e_chiro.models.User;
import com.whebtos.e_chiro.utils.AppCache;
import com.whebtos.e_chiro.utils.DBContext;
import com.whebtos.e_chiro.utils.DefaultSettings;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment {

    private Button btnRegisterLink;

    private Button btnLogin;

    private View view;

    private GoogleSignInButton btnGoogleLogin;

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_login, parent, false);

        btnGoogleLogin = view.findViewById(R.id.btn_google_login);

        btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AccountActivity) getActivity()).openGoogleSignIn();
            }

        });

        btnRegisterLink = view.findViewById(R.id.btn_register_link);

        btnRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSignUp();
            }
        });

        btnLogin = view.findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        return view;
    }

    public void login() {

        try {

            User user = new User();

            EditText etPassword = view.findViewById(R.id.et_password);

            EditText etUserName = view.findViewById(R.id.et_user_name);

            user.setPassword(etPassword.getText().toString());

            user.setUserName(etUserName.getText().toString());

            try {

                JSONObject jsonObject = new JSONObject();

                jsonObject.put("password", user.getPassword());
                jsonObject.put("userName", user.getUserName());

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                        Request.Method.POST, DefaultSettings.URL + DefaultSettings.URL_USER_LOGIN, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {

                                    JSONObject responseObject = response;

                                    ApiResponse apiResponse = new ApiResponse();

                                    apiResponse.setAccessToken(responseObject.getString("accessToken"));

                                    apiResponse.setAccessTokenExpiryDate(responseObject.getString("accessTokenExpiryDate"));

                                    if (apiResponse.getAccessToken() != null && !apiResponse.getAccessToken().isEmpty()) {

                                        user.setAccessToken(apiResponse.getAccessToken());

                                        Toast.makeText(getContext(), "Login Successful!", Toast.LENGTH_LONG).show();

                                        DBContext dbContext = new DBContext(getContext());

                                        SQLiteDatabase sqLiteDatabase = dbContext.getWritableDatabase();

                                        sqLiteDatabase.delete(DefaultSettings.LOGINS_TABLE, null, null);

                                        ContentValues contentValues = new ContentValues();

                                        contentValues.put("user_id", user.getId());

                                        contentValues.put("user_name", user.getUserName());

                                        contentValues.put("password", user.getPassword());

                                        contentValues.put("token", user.getAccessToken());

                                        contentValues.put("token_expiry", apiResponse.getAccessTokenExpiryDate());

                                        Long recId = sqLiteDatabase.insert(DefaultSettings.LOGINS_TABLE, null, contentValues);

                                        if (recId > 0)
                                            goToApplication();

                                    } else {

                                        Toast.makeText(getContext(), "Invalid login attempt", Toast.LENGTH_LONG).show();

                                    }

                                } catch (JSONException e) {
                                    Toast.makeText(getContext(), "Invalid login attempt", Toast.LENGTH_LONG).show();
                                }

                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Invalid login attempt", Toast.LENGTH_LONG).show();

                    }
                }) {

                    /**
                     * Passing some request headers
                     */
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        return headers;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(getContext());

                requestQueue.add(jsonObjReq);

            } catch (Exception ex) {
                Toast.makeText(getContext(), "Invalid login attempt", Toast.LENGTH_LONG).show();
            }

        } catch (Exception ex) {
            Toast.makeText(getContext(), "Invalid login attempt", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void openSignUp() {
        ((AccountActivity) getActivity()).openSignUp();
    }

    public void openForgotPassword() {
        ((AccountActivity) getActivity()).openForgotPassword();
    }

    public void goToApplication() {

        Intent intent = new Intent(getActivity(), MainActivity.class);

        startActivity(intent);
    }

}

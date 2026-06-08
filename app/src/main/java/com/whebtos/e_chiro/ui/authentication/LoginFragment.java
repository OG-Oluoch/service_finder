package com.whebtos.e_chiro.ui.authentication;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.hardware.lights.LightsManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
//import com.shobhitpuri.custombuttons.GoogleSignInButton;
import com.google.android.gms.common.SignInButton;
import com.whebtos.e_chiro.MainActivity;
import com.whebtos.e_chiro.R;
import com.whebtos.e_chiro.models.ApiResponse;
import com.whebtos.e_chiro.ui.addservice.AddServiceFragment;
import com.whebtos.e_chiro.ui.clienthome.ListItem;
import com.whebtos.e_chiro.ui.clienthome.ServiceSelected;
import com.whebtos.e_chiro.ui.clientservices.ClientServicesFragment;
import com.whebtos.e_chiro.utils.DBContext;
import com.whebtos.e_chiro.utils.DBHelper;
import com.whebtos.e_chiro.utils.DatabaseHelper;
import com.whebtos.e_chiro.utils.DefaultSettings;
import com.whebtos.e_chiro.utils.UserDBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class LoginFragment extends Fragment {

    private Button btnRegisterLink;

    private Button btnLogin;

    private View view;

    private SignInButton btnGoogleLogin;

    private Button forgotPassword;

    private EditText editTextEmail;
    private EditText editTextPassword;

    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    private List<User> user_details;

    private ArrayList userDetails;

    private ArrayAdapter<String> userAdapter;

    private String user_ID;
    private String user_Name;
    private String user_Email;
    private String user_PhoneNumber;

    private String clientType;

    public static String user_url;

    private UserDBHelper databaseHelper;

    private TextInputLayout textInputLayoutPassword;

    private static final String PREFS_NAME = "theme_prefs";


    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_login, parent, false);


        textInputLayoutPassword = view.findViewById(R.id.passwordInputLayout);


        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);


        Drawable toggleDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_custom_password_toggle);


        textInputLayoutPassword.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
        textInputLayoutPassword.setEndIconDrawable(toggleDrawable);
        textInputLayoutPassword.setEndIconContentDescription("Toggle password visibility");

        // Check your condition here
        if (conditionIsMet()) {
            // If the condition is met, go directly to the main activity
            startMainActivity();
        } else {


            editTextEmail = view.findViewById(R.id.et_user_name);

            editTextPassword = view.findViewById(R.id.et_login_password);

            progressBar = view.findViewById(R.id.progressbarLogin);

            user_details = new ArrayList<>();

            userAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, userDetails);

            databaseHelper = new UserDBHelper(getContext());

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

            forgotPassword = view.findViewById(R.id.forgotPasswordButton);
            forgotPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openForgotPassword();
                }
            });

            btnLogin = view.findViewById(R.id.btn_login);
            btnLogin.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.main_logo_color));


            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //userLogin();
                    //login();
                    //goToApplication();

                    if (NotificationManagerCompat.from(getContext()).areNotificationsEnabled()) {
                        // Notifications are enabled, continue with your work
                        FirebaseMessaging.getInstance().getToken()
                                .addOnCompleteListener(new OnCompleteListener<String>() {
                                    @Override
                                    public void onComplete(@NonNull Task<String> task) {
                                        if (!task.isSuccessful()) {
                                            Log.w("Error Message", "Fetching FCM registration token failed", task.getException());
                                            return;
                                        }

                                        String token = task.getResult();
                                        //Toast.makeText(getContext(), "Token:"+token, Toast.LENGTH_SHORT).show();
                                        // Continue with your logic here
                                    }
                                });
                    } else {
                        // Notifications are not enabled, show a dialog to guide the user to the settings
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Enable Notifications");
                        builder.setMessage("Kindly enable notifications for efficient use of the app.");

                        builder.setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");

                                // For Android 11 and above, you need to specify the app package
                                intent.putExtra("android.provider.extra.APP_PACKAGE", getContext().getPackageName());

                                startActivity(intent);
                            }
                        });

                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Handle the cancel action if needed
                            }
                        });

                        builder.show();
                    }


                    String email = editTextEmail.getText().toString().trim();
                    String password = editTextPassword.getText().toString().trim();

                    if (email.isEmpty()) {
                        editTextEmail.setError("Email is required");
                        editTextEmail.requestFocus();
                        return;
                    }
                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        editTextEmail.setError("Please provide a valid email");
                        editTextEmail.requestFocus();
                        return;
                    }
                    if (password.isEmpty()) {
                        editTextPassword.setError("Password is required");
                        editTextPassword.requestFocus();
                        return;
                    }
                    if (password.length() < 8) {
                        editTextPassword.setError("The minimum password length should be 8 characters and contain special characters e.g:Abb7tyn!s");
                        editTextPassword.requestFocus();
                        return;
                    }

                    new LoginTask().execute(email, password);
                }
            });
        }
        return view;
    }

    private class LoginTask extends AsyncTask<String, Void, Integer> {
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        protected Integer doInBackground(String... credentials) {
            // Check if notifications are enabled
            if (!NotificationManagerCompat.from(getContext()).areNotificationsEnabled()) {
                return -1; // Handle the case when notifications are not enabled
            }

            // Get FCM token
            Task<String> getTokenTask = FirebaseMessaging.getInstance().getToken();

            try {
                Tasks.await(getTokenTask); // Wait for the task to complete
                String token = getTokenTask.getResult();

                if (token == null) {
                    Log.w("Error Message", "Fetching FCM registration token failed");
                    return -1; // Handle the case when token retrieval fails
                }

                // String urlString = "https://srv19716098.ultasrv.net/whebtos/api/Whebtosuser/" + credentials[0] + "/" + credentials[1] + "?token=" + token;
                String urlString = "https://backend.wencetechnologies.com/whebtos/api/Whebtosuser/" + credentials[0] + "/" + credentials[1] + "/" + "?fcmToken=" + token;

                //https://srv19716098.ultasrv.net/whebtos/api/Whebtosuser/wnabiswa@esriea.com/Arc10.2_ws/{token}?fcmToken=cBHmuJUYQgWv2i65_XnoOy%3AAPA91bH7MDoZ5DAQi2_4r_eIRWoMypD2EdaedMUOxkWveh8rjA2AHa4iL5bYHxbBciqGgvQ7UgCCbH1tY5_CyRF_ypI0RUyBS6kQpxMGU7ELfgUl8mF0HgIBt-FihNGOYrRpiPqMQH8f
                user_url = urlString;
                //Toast.makeText(getContext(), ""+urlString, Toast.LENGTH_SHORT).show();

                try {
                   URL url = new URL(urlString);
                   HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                   urlConnection.setRequestMethod("GET");
                   urlConnection.connect();

                    StringRequest stringRequest = new StringRequest(Request.Method.GET, urlString,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    //Toast.makeText(getContext(), "Response: " + response, Toast.LENGTH_LONG).show();
                                    Log.d("API_RESPONSE", response);


                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        user_ID = jsonObject.getString("id");
                                        user_Name = jsonObject.getString("userName");
                                        user_Email = jsonObject.getString("email");
                                        user_PhoneNumber = jsonObject.getString("phoneNumber");
                                        clientType = jsonObject.getString("userType");

                                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putBoolean("isLoggedIn", true);
                                        editor.putString("user_ID", user_ID);
                                        editor.putString("user_Name", user_Name);
                                        editor.putString("user_Email", user_Email);
                                        editor.putString("user_Phone", user_PhoneNumber);
                                        editor.putString("client_type",clientType);
                                        editor.apply();

                                        try {
                                            // Open the database for writing
                                            SQLiteDatabase db = databaseHelper.getWritableDatabase();

                                            String selection = UserDBHelper.COLUMN_USERID + " = ? AND " + UserDBHelper.COLUMN_USERNAME + " = ?";
                                            String[] selectionArgs = {user_ID, user_Name};

                                            Cursor cursor = db.query(
                                                    UserDBHelper.TABLE_USER_TOKEN,
                                                    null,
                                                    selection,
                                                    selectionArgs,
                                                    null,
                                                    null,
                                                    null
                                            );

                                            if (cursor != null && cursor.getCount() > 0) {
                                                // Data already exists, perform update if needed
                                                // Update code here...
                                            } else {
                                                ContentValues values = new ContentValues();
                                                values.put(UserDBHelper.COLUMN_USERID, user_ID);
                                                values.put(UserDBHelper.COLUMN_USERNAME, user_Name);

                                                // Check if the combination of user_ID and user_Name already exists before inserting
                                                Cursor duplicateCheckCursor = db.query(
                                                        UserDBHelper.TABLE_USER_TOKEN,
                                                        null,
                                                        UserDBHelper.COLUMN_USERID + " = ? OR " + UserDBHelper.COLUMN_USERNAME + " = ?",
                                                        new String[]{user_ID, user_Name},
                                                        null,
                                                        null,
                                                        null
                                                );

                                                if (duplicateCheckCursor != null && duplicateCheckCursor.getCount() == 0) {
                                                    // Data does not exist, proceed with insertion
                                                    long newRowId = db.insert(UserDBHelper.TABLE_USER_TOKEN, null, values);

                                                    // Check if the data was inserted successfully
                                                    if (newRowId != -1) {
                                                        // Data inserted successfully
                                                        Toast.makeText(getContext(), "Your Data has been added successfully", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        // Error occurred while inserting data
                                                        String error = String.valueOf(Log.e("DB_INSERT_ERROR", "Error occurred while inserting data"));
                                                        Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    // Data already exists, handle accordingly (maybe update or ignore)
                                                }

                                                // Close the duplicateCheckCursor
                                                if (duplicateCheckCursor != null) {
                                                    duplicateCheckCursor.close();
                                                }
                                            }

                                            // Close cursor and database
                                            if (cursor != null) {
                                                cursor.close();
                                            }
                                            db.close();

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Toast.makeText(getContext(), "" + e.toString(), Toast.LENGTH_SHORT).show();
                                        }

                                        //Toast.makeText(getContext(), "UserID: " + user_ID, Toast.LENGTH_SHORT).show();

                                        // Create a bundle and set user ID
                                        Bundle bundle = new Bundle();
                                        bundle.putString("userIDfromLogin", user_ID);

                                        // Launch the client services fragment
                                        ClientServicesFragment clientServicesFragment = new ClientServicesFragment();
                                        clientServicesFragment.setArguments(bundle);
                                        goToApplication();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        //Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                        //Toast.makeText(getContext(), "Failed to log in, please check your credentials / email confirmation", Toast.LENGTH_LONG).show();
                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // Handle error
                                    //Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                    //Toast.makeText(getContext(), "Failed to log in, please check your credentials / email confirmation", Toast.LENGTH_LONG).show();

                                }
                            }
                    );

                    RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                    requestQueue.add(stringRequest);

                    return urlConnection.getResponseCode();
                } catch (IOException e) {
                    e.printStackTrace();
                    return -1;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return -1; // Handle any exceptions that occur while waiting for the task
            }
        }

        protected void onPostExecute(Integer responseCode) {
            if (responseCode == 200) {
                // Login successful
                //goToApplication();
                progressBar.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.GONE);
                // Login failed
                Toast.makeText(getContext(), "Failed to log in, please check your credentials / email confirmation", Toast.LENGTH_LONG).show();
            }
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

        intent.putExtra("user_ID",user_ID);
        intent.putExtra("user_Name",user_Name);
        intent.putExtra("user_Email",user_Email);
        intent.putExtra("user_phone_Number",user_PhoneNumber);
        intent.putExtra("client_type",clientType);

        startActivity(intent);

        if (getActivity() != null) {
            getActivity().finish();
        }

       // startActivity(intent);
    }
    private boolean conditionIsMet() {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }


    private void startMainActivity() {

        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra("user_ID",user_ID);
        intent.putExtra("user_Name",user_Name);
        intent.putExtra("user_Email",user_Email);
        intent.putExtra("user_phone_Number",user_PhoneNumber);
        intent.putExtra("client_type",clientType);

        startActivity(intent);
        if (getActivity() != null) {
            getActivity().finish();
        }
    }
}

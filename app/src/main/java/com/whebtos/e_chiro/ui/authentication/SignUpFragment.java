package com.whebtos.e_chiro.ui.authentication;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.whebtos.e_chiro.MainActivity;
import com.whebtos.e_chiro.R;
import com.whebtos.e_chiro.models.ApiResponse;
import com.whebtos.e_chiro.models.User;
import com.whebtos.e_chiro.ui.user.LoginActivity;
import com.whebtos.e_chiro.utils.DatabaseHelper;
import com.whebtos.e_chiro.utils.DefaultSettings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;


public class SignUpFragment extends Fragment {

    private Button btnSignUp;
    private Button btnBackToLogin;

    private View view;

    private FirebaseAuth mAuth;

    private EditText editTextName;
    private EditText editTextPhoneNumber;
    private EditText editTextEmailAddress;
    private EditText editTextUserName;
    private EditText editTextPassword;
    private ProgressBar progressBar;
    private Spinner client_type_spinner;

    private static final String register_user_url = "https://backend.wencetechnologies.com/whebtos/api/Whebtosuser/";

    private String user_id;

    private DatabaseHelper databaseHelper;

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.(com|org|net|edu|gov|co\\.ke|ac\\.ke)$"
    );



    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_sign_up, parent, false);

        databaseHelper = new DatabaseHelper(getContext());

        mAuth = FirebaseAuth.getInstance();

        editTextName = view.findViewById(R.id.et_name);
        editTextPhoneNumber = view.findViewById(R.id.et_phone_number);
        editTextEmailAddress = view.findViewById(R.id.et_email_address);
        editTextUserName = view.findViewById(R.id.et_user_name);
        editTextPassword = view.findViewById(R.id.et_password);
        progressBar = view.findViewById(R.id.progressbar);

        btnSignUp = view.findViewById(R.id.btn_register);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();

                //Toast.makeText(getContext(), user_id, Toast.LENGTH_SHORT).show();

                //registerUser();
            }
        });

        btnBackToLogin = view.findViewById(R.id.btn_back_to_login);
        btnBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AccountActivity) getActivity()).openLogin();
            }
        });

        client_type_spinner = view.findViewById(R.id.client_type_spinner);
        // Create an ArrayAdapter using a string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.choices_array,  // Define an array of choices in strings.xml
                android.R.layout.simple_spinner_item
        );

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        client_type_spinner.setAdapter(adapter);

        // Set a listener for item selection
        client_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedItem = parentView.getItemAtPosition(position).toString();
                // Here, you can perform actions based on the selected choice (Service Provider or Client).
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle case when nothing is selected.
            }
        });


        if(NotificationManagerCompat.from(getContext()).areNotificationsEnabled())
        {
            //Do your Work

            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()) {
                                Log.w("Error Message", "Fetching FCM registration token failed", task.getException());
                                return;
                            }

                            // Get new FCM registration token
                            String token = task.getResult();
                            //editTextName.setText(token);

                            // Log and toast
                            //String msg = getString(R.string.msg_token_fmt, token);
                            //Log.d("Success Message", msg);
                            //Toast.makeText(getContext(), token, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else
        {
            //Ask for permission

            Toast.makeText(getContext(), "Kindly allow Notifications for efficient use of the app", Toast.LENGTH_SHORT).show();
        }


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }

    private boolean isValidKenyanPhone(String phone) {
        // Local format: 07xxxxxxxx OR 01xxxxxxxx (10 digits total)
        String localPattern = "^(07\\d{8}|01\\d{8})$";

        // International format: +2547xxxxxxxx OR +2541xxxxxxxx
        String intlPattern = "^(\\+2547\\d{8}|\\+2541\\d{8})$";

        return phone.matches(localPattern) || phone.matches(intlPattern);
    }


    public void openLogin() {
        ((AccountActivity) getActivity()).openLogin();
    }

    private boolean isValidEmail(String email) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return false;
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return false;
        }
        // Additional check for common email provider typos
        String domain = email.substring(email.indexOf("@") + 1).toLowerCase();
        if (domain.equals("gmail.co") || domain.equals("yahoo.co") || domain.equals("hotmail.co")) {
            return false;
        }
        return true;
    }

    public void goToApplication() {

        Intent intent = new Intent(getActivity(), MainActivity.class);

        startActivity(intent);
    }


    public void register(){

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>()
                {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Error Message", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        String token = task.getResult();



                        String name = editTextName.getText().toString().trim();
                        String phone = editTextPhoneNumber.getText().toString().trim();
                        String email = editTextEmailAddress.getText().toString().trim();
                        String username = editTextUserName.getText().toString().trim();
                        String password = editTextPassword.getText().toString().trim();
                        String ClientType = client_type_spinner.getSelectedItem().toString();

                        if (name.isEmpty())
                        {
                            editTextName.setError("Full Name is required");
                            editTextName.requestFocus();
                            return;
                        }
                        if (phone.isEmpty())
                        {
                            editTextPhoneNumber.setError("Phone Number is required");
                            editTextPhoneNumber.requestFocus();
                            return;
                        }
                        if (!isValidKenyanPhone(phone)) {
                            editTextPhoneNumber.setError("Enter a valid Kenyan phone (e.g. 0712345678 or +254712345678)");
                            editTextPhoneNumber.requestFocus();
                            return;
                        }

                        if (email.isEmpty())
                        {
                            editTextEmailAddress.setError("Email is required");
                            editTextEmailAddress.requestFocus();
                            return;
                        }
                        if (!isValidEmail(email)) {
                            editTextEmailAddress.setError("Please provide a valid email (e.g., example@gmail.com)");
                            editTextEmailAddress.requestFocus();
                            return;
                        }
                        if (username.isEmpty())
                        {
                            editTextUserName.setError("Username is required");
                            editTextUserName.requestFocus();
                            return;
                        }
                        if (password.isEmpty())
                        {
                            editTextPassword.setError("Password is required");
                            editTextPassword.requestFocus();
                            return;
                        }
                        if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$"))
                        {
                            editTextPassword.setError("Password must be 8+ characters with at least one number, uppercase, lowercase, and special character");
                            editTextPassword.requestFocus();
                            return;
                        }
                        if (ClientType.isEmpty()) {
                            client_type_spinner.requestFocus();
                            Toast.makeText(getContext(), "Please make a selection", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Assuming you have selectedClientType as the selected choice
                        String selectedClientType = client_type_spinner.getSelectedItem().toString();


                        // Open the database for writing
                        SQLiteDatabase db = databaseHelper.getWritableDatabase();

                        ContentValues values = new ContentValues();
                        values.put(DatabaseHelper.COLUMN_CLIENT_TYPE, selectedClientType);

                        long newRowId = db.insert(DatabaseHelper.TABLE_CLIENT, null, values);
                        db.close();

                        // Check if the data was inserted successfully
                        if (newRowId != -1) {
                            // Data inserted successfully
                            //Toast.makeText(getContext(), "You have successfully selected "+selectedClientType, Toast.LENGTH_SHORT).show();
                        } else {
                            // Error occurred while inserting data
                        }


                        try {
                            progressBar.setVisibility(View.VISIBLE);
                            //https://whebtosdemoserver.eastasia.cloudapp.azure.com/Images/BIC.jpg
                            //https://whebtosdemoserver.eastasia.cloudapp.azure.com/Images/electrician_icon.jpg
                            JSONObject jsonBody = new JSONObject();
                            jsonBody.put("userName", email);
                            jsonBody.put("email", email);
                            jsonBody.put("emailConfirmed", 0);
                            jsonBody.put("passwordHash", password);
                            jsonBody.put("userType",ClientType);
                            jsonBody.put("fcmToken",token);

                            final String requestBody = jsonBody.toString();
                            //Toast.makeText(getContext(), "Here is the Body \n"+requestBody, Toast.LENGTH_LONG).show();

                            StringRequest stringRequest = new StringRequest(Request.Method.POST, register_user_url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {

                                       // Toast.makeText(getContext(), "Sorry \n"+String.valueOf(response)+"\nPlease Try again or contact us", Toast.LENGTH_SHORT).show();
                                        JSONObject responseObject = new JSONObject(response);

                                        //user_id = responseObject.get("id").toString();
                                        if (responseObject != null && responseObject.getString("id") != null) {
                                            String fieldValue = responseObject.getString("id");
                                            //Toast.makeText(getContext(), "Field Value: "+fieldValue, Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);

                                            new AlertDialog.Builder(getContext())
                                                    .setTitle("Registration Successful")
                                                    .setMessage("You have been registered successfully.\n\nKindly ensure you confirm your email before trying to log in.")
                                                    .setCancelable(false) // Prevent dismiss by tapping outside
                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            openLogin(); // Go to login screen after clicking OK
                                                        }
                                                    })
                                                    .show();

                                            // Use the field value
                                        } else {
                                            // Handle null values
                                            progressBar.setVisibility(View.GONE);
                                        }
                                        progressBar.setVisibility(View.GONE);
                                        ApiResponse apiResponse = new ApiResponse();

                                        apiResponse.setStatus(responseObject.getString("status"));

                                        apiResponse.setMessage(responseObject.getString("Message"));


                                        if (apiResponse.getStatus().equals("Success")) {

                                            Toast.makeText(getContext(), apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                                            Toast.makeText(getContext(), "You have been registered successfully", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                            //openLogin();

                                        } else {
                                            Toast.makeText(getContext(), "Registration has failed, please try again or contact us", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                            //progressBar.setVisibility(View.GONE);
                                            //Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();

                                        }

                                    } catch (JSONException e) {
                                        progressBar.setVisibility(View.GONE);
                                        //Toast.makeText(getContext(), "Something went wrong: "+e, Toast.LENGTH_LONG).show();
                                    }

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    //Toast.makeText(getContext(),"Something went Wrong: \n"+error,Toast.LENGTH_LONG).show();

                                    if (error instanceof NetworkError) {
                                    } else if (error instanceof ServerError) {
                                        //Toast.makeText(getContext(),"Server Error: \n"+error,Toast.LENGTH_LONG).show();
                                        Toast.makeText(getContext(),"The minimum password length should be 8 characters and contain special characters e.g:Abb7tyn!s",Toast.LENGTH_LONG).show();

                                    } else if (error instanceof AuthFailureError) {
                                        Toast.makeText(getContext(),"Authentication Error: \n"+error,Toast.LENGTH_LONG).show();

                                    } else if (error instanceof ParseError) {
                                        Toast.makeText(getContext(),"Parsing Error: \n"+error,Toast.LENGTH_LONG).show();

                                    } else if (error instanceof NoConnectionError) {
                                        Toast.makeText(getContext(),"Connection Error: \n"+error,Toast.LENGTH_LONG).show();

                                    } else if (error instanceof TimeoutError) {
                                        Toast.makeText(getContext(), "Timeout error: \n"+error, Toast.LENGTH_LONG).show();
                                    }

                                }
                            }) {

                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("Content-Type", "application/json; charset=UTF-8");
                                    //params.put("Authorization", "Bearer " + user.getAccessToken());
                                    return params;
                                }

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
//            progressBar.setVisibility(View.GONE);

                            RequestQueue requestQueue = Volley.newRequestQueue(getContext());

                            //stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                            requestQueue.add(stringRequest);


                        } catch (Exception ex) {

                        }


                    }
                });


    }


}

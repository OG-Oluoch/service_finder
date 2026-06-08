package com.whebtos.e_chiro.ui.authentication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.whebtos.e_chiro.R;
import com.whebtos.e_chiro.models.ApiResponse;
import com.whebtos.e_chiro.ui.clientservices.ClientServicesFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class ForgotPasswordFragment extends Fragment {

    private View view;

    private EditText emailEditText;
    private EditText passEditText;

    private Button btnReset;
    private Button btnBackToLogin;

    private ProgressBar progressBar;
    public static String user_url;

    FirebaseAuth auth;

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_forgot_password, parent, false);

        emailEditText = view.findViewById(R.id.forgotPasswordEmail);
        passEditText = view.findViewById(R.id.forgotPasswordPasswordField);
        btnReset = view.findViewById(R.id.btn_forgotPasswordEmail);
        progressBar = view.findViewById(R.id.progressbarForgotPassword);

        auth = FirebaseAuth.getInstance();

        btnBackToLogin = view.findViewById(R.id.btn_back_to_login);

        btnBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogin();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //resetPass();
                String email = emailEditText.getText().toString().trim();
                String password = passEditText.getText().toString().trim();

                if (email.isEmpty())
                {
                    emailEditText.setError("Email is required");
                    emailEditText.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    emailEditText.setError("Please provide a valid email");
                    emailEditText.requestFocus();
                    return;
                }
                if (password.isEmpty())
                {
                    passEditText.setError("password is required");
                    passEditText.requestFocus();
                    return;
                }
                if (password.length()<6)
                {
                    passEditText.setError("The minimum password length should be 8 characters and contain special characters e.g:Abb7tyn!s");
                    passEditText.requestFocus();
                    return;
                }

                new resetPassTask().execute(email, password);
            }
        });

        return view;
    }

    private class resetPassTask extends AsyncTask<String, Void, Integer> {
        protected Integer doInBackground(String... credentials) {
            try {
                // Construct the URL with query parameters
                String urlString = "https://srv19716098.ultasrv.net/whebtos/api/Whebtosuser/PasswordReset" +
                        "?emailSender=" + credentials[0] + "&Password=" + credentials[1];

                URL url = new URL(urlString);

                // Send a POST request (even though the Swagger feedback suggests POST, it's treated as a GET request here)
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");  // Although it's a POST request, it's treated as a GET by the server
                urlConnection.setDoOutput(true);

                // Get the response code
                int responseCode = urlConnection.getResponseCode();

                return responseCode;
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }
        }

        protected void onPostExecute(Integer responseCode) {
            if (responseCode == 200) {
                // Password reset successful
                Toast.makeText(getContext(), "You have successfully changed your password", Toast.LENGTH_SHORT).show();
                openLogin();
            } else {
                // Password reset failed
                Toast.makeText(getContext(), "Failed to change password. Ensure the password is strong enough e.g. Arc10.1_ws", Toast.LENGTH_LONG).show();
            }
        }
    }


    public void openLogin() {
        ((AccountActivity) getActivity()).openLogin();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

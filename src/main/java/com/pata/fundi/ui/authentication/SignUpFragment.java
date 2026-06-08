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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.whebtos.e_chiro.MainActivity;
import com.whebtos.e_chiro.R;
import com.whebtos.e_chiro.models.ApiResponse;
import com.whebtos.e_chiro.models.User;
import com.whebtos.e_chiro.utils.DBContext;
import com.whebtos.e_chiro.utils.DefaultSettings;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class SignUpFragment extends Fragment {

    private Button btnSignUp;

    private View view;

    private SharedPreferences sharedPreferences;

    private SharedPreferences.Editor spEditor;

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_sign_up, parent, false);

        btnSignUp = view.findViewById(R.id.btn_register);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        sharedPreferences = getActivity().getPreferences(MODE_PRIVATE);

        spEditor = sharedPreferences.edit();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void openLogin() {
        ((AccountActivity) getActivity()).openLogin();
    }

    public void goToApplication() {

        Intent intent = new Intent(getActivity(), MainActivity.class);

        startActivity(intent);
    }

    public void register() {

        try {

            User user = new User();

            EditText etFirstName = view.findViewById(R.id.et_first_name);

            EditText etLastName = view.findViewById(R.id.et_last_name);

            EditText etPhoneNumber = view.findViewById(R.id.et_phone_number);

            EditText etEmailAddress = view.findViewById(R.id.et_email_address);

            EditText etPassword = view.findViewById(R.id.et_password);

            EditText etUserName = view.findViewById(R.id.et_user_name);

            EditText etIdNumber = view.findViewById(R.id.et_id_number);

            CheckBox cbIsServiceProvider = view.findViewById(R.id.cb_is_service_provider);

            EditText etConfirmPassword = view.findViewById(R.id.et_confirm_password);

            user.setName(etFirstName.getText().toString() + " " + etLastName.getText().toString());

            user.setPhoneNumber(etPhoneNumber.getText().toString());

            user.setEmailAddress(etEmailAddress.getText().toString());

            user.setPassword(etPassword.getText().toString());

            user.setConfirmPassword(etConfirmPassword.getText().toString());

            user.setUserName(etUserName.getText().toString());

            user.setIdNumber(etIdNumber.getText().toString());

            user.setClientType(cbIsServiceProvider.isChecked() ? "ServiceProvider" : "Consumer");

            if (!user.getPassword().equals(user.getConfirmPassword())) {

                Toast.makeText(getContext(), "Password must match", Toast.LENGTH_LONG).show();

                return;
            }

            try {

                JSONObject jsonObject = new JSONObject();

                jsonObject.put("active", true);
                jsonObject.put("name", user.getName());
                jsonObject.put("phoneNumber", user.getPhoneNumber());
                jsonObject.put("emailAddress", user.getEmailAddress());
                jsonObject.put("password", user.getPassword());
                jsonObject.put("userName", user.getUserName());
                jsonObject.put("idNumber", user.getIdNumber());
                jsonObject.put("clientType", user.getClientType());

                final String requestBody = jsonObject.toString();

                Log.i("whebtos", DefaultSettings.URL + DefaultSettings.URL_REGISTER_USER);

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                        Request.Method.POST, DefaultSettings.URL + DefaultSettings.URL_REGISTER_USER, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {

                                    JSONObject responseObject = response;

                                    ApiResponse apiResponse = new ApiResponse();

                                    apiResponse.setStatus(responseObject.getString("status"));

                                    apiResponse.setMessage(responseObject.getString("message"));

                                    apiResponse.setObject(responseObject.getJSONObject("object"));

                                    if (apiResponse.getStatus().equals("Success")) {

                                        Toast.makeText(getContext(), "Registered successfully!", Toast.LENGTH_LONG).show();

                                        openLogin();

                                    } else {

                                        Toast.makeText(getContext(), apiResponse.getMessage(), Toast.LENGTH_LONG).show();

                                    }

                                } catch (JSONException e) {
                                    Toast.makeText(getContext(), "Registration failed. Please try again!", Toast.LENGTH_LONG).show();
                                }

                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Registration failed. Please try again!", Toast.LENGTH_LONG).show();

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
                Toast.makeText(getContext(), "Registration failed. Please try again!", Toast.LENGTH_LONG).show();
            }

        } catch (Exception ex) {
            Toast.makeText(getContext(), "Registration failed. Please try again!", Toast.LENGTH_LONG).show();
        }

    }
}

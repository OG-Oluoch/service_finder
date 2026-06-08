package com.whebtos.e_chiro.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.whebtos.e_chiro.R;
import com.whebtos.e_chiro.databinding.FragmentProfileBinding;
import com.whebtos.e_chiro.models.User;
import com.whebtos.e_chiro.utils.DefaultSettings;
import com.whebtos.e_chiro.utils.SecureHttpRequest;
import com.whebtos.e_chiro.utils.TokenUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment{

    private FragmentProfileBinding binding;

    private  TextView profileUserName;

    private TextView profileName;

    private TextView profilePhoneNumber;

    private TextView profileEmailAddress;

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    private static final String PREFS_NAME = "theme_prefs";

    private Button logout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView profileUserName = root.findViewById(R.id.profileUserName);
        final TextView profileName = root.findViewById(R.id.profileFullName);
        final TextView profilePhoneNumber = root.findViewById(R.id.profilePhoneNumber);
        final TextView profileEmailAddress = root.findViewById(R.id.profileEmailAddress);

        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String name = prefs.getString("user_Name", "N/A");
        String phone = prefs.getString("user_Phone", "N/A");
        String email = prefs.getString("user_Email", "N/A");

        profileName.setText(name);
        profilePhoneNumber.setText(phone);
        profileEmailAddress.setText(email);



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

//    public void request(User user) {
//
//        String token = user.getAccessToken();
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, DefaultSettings.URL + DefaultSettings.URL_USER_BY_USERNAME + "/" + user.getUserName(), new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                try {
//
//                    JSONObject jsonObject = new JSONObject(response);
//
//                    user.setPhoneNumber(jsonObject.getString("phoneNumber"));
//
//                    user.setName(jsonObject.getString("name"));
//
//                    user.setEmailAddress(jsonObject.getString("emailAddress"));
//
//                    profileName.setText(user.getName() == null ? "" : user.getName());
//
//                    profilePhoneNumber.setText(user.getPhoneNumber() == null ? "" : user.getPhoneNumber());
//
//                    profileEmailAddress.setText(user.getEmailAddress() == null ? "" : user.getEmailAddress());
//
//                } catch (Exception ex) {
//
//
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Content-Type", "application/json; charset=UTF-8");
//                params.put("Authorization", "Bearer " + token);
//                return params;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());
//
//        requestQueue.add(stringRequest);
//
//
//    }
//
//    public void request2(User user) {
//    }
}
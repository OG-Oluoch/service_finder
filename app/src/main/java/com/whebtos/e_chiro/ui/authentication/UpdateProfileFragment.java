package com.whebtos.e_chiro.ui.authentication;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.Exclude;
import com.whebtos.e_chiro.R;

import java.util.HashMap;
import java.util.Map;

public class UpdateProfileFragment extends Fragment {

    private UpdateProfileViewModel mViewModel;

    private View view;

    private FirebaseAuth mAuth;

    private EditText editTextName;
    private EditText editTextPhoneNumber;
    private EditText editTextEmailAddress;
    private EditText editTextUserName;
    private ProgressBar progressBar;

    public static UpdateProfileFragment newInstance() {
        return new UpdateProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_update_profile, container, false);


        mAuth = FirebaseAuth.getInstance();

        editTextName = view.findViewById(R.id.et_name);
        editTextPhoneNumber = view.findViewById(R.id.et_phone_number);
        editTextEmailAddress = view.findViewById(R.id.et_email_address);
        editTextUserName = view.findViewById(R.id.et_user_name);
        progressBar = view.findViewById(R.id.progressbar);




        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(UpdateProfileViewModel.class);
        // TODO: Use the ViewModel
    }

    public class User {
        // ... your class props and constructors here

        String name = editTextName.getText().toString().trim();
        String phone = editTextPhoneNumber.getText().toString().trim();
        String email = editTextEmailAddress.getText().toString().trim();
        String username = editTextUserName.getText().toString().trim();

        // add this method
        @Exclude
        public Map<String, Object> toMap() {
            HashMap<String, Object> result = new HashMap<>();
            if(name != null)
                result.put("name", name);
            if(phone != null)
                result.put("phone", phone);
            if(email != null)
                result.put("email", email);
            if(username != null)
                result.put("username", username);
//            if(uriImage != null)
//                result.put("uriImage", uriImage);

            return result;
        }
    }

}
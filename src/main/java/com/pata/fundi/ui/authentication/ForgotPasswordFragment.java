package com.whebtos.e_chiro.ui.authentication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.whebtos.e_chiro.R;

public class ForgotPasswordFragment extends Fragment {

    private View view;

    private Button btnLoginLink;

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_forgot_password, parent, false);

        btnLoginLink = view.findViewById(R.id.btn_login_link);

        btnLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLogin();
            }
        });

        return view;
    }

    public void openLogin() {
        ((AccountActivity) getActivity()).openLogin();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

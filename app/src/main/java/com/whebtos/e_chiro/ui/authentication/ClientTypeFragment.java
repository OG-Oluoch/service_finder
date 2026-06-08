package com.whebtos.e_chiro.ui.authentication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.whebtos.e_chiro.R;

public class ClientTypeFragment extends Fragment {

    private View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_client_type, parent, false);

        return view;
    }
}

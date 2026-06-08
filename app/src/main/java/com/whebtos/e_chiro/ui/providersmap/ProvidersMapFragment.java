package com.whebtos.e_chiro.ui.providersmap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.whebtos.e_chiro.R;

public class ProvidersMapFragment  extends Fragment{

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_providers_map, parent, false);

        return root;
    }
}

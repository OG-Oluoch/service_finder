package com.whebtos.e_chiro.ui.serviceproviderhome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.whebtos.e_chiro.MainActivity;
import com.whebtos.e_chiro.R;

public class ServiceProviderHomeFragment extends Fragment {

    private Button btnAddService;

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_service_provider_home, parent, false);

        btnAddService=root.findViewById(R.id.btn_add_service);

        btnAddService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addService();
            }
        });

        return root;
    }

    public void addService(){
        ((MainActivity) getActivity()).openAddService();
    }

}

package com.whebtos.e_chiro.ui.requests;

import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.whebtos.e_chiro.R;
import com.whebtos.e_chiro.ui.authentication.LoginFragment;
import com.whebtos.e_chiro.ui.clientservices.ClientService;
import com.whebtos.e_chiro.ui.clientservices.ClientServicesRecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ServiceProviderRequestsFragment extends Fragment {

    private ServiceProviderRequestsViewModel mViewModel;

    private String userName;
    private String userEmail;
    private String userPhone;
    private String userID;

    private static final String PREFS_NAME = "theme_prefs";

    private RecyclerView recyclerView;

    private RequestsRecyclerViewAdapter adapter;

    private List<com.whebtos.e_chiro.ui.requests.Request> list;

    public static ServiceProviderRequestsFragment newInstance() {
        return new ServiceProviderRequestsFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_service_provider_requests, parent, false);


        recyclerView = root.findViewById(R.id.recyclerViewRequestsFrmClients);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        list = new ArrayList<>();
        adapter = new RequestsRecyclerViewAdapter(getContext(), list);
        //recyclerView.setAdapter(adapter);

        getRequestsFromClients();

        return root;

    }

//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_service_provider_requests, container, false);
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        mViewModel = new ViewModelProvider(this).get(ServiceProviderRequestsViewModel.class);
//        // TODO: Use the ViewModel
//    }

    public void getRequestsFromClients()
    {
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userID = prefs.getString("user_ID", "");
        userName = prefs.getString("user_Name", "");
        userEmail = prefs.getString("user_Email", "");

        if (userID.isEmpty()) {
            Toast.makeText(getContext(), "User ID not found", Toast.LENGTH_SHORT).show();
            return;
        }

        final String myServicesURL = "https://backend.wencetechnologies.com/whebtos/api/ServiceRequest/CreatedBy/" + userID;

        if (myServicesURL.isEmpty()) {
            Toast.makeText(getContext(), "The Service Provider Requests URL has a problem", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressDialog servicesSelectedprogressDialog = new ProgressDialog(getContext());
        servicesSelectedprogressDialog.setMessage("Loading Data...");
        servicesSelectedprogressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                myServicesURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        servicesSelectedprogressDialog.dismiss();
                        try {

                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject requestObject = array.getJSONObject(i);
                                com.whebtos.e_chiro.ui.requests.Request item = new com.whebtos.e_chiro.ui.requests.Request(
                                        requestObject.getString("description"),
                                        requestObject.getString("requestLongitude"),
                                        requestObject.getString("requestLatitude")
                                );
                                list.add(item);
                            }

                            adapter = new RequestsRecyclerViewAdapter(list, getContext(), new RequestsRecyclerViewAdapter.ItemClickListener() {
                                @Override
                                public void onItemClick(com.whebtos.e_chiro.ui.requests.Request listItemClick) {
                                    String request_description = listItemClick.getServiceDescription();
                                    String request_latitude = listItemClick.getServiceLatitude();
                                    String request_longitude = listItemClick.getServiceLongitude();

                                    Toast.makeText(getContext(), "Name: " + request_description + "\nDescription: " + request_latitude + "\n Image: " + request_longitude, Toast.LENGTH_LONG).show();
                                }
                            });

                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        servicesSelectedprogressDialog.dismiss();
                        Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

}
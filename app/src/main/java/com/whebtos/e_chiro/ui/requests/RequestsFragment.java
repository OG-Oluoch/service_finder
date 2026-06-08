package com.whebtos.e_chiro.ui.requests;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.whebtos.e_chiro.R;
import com.whebtos.e_chiro.ui.authentication.LoginFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RequestsFragment extends Fragment {

    private RecyclerView recyclerView;

    private RequestsRecyclerViewAdapter adapter;

    //private List<Request> list = new ArrayList<>();

    private static final String PREFS_NAME = "theme_prefs";
    private String userName;
    private String userEmail;
    private String userPhone;
    private String userID;

    private List<com.whebtos.e_chiro.ui.requests.Request> list;

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_client_requests, parent, false);


        recyclerView = root.findViewById(R.id.recyclerViewMyRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();
        adapter = new RequestsRecyclerViewAdapter(list, getContext(), new RequestsRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(com.whebtos.e_chiro.ui.requests.Request listItemClick) {
                String request_description = listItemClick.getServiceDescription();
                String request_latitude = listItemClick.getServiceLatitude();
                String request_longitude = listItemClick.getServiceLongitude();

                Toast.makeText(getContext(), "Name: "+request_description+"\nDescription: "+request_latitude+"\n Image: "+request_longitude, Toast.LENGTH_LONG).show();

            }
        });

        recyclerView.setAdapter(adapter);

        getMyRequests();

        return root;
    }

    public void getMyRequests()
    {

        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userID = prefs.getString("user_ID", "");
        userName = prefs.getString("user_Name", "");
        userEmail = prefs.getString("user_Email", "");

        if (TextUtils.isEmpty(userID)) {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        final String myServicesURL = "https://backend.wencetechnologies.com/whebtos/api/ServiceRequest/CreatedBy/"+userID;

        if (myServicesURL.isEmpty())
        {
            Toast.makeText(getContext(), "The Service Provider Requests URL has a problem", Toast.LENGTH_SHORT).show();
        }
        else
        {

            ProgressDialog servicesSelectedprogressDialog = new ProgressDialog(getContext());
            servicesSelectedprogressDialog.setMessage("Loading Data...");
            servicesSelectedprogressDialog.show();

            StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET,
                    myServicesURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            servicesSelectedprogressDialog.dismiss();
                            try {

                                String desiredId = userID;
                                JSONArray filteredArray = new JSONArray();
                                JSONArray array = new JSONArray(response);
                                //JSONArray array = jsonObject.getJSONArray("");

                                //Toast.makeText(getContext(), "User ID: "+userID, Toast.LENGTH_SHORT).show();

                                for (int i=0; i<array.length(); i++)
                                {
                                    //Toast.makeText(getContext(), "User ID:"+userID, Toast.LENGTH_SHORT).show();
                                    JSONObject o = array.getJSONObject(i);
                                    com.whebtos.e_chiro.ui.requests.Request item = new com.whebtos.e_chiro.ui.requests.Request(o.getString("description"),o.getString("requestLongitude"), o.getString("requestLatitude"));
                                    list.add(item);

                                }
                                adapter.notifyDataSetChanged();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                //Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                                //Toast.makeText(getContext(), "Kindly Ensure you have a stable internet connectivity", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            servicesSelectedprogressDialog.dismiss();
                            String errorMessage = error.getMessage() != null ? error.getMessage() : "Network error occurred";
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
            );
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(stringRequest);
        }
        //                            Toast.makeText(getContext(),
//                                    "User ID: "+userID+"\n"
//                                            +"User Name: "+userName+"\n"
//                                            +"Email: "+userEmail+"\n"
//                                            +"Phone Number: "+userPhone
//                                    , Toast.LENGTH_LONG).show();
//                            System.out.println(userID);


        //Toast.makeText(getContext(), "Name: "+bs_name+"\nDescription: "+bs_desc+"\n Image: "+bs_img, Toast.LENGTH_SHORT).show();


    }

}
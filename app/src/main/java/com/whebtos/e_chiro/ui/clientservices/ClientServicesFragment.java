package com.whebtos.e_chiro.ui.clientservices;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import android.content.SharedPreferences;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.whebtos.e_chiro.R;
import com.whebtos.e_chiro.models.User;
import com.whebtos.e_chiro.ui.authentication.LoginFragment;
import com.whebtos.e_chiro.ui.clienthome.ListItem;
import com.whebtos.e_chiro.ui.clienthome.ServiceSelected;
import com.whebtos.e_chiro.ui.clienthome.ServicesRecyclerViewAdapter;
import com.whebtos.e_chiro.utils.DefaultSettings;
import com.whebtos.e_chiro.utils.SecureHttpRequest;
import com.whebtos.e_chiro.utils.TokenUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientServicesFragment extends Fragment implements SecureHttpRequest {

    private RecyclerView recyclerView;

    private ClientServicesRecyclerViewAdapter adapter;

    private List<ClientService> list;

    private static final String businesses_url = "https://backend.wencetechnologies.com/whebtos/api/Business/Created/";//"https://srv19716098.ultasrv.net/whebtos/api/business";

    private String userName;
    private String userEmail;
    private String userPhone;
    private String userID;

    private static final String PREFS_NAME = "theme_prefs";


    //private String myServicesURL;
    //private Bundle bundle;
    public static ClientServicesFragment newInstance() {
        return new ClientServicesFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_client_services, parent, false);

        getUserDetails();
        list = new ArrayList<>();
        recyclerView = root.findViewById(R.id.recyclerViewServices);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ClientServicesRecyclerViewAdapter(list, getContext(), new ClientServicesRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(ClientService listItemClick) {
                String bs_name = listItemClick.getServiceName();
                String bs_desc = listItemClick.getServiceDescription();
                String bs_img = listItemClick.getServiceImage();

                Toast.makeText(getContext(), "Name: "+bs_name+"\nDescription: "+bs_desc+"\n Image: "+bs_img, Toast.LENGTH_SHORT).show();


            }
        });
        recyclerView.setAdapter(adapter);


        return root;

    }

    @Override
    public void onResume() {
        super.onResume();
        getUserDetails(); // Refresh every time fragment is visible
    }


    public void filteredServices()
    {

    }

    public void getUserDetails()
    {
        // Retrieve stored user details from SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        userID = sharedPreferences.getString("user_ID", null);
        userName = sharedPreferences.getString("user_Name", null);
        userEmail = sharedPreferences.getString("user_Email", null);
        userPhone = sharedPreferences.getString("user_Phone", null);


        // Build services URL
        final String myServicesURL = businesses_url + userID;

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();

        // Fetch services for the logged-in user
        StringRequest servicesRequest = new StringRequest(Request.Method.GET, myServicesURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String servicesResponse) {
                        progressDialog.dismiss();
                        try {
                            JSONArray array = new JSONArray(servicesResponse);
                            list.clear();

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject o = array.getJSONObject(i);
                                String createdById = o.getString("createdById");

                                // Only add services created by this user
                                if (createdById.equals(userID)) {
                                    String imageUrl = o.optString("image", null);
                                    if (imageUrl.isEmpty() || imageUrl.equalsIgnoreCase("null")) {
                                        imageUrl = "https://srv19716098.ultasrv.net/whebtos/images/services_icon.jpg";
                                    }

                                    ClientService item = new ClientService(
                                            o.getString("businessname"),
                                            o.getString("businessdescription"),
                                            imageUrl
                                    );
                                    list.add(item);
                                }
                            }

                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Data parsing error", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Failed to load services", Toast.LENGTH_SHORT).show();
                    }
                });

        Volley.newRequestQueue(requireContext()).add(servicesRequest);

    }

    @Override
    public void request(User user) {

    }

    public void request2(User user) {
    }
}

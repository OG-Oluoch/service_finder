package com.whebtos.e_chiro.ui.services;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.whebtos.e_chiro.MainActivity;
import com.whebtos.e_chiro.R;
import com.whebtos.e_chiro.models.User;
import com.whebtos.e_chiro.utils.DefaultSettings;
import com.whebtos.e_chiro.utils.SecureHttpRequest;
import com.whebtos.e_chiro.utils.TokenUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServicesFragment extends Fragment implements SecureHttpRequest {

    private ArrayList<Service> serviceList;

    private RecyclerView recyclerView;

    private ServicesRecyclerViewAdapter servicesRecyclerViewAdapter;

    private String serviceCategoryId;

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_service_categories, parent, false);

        serviceList = new ArrayList<>();

        recyclerView = root.findViewById(R.id.rv_service_categories);

        recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 2));

        servicesRecyclerViewAdapter = new ServicesRecyclerViewAdapter(this, serviceList, this.getContext());

        recyclerView.setAdapter(servicesRecyclerViewAdapter);

        setServiceCategoryId(getArguments().getString("serviceCategoryId"));

        (new TokenUtil(this)).secureHttpRequest(getContext());

        return root;
    }

    public void openProvidersMap(){

        ((MainActivity)getActivity()).openProvidersMap();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void request(User user) {

        String token = user.getAccessToken();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, DefaultSettings.URL + DefaultSettings.URL_SERVICES + getServiceCategoryId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                serviceList.clear();

                try {

                    JSONArray jsonArray = new JSONArray(response);

                    for (int r = 0; r < jsonArray.length(); r++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(r);

                        Service service = new Service();

                        service.setId(jsonObject.getString("id"));

                        service.setName(jsonObject.getString("name"));

                        service.setImage(jsonObject.getString("image"));

                        serviceList.add(service);

                        servicesRecyclerViewAdapter.notifyDataSetChanged();

                    }

                } catch (Exception ex) {


                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());

        requestQueue.add(stringRequest);

    }

    public String getServiceCategoryId() {
        return serviceCategoryId;
    }

    public void setServiceCategoryId(String serviceCategoryId) {
        this.serviceCategoryId = serviceCategoryId;
    }

    public void request2(User user) {}
}

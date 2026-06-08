package com.whebtos.e_chiro.ui.servicesubcategory;

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
import com.whebtos.e_chiro.ui.servicecategories.ServiceCategory;
import com.whebtos.e_chiro.utils.DefaultSettings;
import com.whebtos.e_chiro.utils.SecureHttpRequest;
import com.whebtos.e_chiro.utils.TokenUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServiceSubCategoriesFragment extends Fragment implements SecureHttpRequest {

    private ArrayList<ServiceCategory> serviceSubCategoryList;

    private RecyclerView recyclerView;

    private ServiceSubCategoriesRecyclerViewAdapter serviceSubCategoriesRecyclerViewAdapter;

    private String serviceCategoryId;

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_service_categories, parent, false);

        serviceSubCategoryList = new ArrayList<>();

        recyclerView = root.findViewById(R.id.rv_service_categories);

        recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 2));

        serviceSubCategoriesRecyclerViewAdapter = new ServiceSubCategoriesRecyclerViewAdapter(this, serviceSubCategoryList, this.getContext());

        recyclerView.setAdapter(serviceSubCategoriesRecyclerViewAdapter);

        setServiceCategoryId(getArguments().getString("serviceCategoryId"));

        (new TokenUtil(this)).secureHttpRequest(getContext());

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void request(User user) {

        String token = user.getAccessToken();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, DefaultSettings.URL + DefaultSettings.URL_SERVICE_SUB_CATEGORIES + getServiceCategoryId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                serviceSubCategoryList.clear();

                try {

                    JSONArray jsonArray = new JSONArray(response);

                    for (int r = 0; r < jsonArray.length(); r++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(r);

                        ServiceCategory serviceCategory = new ServiceCategory();

                        serviceCategory.setId(jsonObject.getString("id"));

                        serviceCategory.setName(jsonObject.getString("name"));

                        serviceCategory.setImage(jsonObject.getString("image"));

                        serviceSubCategoryList.add(serviceCategory);

                        serviceSubCategoriesRecyclerViewAdapter.notifyDataSetChanged();

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

    public void request2(User user) {

        String token = user.getAccessToken();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, DefaultSettings.URL + DefaultSettings.URL_SERVICE_SUB_CATEGORIES + getServiceCategoryId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                serviceSubCategoryList.clear();

                try {

                    JSONArray jsonArray = new JSONArray(response);

                    if(jsonArray.length()>0){

                        for (int r = 0; r < jsonArray.length(); r++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(r);

                            ServiceCategory serviceCategory = new ServiceCategory();

                            serviceCategory.setId(jsonObject.getString("id"));

                            serviceCategory.setName(jsonObject.getString("name"));

                            serviceCategory.setImage(jsonObject.getString("image"));

                            serviceSubCategoryList.add(serviceCategory);

                            serviceSubCategoriesRecyclerViewAdapter.notifyDataSetChanged();

                        }

                    }else{
                        ((MainActivity) getActivity()).openServices(getServiceCategoryId());
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

    public void openServices(String id) {
        setServiceCategoryId(id);

        (new TokenUtil(this)).secureHttpRequest2(getContext());
    }

    public String getServiceCategoryId() {
        return serviceCategoryId;
    }

    public void setServiceCategoryId(String serviceCategoryId) {
        this.serviceCategoryId = serviceCategoryId;
    }
}

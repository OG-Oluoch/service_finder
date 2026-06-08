package com.whebtos.e_chiro.ui.addservice;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;
import com.whebtos.e_chiro.MainActivity;
import com.whebtos.e_chiro.R;
import com.whebtos.e_chiro.models.ApiResponse;
import com.whebtos.e_chiro.models.User;
import com.whebtos.e_chiro.ui.authentication.LoginFragment;
import com.whebtos.e_chiro.utils.DefaultSettings;
import com.whebtos.e_chiro.utils.SecureHttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class AddServiceFragment extends Fragment implements SecureHttpRequest {

    private ArrayList<String> subCategoriesList;

    private ArrayList<String> serviceCategoryList;

    private ArrayList<String> subCategoriesIdList;

    private ArrayList<String> serviceCategoryIdList;

    private Spinner spSubCategories;

    private Spinner spCategories;

    private ArrayAdapter<String> subCategoriesAdapter;

    private ArrayAdapter<String> categoriesAdapter;

    private EditText etName;
    private String business_name;

    private EditText etDescription;
    private String business_description;

    private EditText etPhysicalAddress;
    private String physical_address;

    private String opening_time_weekday;
    private int opening_time_weekday_integer;
    private String closing_time_weekday;
    private int closing_time_weekday_integer;

    private String opening_time_weekend;
    private int opening_time_weekend_integer;
    private String closing_time_weekend;
    private int closing_time_weekend_integer;

    private String userName;
    private String userEmail;
    private String userPhone;
    private String userID;

    private String password;

    private String token;

    Calendar c = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//    "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
//    yyyy-MM-ddHH:mm:ss
    private String currentDateTimeString = df.format(c.getTime());

    FusedLocationProviderClient mFusedLocationClient;
    String latitudeTextView, longitTextView;
    int PERMISSION_ID = 44;

    private Button btnSubmt;
    private static final String services_url ="https://backend.wencetechnologies.com/whebtos/api/Business" ;//"https://srv19716098.ultasrv.net/whebtos/api/business"; //"https://backend.wencetechnologies.com/whebtos/api/Service" ;
    private static final String service_categories_url = "https://backend.wencetechnologies.com/whebtos/api/ServiceCategory";//"https://backend.wencetechnologies.com/whebtos/api/ServiceCategory" ;//"https://srv19716098.ultasrv.net/whebtos/api/servicecategory";
    private static final String services_subCategories_url = "https://backend.wencetechnologies.com/whebtos/api/Service"; //url for sub fetching sub categories

    private String user_api = LoginFragment.user_url;
    private String business_lat;
    private String business_lon;

    private static final String PREFS_NAME = "theme_prefs";

    private String selectedCategoryId;
    private String selectedSubCategoryId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_add_service, container, false);

        getUserDetails();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        getLastLocation();

        business_lat = latitudeTextView;
        business_lon = longitTextView;

        //userName = this.getArguments().getString("userName");

        subCategoriesList = new ArrayList<>();

        subCategoriesIdList = new ArrayList<>();

        serviceCategoryList = new ArrayList<>();

        serviceCategoryIdList = new ArrayList<>();

        spCategories = root.findViewById(R.id.sp_categories);

        spSubCategories = root.findViewById(R.id.sp_sub_category);

        getSubCategories();

        subCategoriesAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, subCategoriesList);

        spSubCategories.setAdapter(subCategoriesAdapter);

        spSubCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected category ID using the position
                selectedSubCategoryId = subCategoriesIdList.get(position);

                // Do something with the selected category ID
                // For example, you can display it or use it in further operations
                //Toast.makeText(getContext(), "Selected Category ID: " + selectedCategoryId, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        getCategories();

        categoriesAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, serviceCategoryList);

        spCategories.setAdapter(categoriesAdapter);

        spCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected category ID using the position
                selectedCategoryId = serviceCategoryIdList.get(position);

                // Do something with the selected category ID
                // For example, you can display it or use it in further operations
                //Toast.makeText(getContext(), "Selected Category ID: " + selectedCategoryId, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });


        //(new TokenUtil(this)).secureHttpRequest(getContext());

        Spinner open_time_spinner = root.findViewById(R.id.open_time_spinner);

        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> openTimeAdapter = ArrayAdapter
                .createFromResource(getContext(), R.array.time,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        openTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        open_time_spinner.setAdapter(openTimeAdapter);

        open_time_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                opening_time_weekday = parent.getItemAtPosition(position).toString();
                try {
                    opening_time_weekday_integer = NumberFormat.getInstance().parse(opening_time_weekday).intValue();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner close_time_spinner = root.findViewById(R.id.close_time_spinner);

        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> closeTimeAdapter = ArrayAdapter
                .createFromResource(getContext(), R.array.time,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        closeTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        close_time_spinner.setAdapter(closeTimeAdapter);

        close_time_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                closing_time_weekday = parent.getItemAtPosition(position).toString();
                try {
                    closing_time_weekday_integer = NumberFormat.getInstance().parse(closing_time_weekday).intValue();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Spinner sun_open_time_spinner = root.findViewById(R.id.sun_open_time_spinner);

        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> sun_openTimeAdapter = ArrayAdapter
                .createFromResource(getContext(), R.array.time,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        sun_openTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        sun_open_time_spinner.setAdapter(sun_openTimeAdapter);

        sun_open_time_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                opening_time_weekend = parent.getItemAtPosition(position).toString();
                try {
                    opening_time_weekend_integer = NumberFormat.getInstance().parse(opening_time_weekend).intValue();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        Spinner sun_close_time_spinner = root.findViewById(R.id.sun_close_time_spinner);

        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> sun_closeTimeAdapter = ArrayAdapter
                .createFromResource(getContext(), R.array.time,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        sun_closeTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        sun_close_time_spinner.setAdapter(sun_closeTimeAdapter);

        sun_close_time_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                closing_time_weekend = parent.getItemAtPosition(position).toString();

                try {
                    closing_time_weekend_integer = NumberFormat.getInstance().parse(closing_time_weekend).intValue();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnSubmt = root.findViewById(R.id.btn_submit);

        //SecureHttpRequest secureHttpRequest = this;

        btnSubmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                etName = root.findViewById(R.id.et_name);
                etDescription = root.findViewById(R.id.et_description);
                etPhysicalAddress = root.findViewById(R.id.et_physical_address);

                business_name = etName.getText().toString();
                business_description = etDescription.getText().toString();
                physical_address = etPhysicalAddress.getText().toString();


                if (business_name.matches(""))
                {
                    Toast.makeText(getContext(), "Kindly "+getString(R.string.service_name), Toast.LENGTH_SHORT).show();
                    etName.requestFocus();
                }
                else if (business_description.matches(""))
                {
                    Toast.makeText(getContext(), "Kindly "+getString(R.string.business_description), Toast.LENGTH_SHORT).show();
                    etDescription.requestFocus();
                }
                else if (physical_address.matches(""))
                {
                    Toast.makeText(getContext(), "kindly "+getString(R.string.physical_address), Toast.LENGTH_SHORT).show();
                    etPhysicalAddress.requestFocus();
                }
                else
                {
                    //Toast.makeText(getContext(), ""+userName, Toast.LENGTH_SHORT).show();
                        addService();
                        openMyServices();

                }



//                if (business_name.equals("")||business_description.equals("")||physical_address.equals(""))
//                {
//                }
//                else
//                {
//                    Toast.makeText(getActivity(), "Kindly fill in the Business Details", Toast.LENGTH_SHORT).show();
//                }
                //(new TokenUtil(secureHttpRequest)).secureHttpRequest2(getContext());

            }
        });




        return root;
    }

    public void getUserDetails()
    {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userEmail = sharedPreferences.getString("user_Email", null);
        userID = sharedPreferences.getString("user_ID", null);
        userName = sharedPreferences.getString("user_Name", null);


        if (userEmail == null || userID == null || userName == null) {
            Log.e("AddServiceFragment", "⚠️ Missing user session details");
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

    }

    public void getCategories()
            {

                ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Loading Data...");
                progressDialog.show();

                StringRequest stringRequest = new StringRequest(Request.Method.GET,
                        service_categories_url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                progressDialog.dismiss();
                                try {

                                    //JSONObject jsonObject = new JSONObject(response);
                                    JSONArray array = new JSONArray(response);
                                    //JSONArray array = jsonObject.getJSONArray("");

                                    for (int i=0; i<array.length(); i++)
                                    {
                                        JSONObject o = array.getJSONObject(i);
                                        serviceCategoryList.add(o.getString("name"));
                                        serviceCategoryIdList.add(o.getString("id"));
                                        //Toast.makeText(getContext(), ""+serviceCategoryList, Toast.LENGTH_SHORT).show();
                                    }

                                    categoriesAdapter.notifyDataSetChanged();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    //Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                                    Toast.makeText(getContext(), "Kindly Ensure you have a stable internet connectivity", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                //Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
                                //Toast.makeText(getContext(), "Kindly Ensure you have a stable internet connectivity", Toast.LENGTH_SHORT).show();

                            }
                        }
                );

                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                requestQueue.add(stringRequest);

            }


    public void getSubCategories()
    {

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                services_subCategories_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        try {

                            //JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = new JSONArray(response);
                            //JSONArray array = jsonObject.getJSONArray("");

                            for (int i=0; i<array.length(); i++)
                            {
                                JSONObject o = array.getJSONObject(i);
                                subCategoriesList.add(o.getString("name"));
                                subCategoriesIdList.add(o.getString("id"));
                                //Toast.makeText(getContext(), ""+serviceCategoryList, Toast.LENGTH_SHORT).show();
                            }

                            subCategoriesAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                            Toast.makeText(getContext(), "Kindly Ensure you have a stable internet connectivity", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        //Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
                        //Toast.makeText(getContext(), "Kindly Ensure you have a stable internet connectivity", Toast.LENGTH_SHORT).show();

                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }


    public void addService(){

        try {

            business_lat = latitudeTextView;
            business_lon = longitTextView;
            //Toast.makeText(getContext(), ""+business_lat, Toast.LENGTH_SHORT).show();

            getUserDetails();

            //https://whebtosdemoserver.eastasia.cloudapp.azure.com/Images/BIC.jpg
            //https://whebtosdemoserver.eastasia.cloudapp.azure.com/Images/electrician_icon.jpg
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("id", userID);
            jsonBody.put("createdById", userID);
            jsonBody.put("createdByUserName", userName);
            jsonBody.put("createdDate", currentDateTimeString);
            jsonBody.put("recordStatus", "0");
            jsonBody.put("updatedById", userID);
            jsonBody.put("updatedByUserName", userName);
            jsonBody.put("updatedDate", currentDateTimeString);
            jsonBody.put("serviceId", selectedSubCategoryId);
            jsonBody.put("image", "https://srv19716098.ultasrv.net/whebtos/images/services_icon.jpg");
            jsonBody.put("isActive", true);
            jsonBody.put("businessname", business_name);
            jsonBody.put("serviceType", 0);
            jsonBody.put("parentId", "123456");
            jsonBody.put("serviceCategoryId", selectedCategoryId);
            jsonBody.put("businessdescription", business_description);
            jsonBody.put("physicalAddress", physical_address);
            jsonBody.put("weekdayopen", opening_time_weekday_integer);
            jsonBody.put("weekdayclose", closing_time_weekday_integer);
            jsonBody.put("weekendopen", opening_time_weekend_integer);
            jsonBody.put("weekendclose", closing_time_weekend_integer);
            jsonBody.put("allday", true);
            jsonBody.put("businessImage", null);
            jsonBody.put("businessLatitude",business_lat);
            jsonBody.put("businessLongitude",business_lon);

            final String requestBody = jsonBody.toString();
//            Toast.makeText(getContext(), ""+requestBody, Toast.LENGTH_LONG).show();
//            Toast.makeText(getContext(), ""+requestBody, Toast.LENGTH_LONG).show();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, services_url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        ApiResponse apiResponse = new ApiResponse();
                        apiResponse.setStatus(responseObject.getString("status"));
                        apiResponse.setMessage(responseObject.getString("message"));

                        if ("Success".equals(apiResponse.getStatus())) {
                            if (isAdded() && getContext() != null) {
                                Toast.makeText(getContext(), apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                                openMyServices();
                            } else {
                                Log.w("AddServiceFragment", "Cannot show Toast: Fragment detached or context null");
                            }
                        } else {
                            if (isAdded() && getContext() != null) {
                                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                            } else {
                                Log.w("AddServiceFragment", "Cannot show Toast: Fragment detached or context null");
                            }
                        }
                    } catch (JSONException e) {
                        if (isAdded() && getContext() != null) {
                            Toast.makeText(getContext(), "Something went wrong: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            Log.w("AddServiceFragment", "Cannot show Toast: Fragment detached or context null");
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (isAdded() && getContext() != null) {
                        Toast.makeText(getContext(), "Something went wrong: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        Log.w("AddServiceFragment", "Cannot show Toast: Fragment detached or context null");
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/json; charset=UTF-8");
                    //params.put("Authorization", "Bearer " + user.getAccessToken());
                    return params;
                }

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;

                    }
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(getContext());

            requestQueue.add(stringRequest);


        } catch (Exception ex) {

            Log.e("AddService", "Error: ", ex);
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();


        }


    }

    public void openMyServices()
    {
                ((MainActivity) requireActivity()).openClientServices();
    }


    public void request(User user) {

        String token = user.getAccessToken();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, DefaultSettings.URL + DefaultSettings.URL_ALL_SERVICES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                subCategoriesList.clear();

                subCategoriesIdList.clear();

                try {

                    JSONArray jsonArray = new JSONArray(response);

                    for (int r = 0; r < jsonArray.length(); r++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(r);

                        subCategoriesList.add(jsonObject.getString("name"));

                        subCategoriesIdList.add(jsonObject.getString("id"));

                    }

                    subCategoriesAdapter.notifyDataSetChanged();

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

        stringRequest = new StringRequest(Request.Method.GET, services_url + DefaultSettings.URL_SERVICE_CATEGORIES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                serviceCategoryList.clear();
                serviceCategoryIdList.clear();

                try {

                    JSONArray jsonArray = new JSONArray(response);

                    for (int r = 0; r < jsonArray.length(); r++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(r);

                        serviceCategoryList.add(jsonObject.getString("name"));

                        serviceCategoryIdList.add(jsonObject.getString("id"));

                    }

                    categoriesAdapter.notifyDataSetChanged();

                } catch (Exception ex) {


                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), ""+error, Toast.LENGTH_SHORT).show();
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

        requestQueue = Volley.newRequestQueue(this.getContext());

        requestQueue.add(stringRequest);
    }

    public void request2(User user) {

        try {

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("clientId", user.getId());

            jsonObject.put("userName", user.getUserName());

            jsonObject.put("serviceId", subCategoriesIdList.get(spSubCategories.getSelectedItemPosition()));

            jsonObject.put("serviceCategoryId", serviceCategoryIdList.get(spCategories.getSelectedItemPosition()));

            jsonObject.put("clientServiceName", etName.getText().toString());

            final String requestBody = jsonObject.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, DefaultSettings.URL + DefaultSettings.URL_CREATE_CLIENT_SERVICE, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {

                        JSONObject responseObject = new JSONObject(response);

                        ApiResponse apiResponse = new ApiResponse();

                        apiResponse.setStatus(responseObject.getString("status"));

                        apiResponse.setMessage(responseObject.getString("message"));

                        if (apiResponse.getStatus() == "Success") {

                            Toast.makeText(getContext(), apiResponse.getMessage(), Toast.LENGTH_LONG).show();

                        } else {

                            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();

                        }

                    } catch (JSONException e) {
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json; charset=UTF-8");
                    params.put("Authorization", "Bearer " + user.getAccessToken());
                    return params;
                }

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;

                    }
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(getContext());

            requestQueue.add(stringRequest);


        } catch (Exception ex) {

        }


    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {

                            latitudeTextView = location.getLatitude() + "";
                            longitTextView = location.getLongitude() + "";
                        }
                    }
                });
            } else {
                Toast.makeText(getContext(), "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitudeTextView = "Latitude: " + mLastLocation.getLatitude() + "";
            longitTextView = "Longitude: " + mLastLocation.getLongitude() + "";
        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            //getLastLocation();
        }

    }


    }

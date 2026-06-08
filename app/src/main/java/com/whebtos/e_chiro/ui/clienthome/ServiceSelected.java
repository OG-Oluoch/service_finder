package com.whebtos.e_chiro.ui.clienthome;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.whebtos.e_chiro.ui.authentication.LoginFragment;
import com.whebtos.e_chiro.ui.clientservices.ClientService;
import com.whebtos.e_chiro.ui.clientservices.ClientServicesRecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceSelected extends Fragment {

    private ServiceSelectedViewModel mViewModel;

    private String business_name;
    private TextView business_name_TXT;

    private String business_description;
    private TextView business_description_TXT;

    private String working_hours;
    private TextView working_hours_TXT;

    private String image_path;
    private ImageView businessImage;

    private String business_service_id;

    private Button requestService;

    private static final String businesses_url = " https://backend.wencetechnologies.com/whebtos/api/Business";//"https://srv19716098.ultasrv.net/whebtos/api/business";
    private static final String services_request_url ="https://backend.wencetechnologies.com/whebtos/api/ServiceRequest";
    private String userName;
    private String serviceID;
    private String business_Description;
    private String userID;

    private String currentUserID;

    private String user_api = LoginFragment.user_url;

    private String latitude;
    private String longitude;

    private EditText ServiceDescription;
    private String description;
    private EditText ServicePhysical;
    private String physical;

    private List<ListItem> listItems;

    FusedLocationProviderClient mFusedLocationClient;
    TextView latitudeTextView, longitTextView;
    int PERMISSION_ID = 44;

    private ImageView location_button;

    private RecyclerView recyclerView;

    public static ServiceSelected newInstance() {
        return new ServiceSelected();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_service_selected, container, false);
        listItems = new ArrayList<>();

        Bundle bundle = this.getArguments();

        if (bundle != null) {
            business_name = getArguments().getString("business_name");
            business_name_TXT = root.findViewById(R.id.bs_name);
            business_name_TXT.setText(business_name);

            business_description = getArguments().getString("business_description");
            business_description_TXT = root.findViewById(R.id.bs_desc);
            business_description_TXT.setText(business_description);

            image_path = getArguments().getString("business_image_path");
            businessImage = root.findViewById(R.id.business_image);
            //businessImage.setImageURI(Uri.parse(image_path));
            Picasso.get().load(image_path).into(businessImage);

            business_service_id = getArguments().getString("service_id");

            requestService = root.findViewById(R.id.btn_request_service);
            requestService.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    request_a_service();


//                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                    // Get the layout inflater
//                    LayoutInflater inflater = requireActivity().getLayoutInflater();
//
//                    // Inflate and set the layout for the dialog
//                    // Pass null as the parent view because its going in the dialog layout
//                    builder.setView(inflater.inflate(R.layout.request_service_dialog, null))
//                            // Add action buttons
//                            .setPositiveButton("R.string.signin", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int id) {
//                                    // sign in the user ...
//                                }
//                            })
//                            .setNegativeButton("R.string.cancel", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    //LoginDialogFragment.this.getDialog().cancel();
//                                }
//                            });
//                    builder.create();

                }
            });

            //Toast.makeText(getActivity(), "Business Name:" + business_name, Toast.LENGTH_SHORT).show();
        }

        return root;
        //return inflater.inflate(R.layout.fragment_service_selected, container, true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ServiceSelectedViewModel.class);
        // TODO: Use the ViewModel
    }

    public void request_a_service() {
        // creating custom dialog
        final Dialog dialog = new Dialog(getContext());

        // setting content view to dialog
        dialog.setContentView(R.layout.request_service_dialog);

        // getting reference of TextView
        TextView dialogButtonYes = (TextView) dialog.findViewById(R.id.textViewProceed);
        TextView dialogButtonNo = (TextView) dialog.findViewById(R.id.textViewCancel);

        ServiceDescription = dialog.findViewById(R.id.selected_service_description);
        ServicePhysical = dialog.findViewById(R.id.selected_service__physical_location);



        latitudeTextView = dialog.findViewById(R.id.latTextView);
        longitTextView = dialog.findViewById(R.id.lonTextView);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

        //Toast.makeText(getContext(), "Service ID: "+business_service_id, Toast.LENGTH_SHORT).show();

        StringRequest stringRequestUser = new StringRequest(com.android.volley.Request.Method.GET,user_api,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            //JSONArray array = new JSONArray(response);
                            //JSONObject jsonObject = array.getJSONObject(0);
                            currentUserID = jsonObject.getString("id");
                            //final String myServicesURL = "https://development.esriea.com/Whebtos/api/ServiceRequest/CreatedBy/"+userID;

                            if (user_api.isEmpty())
                            {
                                Toast.makeText(getContext(), "The Service Provider Requests URL has a problem", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                            //Toast.makeText(getContext(), "Kindly Ensure you have a stable internet connectivity", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //progressDialog.dismiss();
                        //Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
                        //Toast.makeText(getContext(), "Kindly Ensure you have a stable internet connectivity", Toast.LENGTH_SHORT).show();

                    }
                }
        );

        RequestQueue requestQueueUser = Volley.newRequestQueue(getContext());
        requestQueueUser.add(stringRequestUser);


        StringRequest stringRequest = new StringRequest(Request.Method.GET,businesses_url+"/"+business_service_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            //JSONArray array = new JSONArray(response);
                            //JSONObject jsonObject = array.getJSONObject(0);
                            userID = jsonObject.getString("createdById");
                            userName = jsonObject.getString("createdByUserName");
                            serviceID = jsonObject.getString("id");
//                            Toast.makeText(getContext(),
//                                    "createdById: "+userID+"\n"
//                                            +"createdByUserName: "+userName+"\n"
//                                            +"serviceId: "+serviceID+"\n"
//                                    , Toast.LENGTH_LONG).show();


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                            //Toast.makeText(getContext(), "Kindly Ensure you have a stable internet connectivity", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //progressDialog.dismiss();
                        //Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
                        //Toast.makeText(getContext(), "Kindly Ensure you have a stable internet connectivity", Toast.LENGTH_SHORT).show();

                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);


        location_button = dialog.findViewById(R.id.dialog_location_btn);
        location_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "Location", Toast.LENGTH_SHORT).show();
                getLastLocation();
            }
        });

        // click listener for No
        dialogButtonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dismiss the dialog
                dialog.dismiss();
                int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
                int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);

                dialog.getWindow().setLayout(width, height);

            }
        });

        // click listener for Yes
        dialogButtonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                description = ServiceDescription.getText().toString();
                physical = ServicePhysical.getText().toString();
                latitude = latitudeTextView.getText().toString();
                longitude = longitTextView.getText().toString();

                Toast.makeText(getContext(),
                        "createdById: "+userID+
                                "\ncreatedByUserName: "+userName+
                                "\nserviceId: "+serviceID+
                                "\nDescription: " +description+
                                "\n Physical: "+physical+
                                "\n Latitude: " +latitude+
                                "\nLongitude: "+longitude
                        , Toast.LENGTH_LONG).show();


                try {

                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("createdById", userID);
                    jsonBody.put("createdByUserName", userName);
                    jsonBody.put("description", description);
                    jsonBody.put("serviceId", serviceID);
                    jsonBody.put("requestLongitude", longitude);
                    jsonBody.put("requestLatitude", latitude);
                    jsonBody.put("consumerId",currentUserID);

                    final String requestBody = jsonBody.toString();
//            Toast.makeText(getContext(), ""+requestBody, Toast.LENGTH_LONG).show();
//            Toast.makeText(getContext(), ""+requestBody, Toast.LENGTH_LONG).show();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, services_request_url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                JSONArray responseObject = new JSONArray(response);
//                                Toast.makeText(getContext() , "Response: "+responseObject, Toast.LENGTH_LONG).show();
//                                Toast.makeText(getContext() , "Response: "+responseObject, Toast.LENGTH_LONG).show();
//                                Toast.makeText(getContext() , "Response: "+responseObject, Toast.LENGTH_LONG).show();

                                for (int i=0; i<responseObject.length(); i++)
                                {
                                    JSONObject o = responseObject.getJSONObject(i);
                                    ListItem item = new ListItem(o.getString("businessname"),o.getString("businessdescription"), o.getString("image"),o.getString("id"));
                                    //ListItem item = new ListItem(o.getString("name"),o.getString("description"), o.getString("image"),o.getString("id"));
                                    listItems.add(item);
                                    //Toast.makeText(getContext(), "List"+listItems, Toast.LENGTH_SHORT).show();

//                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                                    //builder.setTitle("List Dialog");
//
//                                    // Create a custom layout for the dialog
//                                    LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                                    View dialogView = inflater.inflate(R.layout.dialog_list, null);
//
//                                    // Initialize the RecyclerView in the custom layout
//                                    RecyclerView dialogRecyclerView = dialogView.findViewById(R.id.dialogRecyclerView);
//
//                                    // Create the adapter for the RecyclerView
//                                    ListItemAdapter adapter = new ListItemAdapter(getContext(), listItems);
//                                    dialogRecyclerView.setAdapter(adapter);
//
//                                    builder.setView(dialogView);
//                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            dialog.dismiss();
//                                        }
//                                    });
//
//                                    AlertDialog dialog = builder.create();
//                                    dialog.show();

//                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                                    builder.setTitle("List Dialog");
//
//                                    // Create a custom layout for the dialog
//                                    LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                                    View dialogView = inflater.inflate(R.layout.dialog_list, null);
//
//                                    // Initialize the ListView in the custom layout
//                                    //ListView dialogListView = dialogView.findViewById(R.id.dialogListView);
//                                    RecyclerView dialogrecyclerView = dialogView.findViewById(R.id.dialogRecyclerView);
//
//                                    // Create and set the adapter for the ListView
//                                    Toast.makeText(getContext(), "List:\n"+listItems.toString(), Toast.LENGTH_LONG).show();
//                                    ListItemAdapter adapter = new ListItemAdapter(getContext(), listItems);
//                                    dialogrecyclerView.setAdapter(adapter);
//                                    //dialogListView.setAdapter(adapter);
////
//                                    builder.setView(dialogView);
//                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            dialog.dismiss();
//                                        }
//                                    });
//
//                                    AlertDialog dialog = builder.create();
//                                    dialog.show();

                                }


                                ApiResponse apiResponse = new ApiResponse();

//                                apiResponse.setStatus(responseObject.getString("status"));
//
//                                apiResponse.setMessage(responseObject.getString("message"));

//                                if (apiResponse.getStatus() == "Success") {
//
//                                    Toast.makeText(getContext(), apiResponse.getMessage(), Toast.LENGTH_LONG).show();
//
//                                } else {
//
//                                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
//
//                                }

                            } catch (JSONException e) {
                                Toast.makeText(getContext(), "Something went wrong: "+e, Toast.LENGTH_LONG).show();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(), "Something went wrong"+error, Toast.LENGTH_LONG).show();
                        }
                    }) {

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
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

                    Toast.makeText(getContext(), "You have successfully requested the service", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                } catch (Exception ex) {
                    Toast.makeText(getContext(), "Exception: "+ex, Toast.LENGTH_SHORT).show();
                }


                //Toast.makeText(getContext(), "The Service you are requesting is available", Toast.LENGTH_SHORT).show();
                // method to get the location
                //getLastLocation();
//                {
//                    "createdById": "string",
//                        "createdByUserName": "string",
//                        "description": "string",
//                        "serviceId": "string",
//                        "requestLongitude": 0,
//                        "requestLatitude": 0
//                }

            }
        });

        // show the exit dialog
        dialog.show();
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
                            latitudeTextView.setText(location.getLatitude() + "");
                            longitTextView.setText(location.getLongitude() + "");
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
            latitudeTextView.setText("Latitude: " + mLastLocation.getLatitude() + "");
            longitTextView.setText("Longitude: " + mLastLocation.getLongitude() + "");
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

    private void showListDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("List Dialog");

        // Create a custom layout for the dialog
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_list, null);

        // Initialize the ListView in the custom layout
        //ListView dialogListView = dialogView.findViewById(R.id.dialogListView);
        RecyclerView dialogrecyclerView = dialogView.findViewById(R.id.dialogRecyclerView);

        // Create and set the adapter for the ListView
        Toast.makeText(getContext(), "List:\n"+listItems, Toast.LENGTH_LONG).show();
        ListItemAdapter adapter = new ListItemAdapter(getContext(), listItems);
        dialogrecyclerView.setAdapter(adapter);
        //dialogListView.setAdapter(adapter);
//
        builder.setView(dialogView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

}
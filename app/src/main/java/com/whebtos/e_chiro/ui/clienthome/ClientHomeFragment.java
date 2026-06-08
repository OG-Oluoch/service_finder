package com.whebtos.e_chiro.ui.clienthome;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.whebtos.e_chiro.MainActivity;
import com.whebtos.e_chiro.R;
import com.whebtos.e_chiro.models.User;
import com.whebtos.e_chiro.ui.authentication.AccountActivity;
import com.whebtos.e_chiro.ui.authentication.LoginFragment;
import com.whebtos.e_chiro.ui.notifications.ClientNotificationFragment;
import com.whebtos.e_chiro.ui.notifications.NotificationsFragment;
import com.whebtos.e_chiro.utils.SecureHttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ClientHomeFragment extends Fragment implements SecureHttpRequest {

    //private static final String services_url = "https://srv19716098.ultasrv.net/whebtos/api/service";

    //private static final String businesses_url = "https://srv19716098.ultasrv.net/whebtos/api/business";

    //private static final String businesses_url = "https://srv19716098.ultasrv.net/whebtos/api/ServiceCategory";

    private static final String businesses_url = "https://backend.wencetechnologies.com/whebtos/api/Business/GetOptions/";

    //private ArrayList<Service> serviceList;

    private ArrayList<Service> recentServiceList;

    private ArrayList<Service> popularServiceList;

    private RecyclerView recyclerView;

    private ServicesRecyclerViewAdapter servicesRecyclerViewAdapter;

    private RecyclerView recyclerViewRecentActivities;

    private RecentServicesRecyclerViewAdapter recentServicesRecyclerViewAdapter;

    private RecyclerView recyclerViewPopularServices;

    private PopularServicesRecyclerViewAdapter popularServicesRecyclerViewAdapter;

    private List<ListItem> listItems;

    private SearchView search_service;

    private FragmentManager fragmentManager;

    private Toolbar toolbar;

    private String userName;
    private String userEmail;
    private String userPhone;
    private String userID;

    private static final String PREFS_NAME = "theme_prefs";

    FusedLocationProviderClient mFusedLocationClient;
    String latitudeTextView, longitTextView;
    int PERMISSION_ID = 44;

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_client_home, parent, false);


        recyclerView = root.findViewById(R.id.rv_services);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        listItems = new ArrayList<>();

        search_service = root.findViewById(R.id.search_service);
        search_service.setQueryHint("Search for a service here");

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        getLastLocation();

        getUserDetails();

        //search_service.getQuery();
        search_service.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String service_searched) {

                if (latitudeTextView == null || longitTextView == null) {
                    Toast.makeText(getContext(), "Fetching location, please wait...", Toast.LENGTH_SHORT).show();
                    getLastLocation();
                    return false; // stop until location is ready
                }

                listItems.clear();
                //Toast.makeText(getContext(), "You have searched for: "+service_searched+latitudeTextView+longitTextView, Toast.LENGTH_SHORT).show();
                String LatLong = "/"+latitudeTextView+"/"+longitTextView;

                //Toast.makeText(getContext(), businesses_url+service_searched+LatLong, Toast.LENGTH_SHORT).show();

                ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Loading Data...");
                progressDialog.show();

                StringRequest stringRequest = new StringRequest(Request.Method.GET,
                        businesses_url+service_searched.trim()+LatLong+"/"+userName,
                        new Response.Listener<String>() {
                             @Override
                            public void onResponse(String response) {

                                progressDialog.dismiss();
                                try {

                                    //JSONObject jsonObject = new JSONObject(response);
                                    JSONArray array = new  JSONArray(response);
                                    //JSONArray array = jsonObject.getJSONArray("");

                                    for (int i=0; i<array.length(); i++)
                                    {
                                        JSONObject o = array.getJSONObject(i);
                                        ListItem item = new ListItem(o.getString("businessname"),o.getString("businessdescription"), o.getString("image"),o.getString("id"));
                                        //ListItem item = new ListItem(o.getString("name"),o.getString("description"), o.getString("image"),o.getString("id"));
                                        listItems.add(item);
                                    }

                                    servicesRecyclerViewAdapter = new ServicesRecyclerViewAdapter(listItems, getContext(), new ServicesRecyclerViewAdapter.ItemClickListener() {
                                        @Override
                                        public void onItemClick(ListItem listItemClick) {
                                            String bs_name = listItemClick.getServiceName();
                                            String bs_desc = listItemClick.getServiceDescription();
                                            String bs_img = listItemClick.getServiceImage();
                                            String bs_sevice_id = listItemClick.getServiceID();

                                            //Toast.makeText(getContext(), "Name: "+bs_name+"\nDescription: "+bs_desc+"\n Image: "+bs_img, Toast.LENGTH_SHORT).show();

                                            Bundle bundle = new Bundle();
                                            bundle.putString("business_name",bs_name);
                                            bundle.putString("business_description",bs_desc);
                                            bundle.putString("business_image_path",bs_img);
                                            bundle.putString("service_id",bs_sevice_id);
                                            // Put anything what you want

                                            Toast.makeText(getContext(), "The Service Providers have received your request. Please wait for them to bid.", Toast.LENGTH_SHORT).show();

//                                            ServiceSelected serviceSelected = new ServiceSelected();
//                                            serviceSelected.setArguments(bundle);
//
//                                            getFragmentManager().beginTransaction()
//                                                    .replace(R.id.fragment_container,serviceSelected , "")
//                                                    .addToBackStack("ServiceSelected")
//                                                    .commit();
//                                    toolbar.setTitle("Selected Service");
//                                    setTitle("Selected Service");

                                            //openSelectedService();
                                        }
                                    });
                                    recyclerView.setAdapter(servicesRecyclerViewAdapter);

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
                                progressDialog.dismiss();
                                //Toast.makeText(MainActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                                Toast.makeText(getContext(), "THere is an issue", Toast.LENGTH_SHORT).show();

                            }
                        }
                );

                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                requestQueue.add(stringRequest);


                return true;
            }

            @Override
            public boolean onQueryTextChange(String service_searched_edited) {
                //This is your adapter that will be filtered
                return false;
            }
        });



        return root;
    }

    public void addService() {
        ((MainActivity) getActivity()).openAddService();
    }

    public void openSelectedService()
    {
        ((MainActivity) getActivity()).openServicesSelected();
    }


    public void getUserDetails()
    {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userEmail = sharedPreferences.getString("user_Email", null);
        userID = sharedPreferences.getString("user_ID", null);
        userName = sharedPreferences.getString("user_Name", null);

        if (userID == null || userName == null) {
            // Fallback: If not in SharedPreferences, fetch from API
            Log.d("SharedPreferences", "User details loaded: ID=" + userID + ", Name=" + userName +
                    ", Email=" + userEmail + ", Phone=" + userPhone);
        } else {
            Log.d("USER_DETAILS", "Loaded from SharedPreferences: " + userName + " (" + userEmail + ")");
        }
    }

    public void request(User user) {

        String token = user.getAccessToken();

    }

    public void request2(User user) {
    }



    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation()
                        .addOnCompleteListener(new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    latitudeTextView = String.valueOf(location.getLatitude());
                                    longitTextView = String.valueOf(location.getLongitude());
                                    Log.d("LOCATION", "Lat: " + latitudeTextView + ", Lng: " + longitTextView);
                                }
                            }
                        });
            } else {
                Toast.makeText(getContext(), "Please turn on your location...", Toast.LENGTH_LONG).show();
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        } else {
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(2000);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) return;
            Location mLastLocation = locationResult.getLastLocation();
            latitudeTextView = String.valueOf(mLastLocation.getLatitude());
            longitTextView = String.valueOf(mLastLocation.getLongitude());
            Log.d("LOCATION", "Updated Lat: " + latitudeTextView + ", Lng: " + longitTextView);
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

    private void openNotificationsFragment() {
        FragmentTransaction transaction = requireActivity()
                .getSupportFragmentManager()
                .beginTransaction();

        ClientNotificationFragment clientNotificationFragment = new ClientNotificationFragment();
        transaction.replace(R.id.fragment_container, clientNotificationFragment);
        transaction.addToBackStack("ClientNotificationsFragment");
        transaction.commit();

        Toast.makeText(getContext(), "Opening Notifications...", Toast.LENGTH_SHORT).show();
    }


    private void handleIncomingNotification() {
        // Step 1: Check if the hosting Activity has a notification trigger
        Intent intent = requireActivity().getIntent();
        if (intent != null && "alerts".equals(intent.getStringExtra("trigger"))) {
            openNotificationsFragment();
            // Consume the trigger so it doesn't reopen repeatedly
            intent.removeExtra("trigger");
            return;
        }

        // Step 2: If no trigger in intent, check SharedPreferences (saved by FirebaseMessageReceiver)
        SharedPreferences prefs = requireActivity().getSharedPreferences("theme_prefs", Context.MODE_PRIVATE);
        boolean hasPendingNotification = prefs.getBoolean("hasPendingNotification", false);

        if (hasPendingNotification) {
            String trigger = prefs.getString("pendingTrigger", null);
            if ("alerts".equals(trigger)) {
                openNotificationsFragment();

                // Clear the flag so it doesn’t repeat
                prefs.edit().putBoolean("hasPendingNotification", false).apply();
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            //getLastLocation();
        }

        //handleIncomingNotification();

    }


}

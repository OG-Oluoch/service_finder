package com.whebtos.e_chiro;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.ui.AppBarConfiguration;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.whebtos.e_chiro.databinding.ActivityMainBinding;
import com.whebtos.e_chiro.ui.addservice.AddServiceFragment;
import com.whebtos.e_chiro.ui.authentication.AccountActivity;
import com.whebtos.e_chiro.ui.authentication.ClientTypeFragment;
import com.whebtos.e_chiro.ui.authentication.LoginFragment;
import com.whebtos.e_chiro.ui.authentication.SignUpFragment;
import com.whebtos.e_chiro.ui.authentication.User;
import com.whebtos.e_chiro.ui.bookings.BookingsFragment;
import com.whebtos.e_chiro.ui.clienthome.ClientHomeFragment;
import com.whebtos.e_chiro.ui.clienthome.ServiceSelected;
import com.whebtos.e_chiro.ui.clientservices.ClientServicesFragment;
import com.whebtos.e_chiro.ui.help.HelpFragment;
import com.whebtos.e_chiro.ui.notifications.ClientNotificationFragment;
import com.whebtos.e_chiro.ui.notifications.NotificationsFragment;
import com.whebtos.e_chiro.ui.profile.ProfileFragment;
import com.whebtos.e_chiro.ui.providersmap.ProvidersMapFragment;
import com.whebtos.e_chiro.ui.requests.RequestsFragment;
import com.whebtos.e_chiro.ui.requests.ServiceProviderRequestsFragment;
import com.whebtos.e_chiro.ui.servicecategories.ServiceCategoriesFragment;
import com.whebtos.e_chiro.ui.serviceproviderhome.ServiceProviderHomeFragment;
import com.whebtos.e_chiro.ui.services.Service;
import com.whebtos.e_chiro.ui.services.ServicesFragment;
import com.whebtos.e_chiro.ui.servicesubcategory.ServiceSubCategoriesFragment;
import com.whebtos.e_chiro.ui.settings.SettingsFragment;
import com.whebtos.e_chiro.ui.share.ShareFragment;
import com.whebtos.e_chiro.ui.user.LoginActivity;
import com.whebtos.e_chiro.ui.user.SignupActivity;
import com.whebtos.e_chiro.utils.DatabaseHelper;
import com.whebtos.e_chiro.utils.DefaultSettings;
import com.whebtos.e_chiro.utils.UserDBHelper;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private FragmentManager fragmentManager;
    private FrameLayout frameLayout;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;
    private int clientMode = 0;

    private String user_ID;
    private String user_Name;
    private String user_Email;
    private String user_Phone;

    private String clientType=null;
    private String user_sqlite_id = null;

    private static final String PREFS_NAME = "theme_prefs";
    private static final String PREF_THEME = "selected_theme";

    private static final long DOUBLE_BACK_PRESS_INTERVAL = 2000; // 2 seconds
    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



            // Initialize theme
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            int themeMode = prefs.getInt(PREF_THEME, AppCompatDelegate.MODE_NIGHT_NO);
            AppCompatDelegate.setDefaultNightMode(themeMode);

            super.onCreate(savedInstanceState);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            // Retrieve user data from SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            user_ID = sharedPreferences.getString("user_ID", "");
            user_Name = sharedPreferences.getString("user_Name", "");
            user_Email = sharedPreferences.getString("user_Email", "");
            user_Phone = sharedPreferences.getString("user_Phone", "");
            clientType = sharedPreferences.getString("client_Type", "Client");

                // Handle back press
                OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        // Check if drawer is open
                        if (drawer.isDrawerOpen(GravityCompat.START)) {
                            drawer.closeDrawer(GravityCompat.START);
                            return;
                        }

                        // Check if there are fragments in the back stack
                        if (fragmentManager.getBackStackEntryCount() > 0) {
                            fragmentManager.popBackStack();
                            return;
                        }

                        // Handle double back press to exit
                        if (backPressedTime + DOUBLE_BACK_PRESS_INTERVAL > System.currentTimeMillis()) {
                            finishAffinity(); // Exit the app completely
                        } else {
                            Toast.makeText(MainActivity.this, "Press back again to exit", Toast.LENGTH_SHORT).show();
                        }
                        backPressedTime = System.currentTimeMillis();
                    }
                };
                getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);


                binding = ActivityMainBinding.inflate(getLayoutInflater());
                setContentView(binding.getRoot());

                setSupportActionBar(binding.appBarMain.toolbar);

        Intent getResults = getIntent();
        user_ID = getResults.getStringExtra("user_ID");
        user_Name = getResults.getStringExtra("user_Name");
        user_Email = getResults.getStringExtra("user_Email");
        user_Phone = getResults.getStringExtra("user_Phone");


                toolbar = (Toolbar) findViewById(R.id.toolbar);
                DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                ActionBarDrawerToggle mDrawerToggle;

                setSupportActionBar(toolbar);
                final ActionBar actionBar = getSupportActionBar();

                if (actionBar != null) {
                    actionBar.setDisplayHomeAsUpEnabled(true);
                    mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
                        public void onDrawerClosed(View view) {
                            supportInvalidateOptionsMenu();
                            //drawerOpened = false;
                        }

                        public void onDrawerOpened(View drawerView) {
                            supportInvalidateOptionsMenu();
                            //drawerOpened = true;
                        }
                    };
                    mDrawerToggle.setDrawerIndicatorEnabled(true);
                    drawerLayout.setDrawerListener(mDrawerToggle);
                    mDrawerToggle.syncState();
                }

                frameLayout = findViewById(R.id.fragment_container);

                fragmentManager = getSupportFragmentManager();

//       SharedPreferences sessionPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
//        clientType = sessionPrefs.getString("client_type", "Service Provider"); // Default = Client

                handleNotificationAction(getIntent());


//        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
//
//            @Override
//            public void onBackStackChanged() {
//
//                if(getFragmentManager().getBackStackEntryCount()==0) {
//                    //onResume();
//                }
//            }
//        });

                drawer = binding.drawerLayout;
                NavigationView navigationView = binding.navView;


                //Now, you can use the retrieved clientType in your Login fragment as needed

                if (clientType != null) {
                    // Use clientType in your login logic
                    //Toast.makeText(this, "You are viewing the user interface as a "+clientType, Toast.LENGTH_SHORT).show();
                    String typeofClient = clientType;
                    if (typeofClient.equals("Client")) {
                        MenuItem mi = navigationView.getMenu().findItem(R.id.nav_services);
                        MenuItem s_p_r = navigationView.getMenu().findItem(R.id.nav_ser_pro_requests);

                        mi.setTitle("Requests");
                        mi.setIcon(R.drawable.ic_requests);
                        openClientHome();

                        s_p_r.setVisible(false);

                    } else if (typeofClient.equals("Service Provider")) {
                        MenuItem mi = navigationView.getMenu().findItem(R.id.nav_services);
                        MenuItem s_p_r = navigationView.getMenu().findItem(R.id.nav_ser_pro_requests);

                        mi.setTitle("Services");
                        mi.setIcon(R.drawable.ic_services);
                        openServiceProviderHome();

                        s_p_r.setVisible(true);

                    } else {
                        ClientHomeFragment clientHomeFragment = new ClientHomeFragment();

                        fragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, clientHomeFragment, "ClientHomeFragment")
                                //.addToBackStack("ClientHomeFragment")
                                .commit();

                        toolbar.setTitle("Home");
                        setTitle("Home");
                    }
                }

                navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                        switch (item.getItemId()) {

                            case R.id.nav_home:

                                if (clientType.equals("Client")) {
                                    openClientHome();
                                } else {
                                    openServiceProviderHome();
                                }

                                break;

                            case R.id.nav_profile:
                                openProfile();

                                break;

                            case R.id.nav_services:

                                if (clientType.equals("Client")) {
                                    openRequests();
                                } else {
                                    openClientServices();
                                }

                                break;

                            case R.id.nav_notifications:

                                if (clientType.equals("Client")) {
                                    openClientNotifications();
                                } else {
                                    openNotifications();
                                }

                                break;

                            case R.id.nav_settings:

                                openSettings();

                                break;

                            case R.id.nav_ser_pro_requests:

                                openServiceProviderRequests();

                                break;

                            case R.id.nav_switch:

                                SharedPreferences sessionPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sessionPrefs.edit();
                                if (clientType.equals("Client")) {
                                    clientType = "Service Provider";
                                } else {
                                    clientType = "Client";
                                }
                                editor.putString("client_type", clientType).apply();

//                        if(clientMode==0){
//                            clientMode=1;
//                        }else{
//                            clientMode=0;
//                        }

                                MenuItem mi = navigationView.getMenu().findItem(R.id.nav_services);
                                MenuItem s_p_r = navigationView.getMenu().findItem(R.id.nav_ser_pro_requests);

                                if (clientType.equals("Client")) {
                                    mi.setTitle("Requests");
                                    mi.setIcon(R.drawable.ic_requests);
                                    openClientHome();

                                    s_p_r.setVisible(false);

                                } else {
                                    mi.setTitle("Services");
                                    mi.setIcon(R.drawable.ic_services);
                                    openServiceProviderHome();

                                    s_p_r.setVisible(true);
                                }

                                break;


                            case R.id.nav_help:

                                openHelp();

                                break;

                            case R.id.nav_share:

                                openShare();

                                break;

                            case R.id.nav_theme_toggle:
                                // Toggle theme
                                int currentMode = prefs.getInt(PREF_THEME, AppCompatDelegate.MODE_NIGHT_NO);
                                int newMode = (currentMode == AppCompatDelegate.MODE_NIGHT_NO) ?
                                        AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
                                AppCompatDelegate.setDefaultNightMode(newMode);
                                prefs.edit().putInt(PREF_THEME, newMode).apply();
                                updateThemeMenuItem(navigationView, newMode);
                                break;

                            case R.id.nav_logout:

                                customExitDialog();
                                //openLogin();
                                //logout();

                                break;

                        }

                        drawer.closeDrawer(GravityCompat.START, true);

                        return true;
                    }
                });


    }

    private void updateThemeMenuItem(NavigationView navigationView, int themeMode) {
        MenuItem themeItem = navigationView.getMenu().findItem(R.id.nav_theme_toggle);
        if (themeMode == AppCompatDelegate.MODE_NIGHT_YES) {
            themeItem.setTitle("Light Mode");
            themeItem.setIcon(R.drawable.ic_dark_light); // Add drawable for light mode
        } else {
            themeItem.setTitle("Dark Mode");
            themeItem.setIcon(R.drawable.ic_dark_light); // Add drawable for dark mode
        }
    }

    private  void  logout()
    {

//        // Clear user session from SQLite
//        UserDBHelper dbHelper = new UserDBHelper(MainActivity.this);
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        db.delete(UserDBHelper.TABLE_USER_TOKEN, null, null);
//        db.close();

        // Clear user session from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Clears all data, including user data and theme
        editor.apply();

         // Navigate to LoginActivity and clear back stack
        Intent intent = new Intent(MainActivity.this, AccountActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish(); // finish MainActivity to prevent going back

    }

    public void customExitDialog() {

        // creating custom dialog
        final Dialog dialog = new Dialog(MainActivity.this);

        // setting content view to dialog
        dialog.setContentView(R.layout.custom_exit_dialog);

        // getting reference of TextViews
        TextView dialogButtonYes = dialog.findViewById(R.id.textViewYes);
        TextView dialogButtonNo = dialog.findViewById(R.id.textViewNo);

        // click listener for No
        dialogButtonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dismiss the dialog
                dialog.dismiss();
                int width = (int)(getResources().getDisplayMetrics().widthPixels * 0.90);
                int height = (int)(getResources().getDisplayMetrics().heightPixels * 0.90);
                dialog.getWindow().setLayout(width, height);
            }
        });

        // click listener for Yes
        dialogButtonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();  // close the dialog
                logout();          // perform clean logout
            }
        });

        // show the exit dialog
        dialog.show();
    }
    private void openLogin() {

        LoginFragment loginFragment = new LoginFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, loginFragment, "Login")
                //.addToBackStack("Login")
                .commit();

        getSupportActionBar().hide();
//        toolbar.setTitle("Login");
//        setTitle("Login");

    }

    public void openServiceProviderRequests()
    {
        ServiceProviderRequestsFragment serviceProviderRequestsFragment = new ServiceProviderRequestsFragment();

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, serviceProviderRequestsFragment, "ServiceProviderRequestsFragment")
                //.addToBackStack("ServiceProviderRequestsFragment")
                .commit();

        toolbar.setTitle("Requests from Clients");
        setTitle("Requests from Clients");
    }

    public void openClientHome() {

        ClientHomeFragment clientHomeFragment = new ClientHomeFragment();

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, clientHomeFragment, "ClientHomeFragment")
                //.addToBackStack("ClientHomeFragment")
                .commit();

        toolbar.setTitle("Home");
        setTitle("Home");

    }

    public void openAddService() {

        AddServiceFragment addServiceFragment = new AddServiceFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("userID", user_ID);
//        bundle.putString("userName", user_Name);
//        bundle.putString("userEmail", user_Email);
//        bundle.putString("userPhone", user_Phone);
//        addServiceFragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, addServiceFragment, "AddServiceFragment")
                .commit();
        toolbar.setTitle("Add Service");
        setTitle("Add Service");

    }

    public void openServicesSelected()
    {
        ServiceSelected serviceSelected = new ServiceSelected();

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container,serviceSelected , "")
                .commit();
        toolbar.setTitle("Selected Service");
        setTitle("Selected Service");
    }

    public void openServiceProviderHome() {

        ServiceProviderHomeFragment serviceProviderHomeFragment = new ServiceProviderHomeFragment();

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, serviceProviderHomeFragment, "ServiceProviderHomeFragment")
                .commit();

        toolbar.setTitle("Home");
        setTitle("Home");

    }

    public void openClientType() {

        ClientTypeFragment clientTypeFragment = new ClientTypeFragment();

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, clientTypeFragment, "ClientType")
                .commit();

        toolbar.setTitle("Client Type");
        setTitle("Client Type");
    }

    public void openHome() {

        ServiceCategoriesFragment serviceCategoriesFragment = new ServiceCategoriesFragment();

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, serviceCategoriesFragment, "Home")
                .commit();

        toolbar.setTitle("Home");
        setTitle("Home");
    }

    public void openSettings() {

        SettingsFragment serviceCategoriesFragment = new SettingsFragment();

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, serviceCategoriesFragment, "Settings")
                //.addToBackStack("Settings")
                .commit();

        toolbar.setTitle("Settings");
        setTitle("Settings");
    }

    public void openRequests() {

        RequestsFragment serviceCategoriesFragment = new RequestsFragment();

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, serviceCategoriesFragment, "Requests")
                .commit();

        toolbar.setTitle("Requests");
        setTitle("Requests");
    }

    public void openClientServices() {

        ClientServicesFragment clientServicesFragment = new ClientServicesFragment();

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, clientServicesFragment, "ClientServicesFragment")
                .commit();

        toolbar.setTitle("My Services");
        setTitle("My Services");
    }

    public void openNotifications() {
        NotificationsFragment notificationsFragment = new NotificationsFragment();
        fragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, notificationsFragment, "NotificationsFragment") .commit();
        toolbar.setTitle("Notifications"); setTitle("Notifications");
    }


    public void openClientNotifications() {
        ClientNotificationFragment clientNotificationFragment = new ClientNotificationFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, clientNotificationFragment, "ClientNotificationsFragment") .commit();
        toolbar.setTitle("ClientNotificationsFragment"); setTitle("ClientNotificationsFragment");
    }
    public void openServiceSubCategories(String id) {

        ServiceSubCategoriesFragment serviceSubCategoriesFragment = new ServiceSubCategoriesFragment();

        Bundle args = new Bundle();

        args.putString("serviceCategoryId", id);

        serviceSubCategoriesFragment.setArguments(args);

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, serviceSubCategoriesFragment, "Sub Categories")
                .commit();

        toolbar.setTitle("Sub Categories");

        setTitle("Sub Categories");
    }

    public void openProvidersMap() {

        ProvidersMapFragment providersMapFragment = new ProvidersMapFragment();

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, providersMapFragment, "ProvidersMapFragment")
                .commit();

        toolbar.setTitle("Providers Map");

        setTitle("Providers Map");

    }

    public void openServices(String id) {

        ServicesFragment servicesFragment = new ServicesFragment();

        Bundle args = new Bundle();

        args.putString("serviceCategoryId", id);

        servicesFragment.setArguments(args);

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, servicesFragment, "Services")
                .commit();

        toolbar.setTitle("Services");

        setTitle("Sub Categories");
    }

    public void openProfile() {

        ProfileFragment profileFragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString("user_Name", user_Name);
        args.putString("user_Email", user_Email);
        args.putString("user_Phone", user_Phone);
        profileFragment.setArguments(args);

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, profileFragment, "Profile")
                .commit();

        toolbar.setTitle("Profile");
        setTitle("Profile");
    }

    public void openBookings() {

        BookingsFragment bookingsFragment = new BookingsFragment();

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, bookingsFragment, "Bookings")
                //.addToBackStack("Bookings")
                .commit();

        toolbar.setTitle("Bookings");
        setTitle("Bookings");
    }

    public void openHelp() {

        HelpFragment helpFragment = new HelpFragment();

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, helpFragment, "Help")
                //.addToBackStack("Help")
                .commit();

        toolbar.setTitle("Help");
        setTitle("Help");
    }

    public void openShare() {

//        ShareFragment shareFragment = new ShareFragment();
//
//        fragmentManager.beginTransaction()
//                .replace(R.id.fragment_container, shareFragment, "Share")
//                .addToBackStack("Share")
//                .commit();
//
//        toolbar.setTitle("Share");
//        setTitle("Share");

        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
            String shareMessage= "\nDownload and enjoy the e_chiro app here: ";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch(Exception e) {
            //e.toString();
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        // Handle the action from the notification
        handleNotificationAction(intent);
    }


    public void handleNotificationAction(Intent intent) {
        if (intent == null) return;

        String trigger = intent.getStringExtra("trigger");
        String action = intent.getAction();

        if ("OPEN_NOTIFICATION_FRAGMENT".equals(action) || "alerts".equals(trigger)) {
            // Build a bundle of notification details (may be null)
            Bundle bundle = new Bundle();

            if (intent.hasExtra("title")) {
                bundle.putString("title", intent.getStringExtra("title"));
            }
            if (intent.hasExtra("content")) {
                bundle.putString("content", intent.getStringExtra("content"));
            }
            if (intent.hasExtra("serviceProviderID")) {
                bundle.putString("serviceProviderID", intent.getStringExtra("serviceProviderID"));
            }
            if (intent.hasExtra("serviceProviderEmail")) {
                bundle.putString("serviceProviderEmail", intent.getStringExtra("serviceProviderEmail"));
            }

            // you can also pass a timestamp if you saved one
            NotificationsFragment notificationsFragment = new NotificationsFragment();
            notificationsFragment.setArguments(bundle);

            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, notificationsFragment, "NotificationsFragment")
                    .addToBackStack(null)
                    .commit();

            // keep toolbar consistent
            if (toolbar != null) {
                toolbar.setTitle("Notifications");
                setTitle("Notifications");
            }
        }
    }




//    public void handleNotificationAction(Intent intent) {
//        if (intent == null) return;
//
//        String trigger = intent.getStringExtra("trigger");
//        String action = intent.getAction();
//
//        if ("OPEN_NOTIFICATION_FRAGMENT".equals(action) || "alerts".equals(trigger)) {
//            fragmentManager.beginTransaction()
//                    .replace(R.id.fragment_container, NotificationsFragment.newInstance())
//                    .commit();
//        }
//    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:

                drawer.openDrawer(GravityCompat.START);

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mDrawerToggle != null) {
            mDrawerToggle.syncState();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

//    private void updateTitleAndDrawer (Fragment fragment){
//        String fragClassName = fragment.getClass().getName();
//
//        if (fragClassName.equals(MainActivity.class.getName())){
//            setTitle ("Home");
//            //set selected item position, etc
//        }
//        else if (fragClassName.equals(ProfileFragment.class.getName())){
//            setTitle ("Profile");
//            //set selected item position, etc
//        }
//        else if (fragClassName.equals(ServicesFragment.class.getName())){
//            setTitle ("Services");
//            //set selected item position, etc
//        }
//    }
}
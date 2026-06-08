package com.whebtos.e_chiro;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.navigation.NavigationView;
import com.whebtos.e_chiro.databinding.ActivityMainBinding;
import com.whebtos.e_chiro.ui.bookings.BookingsFragment;
import com.whebtos.e_chiro.ui.profile.ProfileFragment;
import com.whebtos.e_chiro.ui.providersmap.ProvidersMapFragment;
import com.whebtos.e_chiro.ui.servicecategories.ServiceCategoriesFragment;
import com.whebtos.e_chiro.ui.services.ServicesFragment;
import com.whebtos.e_chiro.ui.servicesubcategory.ServiceSubCategoriesFragment;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private FragmentManager fragmentManager;
    private FrameLayout frameLayout;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle mDrawerToggle;

        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
            {

                public void onDrawerClosed(View view)
                {
                    supportInvalidateOptionsMenu();
                    //drawerOpened = false;
                }

                public void onDrawerOpened(View drawerView)
                {
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

        ServiceCategoriesFragment serviceCategoriesFragment = new ServiceCategoriesFragment();

        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, serviceCategoriesFragment, "Home")
                .addToBackStack("home")
                .commit();
        toolbar.setTitle("Home");
        setTitle("Home");

        drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.nav_home:

                        openHome();

                        break;

                    case R.id.nav_profile:

                        openProfile();

                        break;

                    case R.id.nav_bookings:

                        openBookings();

                        break;

                }


                drawer.closeDrawer(GravityCompat.START, true);

                return true;
            }
        });

    }

    public void openHome() {

        ServiceCategoriesFragment serviceCategoriesFragment = new ServiceCategoriesFragment();

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, serviceCategoriesFragment, "Home")
                .addToBackStack("home")
                .commit();

        toolbar.setTitle("Home");
        setTitle("Home");
    }

    public void openServiceSubCategories(String id) {

        ServiceSubCategoriesFragment serviceSubCategoriesFragment = new ServiceSubCategoriesFragment();

        Bundle args = new Bundle();

        args.putString("serviceCategoryId", id);

        serviceSubCategoriesFragment.setArguments(args);

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, serviceSubCategoriesFragment, "Sub Categories")
                .addToBackStack("SubCategories")
                .commit();

        toolbar.setTitle("Sub Categories");

        setTitle("Sub Categories");
    }

    public void openProvidersMap(){

        ProvidersMapFragment providersMapFragment = new ProvidersMapFragment();

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, providersMapFragment, "ProvidersMapFragment")
                .addToBackStack("ProvidersMapFragment")
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
                .addToBackStack("Services")
                .commit();

        toolbar.setTitle("Services");

        setTitle("Sub Categories");
    }

    public void openProfile() {

        ProfileFragment profileFragment = new ProfileFragment();

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, profileFragment, "Profile")
                .addToBackStack("Profile")
                .commit();

        toolbar.setTitle("Profile");
        setTitle("Profile");
    }

    public void openBookings() {

        BookingsFragment bookingsFragment = new BookingsFragment();

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, bookingsFragment, "Bookings")
                .addToBackStack("Bookings")
                .commit();

        toolbar.setTitle("Bookings");
        setTitle("Bookings");
    }

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
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        if(mDrawerToggle!=null) {
            mDrawerToggle.syncState();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}
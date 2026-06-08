package com.whebtos.e_chiro.ui.help;

import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.whebtos.e_chiro.R;


public class HelpFragment extends Fragment {

    private HelpViewModel mViewModel;
    ListView listView;

    private String helpHeader[] =
            {
                    "Home",
                    "Profile",
                    "Switch",
                    "Services",
                    "Requests",
                    "Settings",
                    "Share",
                    "Logout"

            };

    String helpBody[] = {
            "When you land on the home page, what you see depends on whether you are a service provider or client. \n" +
                    "A service provider can visualize their rating, reviews, pending requests, revenue, views, and requests. " +
                    "Furthermore, the service providers can add a service by clicking on the add a new service button then select the categories, " +
                    "subcategories, address and working hours before submitting for consideration and addition to the platform. \n" +
                    "A client visualizes all the available services listed with their icons for their choosing, recent activities that they have selected on the application and popular services say the ones with offers et al. \n",
            "The profile is where all the details of the service providers and clients are visualized and this is where the username, contact information and profile picture are edited and updated when necessary.\n" +
                    "This is initially updated from the information given during signing up which is stored on the servers and is pulled while connected to the internet. \n",
            "The switch tool allows you to change from the service provider interface to the client interface and vice versa. This way it is easy to enjoy services whilst being a service provider conveniently without leaving the app. ",
            "The services section lists all the services that a service provider has selected as part of what they offer. These may therefore be as many as possible given it’s a group of people and may also be as few as one service given the specialization. ",
            "The requests section gives the history of the request that a client has made. This is very important for follow up purposes for the client’s own benefit and for the company’s use to improve the engagement if necessary. ",
            "Under settings, one can change their password, check their notifications, contact the company for help, go through the terms and conditions and delete the account given no more usefulness of the same. ",
            "You can easily send the application to your friends and possible clients for them to also enjoy what e_chiro has to offer. ",
            "With the logout button, all the application activities are stopped, and you’re taken back to the login page where you may log in at your convenience."

    };

    public static HelpFragment newInstance() {
        return new HelpFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_help, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HelpViewModel.class);
        // TODO: Use the ViewModel


        listView = getActivity().findViewById(R.id.listView);
        HelpAdapter customActivitiesList = new HelpAdapter((Activity) getContext(),helpHeader, helpBody);
        listView.setAdapter(customActivitiesList);

    }

}
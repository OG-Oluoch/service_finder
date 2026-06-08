package com.whebtos.e_chiro.ui.notifications;

import androidx.core.text.HtmlCompat;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.whebtos.e_chiro.R;
import com.whebtos.e_chiro.ui.authentication.LoginFragment;
import com.whebtos.e_chiro.utils.DBContext;
import com.whebtos.e_chiro.utils.DBHelper;
import com.whebtos.e_chiro.utils.DefaultSettings;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientNotificationFragment extends Fragment implements ClientNotificationAdapter.ItemClickListener {

    private DBHelper dbHelper;
    private ArrayList<Notification> notificationsList;
    private ClientNotificationViewModel mViewModel;

    private String userName;
    private String userEmail;
    private String userPhone;
    private String userID;

    private String serviceProviderId;

    private static final String PREFS_NAME = "theme_prefs";

    private Button proceed_with_service;
    private Button reject_service;

    private ClientNotificationAdapter adapter;

    public static ClientNotificationFragment newInstance() {
        return new ClientNotificationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_client_notification, container, false);

        dbHelper = new DBHelper(getContext());
        notificationsList = new ArrayList<>();

        RecyclerView recyclerView = root.findViewById(R.id.client_notificationsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ClientNotificationAdapter(getContext(), notificationsList, this);
        recyclerView.setAdapter(adapter);


        loadClientNotifications();

        return root;
    }

    private void loadClientNotifications() {
        notificationsList.clear();
        notificationsList.addAll(dbHelper.getAllNotifications());
        adapter.notifyDataSetChanged();
    }

    public void onItemClick(Notification notification) {
        // Handle item click
    }

    public void onProceedwithBidButtonClick(Notification notification) {
        String requestTitle = notification.getContent();

        Pattern pattern = Pattern.compile(
                "The Service Provider has Accepted to offer you the(.*?)service \\(Request ID (.*?)\\)"
        );

        Matcher matcher = pattern.matcher(requestTitle);

        String requestId = "";
        String offeredService = "";

        if (matcher.find()) {
            offeredService = matcher.group(1).trim();
            requestId = matcher.group(2).trim();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Accept the Service");

        String finalOfferedService = offeredService;
        String finalRequestId = requestId;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setMessage(Html.fromHtml("Request ID: <b>" + requestId + "</b><br>" +
                            "Requested Service: <b>" + finalOfferedService + "</b><br>" +
                            "Message to Service Provider: <b>I have accepted to receive your service</b>",
                    Html.FROM_HTML_MODE_LEGACY));
        } else {
            builder.setMessage(Html.fromHtml("Request ID: <b>" + requestId + "</b><br>" +
                    "Requested Service: <b>" + finalOfferedService + "</b><br>" +
                    "Message to Service Provider: <b>I have accepted to receive your service</b>"));
        }

       /* builder.setMessage(HtmlCompat.fromHtml(
                "Request ID: <b>" + requestId + "</b><br>" +
                        "Requested Service: <b>" + finalOfferedService + "</b><br>" +
                        "Message: <b>I have accepted to receive your service</b>",
                HtmlCompat.FROM_HTML_MODE_LEGACY
        ));*/


//        builder.setMessage(Html.fromHtml("Request ID: <b>" + requestId + "</b><br>" + "Requested Service: <b>" + finalOfferedService + "</b><br>" + "Message to Service Provider: <b>I have accepted to receive your service</b>"));

        builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                serviceProviderId = notification.getServiceProviderID();

                if (serviceProviderId == null || serviceProviderId.isEmpty()) {
                    Toast.makeText(getContext(), "Service Provider ID missing", Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences prefs = requireContext()
                        .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                userID = prefs.getString("user_ID", "");
                userName = prefs.getString("user_Name", "");
                userEmail = prefs.getString("user_Email", "");

                if (finalRequestId != null) {
                    if (!finalRequestId.isEmpty()) {
                       String x = serviceProviderId;
                        final String proceedNotificationURL =
                                "https://backend.wencetechnologies.com/whebtos/api/ServiceRequest/RequesterDetails/"
                                        + x + "/{requestid}/{message}?Id=" + finalRequestId
                                        + "&returnMessage=" + Uri.encode(
                                        "The Customer has accepted to receive your "
                                                + finalOfferedService + " service");



                        if (proceedNotificationURL.isEmpty()) {
                            Toast.makeText(getContext(),
                                    "The Proceed Notification URL has a problem",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            ProgressDialog proceedNotificationDialog =
                                    new ProgressDialog(getContext());
                            proceedNotificationDialog.setMessage("Proceeding with the work...");
                            proceedNotificationDialog.show();

                            StringRequest proceedStringRequest = new StringRequest(
                                    com.android.volley.Request.Method.GET,
                                    proceedNotificationURL,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            proceedNotificationDialog.dismiss();
                                            // Handle response
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            proceedNotificationDialog.dismiss();
                                            String errorMessage = (error.getMessage() != null)
                                                    ? error.getMessage() : "Unknown error";
                                            Toast.makeText(getContext(),
                                                    errorMessage, Toast.LENGTH_LONG).show();
                                        }
                                    }
                            );

                            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                            requestQueue.add(proceedStringRequest);
                        }
                    } else {
                        Toast.makeText(getContext(),
                                "Final Request ID is empty", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(),
                            "Final Request ID is null", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setTextColor(getResources().getColor(R.color.design_default_color_primary_dark));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(getResources().getColor(R.color.grey_5));
            }
        });
        dialog.show();
    }

    public void onCancelBidButtonClick(Notification notification) {
        String requestId = String.valueOf(notification.getId());
        String requestedService = notification.getTitle();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Reject Bid");
        builder.setMessage("Are you sure you want to reject the bid for Request ID: "
                + requestId + " - " + requestedService + "?");

        builder.setPositiveButton("Reject", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setTextColor(getResources().getColor(R.color.design_default_color_primary_dark));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(getResources().getColor(R.color.grey_5));
            }
        });
        dialog.show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ClientNotificationViewModel.class);
        // TODO: Use the ViewModel
    }
}

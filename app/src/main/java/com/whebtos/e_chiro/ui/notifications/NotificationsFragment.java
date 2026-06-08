package com.whebtos.e_chiro.ui.notifications;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.whebtos.e_chiro.MainActivity;
import com.whebtos.e_chiro.R;
import com.whebtos.e_chiro.ui.authentication.AccountActivity;
import com.whebtos.e_chiro.ui.authentication.LoginFragment;
import com.whebtos.e_chiro.ui.clienthome.ListItem;
import com.whebtos.e_chiro.ui.clienthome.ServicesRecyclerViewAdapter;
import com.whebtos.e_chiro.ui.requests.RequestsRecyclerViewAdapter;
import com.whebtos.e_chiro.utils.DBHelper;
import com.whebtos.e_chiro.utils.DefaultSettings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NotificationsFragment extends Fragment implements NotificationAdapter.ItemClickListener {

    private NotificationsViewModel mViewModel;
    private DBHelper dbHelper;
    private ArrayList<Notification> notificationsList;

    private String user_api = LoginFragment.user_url;
    private String userName;
    private String userEmail;
    private String userPhone;
    private String userID;

    private NotificationAdapter adapter;
    private RecyclerView recyclerView;

    private static final String PREFS_NAME = "theme_prefs";

    private Button make_a_bid;
    private Button reject_a_vid;

    public static NotificationsFragment newInstance() {
        return new NotificationsFragment();
    }

    @SuppressLint("Range")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, parent, false);

        notificationsList = new ArrayList<>();
        dbHelper = new DBHelper(getContext());

        recyclerView = root.findViewById(R.id.notificationsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new NotificationAdapter(getContext(), notificationsList, this);
        recyclerView.setAdapter(adapter);

        Bundle args = getArguments();
        if (args != null && (args.containsKey("title") || args.containsKey("content"))) {
            String nTitle = args.getString("title", "Notification");
            String nContent = args.getString("content", "");
            String nServiceProviderId = args.getString("serviceProviderID", null);
            String nServiceProviderEmail = args.getString("serviceProviderEmail", null);

            AlertDialog.Builder detailBuilder = new AlertDialog.Builder(getContext());
            detailBuilder.setTitle(nTitle);

            StringBuilder messageBuilder = new StringBuilder();
            messageBuilder.append(nContent);

            if (nServiceProviderId != null) {
                messageBuilder.append("\n\nService Provider ID: ").append(nServiceProviderId);
            }
            if (nServiceProviderEmail != null) {
                messageBuilder.append("\nService Provider Email: ").append(nServiceProviderEmail);
            }

            detailBuilder.setMessage(messageBuilder.toString());
            detailBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    dbHelper.insertNotification(nTitle, nContent, nServiceProviderId, nServiceProviderEmail);
                    loadNotificationsFromDB();
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = detailBuilder.create();
            dialog.show();
        }

        return root;
    }

    private void loadNotificationsFromDB() {
        notificationsList.clear();
        notificationsList.addAll(dbHelper.getAllNotifications());
        adapter.notifyDataSetChanged();
    }

    public void onItemClick(Notification notification) {

    }

    public void onMakeBidButtonClick(Notification notification) {
        String requestTitle = notification.getContent();


        Pattern pattern = Pattern.compile(
                "Request ID[\\n: ]*(.*?):[\\n ]*Service required!! Someone is seeking (.*?) services[.] The client is ([\\d.]+) km from you"
        );

        Matcher matcher = pattern.matcher(requestTitle);

        String requestId = "";
        String requestedService = "";
        String distanceFromClient = "";

        if (matcher.find()) {
            requestId = matcher.group(1).trim();
            requestedService = matcher.group(2).trim();
            distanceFromClient = matcher.group(3).trim();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Bid for the work");
        builder.setMessage("Request ID: " + requestId +
                "\nRequested Service: " + requestedService +
                "\nDistance from Client: " + distanceFromClient);

        String finalRequestedService = requestedService;
        String finalRequestId = requestId;

        builder.setPositiveButton("Make a Bid", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences sharedPreferences =
                        getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                userID = sharedPreferences.getString("user_ID", "");
                userName = sharedPreferences.getString("user_Name", "");
                userEmail = sharedPreferences.getString("user_Email", "");
                userPhone = sharedPreferences.getString("user_Phone", "");

                if (userID.isEmpty() || userName.isEmpty()) {
                    Toast.makeText(getContext(),
                            "User details not found. Please login again.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                final String clientNotificatioURL =
                        "https://backend.wencetechnologies.com/whebtos/api/ServiceRequest/MessageBack/"
                                + userName + "/{requestid}/{message}?Id=" + finalRequestId
                                + "&returnMessage=The Service Provider has Accepted to offer you the "
                                + finalRequestedService + " service";



                if (clientNotificatioURL.isEmpty()) {
                    Toast.makeText(getContext(),
                            "The Client Notification URL has a problem",
                            Toast.LENGTH_SHORT).show();
                } else {
                    ProgressDialog clientNotificationDialog =
                            new ProgressDialog(getContext());
                    clientNotificationDialog.setMessage("Sending bid to client...");
                    clientNotificationDialog.show();

                    StringRequest stringRequest = new StringRequest(
                            Request.Method.GET,
                            clientNotificatioURL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    clientNotificationDialog.dismiss();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    clientNotificationDialog.dismiss();
//                                    Toast.makeText(getContext(),
//                                            error.getMessage(), Toast.LENGTH_LONG).show();

                                    String errorMessage = (error.getMessage() != null && !error.getMessage().isEmpty())
                                            ? error.getMessage()
                                            : "An unexpected network error occurred. Please try again.";
                                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show();

                                }
                            }
                    );

                    RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                    requestQueue.add(stringRequest);
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

    public void onRejectBidButtonClick(Notification notification) {
        String requestId = notification.getId();
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
        mViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
    }

    public void openNotification() {
        ((MainActivity) getActivity()).openNotifications();
    }
}

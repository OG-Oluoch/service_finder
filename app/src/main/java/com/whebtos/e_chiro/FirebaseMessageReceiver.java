//package com.whebtos.e_chiro;
//
//import android.annotation.SuppressLint;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.Intent;
//import android.database.sqlite.SQLiteDatabase;
//import android.os.Build;
//import android.util.Log;
//
//import androidx.core.app.NotificationCompat;
//import com.google.firebase.messaging.FirebaseMessagingService;
//import com.google.firebase.messaging.RemoteMessage;
//import com.whebtos.e_chiro.MainActivity; // Import your MainActivity class
//import com.whebtos.e_chiro.R;
//import com.whebtos.e_chiro.utils.DBContext;
//import com.whebtos.e_chiro.utils.DefaultSettings;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.Map;

//public class FirebaseMessageReceiver extends FirebaseMessagingService {
//
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        // Process the notification message
//        if (remoteMessage.getNotification() != null) {
//            String title = remoteMessage.getNotification().getTitle();
//            String message = remoteMessage.getNotification().getBody();
//
//            // Save the notification to the database
//            saveNotificationToDatabase(title, message, remoteMessage.getData());
//
//            // Display the notification
//            showNotification(title, message);
//        }
//
//        // Process the data payload if present
//        if (remoteMessage.getData().size() > 0) {
//            // You can access additional data here
//            Map<String, String> data = remoteMessage.getData();
//            // Handle the custom data as needed
//
//        }
//
//    }
//
//    private void saveNotificationToDatabase(String title, String message, Map<String, String> data) {
//        DBContext dbContext = new DBContext(getApplicationContext());
//        SQLiteDatabase sqLiteDatabase = dbContext.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//
//        contentValues.put("title", title);
//        contentValues.put("content", message);
//
//        Date date = Calendar.getInstance().getTime();
//        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
//        String strDate = dateFormat.format(date);
//
//        contentValues.put("date_created", strDate);
//
//        // Save additional data from the data map
//        if (data.containsKey("value[0]")) {
//            String serviceProviderID = data.get("value[0]");
//            contentValues.put("serviceproviderid", serviceProviderID);
//        }
//
//        if (data.containsKey("value[1]")) {
//            String serviceProviderEmail = data.get("value[1]");
//            contentValues.put("serviceprovideremail", serviceProviderEmail);
//        }
//
//        long recId = sqLiteDatabase.insert(DefaultSettings.ALERTS_TABLE, null, contentValues);
//        sqLiteDatabase.close();
//    }
//
//
////    private void saveNotificationToDatabase(String title, String message) {
////        DBContext dbContext = new DBContext(getApplicationContext());
////        SQLiteDatabase sqLiteDatabase = dbContext.getWritableDatabase();
////        ContentValues contentValues = new ContentValues();
////
////        contentValues.put("title", title);
////        contentValues.put("content", message);
////
////        Date date = Calendar.getInstance().getTime();
////        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
////        String strDate = dateFormat.format(date);
////
////        contentValues.put("date_created", strDate);
////
////        long recId = sqLiteDatabase.insert(DefaultSettings.ALERTS_TABLE, null, contentValues);
////        sqLiteDatabase.close();
////    }
//
//
//    private void showNotification(String title, String message) {
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtra("trigger", "alerts");
//        intent.setAction("OPEN_NOTIFICATION_FRAGMENT");
//
//        // Use unique notification ID
//        int notificationId = generateUniqueId();
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(
//                this,
//                notificationId, // Use the notification ID as the requestCode
//                intent,
//                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE
//        );
//
//        String channel_id = "echiro";
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channel_id)
//                .setSmallIcon(R.drawable.logo)
//                .setContentTitle(title)
//                .setContentText(message)
//                .setAutoCancel(true)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
//                .setOnlyAlertOnce(true)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel notificationChannel = new NotificationChannel(
//                    channel_id,
//                    "echiro",
//                    NotificationManager.IMPORTANCE_HIGH
//            );
//            notificationManager.createNotificationChannel(notificationChannel);
//        }
//
//        notificationManager.notify(notificationId, builder.build());
//
//        // Log for debugging
//        Log.d("Notification", "Notification sent with ID: " + notificationId);
//    }
//
//    // Generate a unique ID for notifications
//    private int generateUniqueId() {
//        return (int) System.currentTimeMillis();
//    }
//}

//    private void showNotification(String title, String message) {
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtra("trigger", "alerts");
//        intent.setAction("OPEN_NOTIFICATION_FRAGMENT");
//
//        // Use unique notification ID
//        int notificationId = generateUniqueId();
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(
//                this,
//                0,
//                intent,
//                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE
//        );
//
//        String channel_id = "echiro";
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channel_id)
//                .setSmallIcon(R.drawable.logo)
//                .setContentTitle(title)
//                .setContentText(message)
//                .setAutoCancel(true)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
//                .setOnlyAlertOnce(true)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel notificationChannel = new NotificationChannel(
//                    channel_id,
//                    "echiro",
//                    NotificationManager.IMPORTANCE_HIGH
//            );
//            notificationManager.createNotificationChannel(notificationChannel);
//        }
//
//        notificationManager.notify(notificationId, builder.build());
//
//        // Log for debugging
//        Log.d("Notification", "Notification sent with ID: " + notificationId);
//    }
//
//    // Generate a unique ID for notifications
//    private int generateUniqueId() {
//        return (int) System.currentTimeMillis();
//    }
//


//    private void showNotification(String title, String message) {
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtra("trigger", "alerts");
//        intent.setAction("OPEN_NOTIFICATION_FRAGMENT");
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//
//        String channel_id = "echiro";
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channel_id)
//                .setSmallIcon(R.drawable.logo)
//                .setContentTitle(title)
//                .setContentText(message)
//                .setAutoCancel(true)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
//                .setOnlyAlertOnce(true)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel notificationChannel = new NotificationChannel(
//                    channel_id,
//                    "echiro",
//                    NotificationManager.IMPORTANCE_HIGH
//            );
//            notificationManager.createNotificationChannel(notificationChannel);
//        }
//
//        notificationManager.notify(0, builder.build());
//    }



//
//import android.annotation.SuppressLint;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.Intent;
//import android.database.sqlite.SQLiteDatabase;
//import android.os.Build;
//import android.widget.RemoteViews;
//
//import androidx.core.app.NotificationCompat;
//
//import com.google.firebase.messaging.FirebaseMessagingService;
//import com.google.firebase.messaging.RemoteMessage;
//import com.whebtos.e_chiro.utils.DBContext;
//import com.whebtos.e_chiro.utils.DefaultSettings;
//
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//
//public class FirebaseMessageReceiver extends FirebaseMessagingService {
//    // Override onMessageReceived() method to extract the
//    // title and
//    // body from the message passed in FCM
//    @Override
//    public void
//    onMessageReceived(RemoteMessage remoteMessage) {
//        // First case when notifications are received via
//        // data event
//        // Here, 'title' and 'message' are the assumed names
//        // of JSON
//        // attributes. Since here we do not have any data
//        // payload, This section is commented out. It is
//        // here only for reference purposes.
//        /*if(remoteMessage.getData().size()>0){
//            showNotification(remoteMessage.getData().get("title"),
//                          remoteMessage.getData().get("message"));
//        }*/
//
//        // Second case when notification payload is
//        // received.
//        if (remoteMessage.getNotification() != null) {
//            // Since the notification is received directly from
//            // FCM, the title and the body can be fetched
//            // directly as below.
//            showNotification(
//                    remoteMessage.getNotification().getTitle(),
//                    remoteMessage.getNotification().getBody());
//
//            DBContext dbContext = new DBContext(getApplicationContext());
//
//            SQLiteDatabase sqLiteDatabase = dbContext.getWritableDatabase();
//
//            ContentValues contentValues = new ContentValues();
//
//            contentValues.put("title", remoteMessage.getNotification().getTitle());
//
//            contentValues.put("content", remoteMessage.getNotification().getBody());
//
//            Date date = Calendar.getInstance().getTime();
//
//            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
//
//            String strDate = dateFormat.format(date);
//
//            contentValues.put("date_created", strDate);
//
//            Long recId = sqLiteDatabase.insert(DefaultSettings.ALERTS_TABLE, null, contentValues);
//        }
//    }
//
//    // Method to get the custom Design for the display of
//    // notification.
//    private RemoteViews getCustomDesign(String title,
//                                        String message) {
//        @SuppressLint("RemoteViewLayout") RemoteViews remoteViews = new RemoteViews(
//                getApplicationContext().getPackageName(),
//                R.layout.card_notification);
//        remoteViews.setTextViewText(R.id.tv_title, title);
//        remoteViews.setTextViewText(R.id.tv_description, message);
//        return remoteViews;
//    }
//
//    // Method to display the notifications
//    public void showNotification(String title,
//                                 String message) {
//        // Pass the intent to switch to the MainActivity
//        Intent intent = new Intent(this, MainActivity.class);
//
//        intent.putExtra("trigger", "alerts");
//
//        // Assign channel ID
//        String channel_id = "echiro";
//        // Here FLAG_ACTIVITY_CLEAR_TOP flag is set to clear
//        // the activities present in the activity stack,
//        // on the top of the Activity that is to be launched
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        // Pass the intent to PendingIntent to start the
//        // next Activity
//        intent.setAction("OPEN_NOTIFICATION_FRAGMENT");
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,PendingIntent.FLAG_ONE_SHOT);
//
//        // Create a Builder object using NotificationCompat
//        // class. This will allow control over all the flags
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),channel_id)
//                .setSmallIcon(R.drawable.logo)
//                .setAutoCancel(true)
//                .setPriority(NotificationCompat.PRIORITY_HIGH) // Set notification priority to high
//                .setVibrate(new long[]{1000, 1000, 1000,
//                        1000, 1000})
//                .setOnlyAlertOnce(true)
//                .setContentIntent(pendingIntent);
//
//        // A customized design for the notification can be
//        // set only for Android versions 4.1 and above. Thus
//        // condition for the same is checked here.
//        if (Build.VERSION.SDK_INT
//                >= Build.VERSION_CODES.JELLY_BEAN) {
//            builder = builder.setContent(
//                    getCustomDesign(title, message));
//        } // If Android Version is lower than Jelly Beans,
//        // customized layout cannot be used and thus the
//        // layout is set as follows
//        else {
//            builder = builder.setContentTitle(title)
//                    .setContentText(message)
//                    .setSmallIcon(R.drawable.logo);
//        }
//        // Create an object of NotificationManager class to
//        // notify the
//        // user of events that happen in the background.
//        NotificationManager notificationManager
//                = (NotificationManager) getSystemService(
//                Context.NOTIFICATION_SERVICE);
//
//        // Check if the Android Version is greater than Oreo
//        if (Build.VERSION.SDK_INT
//                >= Build.VERSION_CODES.O) {
//            NotificationChannel notificationChannel
//                    = new NotificationChannel(
//                    channel_id, "konza",
//                    NotificationManager.IMPORTANCE_HIGH);
//            notificationManager.createNotificationChannel(
//                    notificationChannel);
//        }
//
//        notificationManager.notify(0, builder.build());
//    }
//}

package com.whebtos.e_chiro;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.whebtos.e_chiro.ui.authentication.AccountActivity;
import com.whebtos.e_chiro.utils.DBContext;
import com.whebtos.e_chiro.utils.DBHelper;
import com.whebtos.e_chiro.utils.DefaultSettings;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FirebaseMessageReceiver extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Process the notification message
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String message = remoteMessage.getNotification().getBody();
            String serviceProviderID = null;
            String serviceProviderEmail = null;


            if (remoteMessage.getData().size() > 0) {
                // Extract serviceProviderID and serviceProviderEmail from the data payload if present
                Map<String, String> data = remoteMessage.getData();

                if (data.containsKey("ID")) {
                    serviceProviderID = data.get("ID");
                }

                if (data.containsKey("Email")) {
                    serviceProviderEmail = data.get("Email");
                }


           }

            saveNotificationToDatabase(title, message, serviceProviderID, serviceProviderEmail);

            Log.d("FCM", "Received Notification -> ID: " + serviceProviderID
                    + ", Email: " + serviceProviderEmail);

            // Save the notification to the database
            // Display the notification
            showNotification(title, message,serviceProviderID,serviceProviderEmail);
        }
    }

    private void saveNotificationToDatabase(String title, String message,
                                            String serviceProviderID, String serviceProviderEmail) {
        DBHelper dbHelper = new DBHelper(getApplicationContext());

        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String strDate = dateFormat.format(date);

        dbHelper.insertNotification(title, message, serviceProviderID, serviceProviderEmail);
    }

    private void showNotification(String title, String message, String serviceProviderID, String serviceProviderEmail) {



        Intent intent = new Intent(this, MainActivity.class);
            intent.setAction("OPEN_NOTIFICATION_FRAGMENT"); // optional action

//       Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("trigger", "alerts");
        intent.putExtra("title", title);
        intent.putExtra("content", message);
        intent.putExtra("serviceProviderID", serviceProviderID);
        intent.putExtra("serviceProviderEmail", serviceProviderEmail);

        intent.setAction("OPEN_NOTIFICATION_FRAGMENT");

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        // Use unique notification ID
        int notificationId = generateUniqueId();

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                notificationId, // Use the notification ID as the requestCode
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        String channelId = "echiro";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel =
                    new NotificationChannel(channelId, "echiro", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationManager.notify(notificationId, builder.build());

        // Log for debugging
        Log.d("Notification", "Notification sent with ID: " + notificationId);
    }

    // Generate a unique ID for notifications
    private int generateUniqueId() {
        return (int) System.currentTimeMillis();
    }
}


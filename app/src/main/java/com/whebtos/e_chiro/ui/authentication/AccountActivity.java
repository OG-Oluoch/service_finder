package com.whebtos.e_chiro.ui.authentication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.Task;
import com.whebtos.e_chiro.ui.notifications.NotificationsFragment;
import com.whebtos.e_chiro.utils.DBContext;
import com.whebtos.e_chiro.utils.DefaultSettings;
import com.whebtos.e_chiro.MainActivity;
import com.whebtos.e_chiro.R;
import com.whebtos.e_chiro.models.User;

import org.json.JSONObject;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class AccountActivity extends AppCompatActivity {

    private FragmentTransaction fragmentTransaction = null;

    private static final String TAG = "AccountActivity";

    private static final int RC_SIGN_IN = 1;

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_account);

//        handleSSLHandshake();

            openLogin();



    }

    public void openLogin() {

        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container, new LoginFragment());

        fragmentTransaction.commit();

    }

    public void openGoogleSignIn() {

        Toast.makeText(getApplicationContext(), "We are working on this to enable you to have the best experience", Toast.LENGTH_LONG).show();

        //Intent signInIntent = mGoogleSignInClient.getSignInIntent();

        //startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void goToApplication() {

        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        goToApplication();
    }

    public void getToken(String fcm) {

        DBContext dbContext = new DBContext(getApplicationContext());

        SQLiteDatabase db = dbContext.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + DefaultSettings.LOGINS_TABLE, null);

        if (cursor.moveToFirst()) {

            User user = new User();

            user.setEmailAddress(cursor.getString(1));

            JSONObject jsonObject = new JSONObject();
        }
    }

    public void openSignUp() {

        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container, new SignUpFragment());

        fragmentTransaction.commit();

    }

    public void openForgotPassword() {

        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container, new ForgotPasswordFragment());

        fragmentTransaction.commit();

    }

    public  void openNotification(){
        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container, new NotificationsFragment());

        fragmentTransaction.commit();
    }


    public void register(User user) {

    }

}

package com.whebtos.e_chiro.ui.settings;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.whebtos.e_chiro.MainActivity;
import com.whebtos.e_chiro.R;
import com.whebtos.e_chiro.ui.authentication.AccountActivity;
import com.whebtos.e_chiro.ui.authentication.ForgotPasswordFragment;


public class SettingsFragment extends Fragment {

    private ConstraintLayout edit_profile;
    private ConstraintLayout change_password;
    private ConstraintLayout check_notification;
    private ConstraintLayout contact_us;
    private ConstraintLayout terms_and_conditions;
    private ConstraintLayout delete_account;

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_settings, parent, false);

//        edit_profile = root.findViewById(R.id.cl_edit_my_profile);
//
//        edit_profile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getActivity(), "You will be redirected to your Profile Shortly", Toast.LENGTH_SHORT).show();
//            }
//        });

        change_password = root.findViewById(R.id.cl_change_password);

        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "You will be redirected to password settings Shortly", Toast.LENGTH_SHORT).show();

                openForgotPassword();
            }
        });

        check_notification = root.findViewById(R.id.cl_notifications);

        check_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "You will be redirected to your notifications Shortly", Toast.LENGTH_SHORT).show();
                ((MainActivity) getActivity()).openNotifications();
            }
        });

        contact_us = root.findViewById(R.id.cl_contact_us);

        contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "You will be redirected to our contacts Shortly", Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse("https://wa.me/254702340785");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        terms_and_conditions = root.findViewById(R.id.cl_terms_and_conditions);

        terms_and_conditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "You will be redirected to your terms and conditions Shortly", Toast.LENGTH_SHORT).show();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.whebtos.com"));
                startActivity(browserIntent);
            }
        });

        delete_account = root.findViewById(R.id.cl_delete);

        delete_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Kindly let us know what we can improve as you delete the account, thanks.", Toast.LENGTH_SHORT).show();
                //deleteuser();
            }
        });

        return root;
    }



//    private void deleteuser(String email, String password) {
//
//        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//        // Get auth credentials from the user for re-authentication. The example below shows
//        // email and password credentials but there are multiple possible providers,
//        // such as GoogleAuthProvider or FacebookAuthProvider.
//        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
//
//        // Prompt the user to re-provide their sign-in credentials
//        if (user != null) {
//            user.reauthenticate(credential)
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            user.delete()
//                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            if (task.isSuccessful()) {
//                                                Log.d("TAG", "User account deleted.");
//                                                startActivity(new Intent(getContext(), MainActivity.class));
//                                                Toast.makeText(getContext(), "Deleted User Successfully,", Toast.LENGTH_LONG).show();
//                                            }
//                                        }
//                                    });
//                        }
//                    });
//        }
//    }
        @Override
        public void onDestroyView() {
            super.onDestroyView();
        }


    public void openForgotPassword() {
        ForgotPasswordFragment forgotPasswordFragment= new ForgotPasswordFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(getId(), forgotPasswordFragment, "ForgotPasswordFragment")
                .addToBackStack(null)
                .commit();
        }

}

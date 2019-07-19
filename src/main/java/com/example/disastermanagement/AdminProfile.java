package com.example.disastermanagement;

import android.app.Activity;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminProfile extends Fragment {
    Activity activity;
    TextView adminChangePassTv;

    String currentEmail, currentPass, newPass;
    EditText adminCurrentEmailEt, adminCurrentPassEt, adminNewPassEt;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("Which ","OnCreateView");
        return inflater.inflate(R.layout.activity_admin_profile, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_admin_profile);
    }

    @Override
    public void onStart() {
        super.onStart();

        activity = getActivity();
        adminChangePassTv = getActivity().findViewById(R.id.adminChangePassTv);

       adminChangePassTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customDialog();
            }
        });
    }

    public void customDialog()
    {
        // custom dialog
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.change_admin_pass_dialog);
        dialog.setTitle("Title...");


        // set the custom dialog components - text, image and button
        adminCurrentEmailEt = dialog.findViewById(R.id.adminCurrentEmailEt);
        adminCurrentPassEt = dialog.findViewById(R.id.adminCurrentPassEt);
        adminNewPassEt = dialog.findViewById(R.id.adminNewPassEt);


        Button changeButton = dialog.findViewById(R.id.changeBtn);
        // if button is clicked, close the custom dialog
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentEmail = adminCurrentEmailEt.getText().toString().trim();
                currentPass = adminCurrentPassEt.getText().toString().trim();
                newPass = adminNewPassEt.getText().toString().trim();

                firebaseChangePass();
            }
        });
        dialog.show();
    }

    public void firebaseChangePass()
    {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

// Get auth credentials from the user for re-authentication. The example below shows
// email and password credentials but there are multiple possible providers,
// such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider
                .getCredential(currentEmail, currentPass);

// Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("", "Password updated");
                                        Toast.makeText(getActivity(), "Password updated", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getActivity(), "Error password not updated", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Log.d("", "Error auth failed");
                            Toast.makeText(getActivity(), "Error auth failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

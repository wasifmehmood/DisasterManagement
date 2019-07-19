package com.example.disastermanagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends Activity {

    private static int SPLASH_TIME_OUT = 2000;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        final FirebaseUser currentUser = mAuth.getCurrentUser();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(currentUser == null) {

                    Intent splashIntent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(splashIntent);
                    finish();
                }

                else if (currentUser != null) {

                    currentEmail = currentUser.getEmail();
                    readFromAdminDatabase(currentEmail);
                }

                else {

                }
            }
        },SPLASH_TIME_OUT);
    }

    String email;
    String currentEmail;
    void readFromAdminDatabase(final String getEmailStr)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Admin");
        reference.orderByChild("email").equalTo(getEmailStr).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        HashMap<String, String> map = (HashMap<String, String>) snapshot.getValue();

                        email = map.get("email");

                        Toast.makeText(MainActivity.this, ""+email, Toast.LENGTH_SHORT).show();

                        Intent splashIntent = new Intent(MainActivity.this, AdminNavDrawer.class);
                        startActivity(splashIntent);
                        finish();

                    }

                    return;
                }

                Intent splashIntent = new Intent(MainActivity.this, VolunteerNavDrawer.class);
                startActivity(splashIntent);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}

package com.example.disastermanagement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    EditText loginEmailEt, loginPassEt;
    Button loginBtn;
    String getEmailStr, getPassStr;
    FirebaseUser currentUser;
    NavigationView navigationView;
    TextView createAccTv;
    Singleton ls = Singleton.getInstance();
    String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmailEt = findViewById(R.id.loginUsernameEt);
        loginPassEt = findViewById(R.id.loginPassEt);
        loginBtn = findViewById(R.id.loginBtn);

        navigationView = findViewById(R.id.nav_view);


    }

    @Override
    protected void onStart() {
        super.onStart();

        currentUser = ls.mAuth.getCurrentUser();

        createAccTv = findViewById(R.id.createAccTv);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEmailStr = loginEmailEt.getText().toString();
                getPassStr = loginPassEt.getText().toString();

                try {


//                    if (getEmailStr.equals("disastermanagement50@gmail.com") && getPassStr.equals("firebase")) {
//                        login();
//                        Intent i = new Intent(LoginActivity.this, AdminNavDrawer.class);
//                        startActivity(i);
//                        finish();
//
//                    } else {

                        login();

//                    }
                }
                catch (Exception e)
                {
                    if(loginEmailEt.getText().toString().equals("") && loginPassEt.getText().toString().equals(""))
                    {
                        loginEmailEt.setError("Empty");
                        loginPassEt.setError("Empty");
                    }
                    else if(loginEmailEt.getText().toString().equals(""))
                    {
                        loginEmailEt.setError("Empty");
                    }
                    else if(loginPassEt.getText().toString().equals(""))
                    {
                        loginPassEt.setError("Empty");
                    }
                }
            }
        });

        createAccTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(LoginActivity.this, Select.class);
                startActivity(i);
                finish();
                Toast.makeText(LoginActivity.this, "Email: "+getEmailStr+" Pass:"+getPassStr, Toast.LENGTH_SHORT).show();


            }
        });

    }

    public void login() {

            //Sign In
            ls.mAuth.signInWithEmailAndPassword(getEmailStr, getPassStr)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("", "signInWithEmail:onComplete:" + task.isSuccessful());


                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this,
                                        "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                            else {

                                readFromAdminDatabase(getEmailStr);
                            }

                        }
                    });


    }

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

                        Toast.makeText(LoginActivity.this, ""+email, Toast.LENGTH_SHORT).show();

                        login2(getEmailStr);


                    }

                    return;
                }

                Intent i = new Intent(LoginActivity.this, VolunteerNavDrawer.class);
                startActivity(i);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(LoginActivity.this, "err"+databaseError, Toast.LENGTH_SHORT).show();

            }
        });
    }

    void login2(String getEmailStr)
    {

        if(getEmailStr.equals(email)) {

            Intent i = new Intent(LoginActivity.this, AdminNavDrawer.class);
            startActivity(i);
            finish();
        }

    }
}

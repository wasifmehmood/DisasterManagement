package com.example.disastermanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Select extends AppCompatActivity {

    Button adminBtn, volunteerBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        adminBtn = findViewById(R.id.adminBtn);
        volunteerBtn = findViewById(R.id.volunteerBtn);

        adminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Select.this, CreateAdminAccount.class);
                startActivity(i);
                finish();
            }
        });

        volunteerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Select.this, CreateAccount.class);
                startActivity(i);
                finish();
//                Toast.makeText(this, "Email: "+getEmailStr+" Pass:"+getPassStr, Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}

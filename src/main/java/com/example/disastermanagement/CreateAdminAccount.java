package com.example.disastermanagement;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

public class CreateAdminAccount extends AppCompatActivity {

    Singleton ls = Singleton.getInstance();

    EditText createEmailEt, createPassEt, createConfirmPassEt;
    String emailStr, passStr, confirmPassStr;
    Button createAccBtn;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_admin_account);

        createEmailEt = findViewById(R.id.createAdminEmailEt);
        createPassEt = findViewById(R.id.createAdminPassEt);
        createConfirmPassEt = findViewById(R.id.createAdminConfirmPassEt);
        createAccBtn = findViewById(R.id.createAdminAccBtn);


        createAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createAcc();
            }
        });

    }

    public void createAcc()
    {
        emailStr = createEmailEt.getText().toString().trim();
        passStr = createPassEt.getText().toString().trim();
        confirmPassStr = createConfirmPassEt.getText().toString().trim();

        if (passStr.equals(confirmPassStr))
        {
            ls.mAuth.createUserWithEmailAndPassword(emailStr, passStr)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("", "createUserWithEmail:onComplete:" + task.isSuccessful());

                            writeToDatabase(emailStr);

                            Intent i = new Intent(CreateAdminAccount.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                            if (!task.isSuccessful()) {
                                Toast.makeText(CreateAdminAccount.this,
                                        "Authentication failed.", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

        }

        else{
            createConfirmPassEt.setError("Password don't match");
        }
    }

    public void writeToDatabase(String emailStr)
    {
        Map map = new HashMap<>();
        map.put("email", emailStr);

        reference = ls.database.getReference().child("Admin").push();
        reference.setValue(map);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(this, Select.class);
        startActivity(i);
        finish();
    }
}

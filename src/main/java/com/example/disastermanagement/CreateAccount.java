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
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class CreateAccount extends AppCompatActivity {

    Singleton ls = Singleton.getInstance();

    EditText createEmailEt, createPassEt, createConfirmPassEt;
    String emailStr, passStr, confirmPassStr;
    Button createAccBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        createEmailEt = findViewById(R.id.createEmailEt);
        createPassEt = findViewById(R.id.createPassEt);
        createConfirmPassEt = findViewById(R.id.createConfirmPassEt);
        createAccBtn = findViewById(R.id.createAccBtn);


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

//                            writeToDatabase(emailStr);

                            Intent i = new Intent(CreateAccount.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                            if (!task.isSuccessful()) {
                                Toast.makeText(CreateAccount.this,
                                        "Authentication failed.", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

        }

        else{
            createConfirmPassEt.setError("Password don't match");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(this, Select.class);
        startActivity(i);
        finish();
    }
}

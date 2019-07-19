package com.example.disastermanagement;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class VolunteerProfile extends Fragment {

    Singleton ls = Singleton.getInstance();
    DatabaseReference reference;
    String nameStr, addressStr, cityStr, contactStr, emailStr, disasterIdStr;
    EditText volunteerNameEt, volunteerAddressEt, volunteerCityEt, volunteerContactEt, volunteerEmailEt, volunteerDisasterIdSpinner, volunteerDisasterIdEt;
    Button saveBtn;
    Spinner volunteerDisasterSpinner;

    VolunteerHome volunteerHome;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("Which ","OnCreateView");
        return inflater.inflate(R.layout.activity_volunteer_profile, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_volunteer_profile);

        volunteerHome = new VolunteerHome();
        volunteerUtilsList = new ArrayList<>();

    }
TextView volunteerChangePassTv;
    Activity activity;
    @Override
    public void onStart() {
        super.onStart();

        activity = getActivity();
        volunteerNameEt = getActivity().findViewById(R.id.volunteerNameEt);
        volunteerAddressEt = getActivity().findViewById(R.id.volunteerAddressEt);
        volunteerCityEt = getActivity().findViewById(R.id.volunteerCityEt);
        volunteerContactEt = getActivity().findViewById(R.id.volunteerContactEt);
        volunteerEmailEt = getActivity().findViewById(R.id.volunteerEmailEt);
        volunteerDisasterIdEt = getActivity().findViewById(R.id.volunteerDisasterIdEt);

        volunteerChangePassTv = getActivity().findViewById(R.id.volunteerChangePassTv);

        saveBtn = getActivity().findViewById(R.id.saveAndSendBtn);

        addListenerOnSpinnerItemSelection(volunteerDisasterIdEt);


            readFromDatabase();

        disasterIdStr = ls.spinnerItem1;

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fieldsCheck();

            }
        });

        volunteerChangePassTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customDialog();
            }
        });
    }
    String currentEmail, currentPass, newPass;
    EditText volCurrentEmailEt, volCurrentPassEt, volNewPassEt;
    public void customDialog()
    {
        // custom dialog
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.change_pass_dialog);
        dialog.setTitle("Title...");


        // set the custom dialog components - text, image and button
        volCurrentEmailEt = dialog.findViewById(R.id.volunteerCurrentEmailEt);
        volCurrentPassEt = dialog.findViewById(R.id.volunteerCurrentPassEt);
        volNewPassEt = dialog.findViewById(R.id.volunteerNewPassEt);


        Button changeButton = dialog.findViewById(R.id.changeBtn);
        // if button is clicked, close the custom dialog
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentEmail = volCurrentEmailEt.getText().toString().trim();
                currentPass = volCurrentPassEt.getText().toString().trim();
                newPass = volNewPassEt.getText().toString().trim();

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
                                        Log.d("", "Error password not updated");
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

    public void addListenerOnSpinnerItemSelection(EditText disasterId) {

        volunteerDisasterSpinner = getActivity().findViewById(R.id.volunteerDisasterIdSpinner);
        volunteerDisasterSpinner.setOnItemSelectedListener(new volunteerOnItemSelectedListener(disasterId));
    }


    boolean nameCheck, addressCheck, cityCheck, contactCheck, emailCheck;

    public void fieldsCheck()
    {
        nameStr = volunteerNameEt.getText().toString();
        addressStr = volunteerAddressEt.getText().toString();
        cityStr = volunteerCityEt.getText().toString();
        contactStr = volunteerContactEt.getText().toString();
        emailStr = volunteerEmailEt.getText().toString();

        nameCheck = (nameStr.length() > 3);
        addressCheck = (addressStr.length() > 2);
        contactCheck = isValidMobile(contactStr);
        emailCheck = isEmailValid(emailStr);
        cityCheck = true;

        boolean isFilled = true;

        if(!(nameCheck))
        {
            volunteerNameEt.setError("Must be greater than 3");
            isFilled=false;
        }

        if(!(addressCheck))
        {
            volunteerAddressEt.setError("Must be greater than 5");
            isFilled=false;
        }

        if (!(cityCheck))
        {
            volunteerCityEt.setError("Invalid");
            isFilled=false;
        }

        if (!(contactCheck))
        {
            volunteerContactEt.setError("Invalid");
            isFilled=false;
        }

        if (!(emailCheck))
        {
            volunteerEmailEt.setError("Invalid");
            isFilled=false;

        }
        if(isFilled)
        {
            if (volunteerDisasterIdEt.getVisibility() == View.VISIBLE)
            {
                disasterIdStr = volunteerDisasterIdEt.getText().toString();
                writeToDatabase();
                ls.readCheck = true;
            }
            else
            {
                disasterIdStr = ls.spinnerItem1;
                writeToDatabase();
                ls.readCheck = true;
                Toast.makeText(getActivity(), "Written to Database", Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(getActivity(), "Written to Database", Toast.LENGTH_SHORT).show();
        }
    }

    public void writeToDatabase()
    {
        Map map = new HashMap<>();
        map.put("name", nameStr);
        map.put("address", addressStr);
        map.put("city", cityStr);
        map.put("contact", contactStr);
        map.put("email", emailStr);
        map.put("disaster_id", disasterIdStr);
        map.put("isResponded", "none");
        map.put("date", getCurrentDate());

        reference = ls.database.getReference().child("Volunteers").child(nameStr);
        reference.updateChildren(map);

    }
    public String getCurrentDate() {

        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getActivity().getApplicationContext());
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date today = Calendar.getInstance().getTime();
        return dateFormat.format(today);
    }

    List<VolunteerUtils> volunteerUtilsList;

    public void readFromDatabase()
    {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Volunteers");
        reference.orderByChild("email").equalTo(ls.mAuth.getCurrentUser().getEmail()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        HashMap<String, String> map = (HashMap<String, String>) snapshot.getValue();
                        volunteerUtilsList.add(new VolunteerUtils(map.get("name"), map.get("address"), map.get("city"), map.get("contact")
                                ,map.get("email"),map.get("disaster_id"),map.get("date")));

                        volunteerNameEt.setText(map.get("name"));
                        volunteerAddressEt.setText(map.get("address"));
                        volunteerCityEt.setText(map.get("city"));
                        volunteerContactEt.setText(map.get("contact"));
                        volunteerEmailEt.setText(map.get("email"));

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("", "Failed to read value.", error.toException());
            }
        });

    }


    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidMobile(String phone) {
        boolean check;

            if(phone.length() < 6 || phone.length() > 13)
            {
                check = false;
            }
            else
                {
                check = true;
            }

        return check;
    }

}

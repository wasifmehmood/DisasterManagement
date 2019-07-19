package com.example.disastermanagement;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VolunteerHome extends Fragment {


    VolunteerProfile volunteerProfile = new VolunteerProfile();
    TextView volunteerDisasterTv;
    Button volunteerYesBtn, volunteerNoBtn;
    List<VolunteerUtils> volunteerUtilsList;
    Singleton ls = Singleton.getInstance();
    String currentVolDisasterId;

    String key;
    String engagement, disengagement;
    int engagementInt, disEngagementInt;
    String currentVolName;
    DatabaseReference reference;
    Map map;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_volunteer_home, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_volunteer_home);

        volunteerUtilsList = new ArrayList<>();
    }

    @Override
    public void onStart() {
        super.onStart();

        volunteerDisasterTv = getActivity().findViewById(R.id.volunteerDisasterIdTv);
        volunteerYesBtn = getActivity().findViewById(R.id.volunteerYesBtn);
        volunteerNoBtn = getActivity().findViewById(R.id.volunteerNoBtn);

        readFromDatabase();
        readFromDisasterDatabase();


        volunteerYesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                yesBtn();
            }
        });

        volunteerNoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                noBtn();
            }
        });

    }

    public void yesBtn() {
        String engagementStr = Integer.toString(engagementInt + 1);

        Map map = new HashMap<>();
        map.put("volunteer_engagement", engagementStr);
        DatabaseReference reference = ls.database.getReference().child("Disasters").child(key);
        reference.updateChildren(map);

        volunteerDisasterTv.setVisibility(View.GONE);
        volunteerYesBtn.setVisibility(View.GONE);
        volunteerNoBtn.setVisibility(View.GONE);

        writeToVolunteerDatabase(currentVolName);

    }

    public void noBtn() {
        String disEngagementStr = Integer.toString(disEngagementInt + 1);

        Map map = new HashMap<>();
        map.put("volunteer_disengagement", disEngagementStr);
        DatabaseReference reference = ls.database.getReference().child("Disasters").child(key);
        reference.updateChildren(map);

        volunteerDisasterTv.setVisibility(View.GONE);
        volunteerYesBtn.setVisibility(View.GONE);
        volunteerNoBtn.setVisibility(View.GONE);
        writeToVolunteerDatabase(currentVolName);
    }

    public void writeToVolunteerDatabase(String childs)
    {
        map = new HashMap<>();
        map.put("isResponded", "yes");

        reference = ls.database.getReference().child("Volunteers").child(childs);
        reference.updateChildren(map);

    }

    public void snackBar() {

        Snackbar.make(getActivity().findViewById(R.id.homeCoordinatorLayout), "Profile not filled", Snackbar.LENGTH_INDEFINITE)
                .setAction("PROFILE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new VolunteerProfile())
                                .commit();

                    }
                })
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                .show();

    }


    public void readFromDatabase() {
        volunteerProfile = new VolunteerProfile();

//        Toast.makeText(getActivity(), "Read", Toast.LENGTH_SHORT).show();
//        Toast.makeText(getActivity(), ""+ls.mAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
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
                                , map.get("email"), map.get("disaster_id"), map.get("date")));

                        currentVolDisasterId = map.get("disaster_id");
//                        Toast.makeText(getActivity(), "Read" + currentVolDisasterId, Toast.LENGTH_SHORT).show();
                        currentVolName = map.get("name");

                        if(map.get("isResponded").equals("false"))
                        {

                            if(fragmentCheck)
                            {
                                volunteerDisasterTv.setText("Engage");
                                volunteerDisasterTv.setVisibility(View.VISIBLE);
                                volunteerYesBtn.setVisibility(View.VISIBLE);
                                volunteerNoBtn.setVisibility(View.VISIBLE);
                            }

                        }

                    }
                } else {

                    if (fragmentCheck) {
                        snackBar();
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

    public void readFromDisasterDatabase() {


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Disasters");
        reference.orderByChild("Disaster_id").equalTo(volunteerProfile.disasterIdStr).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                key = dataSnapshot.getKey();

                HashMap<String, String> engageMap = (HashMap<String, String>) dataSnapshot.getValue();

                engagement = String.valueOf(engageMap.get("volunteer_engagement"));
                disengagement = String.valueOf(engageMap.get("volunteer_disengagement"));

                engagementInt = Integer.parseInt(engagement);
                disEngagementInt = Integer.parseInt(disengagement);


            }



            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    boolean fragmentCheck;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        fragmentCheck = true;
        ls.fragmentFlag = true;

    }

    @Override
    public void onDetach() {
        super.onDetach();

        fragmentCheck = false;
        ls.fragmentFlag = false;
    }
}
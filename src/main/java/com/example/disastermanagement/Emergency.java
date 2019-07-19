package com.example.disastermanagement;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class Emergency extends Fragment {

    String currentDateStr, addressStr, disasterStr, cityStr;
    EditText emergencyCurrentDate, emergencyAddress, emergencyDisasterId, emergencyDisasterIdSpinner, emergencyCity;
    Button saveSendBtn;
    Spinner emergencyDisasterSpinner;
    DatabaseReference reference, reference1;

    Singleton ls = Singleton.getInstance();
    Map map1;
    Boolean sendMsgFlag = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_emergency, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_emergency);

    }

    @Override
    public void onStart() {
        super.onStart();

        currentDateStr = getCurrentDate();
        emergencyCurrentDate = getActivity().findViewById(R.id.emergencyDateEt);
        emergencyAddress = getActivity().findViewById(R.id.emergencyAddressEt);

        emergencyDisasterId = getActivity().findViewById(R.id.emergencyDisasterIdEt);
        emergencyCity = getActivity().findViewById(R.id.emergencyCityEt);
        saveSendBtn = getActivity().findViewById(R.id.saveAndSendBtn);

        addListenerOnSpinnerItemSelection(emergencyDisasterId);

        emergencyCurrentDate.setText(currentDateStr);

        saveSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveSend();

            }
        });

    }

    public String getCurrentDate() {

        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getActivity().getApplicationContext());
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date today = Calendar.getInstance().getTime();
        return dateFormat.format(today);
    }


    public void saveSend()
    {
        currentDateStr = emergencyCurrentDate.getText().toString();
        addressStr = emergencyAddress.getText().toString();

        disasterStr = ls.spinnerItem;

        cityStr = emergencyCity.getText().toString();

        if(emergencyDisasterId.getVisibility() == View.VISIBLE)
        {
            disasterStr = emergencyDisasterId.getText().toString();

            if(currentDateStr != null && addressStr != null && disasterStr != null && cityStr != null)
            {
                writeToDatabase();
                readFromDatabase();
            }
        }

        else if(currentDateStr != null && addressStr != null && disasterStr != null && cityStr != null)
        {
            writeToDatabase();
            readFromDatabase();
        }

//        Toast.makeText(getActivity(), "date "+currentDateStr+" address"+addressStr+" Disaster: "+disasterStr+" City: "+cityStr, Toast.LENGTH_SHORT).show();
    }

    public void addListenerOnSpinnerItemSelection(EditText disasterId) {

        emergencyDisasterSpinner = getActivity().findViewById(R.id.emergencyDisasterSpinner);
//        Toast.makeText(getActivity(), "null"+emergencyDisasterSpinner, Toast.LENGTH_SHORT).show();
        emergencyDisasterSpinner.setOnItemSelectedListener(new emergencyOnItemSelectedListener(disasterId));

    }

    public void sendMsg(String contact)
    {
        try
        {
            String Message = "Date: "+currentDateStr+"\nAddress: "+addressStr+"\nDisaster: "+disasterStr+"\nCity: "+cityStr;
            SmsManager smgr = SmsManager.getDefault();
            smgr.sendTextMessage(""+contact,null,Message,null,null);
            Toast.makeText(getActivity(), "Sent", Toast.LENGTH_SHORT).show();

        }
        catch (Exception e){
            Toast.makeText(getActivity(), "SMS Failed to Send, Please try again"+e, Toast.LENGTH_SHORT).show();
        }

    }
    Map map;

    public void writeToDatabase()
    {
        map = new HashMap<>();
        map.put("Date", currentDateStr);
        map.put("address", addressStr);
        map.put("disaster_id", disasterStr);
        map.put("city", cityStr);
        map.put("volunteer_engagement", "0");
        map.put("volunteer_disengagement", "0");
        map.put("check", "1");
        reference = ls.database.getReference().child("Disasters").push();
        reference.setValue(map);
        sendMsgFlag = true;
    }


    public void readFromDatabase()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Volunteers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists() && dataSnapshot.getValue() != null)
                {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        if (sendMsgFlag)
                        {

                            HashMap<String, String> map1 = (HashMap<String, String>) snapshot.getValue();
                            writeToVolunteerDatabase2(map1.get("name"));

                            if(map1.get("disaster_id").equals(disasterStr))
                            {
                                sendMsg(map1.get("contact"));
                                writeToVolunteerDatabase(map1.get("name"));
//                                Toast.makeText(getActivity(), "" + map1.get("contact") + "" + map1.get("name"), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                    sendMsgFlag = false;
                }
                else
                {
                    sendMsgFlag = false;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void writeToVolunteerDatabase(String childs)
    {
        map = new HashMap<>();
        map.put("isResponded", "false");

        reference = ls.database.getReference().child("Volunteers").child(childs);
        reference.updateChildren(map);

    }

    public void writeToVolunteerDatabase2(String childs)
    {
        map1 = new HashMap<>();
        map1.put("isResponded", "none");
        reference1 = ls.database.getReference().child("Volunteers").child(childs);
        reference1.updateChildren(map1);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        ls.fragmentFlag = true;

    }

    @Override
    public void onDetach() {
        super.onDetach();

        ls.fragmentFlag = false;

    }
}

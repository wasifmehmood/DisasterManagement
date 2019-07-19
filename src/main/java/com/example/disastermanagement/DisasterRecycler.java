package com.example.disastermanagement;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class DisasterRecycler extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        Toast.makeText(getActivity(), "CreateView", Toast.LENGTH_SHORT).show();
        return inflater.inflate(R.layout.activity_disaster, container, false);
    }

    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;

    List<PersonUtils> personUtilsList;
    Activity activity;
    Spinner reportDurationSpinner, reportOptionsSpinner;
    Singleton ls = Singleton.getInstance();

    EditText reportOptionsEt;
    String reportOptionsStr;
    Button disasterReportBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
//        Toast.makeText(getActivity(), "Create", Toast.LENGTH_SHORT).show();
        personUtilsList = new ArrayList<>();

    }


    @Override
    public void onStart() {
        super.onStart();
        readFromDatabase();
//        Toast.makeText(getActivity(), "Start", Toast.LENGTH_SHORT).show();

        activity = getActivity();

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycleViewContainer);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);


        mAdapter = new DisasterRecyclerAdapter(getActivity(), personUtilsList, activity);

        recyclerView.setAdapter(mAdapter);


        disasterReportBtn = getActivity().findViewById(R.id.disasterReportBtn);

        disasterReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customDialog(ls.spinnerItem2, ls.spinnerItem3);

            }
        });

    }

    //DIALOG
    public void customDialog(final String spinnerItem2, final String spinnerItem3)
    {
        // custom dialog
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.activity_disaster_report_dialog);
        dialog.setTitle("Title...");



        // set the custom dialog components - text, image and button
        reportDurationSpinner = dialog.findViewById(R.id.reportDurationSpinner);
        reportOptionsSpinner = dialog.findViewById(R.id.reportOptionsSpinner);
        reportOptionsEt = dialog.findViewById(R.id.reportOptionsEt);


        Button generateReportBtn = dialog.findViewById(R.id.reportGenerateBtn);
        addListenerOnSpinnerItemSelection();
        addListenerOnSpinnerItemSelection2();

        generateReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reportOptionsStr = reportOptionsEt.getText().toString();
//                disasterGenerateReport.readFromDatabase(spinnerItem2,spinnerItem3,reportOptionsStr);
                if(ls.spinnerItem2.equals("quarterly") && ls.spinnerItem3.equals("city")) {
                    filterQuarterlyCity();
                }
                else if(ls.spinnerItem2.equals("quarterly") && ls.spinnerItem3.equals("disaster_id")) {
                    filterQuarterlyDisasterId();
                }
                else if(ls.spinnerItem2.equals("monthly") && ls.spinnerItem3.equals("city")) {
                    filterMonthlyCity();
                }
                else if(ls.spinnerItem2.equals("monthly") && ls.spinnerItem3.equals("disaster_id")) {
                    filterMonthlyDisasterId();
                }
                else if(ls.spinnerItem2.equals("yearly") && ls.spinnerItem3.equals("city")) {
                    filterYearlyCity();
                }
                else if(ls.spinnerItem2.equals("yearly") && ls.spinnerItem3.equals("disaster_id")) {
                    filterYearlyDisasterId();
                }
            }
        });

        dialog.show();
    }

    public void addListenerOnSpinnerItemSelection() {

//        Toast.makeText(getActivity(), "null"+emergencyDisasterSpinner, Toast.LENGTH_SHORT).show();
        reportDurationSpinner.setOnItemSelectedListener(new ReportOnItemSelectedListener());

    }


    public void addListenerOnSpinnerItemSelection2() {

//        Toast.makeText(getActivity(), "null"+emergencyDisasterSpinner, Toast.LENGTH_SHORT).show();
        reportOptionsSpinner.setOnItemSelectedListener(new ReportOnItemSelectedListener2());

    }



    public void filterMonthlyCity()
    {
        List<PersonUtils> a= new ArrayList<>();

        for(int i=0; i<personUtilsList.size(); i++){

            String date = personUtilsList.get(i).getDate();

            String[] splitted = date.split("/");
            int splittedMonth = Integer.parseInt(splitted[0]);
            int splittedDay = Integer.parseInt(splitted[1]);
            int splittedYear = Integer.parseInt(splitted[2]);

            boolean check = isMonthly(splittedMonth, splittedDay, splittedYear);

            if(check && personUtilsList.get(i).getCity().equalsIgnoreCase(reportOptionsStr)){
                a.add(personUtilsList.get(i));
//                Toast.makeText(getActivity(), "Month "+splittedMonth+" Day "+splittedDay+" Year "+splittedYear+" Size "+personUtilsList.size(), Toast.LENGTH_SHORT).show();
            }

        }
        mAdapter = new DisasterRecyclerAdapter(getActivity(), a, activity);

        recyclerView.setAdapter(mAdapter);
    }

    public void filterMonthlyDisasterId()
    {
        List<PersonUtils> a= new ArrayList<>();

        for(int i=0; i<personUtilsList.size(); i++){

            String date = personUtilsList.get(i).getDate();

            String[] splitted = date.split("/");
            int splittedMonth = Integer.parseInt(splitted[0]);
            int splittedDay = Integer.parseInt(splitted[1]);
            int splittedYear = Integer.parseInt(splitted[2]);

            boolean check = isMonthly(splittedMonth, splittedDay, splittedYear);

            if(check && personUtilsList.get(i).getDisasterId().equalsIgnoreCase(reportOptionsStr)){
                a.add(personUtilsList.get(i));
//                Toast.makeText(getActivity(), "Month "+splittedMonth+" Day "+splittedDay+" Year "+splittedYear+" Size "+personUtilsList.size(), Toast.LENGTH_SHORT).show();
            }

        }
        mAdapter = new DisasterRecyclerAdapter(getActivity(), a, activity);

        recyclerView.setAdapter(mAdapter);
    }
    public void filterQuarterlyDisasterId()
    {
        List<PersonUtils> a= new ArrayList<>();

        for(int i=0; i<personUtilsList.size(); i++){

            String date = personUtilsList.get(i).getDate();

            String[] splitted = date.split("/");
            int splittedMonth = Integer.parseInt(splitted[0]);
            int splittedDay = Integer.parseInt(splitted[1]);
            int splittedYear = Integer.parseInt(splitted[2]);

            boolean check = isQuarterly1(splittedMonth, splittedDay, splittedYear);

            if(check && personUtilsList.get(i).getDisasterId().equalsIgnoreCase(reportOptionsStr)){
                a.add(personUtilsList.get(i));
//                Toast.makeText(getActivity(), "Month "+splittedMonth+" Day "+splittedDay+" Year "+splittedYear+" Size "+personUtilsList.size(), Toast.LENGTH_SHORT).show();
            }

        }
        mAdapter = new DisasterRecyclerAdapter(getActivity(), a, activity);

        recyclerView.setAdapter(mAdapter);
    }

    public void filterQuarterlyCity(){
        List<PersonUtils> a= new ArrayList<>();

        for(int i=0; i<personUtilsList.size(); i++){

            String date = personUtilsList.get(i).getDate();

            String[] splitted = date.split("/");
            int splittedMonth = Integer.parseInt(splitted[0]);
            int splittedDay = Integer.parseInt(splitted[1]);
            int splittedYear = Integer.parseInt(splitted[2]);

            boolean check = isQuarterly1(splittedMonth, splittedDay, splittedYear);

            if(check && personUtilsList.get(i).getCity().equalsIgnoreCase(reportOptionsStr)){
                a.add(personUtilsList.get(i));
//                Toast.makeText(getActivity(), "Month "+splittedMonth+" Day "+splittedDay+" Year "+splittedYear+" Size "+personUtilsList.size(), Toast.LENGTH_SHORT).show();
            }

        }
        mAdapter = new DisasterRecyclerAdapter(getActivity(), a, activity);

        recyclerView.setAdapter(mAdapter);
    }
    public void filterYearlyCity()
    {
        List<PersonUtils> a= new ArrayList<>();

        for(int i=0; i<personUtilsList.size(); i++){

            String date = personUtilsList.get(i).getDate();

            String[] splitted = date.split("/");
            int splittedMonth = Integer.parseInt(splitted[0]);
            int splittedDay = Integer.parseInt(splitted[1]);
            int splittedYear = Integer.parseInt(splitted[2]);

            boolean check = isYearly(splittedMonth, splittedDay, splittedYear);

            if(check && personUtilsList.get(i).getCity().equalsIgnoreCase(reportOptionsStr)){
                a.add(personUtilsList.get(i));
//                Toast.makeText(getActivity(), "Month "+splittedMonth+" Day "+splittedDay+" Year "+splittedYear+" Size "+personUtilsList.size(), Toast.LENGTH_SHORT).show();
            }

        }
        mAdapter = new DisasterRecyclerAdapter(getActivity(), a, activity);

        recyclerView.setAdapter(mAdapter);
    }

    public void filterYearlyDisasterId()
    {
        List<PersonUtils> a= new ArrayList<>();

        for(int i=0; i<personUtilsList.size(); i++){

            String date = personUtilsList.get(i).getDate();

            String[] splitted = date.split("/");
            int splittedMonth = Integer.parseInt(splitted[0]);
            int splittedDay = Integer.parseInt(splitted[1]);
            int splittedYear = Integer.parseInt(splitted[2]);

            boolean check = isMonthly(splittedMonth, splittedDay, splittedYear);

            if(check && personUtilsList.get(i).getDisasterId().equalsIgnoreCase(reportOptionsStr)){
                a.add(personUtilsList.get(i));
//                Toast.makeText(getActivity(), "Month "+splittedMonth+" Day "+splittedDay+" Year "+splittedYear+" Size "+personUtilsList.size(), Toast.LENGTH_SHORT).show();
            }

        }
        mAdapter = new DisasterRecyclerAdapter(getActivity(), a, activity);

        recyclerView.setAdapter(mAdapter);
    }


     public boolean isMonthly(int month, int date, int year)
     {
         Calendar birthDay = new GregorianCalendar(year, month, date);
         Calendar today = new GregorianCalendar();
         today.setTime(new Date());

         int yearsInBetween = today.get(Calendar.YEAR)
                 - birthDay.get(Calendar.YEAR);
         int monthsDiff = today.get(Calendar.MONTH)
                 - birthDay.get(Calendar.MONTH);
         long ageInMonths = yearsInBetween*12 + monthsDiff;
         long age = yearsInBetween;

         if(monthsDiff < 2)
         {
//             Toast.makeText(getActivity(), "Monthly "+monthsDiff, Toast.LENGTH_LONG).show();
             return true;
         }
         else {
             return false;
         }
     }

    public boolean isQuarterly1(int month, int date, int year)
    {

        Calendar birthDay = new GregorianCalendar(year, month, date);
        Calendar today = new GregorianCalendar();
        today.setTime(new Date());

        int yearsInBetween = today.get(Calendar.YEAR)
                - birthDay.get(Calendar.YEAR);
        int monthsDiff = today.get(Calendar.MONTH)
                - birthDay.get(Calendar.MONTH);
        long ageInMonths = yearsInBetween*12 + monthsDiff;
        long age = yearsInBetween;

        if(monthsDiff < 3)
        {
//            Toast.makeText(getActivity(), "Quarterly "+monthsDiff, Toast.LENGTH_LONG).show();
            return true;
        }
        else {
            return false;
        }
    }

     public boolean isYearly(int month, int date, int year)
     {
         Calendar birthDay = new GregorianCalendar(year, month, date);
         Calendar today = new GregorianCalendar();
         today.setTime(new Date());

         int yearsInBetween = today.get(Calendar.YEAR)
                 - birthDay.get(Calendar.YEAR);
         int monthsDiff = today.get(Calendar.MONTH)
                 - birthDay.get(Calendar.MONTH);
         long ageInMonths = yearsInBetween*12 + monthsDiff;
         long age = yearsInBetween;

         if(monthsDiff > 3 && monthsDiff < 12)
         {
//             Toast.makeText(getActivity(), "Yearly "+monthsDiff, Toast.LENGTH_LONG).show();
             return true;
         }
         else {
             return false;
         }
     }


    @Override
    public void onResume() {
        super.onResume();
    }

    // Read from the database
    public void readFromDatabase()
    {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Disasters");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        HashMap<String, String> map = (HashMap<String, String>) snapshot.getValue();
                        personUtilsList.add(new PersonUtils(map.get("disaster_id"), map.get("Date"), map.get("address"), map.get("city")
                                ,map.get("volunteer_engagement"),map.get("volunteer_disengagement")));
                      //Toast.makeText(getActivity(), "" + map.get("Date"), Toast.LENGTH_SHORT).show();
                        recyclerView.setAdapter(mAdapter);
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


}

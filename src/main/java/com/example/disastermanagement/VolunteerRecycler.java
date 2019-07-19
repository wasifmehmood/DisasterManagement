package com.example.disastermanagement;

import android.app.Activity;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class VolunteerRecycler extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;

    List<VolunteerUtils> volunteerUtilsList;
    Activity activity;
    Singleton ls = Singleton.getInstance();
    String currentUser;

    Button volunteerReportBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_volunteer_recycler, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_volunteer_recycler);

        volunteerUtilsList = new ArrayList<>();
    }


    @Override
    public void onStart() {
        super.onStart();

        currentUser = ls.mAuth.getCurrentUser().getEmail();

        readFromDatabase();

        activity = getActivity();
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycleViewContainer1);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);


        mAdapter = new VolunteerRecyclerAdapter(getActivity(), volunteerUtilsList, activity, currentUser);

        recyclerView.setAdapter(mAdapter);

        volunteerReportBtn = getActivity().findViewById(R.id.volunteerReportBtn);


        volunteerReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customDialog(ls.spinnerItem2, ls.spinnerItem3);

            }
        });
    }
    Spinner reportDurationSpinner1, reportOptionsSpinner1;
    EditText reportOptionsEt1;
    String reportOptionsStr1;

    //DIALOG
    public void customDialog(final String spinnerItem2, final String spinnerItem3)
    {
        // custom dialog
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.volunteer_report_dialog);
        dialog.setTitle("Title...");


        // set the custom dialog components - text, image and button
        reportDurationSpinner1 = dialog.findViewById(R.id.reportDurationSpinner1);
        reportOptionsSpinner1 = dialog.findViewById(R.id.reportOptionsSpinner1);
        reportOptionsEt1 = dialog.findViewById(R.id.reportOptionsEt1);


        Button generateReportBtn1 = dialog.findViewById(R.id.reportGenerateBtn1);
        addListenerOnSpinnerItemSelection();
        addListenerOnSpinnerItemSelection2();

        generateReportBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reportOptionsStr1 = reportOptionsEt1.getText().toString();
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

        reportDurationSpinner1.setOnItemSelectedListener(new ReportOnItemSelectedListener());

    }


    public void addListenerOnSpinnerItemSelection2() {

        reportOptionsSpinner1.setOnItemSelectedListener(new ReportOnItemSelectedListener2());

    }

    public void filterMonthlyCity()
    {
        List<VolunteerUtils> a= new ArrayList<>();

        for(int i=0; i<volunteerUtilsList.size(); i++){

            String date = volunteerUtilsList.get(i).getDate();

            String[] splitted = date.split("/");
            int splittedMonth = Integer.parseInt(splitted[0]);
            int splittedDay = Integer.parseInt(splitted[1]);
            int splittedYear = Integer.parseInt(splitted[2]);

            boolean check = isMonthly(splittedMonth, splittedDay, splittedYear);

            if(check && volunteerUtilsList.get(i).getCity().equalsIgnoreCase(reportOptionsStr1)){
                a.add(volunteerUtilsList.get(i));
//                Toast.makeText(getActivity(), "Month "+splittedMonth+" Day "+splittedDay+" Year "+splittedYear+" Size "+personUtilsList.size(), Toast.LENGTH_SHORT).show();
            }

        }
        mAdapter = new VolunteerRecyclerAdapter(getActivity(), a, activity, currentUser);

        recyclerView.setAdapter(mAdapter);
    }


    public void filterMonthlyDisasterId()
    {
        List<VolunteerUtils> a= new ArrayList<>();

        for(int i=0; i<volunteerUtilsList.size(); i++){

            String date = volunteerUtilsList.get(i).getDate();

            String[] splitted = date.split("/");
            int splittedMonth = Integer.parseInt(splitted[0]);
            int splittedDay = Integer.parseInt(splitted[1]);
            int splittedYear = Integer.parseInt(splitted[2]);

            boolean check = isMonthly(splittedMonth, splittedDay, splittedYear);

            if(check && volunteerUtilsList.get(i).getDisasterId().equalsIgnoreCase(reportOptionsStr1)){
                a.add(volunteerUtilsList.get(i));
//                Toast.makeText(getActivity(), "Month "+splittedMonth+" Day "+splittedDay+" Year "+splittedYear+" Size "+personUtilsList.size(), Toast.LENGTH_SHORT).show();
            }

        }
        mAdapter = new VolunteerRecyclerAdapter(getActivity(), a, activity, currentUser);

        recyclerView.setAdapter(mAdapter);
    }
    public void filterQuarterlyDisasterId()
    {
        List<VolunteerUtils> a= new ArrayList<>();

        for(int i=0; i<volunteerUtilsList.size(); i++){

            String date = volunteerUtilsList.get(i).getDate();

            String[] splitted = date.split("/");
            int splittedMonth = Integer.parseInt(splitted[0]);
            int splittedDay = Integer.parseInt(splitted[1]);
            int splittedYear = Integer.parseInt(splitted[2]);

            boolean check = isQuarterly1(splittedMonth, splittedDay, splittedYear);

            if(check && volunteerUtilsList.get(i).getDisasterId().equalsIgnoreCase(reportOptionsStr1)){
                a.add(volunteerUtilsList.get(i));
//                Toast.makeText(getActivity(), "Month "+splittedMonth+" Day "+splittedDay+" Year "+splittedYear+" Size "+personUtilsList.size(), Toast.LENGTH_SHORT).show();
            }

        }
        mAdapter = new VolunteerRecyclerAdapter(getActivity(), a, activity, currentUser);

        recyclerView.setAdapter(mAdapter);
    }

    public void filterQuarterlyCity(){
        List<VolunteerUtils> a= new ArrayList<>();

        for(int i=0; i<volunteerUtilsList.size(); i++){

            String date = volunteerUtilsList.get(i).getDate();

            String[] splitted = date.split("/");
            int splittedMonth = Integer.parseInt(splitted[0]);
            int splittedDay = Integer.parseInt(splitted[1]);
            int splittedYear = Integer.parseInt(splitted[2]);

            boolean check = isQuarterly1(splittedMonth, splittedDay, splittedYear);

            if(check && volunteerUtilsList.get(i).getCity().equalsIgnoreCase(reportOptionsStr1)){
                a.add(volunteerUtilsList.get(i));
//                Toast.makeText(getActivity(), "Month "+splittedMonth+" Day "+splittedDay+" Year "+splittedYear+" Size "+personUtilsList.size(), Toast.LENGTH_SHORT).show();
            }

        }
        mAdapter = new VolunteerRecyclerAdapter(getActivity(), a, activity, currentUser);

        recyclerView.setAdapter(mAdapter);
    }
    public void filterYearlyCity()
    {
        List<VolunteerUtils> a= new ArrayList<>();

        for(int i=0; i<volunteerUtilsList.size(); i++){

            String date = volunteerUtilsList.get(i).getDate();

            String[] splitted = date.split("/");
            int splittedMonth = Integer.parseInt(splitted[0]);
            int splittedDay = Integer.parseInt(splitted[1]);
            int splittedYear = Integer.parseInt(splitted[2]);

            boolean check = isYearly(splittedMonth, splittedDay, splittedYear);

            if(check && volunteerUtilsList.get(i).getCity().equalsIgnoreCase(reportOptionsStr1)){
                a.add(volunteerUtilsList.get(i));
//                Toast.makeText(getActivity(), "Month "+splittedMonth+" Day "+splittedDay+" Year "+splittedYear+" Size "+personUtilsList.size(), Toast.LENGTH_SHORT).show();
            }

        }
        mAdapter = new VolunteerRecyclerAdapter(getActivity(), a, activity, currentUser);

        recyclerView.setAdapter(mAdapter);
    }

    public void filterYearlyDisasterId()
    {
        List<VolunteerUtils> a= new ArrayList<>();

        for(int i=0; i<volunteerUtilsList.size(); i++){

            String date = volunteerUtilsList.get(i).getDate();

            String[] splitted = date.split("/");
            int splittedMonth = Integer.parseInt(splitted[0]);
            int splittedDay = Integer.parseInt(splitted[1]);
            int splittedYear = Integer.parseInt(splitted[2]);

            boolean check = isMonthly(splittedMonth, splittedDay, splittedYear);

            if(check && volunteerUtilsList.get(i).getDisasterId().equalsIgnoreCase(reportOptionsStr1)){
                a.add(volunteerUtilsList.get(i));
//                Toast.makeText(getActivity(), "Month "+splittedMonth+" Day "+splittedDay+" Year "+splittedYear+" Size "+personUtilsList.size(), Toast.LENGTH_SHORT).show();
            }

        }
        mAdapter = new VolunteerRecyclerAdapter(getActivity(), a, activity, currentUser);

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
//            Toast.makeText(getActivity(), "Monthly "+monthsDiff, Toast.LENGTH_LONG).show();
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
//            Toast.makeText(getActivity(), "Quarterly "+monthsDiff+""+birthDay.get(Calendar.MONTH), Toast.LENGTH_LONG).show();
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
//            Toast.makeText(getActivity(), "Yearly "+monthsDiff, Toast.LENGTH_LONG).show();
            return true;
        }
        else {
            return false;
        }
    }



    // Read from the database
    public void readFromDatabase()
    {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Volunteers");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        HashMap<String, String> map = (HashMap<String, String>) snapshot.getValue();
                        volunteerUtilsList.add(new VolunteerUtils(map.get("name"), map.get("disaster_id"), map.get("city"),
                                map.get("contact"), map.get("email"),map.get("address"),map.get("date")));
//                        Toast.makeText(getActivity(), "name" + map.get("name"), Toast.LENGTH_SHORT).show();
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

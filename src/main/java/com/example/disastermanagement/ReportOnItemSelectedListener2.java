package com.example.disastermanagement;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

public class ReportOnItemSelectedListener2 implements AdapterView.OnItemSelectedListener {
    Singleton ls =Singleton.getInstance();
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//        Toast.makeText(parent.getContext(),
//                "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
//                Toast.LENGTH_SHORT).show();

        ls.spinnerItem3 = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

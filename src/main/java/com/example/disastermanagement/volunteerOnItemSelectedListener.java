package com.example.disastermanagement;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Toast;

public class volunteerOnItemSelectedListener implements OnItemSelectedListener {

    EditText disasterId;

    Singleton ls = Singleton.getInstance();
    volunteerOnItemSelectedListener()
    {

    }

    volunteerOnItemSelectedListener(EditText disasterId)
    {
        this.disasterId = disasterId;
    }
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//        Toast.makeText(parent.getContext(),
//                "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
//                Toast.LENGTH_SHORT).show();

        ls.spinnerItem1 = parent.getItemAtPosition(pos).toString();

        if (ls.spinnerItem1.equals("Other"))
        {
            disasterId.setVisibility(View.VISIBLE);
        }
        else
        {
            disasterId.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}

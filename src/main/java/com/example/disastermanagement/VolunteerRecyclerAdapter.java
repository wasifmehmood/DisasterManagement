package com.example.disastermanagement;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class VolunteerRecyclerAdapter extends RecyclerView.Adapter<VolunteerRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<VolunteerUtils> volunteerUtils;
    Activity activity;
    String currentUser;
    TextView dialogVNameTv, dialogVDisasterIdTv, dialogVCityTv, dialogVContactTv, dialogVEmailTv, dialogVAddressTv, dialogVDateTv;

    public VolunteerRecyclerAdapter(Context context, List volunteerUtils, Activity activity, String currentUser) {
        this.context = context;
        this.volunteerUtils = volunteerUtils;
        this.activity = activity;
        this.currentUser = currentUser;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item1, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.itemView.setTag(volunteerUtils.get(position));

        VolunteerUtils vu = volunteerUtils.get(position);

        holder.volunteerNameTv.setText(vu.getName());
        holder.volunteerDisasterIdTv.setText(vu.getDisasterId());

    }

    @Override
    public int getItemCount() {
        return volunteerUtils.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView volunteerDisasterIdTv;
        public TextView volunteerNameTv;

        public ViewHolder(View itemView) {
            super(itemView);

            volunteerNameTv = (TextView) itemView.findViewById(R.id.volunteerNameTv);
            volunteerDisasterIdTv = (TextView) itemView.findViewById(R.id.volunteerDisasterIdTv1);

                    itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    VolunteerUtils cpu = (VolunteerUtils) view.getTag();

                    customDialog(cpu.getName(),cpu.getDisasterId(),cpu.getCity(),cpu.getContact(),cpu.getEmail(),cpu.getAddress(),cpu.getDate());

//                    Toast.makeText(view.getContext(), cpu.getDisasterId()+"  "+ cpu.getName()+" ", Toast.LENGTH_SHORT).show();
//
//                    Toast.makeText(view.getContext(), ""+volunteerUtils.size(), Toast.LENGTH_SHORT).show();

                }
            });

        }
    }


    public void customDialog(String name, String disaster_id, String city, String contact, String email, String address, String date)
    {
        // custom dialog
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.volunteer_dialog);
        dialog.setTitle("Title...");


        // set the custom dialog components - text, image and button
        dialogVNameTv = dialog.findViewById(R.id.dialogVNameTv);
        dialogVDisasterIdTv = dialog.findViewById(R.id.dialogVDisasterIdTv);
        dialogVCityTv = dialog.findViewById(R.id.dialogVCityTv);
        dialogVContactTv = dialog.findViewById(R.id.dialogVContactTv);
        dialogVEmailTv = dialog.findViewById(R.id.dialogVEmailTv);
        dialogVAddressTv = dialog.findViewById(R.id.dialogVAddressTv);
        dialogVDateTv = dialog.findViewById(R.id.dialogVDateTv);

        dialogVNameTv.setText(name);
        dialogVDisasterIdTv.setText(disaster_id);
        dialogVCityTv.setText(city);
        dialogVContactTv.setText(contact);
        dialogVEmailTv.setText(email);
        dialogVAddressTv.setText(address);
        dialogVDateTv.setText(date);

        dialog.show();
    }

}

package com.example.disastermanagement;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class DisasterRecyclerAdapter extends RecyclerView.Adapter<DisasterRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<PersonUtils> personUtils;
    Activity activity;
    TextView dialogDisasterIdTv, dialogDateTv, dialogAddressTv, dialogCityTv, dialogEngagementTv, dialogDisEngagementTv;

    public DisasterRecyclerAdapter(Context context, List personUtils, Activity activity) {
        this.context = context;
        this.personUtils = personUtils;
        this.activity = activity;
//        Toast.makeText(context, "Adapter", Toast.LENGTH_SHORT).show();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Toast.makeText(context, "holder", Toast.LENGTH_SHORT).show();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.itemView.setTag(personUtils.get(position));

        PersonUtils pu = personUtils.get(position);

        holder.disasterIdTv.setText(pu.getDisasterId());
        holder.disasterDateTv.setText(pu.getDate());

    }

    @Override
    public int getItemCount() {
        return personUtils.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView disasterIdTv;
        public TextView disasterDateTv;

        public ViewHolder(View itemView) {
            super(itemView);

            disasterIdTv = (TextView) itemView.findViewById(R.id.disasterIdTv);
            disasterDateTv = (TextView) itemView.findViewById(R.id.disasterDateTv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    PersonUtils cpu = (PersonUtils) view.getTag();

                    customDialog(cpu.getDisasterId(),cpu.getDate(),cpu.getAddress(),cpu.getCity(),cpu.getEngagement(),cpu.getDisEngagement());


//                    Toast.makeText(view.getContext(), cpu.getDisasterId()+"  "+ cpu.getDate()+" ", Toast.LENGTH_SHORT).show();
//
//                    Toast.makeText(view.getContext(), ""+personUtils.size(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }


    public void customDialog(String disasterId, String date, String address, String city, String engagement, String disengagement)
    {
        // custom dialog
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.disaster_dialog);
        dialog.setTitle("Title...");


        // set the custom dialog components - text, image and button
        dialogDisasterIdTv = dialog.findViewById(R.id.dialogDisasterIdTv);
        dialogDateTv = dialog.findViewById(R.id.dialogDateTv);
        dialogAddressTv = dialog.findViewById(R.id.dialogAddressTv);
        dialogCityTv = dialog.findViewById(R.id.dialogCityTv);
        dialogEngagementTv = dialog.findViewById(R.id.dialogEngagementTv);
        dialogDisEngagementTv = dialog.findViewById(R.id.dialogDisEngagementTv);

        dialogDisasterIdTv.setText(disasterId);
        dialogDateTv.setText(date);
        dialogAddressTv.setText(address);
        dialogCityTv.setText(city);
        dialogEngagementTv.setText(engagement);
        dialogDisEngagementTv.setText(disengagement);

        dialog.show();
    }


}
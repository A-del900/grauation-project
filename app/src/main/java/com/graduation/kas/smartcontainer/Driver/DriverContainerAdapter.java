package com.graduation.kas.smartcontainer.Driver;


import static com.graduation.kas.smartcontainer.API.FireBASEURL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.graduation.kas.smartcontainer.Admin.Container.Container;
import com.graduation.kas.smartcontainer.R;

import java.util.ArrayList;

public class DriverContainerAdapter extends RecyclerView.Adapter<DriverContainerAdapter.SingleItemRowHolder> {

    private ArrayList<Container> itemsList;
    private Activity mContext;
    DatabaseReference mDatabase = null;


    public DriverContainerAdapter(Activity context, ArrayList<Container> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
        mDatabase = FirebaseDatabase.getInstance(FireBASEURL).getReference();



    }
    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_container_driver, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, @SuppressLint("RecyclerView") final int pos) {

        final Container singleItem = itemsList.get(pos);

        holder.no.setText(singleItem.getNo());
        holder.sensorid.setText(singleItem.getSensorid());
        holder.level.setText(singleItem.getLevel());
        if(!singleItem.getLevel().equals("Not Detected"))
        {
            holder.level.setText(singleItem.getLevel()+"%");

        }



        holder.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+singleItem.getLatitude()+","+singleItem.getLongitude());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mapIntent.setPackage("com.google.android.apps.maps");
                mContext.startActivity(mapIntent);
            }
        });




    }


    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView no,sensorid,level;
        ImageView location;

        public SingleItemRowHolder(View view) {
            super(view);

            this.sensorid = (TextView) view.findViewById(R.id.sensorid);
            this.no = (TextView) view.findViewById(R.id.no);
            this.level = (TextView) view.findViewById(R.id.level);

            this.location = (ImageView) view.findViewById(R.id.location);


        }

    }

}
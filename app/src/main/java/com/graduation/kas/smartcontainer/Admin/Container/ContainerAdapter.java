package com.graduation.kas.smartcontainer.Admin.Container;


import static com.graduation.kas.smartcontainer.API.FireBASEURL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.graduation.kas.smartcontainer.R;

import java.util.ArrayList;

public class ContainerAdapter extends RecyclerView.Adapter<ContainerAdapter.SingleItemRowHolder> {

    private ArrayList<Container> itemsList;
    private Activity mContext;
    DatabaseReference mDatabase = null;


    public ContainerAdapter(Activity context, ArrayList<Container> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
        mDatabase = FirebaseDatabase.getInstance(FireBASEURL).getReference();



    }
    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_container, null);
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


        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UpdateContainerActivity.container = singleItem;
                mContext.startActivity(new Intent(mContext,UpdateContainerActivity.class));

            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                builder1.setMessage("Are You Sure You Want To Delete?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Delete(singleItem.getId(),pos);

                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();


            }
        });

    }

    private void Delete(String id,int i){
        mDatabase.child("Container").child(id).removeValue();
        Toast.makeText(mContext, "Deleted", Toast.LENGTH_SHORT).show();
        itemsList.remove(i);
        notifyDataSetChanged();


    }
    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView no,sensorid,level;
        ImageView delete,update,location;

        public SingleItemRowHolder(View view) {
            super(view);

            this.no = (TextView) view.findViewById(R.id.no);
            this.sensorid = (TextView) view.findViewById(R.id.sensorid);
            this.level = (TextView) view.findViewById(R.id.level);

            this.location = (ImageView) view.findViewById(R.id.location);
            this.update = (ImageView) view.findViewById(R.id.update);
            this.delete = (ImageView) view.findViewById(R.id.delete);


        }

    }

}
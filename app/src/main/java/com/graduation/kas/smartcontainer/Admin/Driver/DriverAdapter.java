package com.graduation.kas.smartcontainer.Admin.Driver;


import static com.graduation.kas.smartcontainer.API.FireBASEURL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.graduation.kas.smartcontainer.R;

import java.util.ArrayList;

public class DriverAdapter extends RecyclerView.Adapter<DriverAdapter.SingleItemRowHolder> {

    private ArrayList<Driver> itemsList;
    private Activity mContext;
    DatabaseReference mDatabase = null;


    public DriverAdapter(Activity context, ArrayList<Driver> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
        mDatabase = FirebaseDatabase.getInstance(FireBASEURL).getReference();



    }
    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_driver, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, @SuppressLint("RecyclerView") final int pos) {

        final Driver singleItem = itemsList.get(pos);

        holder.username.setText(singleItem.getUsername());
        holder.password.setText(singleItem.getPassword());
        holder.name.setText(singleItem.getName());
        holder.email.setText(singleItem.getEmail());
        holder.phone.setText(singleItem.getPhone());


        holder.containers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PickContainerActivity.empid = singleItem.getId();
                mContext.startActivity(new Intent(mContext,PickContainerActivity.class));

            }
        });



        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UpdateDriverActivity.driver = singleItem;
                mContext.startActivity(new Intent(mContext, UpdateDriverActivity.class));

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
        mDatabase.child("Users").child(id).removeValue();
        Toast.makeText(mContext, "Deleted", Toast.LENGTH_SHORT).show();
        itemsList.remove(i);
        notifyDataSetChanged();


    }
    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView username,name,phone,email,password;
        ImageView delete,update;
        Button containers;
        public SingleItemRowHolder(View view) {
            super(view);

            this.username = (TextView) view.findViewById(R.id.username);
            this.name = (TextView) view.findViewById(R.id.name);
            this.phone = (TextView) view.findViewById(R.id.phone);
            this.email = (TextView) view.findViewById(R.id.email);
            this.password = (TextView) view.findViewById(R.id.password);


            this.containers = (Button) view.findViewById(R.id.containers);

            this.update = (ImageView) view.findViewById(R.id.update);

            this.delete = (ImageView) view.findViewById(R.id.delete);


        }

    }

}
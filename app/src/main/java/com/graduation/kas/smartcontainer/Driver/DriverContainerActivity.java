package com.graduation.kas.smartcontainer.Driver;

import static com.graduation.kas.smartcontainer.API.FireBASEURL;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.graduation.kas.smartcontainer.Admin.Container.Container;
import com.graduation.kas.smartcontainer.DrawerActivity;
import com.graduation.kas.smartcontainer.R;
import com.graduation.kas.smartcontainer.Session;

import java.util.ArrayList;

public class DriverContainerActivity extends DrawerActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_driver_container;
    }

    @Override
    public String getCustomTitle() {
        return "Containers";

    }

    RecyclerView recyclerView;

    public static ArrayList<Container> arrayList;
    DriverContainerAdapter adapter;
    DatabaseReference mDatabase = null;
    Session session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance(FireBASEURL).getReference();

        session = new Session(DriverContainerActivity.this);
        arrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.list);
        adapter = new DriverContainerAdapter(DriverContainerActivity.this, arrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(),RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);




    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }
    private void getData() {
        arrayList.clear();
        DatabaseReference query = mDatabase.child("Container");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Iterable<DataSnapshot> data = dataSnapshot.getChildren();

                    for (DataSnapshot dataSnapshot1 : data) {
                        Container dd = new Container();

                        dd.setId(dataSnapshot1.getKey().toString());
                        dd.setSensorid(dataSnapshot1.child("sensorid").getValue().toString());
                        dd.setNo(dataSnapshot1.child("no").getValue().toString());
                        dd.setLatitude(dataSnapshot1.child("latitude").getValue().toString());
                        dd.setLongitude(dataSnapshot1.child("longitude").getValue().toString());
                        dd.setLevel("Not Detected");

                        if(dataSnapshot1.child("driverid").getValue().toString().equals(session.getId())) {

                            DatabaseReference query1 = mDatabase.child("Sensors").child(dd.getSensorid());

                            query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        Iterable<DataSnapshot> data = dataSnapshot.getChildren();


                                        dd.setLevel(dataSnapshot.child("level").getValue().toString());
                                        adapter.notifyDataSetChanged();

                                    }else{

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            arrayList.add(dd);
                        }


                        adapter.notifyDataSetChanged();
                    }
                    adapter.notifyDataSetChanged();

                }else{
//                    hide.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
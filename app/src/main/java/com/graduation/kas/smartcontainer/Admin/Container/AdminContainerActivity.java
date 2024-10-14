package com.graduation.kas.smartcontainer.Admin.Container;

import static com.graduation.kas.smartcontainer.API.FireBASEURL;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.graduation.kas.smartcontainer.DrawerActivity;
import com.graduation.kas.smartcontainer.R;

import java.util.ArrayList;

public class AdminContainerActivity extends DrawerActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_admin_container;
    }

    @Override
    public String getCustomTitle() {
        return "Containers";

    }

    RecyclerView recyclerView;

    public static ArrayList<Container> arrayList;
    ContainerAdapter adapter;
    DatabaseReference mDatabase = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance(FireBASEURL).getReference();

        arrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.list);
        adapter = new ContainerAdapter(AdminContainerActivity.this, arrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(),RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);

        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getBaseContext(),AddContainerActivity.class));

            }
        });



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


//                        dd.setStatus(dataSnapshot1.child("status").getValue().toString());

                        arrayList.add(dd);



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
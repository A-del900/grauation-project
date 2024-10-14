package com.graduation.kas.smartcontainer.Admin;

import static com.graduation.kas.smartcontainer.API.FireBASEURL;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.graduation.kas.smartcontainer.Admin.Driver.AddDriverActivity;
import com.graduation.kas.smartcontainer.Admin.Driver.Driver;
import com.graduation.kas.smartcontainer.Admin.Driver.DriverAdapter;
import com.graduation.kas.smartcontainer.DrawerActivity;
import com.graduation.kas.smartcontainer.R;

import java.util.ArrayList;

public class AdminMainActivity extends DrawerActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_admin_main;
    }

    @Override
    public String getCustomTitle() {
        return "Drivers";

    }

    RecyclerView recyclerView;
    public static ArrayList<Driver> arrayList;
    DriverAdapter adapter;
    DatabaseReference mDatabase = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance(FireBASEURL).getReference();

        arrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.list);
        adapter = new DriverAdapter(AdminMainActivity.this, arrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(),RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);

        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getBaseContext(), AddDriverActivity.class));

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
        DatabaseReference query = mDatabase.child("Users");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Iterable<DataSnapshot> data = dataSnapshot.getChildren();

                    for (DataSnapshot dataSnapshot1 : data) {
                        Driver dd = new Driver();

                        dd.setId(dataSnapshot1.getKey().toString());
                        dd.setName(dataSnapshot1.child("name").getValue().toString());
                        dd.setEmail(dataSnapshot1.child("email").getValue().toString());
                        dd.setPhone(dataSnapshot1.child("phone").getValue().toString());
                        dd.setUsername(dataSnapshot1.child("username").getValue().toString());
                        dd.setPassword(dataSnapshot1.child("password").getValue().toString());


                        if(dataSnapshot1.child("usertype").getValue().toString().equals("driver")) {
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
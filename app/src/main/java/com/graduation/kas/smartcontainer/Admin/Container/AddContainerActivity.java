package com.graduation.kas.smartcontainer.Admin.Container;

import static com.graduation.kas.smartcontainer.API.FireBASEURL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.app.akplacepicker.models.AddressData;
import com.app.akplacepicker.utilities.Constants;
import com.app.akplacepicker.utilities.PlacePicker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.graduation.kas.smartcontainer.DrawerActivity;
import com.graduation.kas.smartcontainer.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddContainerActivity  extends DrawerActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_container;
    }

    @Override
    public String getCustomTitle() {
        return "Add Container";

    }

    String lattitude = "";
    String longitude = "";
    Button location;

    EditText no,sensorid;
    Button add;

    Boolean userflag = true;
    DatabaseReference mDatabase = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance(FireBASEURL).getReference();

        location = findViewById(R.id.location);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(AddContainerActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AddContainerActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddContainerActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    return;
                }else {

                    Intent intent = new PlacePicker.IntentBuilder()
                            .setGoogleMapApiKey("AIzaSyC5Lbc-qWP_DtFn8p1WJ9GKaK4gihBDYcE")
                            .setLatLong(24.7, 46.6)
                            .setMapZoom(10.0f)
                            .setAddressRequired(true)
                            .setPrimaryTextColor(R.color.black)
                            .build(AddContainerActivity.this);

                    startActivityForResult(intent, 1000);
                }

            }
        });


        add = findViewById(R.id.add);

        no = findViewById(R.id.no);
        sensorid = findViewById(R.id.sensorid);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Add();

            }
        });

    }

    private void Add() {
        Boolean flag = false;
        if (no.getText().toString().isEmpty()) {
            no.setError("Please Enter Container No");
            flag = true;
        }


        if (sensorid.getText().toString().isEmpty()) {
            sensorid.setError("Please Enter Sensor ID");
            flag = true;
        }

        if (longitude.equals("")) {
            Toast.makeText(this, "Please Select Location", Toast.LENGTH_SHORT).show();
            flag = true;
        }


        if (!flag) {


            userflag = true;
            DatabaseReference query = mDatabase.child("Container");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.exists()) {

                    Iterable<DataSnapshot> data = dataSnapshot.getChildren();

                    for (DataSnapshot dataSnapshot1 : data) {
                        if(dataSnapshot1.child("no").getValue().toString().equals(no.getText().toString())){
                            Toast.makeText(AddContainerActivity.this, "Container No Already Used!", Toast.LENGTH_SHORT).show();
                            userflag = false;
                            break;
                        }


                    }
                    if(userflag) {

                        String uniqueKey = mDatabase.child("Container").push().getKey().toString();
                        mDatabase.child("Container").child(uniqueKey.toString())
                                .child("no").setValue(no.getText().toString());

                        mDatabase.child("Container").child(uniqueKey.toString())
                                .child("sensorid").setValue(sensorid.getText().toString());

                        mDatabase.child("Container").child(uniqueKey.toString())
                                .child("longitude").setValue(longitude);

                        mDatabase.child("Container").child(uniqueKey.toString())
                                .child("latitude").setValue(lattitude);


                        mDatabase.child("Container").child(uniqueKey.toString())
                                .child("status").setValue("Not Detected");


                        mDatabase.child("Container").child(uniqueKey.toString())
                                .child("driverid").setValue("0");




                        Toast.makeText(AddContainerActivity.this, "Container Added", Toast.LENGTH_SHORT).show();
                        finish();


                    }

                }
//                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("aboooooood",databaseError.getMessage().toString());

                }
            });



        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000) {
            if (resultCode == RESULT_OK) {
                AddressData addressData =  data.getParcelableExtra(Constants.ADDRESS_INTENT);
                this.longitude = String.valueOf(addressData.getLongitude());
                this.lattitude = String.valueOf(addressData.getLatitude());
            }else{

                Toast.makeText(this, String.valueOf(resultCode), Toast.LENGTH_SHORT).show();


            }
        }
    }
}
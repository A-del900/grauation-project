package com.graduation.kas.smartcontainer.Admin.Driver;

import static com.graduation.kas.smartcontainer.API.FireBASEURL;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.graduation.kas.smartcontainer.DrawerActivity;
import com.graduation.kas.smartcontainer.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddDriverActivity extends DrawerActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_driver;
    }

    @Override
    public String getCustomTitle() {
        return "Add Driver";

    }


    EditText username, phone,email,password,cpassword,name;
    Button add;

    Boolean userflag = true;
    DatabaseReference mDatabase = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mDatabase = FirebaseDatabase.getInstance(FireBASEURL).getReference();

        add = findViewById(R.id.add);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        cpassword = findViewById(R.id.cpassword);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        name = findViewById(R.id.name);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Add();

            }
        });


    }
    private void Add() {
        Boolean flag = false;
        if (name.getText().toString().isEmpty()) {
            name.setError("Please Enter Name");
            flag = true;
        }


        if (username.getText().toString().isEmpty()) {
            username.setError("Please Enter Username");
            flag = true;
        }

        if (phone.getText().toString().isEmpty()) {
            phone.setError("Please Enter Phone Number");
            flag = true;
        } else {
            if (phone.getText().toString().length() != 10) {
                phone.setError("Please Enter Valid Phone Number");
                flag = true;
            } else if (phone.getText().toString().charAt(0) != '0' || phone.getText().toString().charAt(1) != '5') {
                phone.setError("Please Enter Valid Phone Number");
                flag = true;
            }
        }
        if (email.getText().toString().isEmpty()) {
            email.setError("Please Enter Email");
            flag = true;
        } else {
            Pattern pattern;
            Matcher matcher;

            String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                    "[a-zA-Z0-9_+&*-]+)*@" +
                    "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                    "A-Z]{2,7}$";
            pattern = Pattern.compile(EMAIL_PATTERN);
            matcher = pattern.matcher(email.getText().toString());
            if (!matcher.matches()) {
                email.setError("Please Enter Valid Email");
                flag = true;
            }

        }
        if (cpassword.getText().toString().isEmpty()) {
            cpassword.setError("Please Enter Confirm Password");
            flag = true;
        }
        if (!password.getText().toString().equals(cpassword.getText().toString())) {
            cpassword.setError("Please Enter Check Confirm Password");
            flag = true;
        }


        if (password.getText().toString().isEmpty()) {
            password.setError("Please Enter Password");
            flag = true;
        } else {
            if (password.getText().toString().equals(password.getText().toString().toLowerCase())) {
                password.setError("Password Must contains Capital and small Letter");
                flag = true;

            }

            if (password.getText().toString().length() < 8) {
                password.setError("Password Must contains 8 Letters");
                flag = true;
            }
        }

        if (!flag) {
            userflag = true;
            DatabaseReference query = mDatabase.child("Users");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.exists()) {

                        Iterable<DataSnapshot> data = dataSnapshot.getChildren();

                        for (DataSnapshot dataSnapshot1 : data) {
                            if(dataSnapshot1.child("email").getValue().toString().equals(email.getText().toString())){
                                Toast.makeText(AddDriverActivity.this, "Email Already Used!", Toast.LENGTH_SHORT).show();
                                userflag = false;
                                break;
                            }
                            if(dataSnapshot1.child("phone").getValue().toString().equals(phone.getText().toString())){
                                Toast.makeText(AddDriverActivity.this, "Phone Number Already Used!", Toast.LENGTH_SHORT).show();
                                userflag = false;
                                break;

                            }
                            if(dataSnapshot1.child("username").getValue().toString().equals(username.getText().toString())){
                                Toast.makeText(AddDriverActivity.this, "Username Already Used!", Toast.LENGTH_SHORT).show();
                                userflag = false;
                                break;

                            }

                        }
                        if(userflag) {

                            String uniqueKey = mDatabase.child("Users").push().getKey().toString();
                            mDatabase.child("Users").child(uniqueKey.toString())
                                    .child("name").setValue(name.getText().toString());

                            mDatabase.child("Users").child(uniqueKey.toString())
                                    .child("email").setValue(email.getText().toString());

                            mDatabase.child("Users").child(uniqueKey.toString())
                                    .child("password").setValue(password.getText().toString());

                            mDatabase.child("Users").child(uniqueKey.toString())
                                    .child("phone").setValue(phone.getText().toString());

                            mDatabase.child("Users").child(uniqueKey.toString())
                                    .child("username").setValue(username.getText().toString());


                            mDatabase.child("Users").child(uniqueKey.toString())
                                    .child("usertype").setValue("driver");

                            Toast.makeText(AddDriverActivity.this, "Driver Added", Toast.LENGTH_SHORT).show();
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
}
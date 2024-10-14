package com.graduation.kas.smartcontainer;

import static com.graduation.kas.smartcontainer.API.FireBASEURL;

import android.os.Bundle;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateProfileActivity extends DrawerActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_update_profile;
    }

    @Override
    public String getCustomTitle() {
        return "Profile";
    }

    EditText phone,email,name;
    Button update;
    Session session;
    DatabaseReference mDatabase = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new Session(this);

        mDatabase = FirebaseDatabase.getInstance(FireBASEURL).getReference();

        update = findViewById(R.id.update);


        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        name = findViewById(R.id.name);


        name.setText(session.getName());
        email.setText(session.getEmail());
        phone.setText(session.getPhone());


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Update();
            }
        });

    }
    private void Update() {
        Boolean flag = false;
        if (name.getText().toString().isEmpty()) {
            name.setError("Please Enter Name");
            flag = true;
        }
        if (phone.getText().toString().isEmpty()) {
            phone.setError("Please Enter Phone number");
            flag = true;
        } else {
            if (phone.getText().toString().length() != 10) {
                phone.setError("Please Enter Valid Phone number");
                flag = true;
            } else if (phone.getText().toString().charAt(0) != '0' || phone.getText().toString().charAt(1) != '5') {
                phone.setError("Please Enter Valid Phone number");
                flag = true;
            }
        }
        if (email.getText().toString().isEmpty()) {
            email.setError("Please Enter E-mail");
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
                email.setError("Please Enter Valid E-mail");
                flag = true;
            }
        }

        if (!flag) {
            DatabaseReference query = mDatabase.child("Users");

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Iterable<DataSnapshot> data = dataSnapshot.getChildren();
                        Boolean flag = false;
                        for (DataSnapshot dataSnapshot1 : data) {
                            if ((dataSnapshot1.child("email").getValue().toString().equals(email.getText().toString()) && !dataSnapshot1.getKey().toString().equals(session.getId()))
                                    || (dataSnapshot1.child("phone").getValue().toString().equals(phone.getText().toString()) && !dataSnapshot1.getKey().toString().equals(session.getId()))) {

                                flag = true;
                                if(dataSnapshot1.child("email").getValue().toString().equals(email.getText().toString())){
                                    Toast.makeText(UpdateProfileActivity.this,"Email Already used!", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(UpdateProfileActivity.this, "Phone Number Already used!", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            }
                        }
                        if(!flag){
                            mDatabase.child("Users").child(session.getId()).child("name").setValue(name.getText().toString());
                            mDatabase.child("Users").child(session.getId()).child("email").setValue(email.getText().toString());
                            mDatabase.child("Users").child(session.getId()).child("phone").setValue(phone.getText().toString());
                            session.setName(name.getText().toString());
                            session.setPhone(phone.getText().toString());
                            session.setEmail(email.getText().toString());


                            Toast.makeText(getBaseContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
//                            finish();
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }
}
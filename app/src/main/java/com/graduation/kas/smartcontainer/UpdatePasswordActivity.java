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


public class UpdatePasswordActivity extends DrawerActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_update_password;
    }

    @Override
    public String getCustomTitle() {
        return "Update Password";
    }


    EditText newpassword,cpassword,oldpassword;
    Button update;

    Session session;
    DatabaseReference mDatabase = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        session = new Session(UpdatePasswordActivity.this);

        mDatabase = FirebaseDatabase.getInstance(FireBASEURL).getReference();


        newpassword = findViewById(R.id.newpassword);
        cpassword = findViewById(R.id.cpassword);
        oldpassword = findViewById(R.id.oldpassword);
        update = findViewById(R.id.update);


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePassword();
            }
        });

    }

    private void ChangePassword() {
        if (oldpassword.getText().toString().isEmpty()) {
            oldpassword.setError("Please Enter Old Password");
        } else if (newpassword.getText().toString().isEmpty()) {
            newpassword.setError("Please Enter New Password");
        } else {
            if (newpassword.getText().toString().length() < 8) {
                newpassword.setError("New Password Must Contains 8 Letters at least");
                return;
            }
            if (newpassword.getText().toString().equals(newpassword.getText().toString().toLowerCase())) {
                newpassword.setError("New Password Must Contains Capital and Small Letters");
                return;
            }
            if (cpassword.getText().toString().isEmpty()) {
                cpassword.setError("Please Enter Confirm Password");
                return;
            }

            if (!newpassword.getText().toString().equals(cpassword.getText().toString())) {
                cpassword.setError("Please Check Confirm Password");
                return;
            }


            DatabaseReference query = mDatabase.child("Users").child(session.getId());

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {

                        if(dataSnapshot.child("password").getValue().toString().equals(oldpassword.getText().toString())){

                            mDatabase.child("Users").child(session.getId()).child("password").setValue(newpassword.getText().toString());
                            Toast.makeText(UpdatePasswordActivity.this, "Password Updated", Toast.LENGTH_SHORT).show();
//                            finish();

                        }else{
                            Toast.makeText(UpdatePasswordActivity.this, "Previous Password Im Correct !!", Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        Toast.makeText(UpdatePasswordActivity.this, "User Not found!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }
}
package com.graduation.kas.smartcontainer;

import static com.graduation.kas.smartcontainer.API.FireBASEURL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText username,password;
    Button login;
    TextView forget;

    Session session;
    DatabaseReference mDatabase = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase = FirebaseDatabase.getInstance(FireBASEURL).getReference();



        session = new Session(this);


        if(session.getLoggedIn()) {
            Intent i = new Intent(getBaseContext(), WelcomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }


        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login =  findViewById(R.id.login);
        forget = findViewById(R.id.forget);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });



        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), ForgetPasswordActivity.class));

            }
        });
    }
    private void Login(){
        if (!username.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {

            DatabaseReference query = mDatabase.child("Users");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Iterable<DataSnapshot> data = dataSnapshot.getChildren();

                        for (DataSnapshot dataSnapshot1 : data) {
                            if (dataSnapshot1.child("username").getValue().toString().equals(username.getText().toString())
                                    && dataSnapshot1.child("password").getValue().toString().equals(password.getText().toString())) {

                                String usertype = dataSnapshot1.child("usertype").getValue().toString();

                                if (usertype.equals("admin")) {


                                    session.setId(dataSnapshot1.getKey().toString());
                                    session.setEmail(dataSnapshot1.child("email").getValue().toString());
                                    session.setPhone(dataSnapshot1.child("phone").getValue().toString());
                                    session.setUsername(dataSnapshot1.child("username").getValue().toString());
                                    session.setName(dataSnapshot1.child("name").getValue().toString());

                                    session.setLogin(true);
                                    session.setUsertype(usertype);


                                    Toast.makeText(MainActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getBaseContext(), WelcomeActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                    finish();


                                } if (usertype.equals("driver")) {


                                    session.setId(dataSnapshot1.getKey().toString());
                                    session.setEmail(dataSnapshot1.child("email").getValue().toString());
                                    session.setPhone(dataSnapshot1.child("phone").getValue().toString());
                                    session.setUsername(dataSnapshot1.child("username").getValue().toString());
                                    session.setName(dataSnapshot1.child("name").getValue().toString());
                                    session.setLogin(true);
                                    session.setUsertype(usertype);
                                    Toast.makeText(MainActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getBaseContext(), WelcomeActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                    finish();
                                }


                            }
                        }
                        if(!session.getLoggedIn()){
                            Toast.makeText(MainActivity.this, "Username or password incorrect!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }else if(username.getText().toString().isEmpty()){
            username.setError("Please Enter Username");

        }else if(password.getText().toString().isEmpty()){
            password.setError("Please Enter Password");
        }
    }
}
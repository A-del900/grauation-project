package com.graduation.kas.smartcontainer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class WelcomeActivity extends DrawerActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    public String getCustomTitle() {
        return "Home";

    }


    Session session;
    TextView welcome;

    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        image = findViewById(R.id.image);
        welcome = findViewById(R.id.welcome);
        session = new Session(WelcomeActivity.this);

        welcome.setText(session.getName());




    }
}
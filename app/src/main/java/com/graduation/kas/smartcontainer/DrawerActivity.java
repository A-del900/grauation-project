package com.graduation.kas.smartcontainer;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.graduation.kas.smartcontainer.Admin.AdminMainActivity;
import com.graduation.kas.smartcontainer.Admin.Container.AdminContainerActivity;
import com.graduation.kas.smartcontainer.Driver.DriverContainerActivity;


public abstract class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected NavigationView navigationView;

    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigration_activity);
        session = new Session(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getCustomTitle());
        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.main_content);
        getLayoutInflater().inflate(getLayoutId(), frameLayout);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if(session.getUsertype().equals("admin")) {
            navigationView.inflateMenu(R.menu.admin);
        }else if(session.getUsertype().equals("driver")) {
            navigationView.inflateMenu(R.menu.driver);
        }


    }

    public abstract int getLayoutId();

    public abstract String getCustomTitle();

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if(session.getUsertype().equals("admin")) {
            int id = item.getItemId();

            if (id == R.id.profile) {
                launchActivity(UpdateProfileActivity.class);
            } else if (id == R.id.password) {
                launchActivity(UpdatePasswordActivity.class);
            }

            else if (id == R.id.drivers) {
                launchActivity(AdminMainActivity.class);
            }

            else if (id == R.id.containers) {
                launchActivity(AdminContainerActivity.class);
            }

            else if (id == R.id.logout) {
                session.setLogin(false);
                session.setEmail("");
                session.setId("");
                session.setPhone("");
                session.setName("");
                startActivity(new Intent(getBaseContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }else  if(session.getUsertype().equals("driver")) {
            int id = item.getItemId();

            if (id == R.id.profile) {
                launchActivity(UpdateProfileActivity.class);
            } else if (id == R.id.password) {
                launchActivity(UpdatePasswordActivity.class);
            }else if (id == R.id.containers) {
                launchActivity(DriverContainerActivity.class);
            }

            else if (id == R.id.logout) {
                session.setLogin(false);
                session.setEmail("");
                session.setId("");
                session.setPhone("");
                session.setName("");
                startActivity(new Intent(getBaseContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }

        return false;
    }

    private void launchActivity(Class _class) {
        //Avoid launch the same activity
        if (this.getClass() != _class) {
            startActivity(makePageIntent(_class));
            overridePendingTransition(0,0);
            //  finish();
        }
    }

    private void launchActivity(Class _class,String extra) {
        //Avoid launch the same activity
//        if (this.getClass() != _class) {
            startActivity(makePageIntent(_class).putExtra("type",extra));
            overridePendingTransition(0,0);
            // finish();
//        }
    }

    public Intent makePageIntent(Class _class) {
        Intent intent = new Intent(this, _class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }
}

package com.example.disastermanagement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.firebase.auth.FirebaseAuth;

public class AdminNavDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    private DrawerLayout drawer;
    Singleton ls = Singleton.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_nav_drawer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.syncState();

        if (savedInstanceState == null) {
            hideKeyboard(this);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new Emergency()).commit();
            navigationView.setCheckedItem(R.id.nav_emergency);

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_emergency:
                hideKeyboard(this);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Emergency()).commit();
                break;
            case R.id.nav_disasters:
                hideKeyboard(this);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new DisasterRecycler()).commit();
                break;
            case R.id.nav_volunteers:
                hideKeyboard(this);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new VolunteerRecycler()).commit();
                break;
            case R.id.nav_createVolunteer:
                hideKeyboard(this);
                Intent i = new Intent(this, CreateVolunteerAccount.class);
                startActivity(i);
                finish();
                break;
            case R.id.nav_profile:
                hideKeyboard(this);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AdminProfile()).commit();
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                Intent backIntent = new Intent(this,LoginActivity.class);
                startActivity(backIntent);
                finish();
                break;
            case R.id.nav_quit:
                finish();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View currentFocusedView = activity.getCurrentFocus();
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        else if(ls.fragmentFlag){

            super.onBackPressed();
        }

        else if (!(ls.fragmentFlag)) {

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new Emergency()).commit();
            navigationView.setCheckedItem(R.id.nav_emergency);
            ls.fragmentFlag = true;
        }

        else {
            getSupportFragmentManager().popBackStack();
        }
    }
}

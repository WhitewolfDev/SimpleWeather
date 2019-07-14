package com.jggdevelopment.simpleweather;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.core.view.GravityCompat;

import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import com.jggdevelopment.simpleweather.fragments.MasterFragment;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;

import static com.jggdevelopment.simpleweather.utilities.FragmentHelper.pushToFragmentManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefs = getSharedPreferences("com.jggdevelopment.simpleweather", Context.MODE_PRIVATE);
        if (prefs.getBoolean("useDarkTheme", false)) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.LightTheme);
        }

        super.onCreate(savedInstanceState);

        // Setup the content view
        setContentView(R.layout.activity_main);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        // This is a mostly blank activity, so MasterFragment needs to be pushed onto the content_frame
        MasterFragment master = new MasterFragment();
        pushToFragmentManager(getSupportFragmentManager(), R.id.content_frame, master, false);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

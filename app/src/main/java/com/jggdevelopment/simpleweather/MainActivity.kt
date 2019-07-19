package com.jggdevelopment.simpleweather

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.core.view.GravityCompat

import android.view.MenuItem
import com.google.android.material.navigation.NavigationView
import com.jggdevelopment.simpleweather.fragments.MasterFragment

import androidx.drawerlayout.widget.DrawerLayout

import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.jggdevelopment.simpleweather.models.Forecast

import com.jggdevelopment.simpleweather.utilities.FragmentHelper.pushToFragmentManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var prefs: SharedPreferences
    private lateinit var listener : SharedPreferences.OnSharedPreferenceChangeListener

    override fun onCreate(savedInstanceState: Bundle?) {
        prefs = getSharedPreferences("com.jggdevelopment.simpleweather", Context.MODE_PRIVATE)
        if (prefs.getBoolean("useDarkTheme", false)) {
            setTheme(R.style.DarkTheme)
        } else {
            setTheme(R.style.LightTheme)
        }

        super.onCreate(savedInstanceState)

        // Setup the content view
        setContentView(R.layout.activity_main)

        nav_view.setNavigationItemSelectedListener(this)

        // This is a mostly blank activity, so MasterFragment needs to be pushed onto the content_frame
        pushToFragmentManager(supportFragmentManager, R.id.content_frame, MasterFragment(), false)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        if (id == R.id.nav_settings) {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}

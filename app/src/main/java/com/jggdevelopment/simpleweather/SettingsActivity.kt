package com.jggdevelopment.simpleweather

import android.app.Activity
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import com.crashlytics.android.Crashlytics
import kotlinx.android.synthetic.main.fragment_master.toolbar

class SettingsActivity : AppCompatActivity() {
    private lateinit var mToolbar: Toolbar
    private lateinit var prefs: SharedPreferences
    public var settingsChanged : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        prefs = getSharedPreferences("com.jggdevelopment.simpleweather", Context.MODE_PRIVATE)
        if (prefs.getBoolean("useDarkTheme", false)) {
            setTheme(R.style.DarkTheme)
        } else {
            setTheme(R.style.LightTheme)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        mToolbar = toolbar

        setSupportActionBar(mToolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
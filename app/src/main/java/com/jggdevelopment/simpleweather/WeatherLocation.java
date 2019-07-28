package com.jggdevelopment.simpleweather;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.libraries.places.api.Places;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class WeatherLocation {

    private LocationManager locationManager;
    private Double latitude = null;
    private Double longitude = null;

    /**
     * Gets user's location.  If location services are off, it warns the user to turn them on
     * @param activity parent activity
     */
    public WeatherLocation(Activity activity) {
        // Initialize Places.
        Places.initialize(activity, BuildConfig.googlePlacesAPI_KEY);
        Places.createClient(activity);

        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps(activity);
        } else {
            getLocation(activity);
        }
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    /**
     * gets the user's current location
     * @param activity parent activity
     */
    private void getLocation(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[] {ACCESS_FINE_LOCATION}, 200);
        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            } else if (location1 != null) {
                latitude = location1.getLatitude();
                longitude = location1.getLongitude();
            } else if (location2 != null) {
                latitude = location2.getLatitude();
                longitude = location2.getLongitude();
            } else {
                Toast.makeText(activity, "Unable to get location", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Creates an alert message to tel lthe user to turn their location on
     * Allows them to open settings to do so
     * @param activity parent activity
     */
    private void buildAlertMessageNoGps(final Activity activity) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Please Turn on your GPS connection")
                .setCancelable(false)
                .setPositiveButton("Turn on", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        activity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}

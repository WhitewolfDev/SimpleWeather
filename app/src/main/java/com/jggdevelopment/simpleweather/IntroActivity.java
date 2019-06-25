package com.jggdevelopment.simpleweather;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class IntroActivity extends AppCompatActivity {

    private Button enableLocationButton;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = this.getSharedPreferences("com.jggdevelopment.simpleweather", Context.MODE_PRIVATE);

        Mapbox.getInstance(this, BuildConfig.mapboxAPI_KEY);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (introShownBefore()) {
            nextScreen();
        }


        setContentView(R.layout.activity_intro);

        enableLocationButton = findViewById(R.id.enable_location_button);
        enableLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission();
            }
        });
    }

    private void endIntro(Point point) {
        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);

        prefs.edit().putString("defaultLatitude", Double.toString(point.latitude())).commit();
        prefs.edit().putString("defaultLongitude", Double.toString(point.longitude())).commit();
        prefs.edit().putBoolean("defaultLocationSet", true).commit();
        startActivity(mainActivity);
        setIntroShown();
        finish();
    }

    private void endIntro() {
        prefs.edit().putBoolean("defaultLocationSet", false).commit();

        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainActivity);
        setIntroShown();
        finish();
    }

    private void nextScreen() {
        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainActivity);
        setIntroShown();
        finish();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[] {ACCESS_FINE_LOCATION}, 200);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                    showMessageGetLocation(getString(R.string.locationAccessDeniedMessage), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[] {ACCESS_FINE_LOCATION}, 200);
                        }
                    });
                }
            }
        } else {
            endIntro();
        }
    }

    private void showMessageGetLocation(String message, DialogInterface.OnClickListener okListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        builder.setTitle("Default Location");
        builder.setMessage(message);

        // prompts user to search for location.  Once they pick, it will end the activity
        // and pass the selection to MainActivity
        builder.setPositiveButton("Enter location", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new PlaceAutocomplete.IntentBuilder()
                        .accessToken(Mapbox.getAccessToken())
                        .placeOptions(PlaceOptions.builder()
                                .backgroundColor(Color.parseColor("#EEEEEE"))
                                .limit(10)
                                .build(PlaceOptions.MODE_CARDS))
                        .build(IntroActivity.this);
                startActivityForResult(intent, 1);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        final AlertDialog dialog = builder.create();

        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.colorPrimary));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getColor(R.color.greyText));
                TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                textView.setTextSize(18);
            }
        });

        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                CarmenFeature feature = PlaceAutocomplete.getPlace(data);
                endIntro(feature.center());
            }
        }
    }

    private boolean introShownBefore() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        return pref.getBoolean("wasIntroOpened",false);
    }

    private void setIntroShown() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("wasIntroOpened",true);
        editor.apply();
    }

}

package com.example.trackingapp;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.trackingapp.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataType;

import java.util.concurrent.TimeUnit;

public class MainActivity extends FragmentActivity {

    private TextView mTextView;
    private ImageView imageView;
    private ActivityMainBinding binding;
    private float[] heartRateData;

    private static final int MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION = 100;
    private static final int REQUEST_CODE = 1;
    private FitnessOptions fitnessOptions;
    private GoogleSignInAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Prüfe, ob nötige Berechtigungen gewährt wurden:
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
        }
        //Falls Berechtigungen noch nicht gewärt, anforden:

        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.BODY_SENSORS, Manifest.permission.ACTIVITY_RECOGNITION}, MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        //Initialisiere fitnessOptions mit erforderlichen Datentypen
        fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_HEART_RATE_BPM, FitnessOptions.ACCESS_READ)
                .build();


        //Referenz ImageView & startButton
        Button startButton = findViewById(R.id.startButton);

        //OnClickListener für startButton
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "Aufnahme gestartet";
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT);
            }
        });

        /* Hier API Integration
        heartRateGraphView = findViewById(R.id.)
        */
    }

    public void setHeartRateData(float[] data) {
        heartRateData = data;

        //Aktualisiere die Ansicht mit neuen Daten
        //heartRateGraphView.setDataPoints(heartRateData);
    }

}
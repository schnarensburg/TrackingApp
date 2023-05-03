package com.example.trackingapp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.trackingapp.databinding.ActivityMainBinding;
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

import java.util.concurrent.TimeUnit;

public class MainActivity extends FragmentActivity {

    private TextView mTextView;
    private ImageView imageView;
    private ActivityMainBinding binding;
    private float[] heartRateData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        binding = ActivityMainBinding.inflate(getLayoutInflater());


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
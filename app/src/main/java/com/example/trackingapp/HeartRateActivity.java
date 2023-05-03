package com.example.trackingapp;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;


public class HeartRateActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Referenz auf den SensorManager
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //Referenz auf den Herzfrequenzsensor
        Sensor heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);

        //Erstelle SensorEventListener, um auf Sensor-Updates zu reagieren
        SensorEventListener heartRateListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                // Verarbeitung Herzfrequenzdaten
                float heartRate = event.values[0];
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // Handhabung von Genauigkeitsänderungen
            }
        };

        //Registriere den SensorEventListener, um auf Sensor-Updates zu lauschen
        sensorManager.registerListener(heartRateListener, heartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

}

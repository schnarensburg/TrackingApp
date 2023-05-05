package com.example.trackingapp;

import java.awt.event.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBox;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import androidx.fragment.app.FragmentActivity;

public class MainActivity extends FragmentActivity implements ItemListener {

    private final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate");

        //Get references to the main UI components
        CheckBox heartRate = findViewById(R.id.heartRate);
        heartRate.addItemListener(this);
        CheckBox heartRateVariability = findViewById(R.id.heartRateVariability);
        CheckBox skinTemperature = findViewById(R.id.skinTemperature);
        Button startButton = findViewById(R.id.startButton);

        startButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_heart_rate);
            }
        });

        //More sensors to add here, if needed
        //Probably belongs in onClick above..?
        String[] values = new String[]{"HeartRate"};
        ListView mListView = findViewById(R.id.sensors_list_view);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, values);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener( (parent, view, position, id) -> {
            startActivity(new Intent(this, HeartRateActivity.class));
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }
}

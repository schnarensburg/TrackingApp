package com.example.trackingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;

import com.example.trackingapp.databinding.ActivityHeartRateBinding;
import com.samsung.android.service.health.tracking.ConnectionListener;
import com.samsung.android.service.health.tracking.HealthTracker;
import com.samsung.android.service.health.tracking.HealthTrackerException;
import com.samsung.android.service.health.tracking.HealthTrackingService;
import com.samsung.android.service.health.tracking.data.DataPoint;
import com.samsung.android.service.health.tracking.data.HealthTrackerType;
import com.samsung.android.service.health.tracking.data.ValueKey;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import javax.sql.ConnectionEventListener;

public class HeartRateActivity extends FragmentActivity implements ConnectionListener {

    private final String TAG = HeartRateActivity.class.getSimpleName();
    private ActivityHeartRateBinding binding;
    private HealthTrackingService healthTrackingService = null;
    @NotNull
    private final String[] permissions = {"android.permission.BODY_SENSORS"};
    private final int REQUEST_ACCOUNT_PERMISSION = 100;
    private boolean isHandlerRunning;
    private final Handler handler = new Handler(Looper.myLooper());
    private HealthTracker heartRateTracker = null;

    private final ConnectionListener connectionListener = new ConnectionListener() {
        @Override
        public void onConnectionSuccess() {
            Toast.makeText(
                    getApplicationContext(),"Connected to HSP",Toast.LENGTH_SHORT
            ).show();
            binding.progressBar.setVisibility(View.INVISIBLE);
            try {
                heartRateTracker = healthTrackingService.getHealthTracker(HealthTrackerType.HEART_RATE);
            } catch (final IllegalArgumentException e) {
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show()
                );
                finish();
            }

            binding.hrTrackerStartButton.setOnClickListener(view -> {
                Log.i(TAG, " setEventListener called ");
                if(!isHandlerRunning) {
                    handler.post(() -> {
                        heartRateTracker.setEventListener(trackerEventListener);
                        isHandlerRunning = true;
                    });
                }
            });

            binding.hrTrackerStopButton.setOnClickListener(view -> {
                Log.i(TAG, " unsetEventListener called ");
                if (heartRateTracker != null) {
                    heartRateTracker.unsetEventListener();
                }
                handler.removeCallbacksAndMessages(null);
                isHandlerRunning = false;
            });

            binding.hrTrackerFlushButton.setOnClickListener(view -> {
                Log.i(TAG, " flush called ");
                if(!heartRateTracker.flush()) {
                    Toast.makeText(getApplicationContext(), "Sensor has not started yet",
                            Toast.LENGTH_LONG).show();
                }
            } );
        }

        @Override
        public void onConnectionEnded() {

        }

        @Override
        public void onConnectionFailed(HealthTrackerException e) {
            if(e.hasResolution()) {
                e.resolve(HeartRateActivity.this);
            }
            runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Unable to connect to HSP", Toast.LENGTH_LONG).show()
            );
            finish();
        }
    };

    private final HealthTracker.TrackerEventListener trackerEventListener = new HealthTracker.TrackerEventListener() {
        @Override
        public void onDataReceived(@NonNull List<DataPoint> list) {
            if (list.size() != 0) {
                Log.i(TAG, "List Size : "+list.size());
                for(DataPoint dataPoint : list) {
                    Log.i(TAG, "Timestamp : "+dataPoint.getTimestamp());
                    int hr = dataPoint.getValue(ValueKey.HeartRateSet.HEART_RATE);
                    int hrIbi = dataPoint.getValue(ValueKey.HeartRateSet.HEART_RATE_IBI);
                    int status = dataPoint.getValue(ValueKey.HeartRateSet.STATUS);
                    Log.i(TAG, "HR : "+hr+" HR_IBI : "+hrIbi+" Status : "+status);
                    runOnUiThread(() -> {
                        binding.hrValue.setText(String.valueOf(hr));
                        binding.hrIbiValue.setText(String.valueOf(hrIbi));
                        if(status == 1)
                            binding.hrFlagValue.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                        else
                            binding.hrFlagValue.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
                        binding.hrFlagValue.setText(String.valueOf(status));
                    });
                }
            } else {
                Log.i(TAG, "onDataReceived List is zero");
            }
        }

        @Override
        public void onFlushCompleted() {
            Log.i(TAG, " onFlushCompleted called");
        }

        @Override
        public void onError(HealthTracker.TrackerError trackerError) {
            Log.i(TAG, " onError called");
            if (trackerError == HealthTracker.TrackerError.PERMISSION_ERROR) {
                runOnUiThread(() -> Toast.makeText(getApplicationContext(),
                        "Permissions Check Failed", Toast.LENGTH_SHORT).show());
            }
            if (trackerError == HealthTracker.TrackerError.SDK_POLICY_ERROR) {
                runOnUiThread(() -> Toast.makeText(getApplicationContext(),
                        "SDK Policy denied", Toast.LENGTH_SHORT).show());
            }
            isHandlerRunning = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (PermissionActivity.checkPermission((Context)this, this.permissions)) {
            Log.i(TAG, "onCreate Permission granted");
            setUp();
        } else {
            Log.i(TAG, "onCreate Permission not granted");
            PermissionActivity.showPermissionPrompt((Activity)this, this.REQUEST_ACCOUNT_PERMISSION, this.permissions);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult requestCode = " + requestCode + " resultCode = " + resultCode);
        if (requestCode == this.REQUEST_ACCOUNT_PERMISSION) {
            if (resultCode == -1) {
                setUp();
            } else {
                finish();
            }
        }
    }

    public final void setUp() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_heart_rate);
        healthTrackingService = new HealthTrackingService(connectionListener, getApplicationContext());
        healthTrackingService.connectService();
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(heartRateTracker != null) {
            heartRateTracker.unsetEventListener();
        }
        handler.removeCallbacksAndMessages(null);
        isHandlerRunning = false;
        if(healthTrackingService != null) {
            healthTrackingService.disconnectService();
        }
    }
}
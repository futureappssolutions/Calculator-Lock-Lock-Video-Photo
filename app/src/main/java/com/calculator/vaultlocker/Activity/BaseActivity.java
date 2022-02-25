package com.calculator.vaultlocker.Activity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.calculator.vaultlocker.panicswitch.AccelerometerListener;
import com.calculator.vaultlocker.panicswitch.AccelerometerManager;
import com.calculator.vaultlocker.panicswitch.PanicSwitchActivityMethods;
import com.calculator.vaultlocker.panicswitch.PanicSwitchCommon;
import com.calculator.vaultlocker.securitylocks.SecurityLocksCommon;
import com.calculator.vaultlocker.storageoption.StorageOptionsCommon;
import com.calculator.vaultlocker.utilities.Common;
import com.calculator.vaultlocker.utilities.FileUtils;

import java.io.File;
import java.io.IOException;

public class BaseActivity extends AppCompatActivity implements AccelerometerListener, SensorEventListener {
    private static BaseActivity baseActivity;
    private SensorManager sensorManager;

    public static BaseActivity getBaseActivity() {
        return baseActivity;
    }

    @Override
    public void onAccelerationChanged(float f, float f2, float f3) {
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        baseActivity = this;
        this.sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (!Common.imageLoader.isInited()) {
            Common.initImageLoader(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (SecurityLocksCommon.IsAppDeactive) {
            File file = new File(StorageOptionsCommon.STORAGEPATH + "/" + StorageOptionsCommon.AUDIOS_TEMP_FOLDER);
            if (file.exists() && file.isDirectory()) {
                try {
                    FileUtils.deleteDirectory(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        this.sensorManager.unregisterListener(this);
        if (AccelerometerManager.isListening()) {
            AccelerometerManager.stopListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AccelerometerManager.isSupported(this)) {
            AccelerometerManager.startListening(this);
        }
        SensorManager sensorManager = this.sensorManager;
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(8), 3);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == 8 && sensorEvent.values[0] == 0.0f && PanicSwitchCommon.IsPalmOnFaceOn) {
            PanicSwitchActivityMethods.SwitchApp(this);
        }
    }

    @Override
    public void onShake(float f) {
        if (PanicSwitchCommon.IsFlickOn || PanicSwitchCommon.IsShakeOn) {
            PanicSwitchActivityMethods.SwitchApp(this);
        }
    }
}

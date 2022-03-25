package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.R;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.common.DataRecover;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.AccelerometerManager;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchActivityMethods;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.securitylocks.SecurityLocksCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.storageoption.StorageOptionsCommon;

import java.util.Objects;

public class DataRecoveryActivity extends BaseActivity {
    private ProgressDialog myProgressDialog;
    Handler handle = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            if (message.what == 3) {
                if (StorageOptionsCommon.IsAllDataRecoveryInProg) {
                    hideProgress();
                    StorageOptionsCommon.IsAllDataRecoveryInProg = false;
                    if (StorageOptionsCommon.IsUserHasDataToRecover) {
                        StorageOptionsCommon.IsUserHasDataToRecover = false;
                        Toast.makeText(DataRecoveryActivity.this, R.string.toast_dataRecovery_Success, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(DataRecoveryActivity.this, R.string.toast_dataRecovery_have_no_data, Toast.LENGTH_LONG).show();
                    }
                }
            } else if (message.what == 2) {
                hideProgress();
            }
            super.handleMessage(message);
        }
    };
    private SensorManager sensorManager;

    @Override
    public void onAccelerationChanged(float f, float f2, float f3) {
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public void showDataRecoveryProgress() {
        myProgressDialog = ProgressDialog.show(this, null, "Your data is being recovered\nWarning: Please be patient and do not close this app otherwise you may lose your data.", true);
    }

    public void hideProgress() {
        if (myProgressDialog != null && myProgressDialog.isShowing()) {
            myProgressDialog.dismiss();
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_data_recovery);
        
        SecurityLocksCommon.IsAppDeactive = true;
        StorageOptionsCommon.IsUserHasDataToRecover = false;
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        
        Toolbar toolbar = findViewById(R.id.toolbar);
        AppCompatButton btnRecover = findViewById(R.id.btnRecover);
        
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Data Recovery");
        toolbar.setNavigationIcon(R.drawable.ic_top_back_icon);
        toolbar.setNavigationOnClickListener(view -> btnBackonClick());

        btnRecover.setOnClickListener(view -> {
            showDataRecoveryProgress();
            StorageOptionsCommon.IsAllDataRecoveryInProg = true;
            new Thread() {
                @Override
                public void run() {
                    try {
                        new DataRecover().RecoverALLData(DataRecoveryActivity.this);
                        Message message = new Message();
                        message.what = 3;
                        handle.sendMessage(message);
                    } catch (Exception unused) {
                        Message message2 = new Message();
                        message2.what = 2;
                        handle.sendMessage(message2);
                    }
                }
            }.start();
        });
    }

    public void btnBackonClick() {
        SecurityLocksCommon.IsAppDeactive = false;
        startActivity(new Intent(this, SettingActivity.class));
        finish();
    }

    @Override
    public void onShake(float f) {
        if (PanicSwitchCommon.IsFlickOn || PanicSwitchCommon.IsShakeOn) {
            PanicSwitchActivityMethods.SwitchApp(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == 8 && sensorEvent.values[0] == 0.0f && PanicSwitchCommon.IsPalmOnFaceOn) {
            PanicSwitchActivityMethods.SwitchApp(this);
        }
    }

    @Override
    public void onPause() {
        sensorManager.unregisterListener(this);
        if (AccelerometerManager.isListening()) {
            AccelerometerManager.stopListening();
        }
        if (SecurityLocksCommon.IsAppDeactive && !StorageOptionsCommon.IsAllDataRecoveryInProg) {
            finish();
            System.exit(0);
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        if (AccelerometerManager.isSupported(this)) {
            AccelerometerManager.startListening(this);
        }
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(8), 3);
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            SecurityLocksCommon.IsAppDeactive = false;
            startActivity(new Intent(this, SettingActivity.class));
            finish();
        }
        return super.onKeyDown(i, keyEvent);
    }
}

package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Activity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.R;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.AccelerometerManager;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchActivityMethods;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.common.RecoveryOfCredentialsMethods;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.securitylocks.SecurityLocksCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.securitylocks.SecurityLocksSharedPreferences;

import java.util.Objects;

public class RecoveryOfCredentialsActivity extends BaseActivity {
    private EditText txtrecovery_email;
    private SensorManager sensorManager;

    @Override
    public void onAccelerationChanged(float f, float f2, float f3) {
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_recovery_of_credentials);

        SecurityLocksCommon.IsAppDeactive = true;
        this.sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        getWindow().setSoftInputMode(5);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Recovery Of Security Locks");
        toolbar.setNavigationIcon(R.drawable.ic_top_back_icon);
        toolbar.setNavigationOnClickListener(view -> RecoveryOfCredentialsActivity.this.btnBackonClick());

        this.txtrecovery_email = findViewById(R.id.txtrecovery_email);
        TextView lblrecovery_email_description = findViewById(R.id.lblrecovery_email_description);
        LinearLayout ll_Cancel = findViewById(R.id.ll_Cancel);
        LinearLayout ll_Save = findViewById(R.id.ll_Save);

        this.sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        lblrecovery_email_description.setText(R.string.lblsetting_SecurityCredentials_recoery_email_activity_description);

        this.txtrecovery_email.setTextColor(getResources().getColor(R.color.Color_Secondary_Font));
        this.txtrecovery_email.setText(SecurityLocksSharedPreferences.GetObject(this).GetEmail());
        this.txtrecovery_email.setFocusable(false);
        this.txtrecovery_email.setFocusableInTouchMode(false);
        this.txtrecovery_email.setClickable(false);

        this.txtrecovery_email.setOnClickListener(view -> {
            RecoveryOfCredentialsActivity.this.txtrecovery_email.setFocusable(true);
            RecoveryOfCredentialsActivity.this.txtrecovery_email.setFocusableInTouchMode(true);
            RecoveryOfCredentialsActivity.this.txtrecovery_email.setClickable(true);
        });

        ll_Cancel.setOnClickListener(view -> RecoveryOfCredentialsActivity.this.Cancel());

        ll_Save.setOnClickListener(view -> RecoveryOfCredentialsActivity.this.Save());
    }

    public void btnBackonClick() {
        SecurityLocksCommon.isBackupPasswordPin = false;
        SecurityLocksCommon.IsAppDeactive = false;
        startActivity(new Intent(this, SettingActivity.class));
        finish();
    }

    public void Cancel() {
        SecurityLocksCommon.isBackupPasswordPin = false;
        SecurityLocksCommon.IsAppDeactive = false;
        startActivity(new Intent(this, SettingActivity.class));
        finish();
    }

    public void Save() {
        SecurityLocksSharedPreferences GetObject = SecurityLocksSharedPreferences.GetObject(this);
        if (this.txtrecovery_email.getText().toString().length() <= 0) {
            Toast.makeText(this, R.string.toast_setting_SecurityCredentials_Recoveryemail_please_enter, Toast.LENGTH_SHORT).show();
        } else if (new RecoveryOfCredentialsMethods().isEmailValid(this.txtrecovery_email.getText().toString())) {
            GetObject.SetEmail(this.txtrecovery_email.getText().toString());
            Toast.makeText(this, R.string.toast_setting_SecurityCredentials_Recoveryemailsetsuccesfully, Toast.LENGTH_SHORT).show();
            SecurityLocksCommon.isBackupPasswordPin = false;
            SecurityLocksCommon.IsAppDeactive = false;
            startActivity(new Intent(this, SettingActivity.class));
            finish();
        } else {
            Toast.makeText(this, R.string.toast_setting_SecurityCredentials_Recoveryemail_invalid, Toast.LENGTH_SHORT).show();
        }
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
        this.sensorManager.unregisterListener(this);
        if (AccelerometerManager.isListening()) {
            AccelerometerManager.stopListening();
        }
        if (SecurityLocksCommon.IsAppDeactive) {
            finish();
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
            SecurityLocksCommon.isBackupPasswordPin = false;
            SecurityLocksCommon.IsAppDeactive = false;
            startActivity(new Intent(this, SettingActivity.class));
            finish();
        }
        return super.onKeyDown(i, keyEvent);
    }
}

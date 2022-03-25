package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.widget.Toolbar;

import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.R;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Activity.BaseActivity;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.securitylocks.SecurityLocksCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Activity.SettingActivity;

public class PanicSwitchActivity extends BaseActivity {
    ToggleButton btnFlick;
    ToggleButton btnPalmOnScreen;
    ToggleButton btnShake;
    private SensorManager sensorManager;
    private Toolbar toolbar;

    @Override
    // calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Activity.BaseActivity, calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.AccelerometerListener
    public void onAccelerationChanged(float f, float f2, float f3) {
    }

    @Override // calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Activity.BaseActivity, android.hardware.SensorEventListener
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override
    // calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Activity.BaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.panic_switch_activity);
        SecurityLocksCommon.IsAppDeactive = true;
        getWindow().addFlags(128);
        SecurityLocksCommon.IsAppDeactive = true;
        this.btnFlick = (ToggleButton) findViewById(R.id.togglebtnFlick);
        this.btnShake = (ToggleButton) findViewById(R.id.togglebtnShake);
        this.btnPalmOnScreen = (ToggleButton) findViewById(R.id.togglebtnPalmOnScreen);
        this.sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.toolbar = toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Panic Switch");
        this.toolbar.setNavigationIcon(R.drawable.ic_top_back_icon);
        this.toolbar.setNavigationOnClickListener(new View.OnClickListener() { // from class: calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                PanicSwitchActivity.this.btnBackonClick();
            }
        });
        final PanicSwitchSharedPreferences GetObject = PanicSwitchSharedPreferences.GetObject(this);
        PanicSwitchCommon.IsFlickOn = GetObject.GetIsFlickOn();
        PanicSwitchCommon.IsShakeOn = GetObject.GetIsShakeOn();
        PanicSwitchCommon.IsPalmOnFaceOn = GetObject.GetIsPalmOnScreenOn();
        PanicSwitchCommon.SwitchingApp = GetObject.GetSwitchApp();
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioChooseSwitchApp);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchActivity.2
            @Override // android.widget.RadioGroup.OnCheckedChangeListener
            public void onCheckedChanged(RadioGroup radioGroup2, int i) {
                if (i == R.id.HomeScreen) {
                    GetObject.SetSwitchApp(PanicSwitchCommon.SwitchApp.HomeScreen.toString());
                    PanicSwitchCommon.SwitchingApp = PanicSwitchCommon.SwitchApp.HomeScreen.toString();
                }
            }
        });
        if (PanicSwitchCommon.SwitchingApp.equals(PanicSwitchCommon.SwitchApp.HomeScreen.toString())) {
            radioGroup.check(R.id.HomeScreen);
        } else {
            radioGroup.check(R.id.Browser);
        }
        if (PanicSwitchCommon.IsFlickOn) {
            this.btnFlick.setChecked(true);
        } else {
            this.btnFlick.setChecked(false);
        }
        if (PanicSwitchCommon.IsShakeOn) {
            this.btnShake.setChecked(true);
        } else {
            this.btnShake.setChecked(false);
        }
        if (PanicSwitchCommon.IsPalmOnFaceOn) {
            this.btnPalmOnScreen.setChecked(true);
        } else {
            this.btnPalmOnScreen.setChecked(false);
        }
        this.btnFlick.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchActivity.3
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    PanicSwitchActivity.this.btnFlick.setChecked(true);
                    GetObject.SetIsFlickOn(true);
                    Toast.makeText(PanicSwitchActivity.this, "Panic Switch Flick now activated", Toast.LENGTH_SHORT).show();
                    PanicSwitchCommon.IsFlickOn = true;
                    return;
                }
                PanicSwitchActivity.this.btnFlick.setChecked(false);
                GetObject.SetIsFlickOn(false);
                Toast.makeText(PanicSwitchActivity.this, "Panic Switch Flick now deactivated", Toast.LENGTH_SHORT).show();
                PanicSwitchCommon.IsFlickOn = false;
            }
        });
        this.btnShake.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchActivity.4
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    PanicSwitchActivity.this.btnShake.setChecked(true);
                    GetObject.SetIsShakeOn(true);
                    Toast.makeText(PanicSwitchActivity.this, "Panic Switch Flick now activated", Toast.LENGTH_SHORT).show();
                    PanicSwitchCommon.IsShakeOn = true;
                    return;
                }
                PanicSwitchActivity.this.btnShake.setChecked(false);
                GetObject.SetIsShakeOn(false);
                Toast.makeText(PanicSwitchActivity.this, "Panic Switch Flick now deactivated", Toast.LENGTH_SHORT).show();
                PanicSwitchCommon.IsShakeOn = false;
            }
        });
        this.btnPalmOnScreen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchActivity.5
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    PanicSwitchActivity.this.btnPalmOnScreen.setChecked(true);
                    GetObject.SetIsPalmOnScreenOn(true);
                    Toast.makeText(PanicSwitchActivity.this, "Panic Switch Flick now activated", Toast.LENGTH_SHORT).show();
                    PanicSwitchCommon.IsPalmOnFaceOn = true;
                    return;
                }
                PanicSwitchActivity.this.btnPalmOnScreen.setChecked(false);
                GetObject.SetIsPalmOnScreenOn(false);
                Toast.makeText(PanicSwitchActivity.this, "Panic Switch Flick now deactivated", Toast.LENGTH_SHORT).show();
                PanicSwitchCommon.IsPalmOnFaceOn = false;
            }
        });
    }

    public void btnBackonClick() {
        SecurityLocksCommon.IsAppDeactive = false;
        startActivity(new Intent(this, SettingActivity.class));
        finish();
    }

    @Override
    // calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Activity.BaseActivity, calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.AccelerometerListener
    public void onShake(float f) {
        if (PanicSwitchCommon.IsFlickOn || PanicSwitchCommon.IsShakeOn) {
            PanicSwitchActivityMethods.SwitchApp(this);
        }
    }

    @Override // calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Activity.BaseActivity, android.hardware.SensorEventListener
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == 8 && sensorEvent.values[0] == 0.0f && PanicSwitchCommon.IsPalmOnFaceOn) {
            PanicSwitchActivityMethods.SwitchApp(this);
        }
    }

    @Override
    // calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Activity.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        this.sensorManager.unregisterListener(this);
        if (AccelerometerManager.isListening()) {
            AccelerometerManager.stopListening();
        }
        if (SecurityLocksCommon.IsAppDeactive) {
            finish();
            System.exit(0);
        }
        super.onPause();
    }

    @Override
    // calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Activity.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        if (AccelerometerManager.isSupported(this)) {
            AccelerometerManager.startListening(this);
        }
        SensorManager sensorManager = this.sensorManager;
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(8), 3);
        super.onResume();
    }

    @Override
    // androidx.appcompat.app.AppCompatActivity, android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            SecurityLocksCommon.IsAppDeactive = false;
            startActivity(new Intent(getApplicationContext(), SettingActivity.class));
            finish();
        }
        return super.onKeyDown(i, keyEvent);
    }
}

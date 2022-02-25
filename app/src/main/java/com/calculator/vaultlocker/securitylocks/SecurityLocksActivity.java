package com.calculator.vaultlocker.securitylocks;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.calculator.vaultlocker.R;
import com.calculator.vaultlocker.Activity.BaseActivity;
import com.calculator.vaultlocker.Activity.CalculatorPinSetting;
import com.calculator.vaultlocker.panicswitch.AccelerometerManager;
import com.calculator.vaultlocker.panicswitch.PanicSwitchActivityMethods;
import com.calculator.vaultlocker.panicswitch.PanicSwitchCommon;
import com.calculator.vaultlocker.Activity.SettingActivity;
import com.calculator.vaultlocker.utilities.Utilities;

import java.util.ArrayList;

public class SecurityLocksActivity extends BaseActivity {
    public LinearLayout btnCancel;
    public LinearLayout btnOk;
    public CheckBox checkboxCal;
    public boolean ischeckbox = false;
    TextView SecurityCredentialsToBaar_Title;
    boolean isSettingDecoy = false;
    String isconfirmdialog = "";
    TextView lblloginoptionitem;
    LinearLayout ll_SecurityCredentials_TopBaar;
    LinearLayout ll_background;
    LinearLayout rootViewGroup;
    private SecurityLocksListAdapter adapter;
    private ArrayList<SecurityLocksEnt> securityLocksEntEntList;
    private ListView securityLocksListView;
    private SensorManager sensorManager;
    private Toolbar toolbar;

    @Override
    // com.calculator.vaultlocker.Activity.BaseActivity, com.calculator.vaultlocker.panicswitch.AccelerometerListener
    public void onAccelerationChanged(float f, float f2, float f3) {
    }

    @Override // com.calculator.vaultlocker.Activity.BaseActivity, android.hardware.SensorEventListener
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override
    // com.calculator.vaultlocker.Activity.BaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.security_lock_activity);
        SecurityLocksCommon.IsAppDeactive = true;
        getWindow().addFlags(128);
        this.sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (getIntent().getStringExtra("isconfirmdialog") != null) {
            this.isconfirmdialog = getIntent().getStringExtra("isconfirmdialog");
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.toolbar = toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("   Select Security Locks");
        this.ll_background = (LinearLayout) findViewById(R.id.ll_background);
        this.securityLocksListView = (ListView) findViewById(R.id.SecurityCredentialsListView);
        this.sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_SecurityCredentials_TopBaar);
        this.ll_SecurityCredentials_TopBaar = linearLayout;
        linearLayout.setBackgroundColor(getResources().getColor(R.color.ColorAppTheme));
        TextView textView = (TextView) findViewById(R.id.SecurityCredentialsToBaar_Title);
        this.SecurityCredentialsToBaar_Title = textView;
        textView.setTextColor(getResources().getColor(R.color.ColorWhite));
        this.rootViewGroup = (LinearLayout) findViewById(R.id.rootViewGroup);
        this.isSettingDecoy = SecurityLocksCommon.isSettingDecoy;
        if (Utilities.getScreenOrientation(this) == 1) {
            this.rootViewGroup.setVisibility(View.INVISIBLE);
        } else if (Utilities.getScreenOrientation(this) == 2) {
            this.rootViewGroup.setVisibility(View.GONE);
        }
        this.securityLocksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.calculator.vaultlocker.securitylocks.SecurityLocksActivity.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                if (i == 0) {
                    SecurityLocksCommon.IsAppDeactive = false;
                    SecurityLocksActivity.this.agreeDisguiseModeDialog();
                } else if (i == 1) {
                    SecurityLocksCommon.IsAppDeactive = false;
                    Intent intent = new Intent(SecurityLocksActivity.this, SetPasswordActivity.class);
                    intent.putExtra("LoginOption", "Pin");
                    intent.putExtra("isSettingDecoy", SecurityLocksActivity.this.isSettingDecoy);
                    SecurityLocksActivity.this.startActivity(intent);
                    SecurityLocksActivity.this.finish();
                } else if (i == 2) {
                    SecurityLocksCommon.IsAppDeactive = false;
                    Intent intent2 = new Intent(SecurityLocksActivity.this, SetPatternActivity.class);
                    intent2.putExtra("isSettingDecoy", SecurityLocksActivity.this.isSettingDecoy);
                    SecurityLocksActivity.this.startActivity(intent2);
                    SecurityLocksActivity.this.finish();
                } else if (i == 3) {
                    SecurityLocksCommon.IsAppDeactive = false;
                    Intent intent3 = new Intent(SecurityLocksActivity.this, SetPasswordActivity.class);
                    intent3.putExtra("LoginOption", "Password");
                    intent3.putExtra("isSettingDecoy", SecurityLocksActivity.this.isSettingDecoy);
                    SecurityLocksActivity.this.startActivity(intent3);
                    SecurityLocksActivity.this.finish();
                }
            }
        });
        BindSecurityLocks();
    }

    private void BindSecurityLocks() {
        this.securityLocksEntEntList = new SecurityLocksActivityMethods().GetSecurityCredentialsDetail(this);
        SecurityLocksListAdapter securityLocksListAdapter = new SecurityLocksListAdapter(this, 17367043, this.securityLocksEntEntList);
        this.adapter = securityLocksListAdapter;
        this.securityLocksListView.setAdapter((ListAdapter) securityLocksListAdapter);
    }

    @Override
    // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (configuration.orientation == 2) {
            this.rootViewGroup.setVisibility(View.GONE);
        } else if (configuration.orientation == 1) {
            this.rootViewGroup.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    // com.calculator.vaultlocker.Activity.BaseActivity, com.calculator.vaultlocker.panicswitch.AccelerometerListener
    public void onShake(float f) {
        if (PanicSwitchCommon.IsFlickOn || PanicSwitchCommon.IsShakeOn) {
            PanicSwitchActivityMethods.SwitchApp(this);
        }
    }

    @Override // com.calculator.vaultlocker.Activity.BaseActivity, android.hardware.SensorEventListener
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == 8 && sensorEvent.values[0] == 0.0f && PanicSwitchCommon.IsPalmOnFaceOn) {
            PanicSwitchActivityMethods.SwitchApp(this);
        }
    }

    @Override
    // com.calculator.vaultlocker.Activity.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
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
    // com.calculator.vaultlocker.Activity.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
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
        if (i == 4 && !SecurityLocksCommon.IsFirstLogin) {
            SecurityLocksCommon.IsAppDeactive = false;
            startActivity(new Intent(this, SettingActivity.class));
            finish();
        }
        return super.onKeyDown(i, keyEvent);
    }

    public void agreeDisguiseModeDialog() {
        SecurityLocksCommon.IsAppDeactive = false;
        Intent intent = new Intent(this, CalculatorPinSetting.class);
        intent.putExtra("from", "Cal");
        intent.putExtra("isconfirmdialog", this.isconfirmdialog);
        intent.putExtra("from", "isSettingDecoy");
        startActivity(intent);
        finish();
    }
}

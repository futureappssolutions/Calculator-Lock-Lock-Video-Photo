package com.calculator.vaultlocker.securitylocks;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.calculator.vaultlocker.R;
import com.calculator.vaultlocker.Activity.BaseActivity;
import com.calculator.vaultlocker.panicswitch.AccelerometerManager;
import com.calculator.vaultlocker.panicswitch.PanicSwitchActivityMethods;
import com.calculator.vaultlocker.panicswitch.PanicSwitchCommon;
import com.calculator.vaultlocker.Activity.RecoveryOfCredentialsActivity;
import com.calculator.vaultlocker.Activity.SettingActivity;


public class ConfirmPasswordPinActivity extends BaseActivity {
    public String PasswordOrPin = "";
    String LoginOption = "";
    CheckBox cb_show_password_pin;
    TextView lblCancel;
    TextView lblConfirmPinOrPassword;
    TextView lblOk;
    TextView lblconfirmPasswordPintop;
    LinearLayout ll_Cancel;
    LinearLayout ll_ConfirmPasswordPinTopBaar;
    LinearLayout ll_Ok;
    LinearLayout ll_background;
    EditText txtConfirmPinOrPassword;
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
        setContentView(R.layout.confirm_password_pin_activity);
        this.ll_background = (LinearLayout) findViewById(R.id.ll_background);
        SecurityLocksCommon.IsAppDeactive = true;
        getWindow().addFlags(128);
        Typeface createFromAsset = Typeface.createFromAsset(getAssets(), "ebrima.ttf");
        this.ll_ConfirmPasswordPinTopBaar = (LinearLayout) findViewById(R.id.ll_ConfirmPasswordPinTopBaar);
        this.sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.toolbar = toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.lbl_Enter_password);
        this.toolbar.setNavigationIcon(R.drawable.ic_top_back_icon);
        this.toolbar.setNavigationOnClickListener(new View.OnClickListener() { // from class: com.calculator.vaultlocker.securitylocks.ConfirmPasswordPinActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ConfirmPasswordPinActivity.this.btnBackonClick();
            }
        });
        EditText editText = (EditText) findViewById(R.id.txtconfirm_password_pin);
        this.txtConfirmPinOrPassword = editText;
        editText.setTypeface(createFromAsset);
        TextView textView = (TextView) findViewById(R.id.lblconfirm_password_pin);
        this.lblConfirmPinOrPassword = textView;
        textView.setTypeface(createFromAsset);
        TextView textView2 = (TextView) findViewById(R.id.lblconfirmPasswordPintop);
        this.lblconfirmPasswordPintop = textView2;
        textView2.setTypeface(createFromAsset);
        this.ll_Cancel = (LinearLayout) findViewById(R.id.ll_Cancel);
        this.ll_Ok = (LinearLayout) findViewById(R.id.ll_Ok);
        TextView textView3 = (TextView) findViewById(R.id.lblCancel);
        this.lblCancel = textView3;
        textView3.setTypeface(createFromAsset);
        TextView textView4 = (TextView) findViewById(R.id.lblOk);
        this.lblOk = textView4;
        textView4.setTypeface(createFromAsset);
        CheckBox checkBox = (CheckBox) findViewById(R.id.cb_show_password_pin);
        this.cb_show_password_pin = checkBox;
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.calculator.vaultlocker.securitylocks.ConfirmPasswordPinActivity.2
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    if (SecurityLocksCommon.LoginOptions.Pin.toString().equals(ConfirmPasswordPinActivity.this.LoginOption)) {
                        if (ConfirmPasswordPinActivity.this.txtConfirmPinOrPassword.getText().toString().length() > 0) {
                            int selectionStart = ConfirmPasswordPinActivity.this.txtConfirmPinOrPassword.getSelectionStart();
                            int selectionEnd = ConfirmPasswordPinActivity.this.txtConfirmPinOrPassword.getSelectionEnd();
                            ConfirmPasswordPinActivity.this.txtConfirmPinOrPassword.setInputType(2);
                            ConfirmPasswordPinActivity.this.txtConfirmPinOrPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            ConfirmPasswordPinActivity.this.txtConfirmPinOrPassword.setSelection(selectionStart, selectionEnd);
                        }
                    } else if (ConfirmPasswordPinActivity.this.txtConfirmPinOrPassword.getText().toString().length() > 0) {
                        int selectionStart2 = ConfirmPasswordPinActivity.this.txtConfirmPinOrPassword.getSelectionStart();
                        int selectionEnd2 = ConfirmPasswordPinActivity.this.txtConfirmPinOrPassword.getSelectionEnd();
                        ConfirmPasswordPinActivity.this.txtConfirmPinOrPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        ConfirmPasswordPinActivity.this.txtConfirmPinOrPassword.setSelection(selectionStart2, selectionEnd2);
                    }
                } else if (SecurityLocksCommon.LoginOptions.Pin.toString().equals(ConfirmPasswordPinActivity.this.LoginOption)) {
                    if (ConfirmPasswordPinActivity.this.txtConfirmPinOrPassword.getText().toString().length() > 0) {
                        int selectionStart3 = ConfirmPasswordPinActivity.this.txtConfirmPinOrPassword.getSelectionStart();
                        int selectionEnd3 = ConfirmPasswordPinActivity.this.txtConfirmPinOrPassword.getSelectionEnd();
                        ConfirmPasswordPinActivity.this.txtConfirmPinOrPassword.setInputType(2);
                        ConfirmPasswordPinActivity.this.txtConfirmPinOrPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        ConfirmPasswordPinActivity.this.txtConfirmPinOrPassword.setSelection(selectionStart3, selectionEnd3);
                    }
                } else if (ConfirmPasswordPinActivity.this.txtConfirmPinOrPassword.getText().toString().length() > 0) {
                    int selectionStart4 = ConfirmPasswordPinActivity.this.txtConfirmPinOrPassword.getSelectionStart();
                    int selectionEnd4 = ConfirmPasswordPinActivity.this.txtConfirmPinOrPassword.getSelectionEnd();
                    ConfirmPasswordPinActivity.this.txtConfirmPinOrPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ConfirmPasswordPinActivity.this.txtConfirmPinOrPassword.setSelection(selectionStart4, selectionEnd4);
                }
            }
        });
        this.ll_Cancel.setOnClickListener(new View.OnClickListener() { // from class: com.calculator.vaultlocker.securitylocks.ConfirmPasswordPinActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SecurityLocksCommon.IsAppDeactive = false;
                ConfirmPasswordPinActivity.this.startActivity(new Intent(ConfirmPasswordPinActivity.this, SettingActivity.class));
                ConfirmPasswordPinActivity.this.finish();
            }
        });
        this.ll_Ok.setOnClickListener(new View.OnClickListener() { // from class: com.calculator.vaultlocker.securitylocks.ConfirmPasswordPinActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ConfirmPasswordPinActivity.this.Ok();
            }
        });
        SecurityLocksSharedPreferences GetObject = SecurityLocksSharedPreferences.GetObject(this);
        this.LoginOption = GetObject.GetLoginType();
        this.PasswordOrPin = GetObject.GetSecurityCredential();
        if (SecurityLocksCommon.LoginOptions.Pin.toString().equals(this.LoginOption)) {
            this.cb_show_password_pin.setText(R.string.lbl_show_pin);
            this.txtConfirmPinOrPassword.setInputType(2);
            this.txtConfirmPinOrPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            getSupportActionBar().setTitle(R.string.lbl_Enter_pin);
            this.lblconfirmPasswordPintop.setText(R.string.lbl_Enter_pin);
            this.lblConfirmPinOrPassword.setText(R.string.lblsetting_SecurityCredentials_ConfirmYourPin);
            return;
        }
        this.cb_show_password_pin.setText(R.string.lbl_show_password);
    }

    public void btnBackonClick() {
        SecurityLocksCommon.IsAppDeactive = false;
        SecurityLocksCommon.isBackupPasswordPin = false;
        startActivity(new Intent(this, SettingActivity.class));
        finish();
    }

    public void Ok() {
        Intent intent;
        Intent intent2;
        if (SecurityLocksCommon.LoginOptions.Password.toString().equals(this.LoginOption)) {
            if (this.txtConfirmPinOrPassword.getText().toString().contentEquals(this.PasswordOrPin)) {
                SecurityLocksCommon.IsAppDeactive = false;
                if (SecurityLocksCommon.isBackupPasswordPin) {
                    SecurityLocksCommon.isBackupPasswordPin = false;
                    intent2 = new Intent(this, RecoveryOfCredentialsActivity.class);
                } else if (SecurityLocksCommon.isSettingDecoy) {
                    SecurityLocksCommon.isSettingDecoy = false;
                    intent2 = new Intent(this, SetPasswordActivity.class);
                    intent2.putExtra("LoginOption", "Password");
                    intent2.putExtra("isSettingDecoy", true);
                } else {
                    intent2 = new Intent(this, SecurityLocksActivity.class);
                }
                startActivity(intent2);
                finish();
                return;
            }
            this.lblConfirmPinOrPassword.setText(R.string.lblsetting_SecurityCredentials_Setpasword_Tryagain);
            this.txtConfirmPinOrPassword.setText("");
        } else if (SecurityLocksCommon.LoginOptions.Pin.toString().equals(this.LoginOption)) {
            if (this.txtConfirmPinOrPassword.getText().toString().contentEquals(this.PasswordOrPin)) {
                SecurityLocksCommon.IsAppDeactive = false;
                if (SecurityLocksCommon.isBackupPasswordPin) {
                    SecurityLocksCommon.isBackupPasswordPin = false;
                    intent = new Intent(this, RecoveryOfCredentialsActivity.class);
                } else if (SecurityLocksCommon.isSettingDecoy) {
                    SecurityLocksCommon.isSettingDecoy = false;
                    intent = new Intent(this, SetPasswordActivity.class);
                    intent.putExtra("LoginOption", "Pin");
                    intent.putExtra("isSettingDecoy", true);
                } else {
                    intent = new Intent(this, SecurityLocksActivity.class);
                }
                startActivity(intent);
                finish();
                return;
            }
            this.lblConfirmPinOrPassword.setText(R.string.lblsetting_SecurityCredentials_Setpin_Tryagain);
            this.txtConfirmPinOrPassword.setText("");
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
        super.onPause();
        this.sensorManager.unregisterListener(this);
        if (AccelerometerManager.isListening()) {
            AccelerometerManager.stopListening();
        }
        if (SecurityLocksCommon.IsAppDeactive) {
            finish();
            System.exit(0);
        }
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
        if (i == 4) {
            SecurityLocksCommon.IsAppDeactive = false;
            SecurityLocksCommon.isBackupPasswordPin = false;
            startActivity(new Intent(this, SettingActivity.class));
            finish();
        }
        return super.onKeyDown(i, keyEvent);
    }
}

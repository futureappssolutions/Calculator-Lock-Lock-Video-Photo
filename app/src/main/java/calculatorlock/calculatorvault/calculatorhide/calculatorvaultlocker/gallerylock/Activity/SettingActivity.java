package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Activity;

import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.R;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.AccelerometerManager;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchActivity;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchActivityMethods;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.securitylocks.ConfirmPasswordPinActivity;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.securitylocks.ConfirmPatternActivity;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.securitylocks.SecurityLocksActivity;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.securitylocks.SecurityLocksCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.securitylocks.SecurityLocksSharedPreferences;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.storageoption.StorageOptionsCommon;

import java.util.List;
import java.util.Objects;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;


public class SettingActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    public static ProgressDialog myProgressDialog;
    private final String[] PERMISSIONS_ = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
    public SecurityLocksSharedPreferences securityLocksSharedPreferences;
    private SwitchCompat fingerSwitch;
    private String LoginOption = "";
    private int number = 0;
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;
    private SensorManager sensorManager;

    public static boolean hasPermissions(Context context, String... strArr) {
        if (context == null || strArr == null) {
            return true;
        }
        for (String str : strArr) {
            if (ActivityCompat.checkSelfPermission(context, str) != 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onAccelerationChanged(float f, float f2, float f3) {
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override
    public void onPermissionsGranted(int i, @NonNull List<String> list) {
    }

    public void hideProgress() {
        ProgressDialog progressDialog = myProgressDialog;
        if (progressDialog != null && progressDialog.isShowing()) {
            myProgressDialog.dismiss();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_settings);

        SecurityLocksCommon.IsAppDeactive = true;

        securityLocksSharedPreferences = SecurityLocksSharedPreferences.GetObject(this);
        LinearLayout fingerprint = findViewById(R.id.fingerprint);
        fingerSwitch = findViewById(R.id.fingerSwitch);

        keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        fingerprintManager = (FingerprintManager) getSystemService(Context.FINGERPRINT_SERVICE);
        if (!fingerprintManager.isHardwareDetected()) {
            fingerprint.setVisibility(View.GONE);
        }

        fingerSwitch.setChecked(securityLocksSharedPreferences.GetFingerprint());

        fingerSwitch.setOnClickListener(view -> {
            if (ActivityCompat.checkSelfPermission(SettingActivity.this, "android.permission.USE_FINGERPRINT") != 0) {
                printtoast("Please enable the fingerprint permission");
                fingerSwitch.setChecked(false);
                securityLocksSharedPreferences.SetFingerPrint(true);
            }
            if (!fingerprintManager.hasEnrolledFingerprints()) {
                fingerSwitch.setChecked(false);
                securityLocksSharedPreferences.SetFingerPrint(true);
                printtoast("No fingerprint configured. Please register at least one fingerprint in your device's Settings");
            }
            if (!keyguardManager.isKeyguardSecure()) {
                fingerSwitch.setChecked(false);
                securityLocksSharedPreferences.SetFingerPrint(true);
                printtoast("Please enable lockscreen security in your device's Settings");
            }
        });

        fingerSwitch.setOnCheckedChangeListener((compoundButton, z) -> {
            if (z) {
                fingerSwitch.setChecked(true);
                securityLocksSharedPreferences.SetFingerPrint(true);
                return;
            }
            fingerSwitch.setChecked(false);
            securityLocksSharedPreferences.SetFingerPrint(false);
        });
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Settings");
        toolbar.setNavigationIcon(R.drawable.ic_top_back_icon);

        toolbar.setNavigationOnClickListener(view -> btnBackonClick());
        SecurityLocksSharedPreferences securityCredentialsSharedPreferences = SecurityLocksSharedPreferences.GetObject(this);
        LoginOption = securityCredentialsSharedPreferences.GetLoginType();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        LinearLayout ll_security_credentials = findViewById(R.id.ll_security_credentials);
        LinearLayout ll_data_recovery = findViewById(R.id.ll_data_recovery);
        LinearLayout ll_recovery_credentials = findViewById(R.id.ll_recovery_credentials);
        LinearLayout ll_decoy_security_lock = findViewById(R.id.ll_decoy_security_lock);
        LinearLayout ll_storage_options = findViewById(R.id.ll_storage_options);
        LinearLayout ll_panic_switch = findViewById(R.id.ll_panic_switch);

        if (SecurityLocksCommon.IsFakeAccount == 1) {
            ll_data_recovery.setVisibility(View.GONE);
            ll_recovery_credentials.setVisibility(View.GONE);
            ll_decoy_security_lock.setVisibility(View.GONE);
            ll_storage_options.setVisibility(View.GONE);
            ll_panic_switch.setVisibility(View.GONE);
        }

        ll_security_credentials.setOnClickListener(view -> {
            SecurityLocksCommon.IsAppDeactive = false;
            if (SecurityLocksCommon.LoginOptions.Pattern.toString().equals(LoginOption)) {
                SecurityLocksCommon.IsConfirmPatternActivity = true;
                startActivity(new Intent(SettingActivity.this, ConfirmPatternActivity.class));
                finish();
            } else if (SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption) || SecurityLocksCommon.LoginOptions.Password.toString().equals(LoginOption)) {
                startActivity(new Intent(SettingActivity.this, ConfirmPasswordPinActivity.class));
                finish();
            } else if (SecurityLocksCommon.LoginOptions.Calculator.toString().equals(LoginOption)) {
                Intent intent = new Intent(SettingActivity.this, CalculatorPinSetting.class);
                intent.putExtra("from", "SettingFragment");
                startActivity(intent);
                finish();
            } else {
                startActivity(new Intent(SettingActivity.this, SecurityLocksActivity.class));
                finish();
            }
        });

        ll_data_recovery.setOnClickListener(view -> {
            SettingActivity settingActivity = SettingActivity.this;
            settingActivity.number = 1;
            SecurityLocksCommon.IsAppDeactive = false;
            settingActivity.requestPermission(settingActivity.PERMISSIONS_);
        });

        ll_recovery_credentials.setOnClickListener(view -> {
            SecurityLocksCommon.IsAppDeactive = false;
            if (SecurityLocksCommon.LoginOptions.Pattern.toString().equals(LoginOption)) {
                SecurityLocksCommon.isBackupPattern = true;
                startActivity(new Intent(SettingActivity.this, ConfirmPatternActivity.class));
                finish();
            } else if (SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption) || SecurityLocksCommon.LoginOptions.Password.toString().equals(LoginOption)) {
                SecurityLocksCommon.isBackupPasswordPin = true;
                startActivity(new Intent(SettingActivity.this, ConfirmPasswordPinActivity.class));
                finish();
            } else if (SecurityLocksCommon.LoginOptions.Calculator.toString().equals(LoginOption)) {
                SecurityLocksCommon.isBackupPasswordPin = true;
                Intent intent = new Intent(SettingActivity.this, CalculatorPinSetting.class);
                intent.putExtra("from", "SettingFragment");
                startActivity(intent);
                finish();
            }
        });

        ll_decoy_security_lock.setOnClickListener(view -> {
            if (SecurityLocksCommon.LoginOptions.Pattern.toString().equals(LoginOption)) {
                SecurityLocksCommon.IsAppDeactive = false;
                SecurityLocksCommon.isSettingDecoy = true;
                startActivity(new Intent(SettingActivity.this, ConfirmPatternActivity.class));
                finish();
            } else if (SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption) || SecurityLocksCommon.LoginOptions.Password.toString().equals(LoginOption)) {
                SecurityLocksCommon.IsAppDeactive = false;
                SecurityLocksCommon.isSettingDecoy = true;
                startActivity(new Intent(SettingActivity.this, ConfirmPasswordPinActivity.class));
                finish();
            } else {
                Toast.makeText(SettingActivity.this, R.string.decoy_mode_toast_disguise, Toast.LENGTH_LONG).show();
            }
        });

        ll_storage_options.setOnClickListener(view -> SetStorageOption());

        ll_panic_switch.setOnClickListener(view -> {
            SecurityLocksCommon.IsAppDeactive = false;
            startActivity(new Intent(SettingActivity.this, PanicSwitchActivity.class));
            finish();
        });
    }

    public void printtoast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    public void btnBackonClick() {
        SecurityLocksCommon.IsAppDeactive = false;

        startActivity(new Intent(this, ActivityMain.class));
        finish();
    }

    public void SetStorageOption() {
        Toast.makeText(this, R.string.lbl_Lollipop_StorageOption_Alert, Toast.LENGTH_LONG).show();
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
        if (SecurityLocksCommon.IsAppDeactive && !StorageOptionsCommon.IsAllDataMoveingInProg) {
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
            startActivity(new Intent(this, ActivityMain.class));
            finish();
        }
        return super.onKeyDown(i, keyEvent);
    }

    @AfterPermissionGranted(123)
    public void requestPermission(String[] strArr) {
        SecurityLocksCommon.IsAppDeactive = false;
        if (!EasyPermissions.hasPermissions(this, strArr)) {
            EasyPermissions.requestPermissions(new PermissionRequest.Builder(this, 123, strArr).setRationale("For the best Calculator Vault experience, please Allow Permission").setPositiveButtonText("ok").setNegativeButtonText("").build());
        } else if (number == 1) {
            startActivity(new Intent(this, DataRecoveryActivity.class));
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (iArr.length <= 0 || iArr[0] != 0) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.WRITE_EXTERNAL_STORAGE") || ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.READ_EXTERNAL_STORAGE") || ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.PROCESS_OUTGOING_CALLS")) {
                EasyPermissions.hasPermissions(this, number == 11 ? new String[]{"android.permission.PROCESS_OUTGOING_CALLS"} : new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"});
                Toast.makeText(this, "Permission is denied", Toast.LENGTH_SHORT).show();
                return;
            }
            EasyPermissions.onRequestPermissionsResult(i, strArr, iArr, this);
        } else if (hasPermissions(this, strArr)) {
            if (number == 1) {
                startActivity(new Intent(this, DataRecoveryActivity.class));
                finish();
            }
            Toast.makeText(this, "Permission is granted ", Toast.LENGTH_SHORT).show();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.WRITE_EXTERNAL_STORAGE") || ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.READ_EXTERNAL_STORAGE")) {
            String[] strArr2 = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
            if (EasyPermissions.hasPermissions(this, strArr2)) {
                Toast.makeText(this, "Permission  again...", Toast.LENGTH_SHORT).show();
            } else {
                EasyPermissions.requestPermissions(new PermissionRequest.Builder(this, 123, strArr2).setRationale("For the best Calculator Vault experience, please Allow Permission").setPositiveButtonText("ok").setNegativeButtonText("").build());
            }
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        } else {
            EasyPermissions.onRequestPermissionsResult(i, strArr, iArr, this);
        }
    }

    @Override
    public void onPermissionsDenied(int i, @NonNull List<String> list) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, list)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

}

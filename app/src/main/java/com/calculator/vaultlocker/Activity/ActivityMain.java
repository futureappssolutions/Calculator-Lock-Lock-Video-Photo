package com.calculator.vaultlocker.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.calculator.vaultlocker.Adapter.MainAdapter;
import com.calculator.vaultlocker.Ads.GoogleAds;
import com.calculator.vaultlocker.Model.FeatureActivityEnt;
import com.calculator.vaultlocker.R;
import com.calculator.vaultlocker.common.FeatureActivityMethods;
import com.calculator.vaultlocker.panicswitch.AccelerometerListener;
import com.calculator.vaultlocker.panicswitch.AccelerometerManager;
import com.calculator.vaultlocker.panicswitch.PanicSwitchActivityMethods;
import com.calculator.vaultlocker.panicswitch.PanicSwitchCommon;
import com.calculator.vaultlocker.securitylocks.SecurityLocksCommon;
import com.calculator.vaultlocker.securitylocks.SecurityLocksSharedPreferences;
import com.calculator.vaultlocker.storageoption.StorageOptionSharedPreferences;
import com.calculator.vaultlocker.storageoption.StorageOptionsCommon;
import com.calculator.vaultlocker.utilities.Common;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class ActivityMain extends AppCompatActivity implements AccelerometerListener, SensorEventListener, EasyPermissions.PermissionCallbacks, NavigationView.OnNavigationItemSelectedListener {
    private final String[] PERMISSIONS = {"android.permission.CAMERA", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private final String[] perms = {"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
    public int pos;
    public boolean isSelectedStoraged = false;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private GridView gv_featureGrid;
    private SensorManager sensorManager;
    private SecurityLocksSharedPreferences securityLocksSharedPreferences;

    @Override
    public void onAccelerationChanged(float f, float f2, float f3) {
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override
    public void onPermissionsGranted(int i, @NonNull List<String> list) {
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);

        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        GoogleAds.showBannerAds(ActivityMain.this, ll_banner);

        invalidateOptionsMenu();

        gv_featureGrid = findViewById(R.id.gridview);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        AppCompatTextView toolbar_title = toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText(getString(R.string.tool_name));
        final LinearLayout linearLayout = findViewById(R.id.holder);
        ActionBarDrawerToggle r0 = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) { // from class: com.calculator.vaultlocker.features.FeaturesActivity.1
            @Override
            public void onDrawerSlide(View view, float f) {
                linearLayout.setTranslationX(((float) view.getWidth()) * f);
                float f2 = 1.0f - (f / 7.0f);
                linearLayout.setScaleX(f2);
                linearLayout.setScaleY(f2);
                super.onDrawerSlide(view, f);
            }
        };
        drawerLayout.addDrawerListener(r0);
        drawerLayout.setScrimColor(0);
        r0.syncState();

        ((NavigationView) findViewById(R.id.nav_view)).setNavigationItemSelectedListener(this);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        securityLocksSharedPreferences = SecurityLocksSharedPreferences.GetObject(this);
        Common.initImageLoader(this);

        gv_featureGrid.setOnItemClickListener((adapterView, view, i, j) -> {
            switch (i) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 9:
                    pos = i;
                    SecurityLocksCommon.IsAppDeactive = false;
                    requestPermission(PERMISSIONS);
                    return;
                case 8:
                default:
                    return;
                case 10:
                    securityLocksSharedPreferences.isGetCalModeEnable();
                    return;
                case 11:
                    SecurityLocksCommon.IsAppDeactive = false;
            }
        });
        SetFeatureinGridView();
    }

    public void SetFeatureinGridView() {
        ArrayList<FeatureActivityEnt> featureEntList = new FeatureActivityMethods().GetFeatures(this);
        MainAdapter mainAdapter = new MainAdapter(this, 1, featureEntList);
        gv_featureGrid.setAdapter(mainAdapter);
        mainAdapter.notifyDataSetChanged();
    }

    @AfterPermissionGranted(123)
    public void requestPermission(String[] strArr) {
        if (EasyPermissions.hasPermissions(this, strArr)) {
            IntergerActivty(pos);
        } else {
            EasyPermissions.requestPermissions(this, "For the best Calc Vault experience, please Allow Permission", 123, strArr);
        }
    }

    private void IntergerActivty(int i) {
        if (i == 0) {
            startActivity(new Intent(this, PhotosAlbumActivty.class));
            finish();
        } else if (i == 1) {
            startActivity(new Intent(this, VideosAlbumActivty.class));
            finish();
        } else if (i == 2) {
            startActivity(new Intent(this, GalleryActivity.class));
            finish();
        } else if (i == 3) {
            startActivity(new Intent(this, AudioPlayListActivity.class));
            finish();
        } else if (i == 4) {
            startActivity(new Intent(this, DocumentsFolderActivity.class));
            finish();
        } else if (i == 5) {
            startActivity(new Intent(this, WalletCategoriesActivity.class));
            finish();
        } else if (i == 6) {
            startActivity(new Intent(this, NotesFoldersActivity.class));
            finish();
        } else if (i == 7) {
            startActivity(new Intent(this, ToDoActivity.class));
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (iArr.length > 0 && iArr[0] == 0) {
            IntergerActivty(pos);
            Toast.makeText(getApplicationContext(), "Permission is granted ", Toast.LENGTH_SHORT).show();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.CAMERA") || ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.WRITE_EXTERNAL_STORAGE") || ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.READ_EXTERNAL_STORAGE") || ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.PROCESS_OUTGOING_CALLS")) {
            if (!EasyPermissions.hasPermissions(this, perms)) {
                EasyPermissions.requestPermissions(this, "For the best Calc Vault experience, please Allow Permission", 123, perms);
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
            if (!isSelectedStoraged) {
                SecurityLocksCommon.IsAppDeactive = false;
                StorageOptionSharedPreferences storageOptionSharedPreferences = StorageOptionSharedPreferences.GetObject(this);
                StorageOptionsCommon.STORAGEPATH = storageOptionSharedPreferences.GetStoragePath();
                if (StorageOptionsCommon.STORAGEPATH.length() <= 0) {
                    StorageOptionsCommon.STORAGEPATH = StorageOptionsCommon.STORAGEPATH_1;
                    storageOptionSharedPreferences.SetStoragePath(StorageOptionsCommon.STORAGEPATH);
                }
                isSelectedStoraged = true;
                return false;
            } else {
                finishAffinity();
            }
        }
        return super.onKeyDown(i, keyEvent);
    }

    @Override
    public void onPause() {
        sensorManager.unregisterListener(this);
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

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_Hack:
                SecurityLocksCommon.IsAppDeactive = false;
                startActivity(new Intent(this, HackAttemptActivity.class));
                finish();
                break;
            case R.id.nav_Setting:
                SecurityLocksCommon.IsAppDeactive = false;
                startActivity(new Intent(this, SettingActivity.class));
                finish();
                break;
            default:
                break;
        }
        drawerLayout.closeDrawers();
        return false;
    }
}

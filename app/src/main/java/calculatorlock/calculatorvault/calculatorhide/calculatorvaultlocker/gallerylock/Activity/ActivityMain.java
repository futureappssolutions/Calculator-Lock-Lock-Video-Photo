package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
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

import com.facebook.ads.NativeAdLayout;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Adapter.MainAdapter;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Ads.Advertisement;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.BuildConfig;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.FeatureActivityEnt;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.R;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.common.FeatureActivityMethods;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.AccelerometerListener;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.AccelerometerManager;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchActivityMethods;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.securitylocks.SecurityLocksCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.securitylocks.SecurityLocksSharedPreferences;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.storageoption.StorageOptionSharedPreferences;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.storageoption.StorageOptionsCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.Common;
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


        NativeAdLayout  fl_native = findViewById(R.id.fl_native);
        Advertisement.shoNativeAds(ActivityMain.this,fl_native);



        invalidateOptionsMenu();

        gv_featureGrid = findViewById(R.id.gridview);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        AppCompatTextView toolbar_title = toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText(getString(R.string.tool_name));
        final LinearLayout linearLayout = findViewById(R.id.holder);
        ActionBarDrawerToggle r0 = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) { // from class: calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.features.FeaturesActivity.1
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

    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                Log.i("Permission", "Granted");
            } else {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", getPackageName())));
                startActivity(intent);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
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
            Advertisement.getInstance((ActivityMain.this)).showFull(new Advertisement.MyCallback() {
                @Override
                public void callbackCall() {
                    startActivity(new Intent(ActivityMain.this, PhotosAlbumActivty.class));
                    finish();
                }
            });
        } else if (i == 1) {
            Advertisement.getInstance((ActivityMain.this)).showFull(new Advertisement.MyCallback() {
                @Override
                public void callbackCall() {
                    startActivity(new Intent(ActivityMain.this, VideosAlbumActivty.class));
                    finish();
                }
            });

        } else if (i == 2) {
            Advertisement.getInstance((ActivityMain.this)).showFull(new Advertisement.MyCallback() {
                @Override
                public void callbackCall() {
                    startActivity(new Intent(ActivityMain.this, GalleryActivity.class));
                    finish();
                }
            });

        } else if (i == 3) {
            Advertisement.getInstance((ActivityMain.this)).showFull(new Advertisement.MyCallback() {
                @Override
                public void callbackCall() {
                    startActivity(new Intent(ActivityMain.this, AudioPlayListActivity.class));
                    finish();
                }
            });


        } else if (i == 4) {
            Advertisement.getInstance((ActivityMain.this)).showFull(new Advertisement.MyCallback() {
                @Override
                public void callbackCall() {
                    startActivity(new Intent(ActivityMain.this, DocumentsFolderActivity.class));
                    finish();
                }
            });

        } else if (i == 5) {
            Advertisement.getInstance((ActivityMain.this)).showFull(new Advertisement.MyCallback() {
                @Override
                public void callbackCall() {
                    startActivity(new Intent(ActivityMain.this, WalletCategoriesActivity.class));
                    finish();
                }
            });

        } else if (i == 6) {
            Advertisement.getInstance((ActivityMain.this)).showFull(new Advertisement.MyCallback() {
                @Override
                public void callbackCall() {
                    startActivity(new Intent(ActivityMain.this, NotesFoldersActivity.class));
                    finish();
                }
            });

        } else if (i == 7) {
            Advertisement.getInstance((ActivityMain.this)).showFull(new Advertisement.MyCallback() {
                @Override
                public void callbackCall() {
                    startActivity(new Intent(ActivityMain.this, ToDoActivity.class));
                    finish();
                }
            });
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
            case R.id.nav_Tell:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            case R.id.nav_Rate:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                }
                break;
            default:
                break;
        }
        drawerLayout.closeDrawers();
        return false;
    }
}

package com.calculator.vaultlocker.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import com.calculator.vaultlocker.R;
import com.calculator.vaultlocker.common.Constants;
import com.calculator.vaultlocker.panicswitch.AccelerometerListener;
import com.calculator.vaultlocker.panicswitch.AccelerometerManager;
import com.calculator.vaultlocker.panicswitch.PanicSwitchActivityMethods;
import com.calculator.vaultlocker.panicswitch.PanicSwitchCommon;
import com.calculator.vaultlocker.securitylocks.SecurityLocksCommon;
import com.calculator.vaultlocker.wallet.CommonSharedPreferences;
import com.calculator.vaultlocker.Adapter.WalletCategoriesAdapter;
import com.calculator.vaultlocker.DB.WalletCategoriesDAL;
import com.calculator.vaultlocker.Model.WalletCategoriesFileDB_Pojo;
import com.calculator.vaultlocker.wallet.WalletCommon;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WalletCategoriesActivity extends AppCompatActivity implements AccelerometerListener, SensorEventListener {
    private List<WalletCategoriesFileDB_Pojo> categoryFileDB_List;
    private Constants constants;
    private GridView gv_wallet;
    private SensorManager sensorManager;
    private WalletCategoriesAdapter gvAdapter;
    private WalletCategoriesDAL walletCategoriesDAL;
    private boolean isGridview = true;

    @Override
    public void onAccelerationChanged(float f, float f2, float f3) {
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_wallet);

        Toolbar toolbar = findViewById(R.id.toolbar);
        gv_wallet = findViewById(R.id.gv_wallet);

        CommonSharedPreferences walletSharedPreferences = CommonSharedPreferences.GetObject(this);
        categoryFileDB_List = new ArrayList<>();
        walletCategoriesDAL = new WalletCategoriesDAL(this);

        WalletCommon walletCommon = new WalletCommon();
        constants = new Constants();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        boolean z = true;
        SecurityLocksCommon.IsAppDeactive = true;

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.wallet));
        toolbar.setNavigationIcon(R.drawable.back_top_bar_icon);

        if (walletSharedPreferences.get_ViewByWalletCategory() == 0) {
            z = false;
        }

        isGridview = z;
        walletCommon.createDefaultCategories(this);

        setGridview();

        gv_wallet.setSelection(WalletCommon.walletCategoryScrollIndex);

        gv_wallet.setOnItemClickListener((adapterView, view, i, j) -> {
            WalletCommon.WalletCurrentCategoryId = categoryFileDB_List.get(i).getCategoryFileId();
            WalletCommon.walletCurrentCategoryName = categoryFileDB_List.get(i).getCategoryFileName();
            WalletCommon.walletCategoryScrollIndex = i;
            SecurityLocksCommon.IsAppDeactive = false;
            startActivity(new Intent(WalletCategoriesActivity.this, WalletEntriesActivity.class));
            finish();
        });
    }

    public void setGridview() {
        StringBuilder sb2 = new StringBuilder();
        constants.getClass();
        sb2.append("SELECT \t     * \t\t\t\t\t\t   FROM  TableWalletCategories WHERE ");
        constants.getClass();
        sb2.append("WalletCategoriesFileIsDecoy");
        sb2.append(" = ");
        sb2.append(SecurityLocksCommon.IsFakeAccount);
        sb2.append(" ORDER BY ");
        constants.getClass();
        sb2.append("WalletCategoriesFileModifiedDate");
        sb2.append(" DESC");
        categoryFileDB_List = walletCategoriesDAL.getAllCategoriesInfoFromDatabase(sb2.toString());

        gvAdapter = new WalletCategoriesAdapter(this, categoryFileDB_List);
        gvAdapter.setFocusedPosition(0);
        gvAdapter.setIsEdit(false);
        gvAdapter.setIsGridview(isGridview);
        gv_wallet.setAdapter(gvAdapter);
        gvAdapter.notifyDataSetChanged();
    }

    public void bindSearchResult(List<WalletCategoriesFileDB_Pojo> list) {
        gvAdapter = new WalletCategoriesAdapter(this, list);
        gvAdapter.setIsGridview(isGridview);
        gv_wallet.setAdapter(gvAdapter);
        gvAdapter.notifyDataSetChanged();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_cloud_view_sort, menu);

        ((SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search))).setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String str) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String str) {
                List<WalletCategoriesFileDB_Pojo> arrayList = new ArrayList<>();
                if (str.length() > 0) {
                    for (WalletCategoriesFileDB_Pojo walletCategoriesFileDB_Pojo : categoryFileDB_List) {
                        if (walletCategoriesFileDB_Pojo.getCategoryFileName().toLowerCase().contains(str)) {
                            arrayList.add(walletCategoriesFileDB_Pojo);
                        }
                    }
                } else {
                    arrayList = categoryFileDB_List;
                }
                bindSearchResult(arrayList);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            WalletCommon.walletCategoryScrollIndex = 0;
            SecurityLocksCommon.IsAppDeactive = false;
            startActivity(new Intent(this, ActivityMain.class));
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        WalletCommon.walletCategoryScrollIndex = 0;
        SecurityLocksCommon.IsAppDeactive = false;
        startActivity(new Intent(this, ActivityMain.class));
        finish();
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        if (AccelerometerManager.isListening()) {
            AccelerometerManager.stopListening();
        }
        if (SecurityLocksCommon.IsAppDeactive) {
            finish();
            System.exit(0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AccelerometerManager.isSupported(this)) {
            AccelerometerManager.startListening(this);
        }
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

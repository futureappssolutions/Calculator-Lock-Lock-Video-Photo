package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Activity;

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
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.R;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.common.Constants;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.AccelerometerListener;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.AccelerometerManager;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchActivityMethods;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.securitylocks.SecurityLocksCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.Common;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.wallet.CommonSharedPreferences;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB.WalletCategoriesDAL;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.WalletCategoriesFileDB_Pojo;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.wallet.WalletCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Adapter.WalletEntriesAdapter;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB.WalletEntriesDAL;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.WalletEntryFileDB_Pojo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WalletEntriesActivity extends AppCompatActivity implements AccelerometerListener, SensorEventListener {
    private boolean isGridview = false;
    private Constants constants;
    private GridView gv_wallet;
    private LinearLayout ll_noWallet;
    private WalletEntriesAdapter gvAdapter;
    private WalletCategoriesFileDB_Pojo categoriesFileDB_Pojo;
    private List<WalletEntryFileDB_Pojo> entryFileDB_Pojo;
    private WalletCategoriesDAL walletCategoriesDAL;
    private WalletEntriesDAL walletEntriesDAL;
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
        setContentView(R.layout.activity_wallet);

        FloatingActionButton mFab = findViewById(R.id.fabbutton);
        Toolbar toolbar = findViewById(R.id.toolbar);
        gv_wallet = findViewById(R.id.gv_wallet);
        ll_noWallet = findViewById(R.id.ll_noWallet);

        CommonSharedPreferences walletSharedPreferences = CommonSharedPreferences.GetObject(this);
        entryFileDB_Pojo = new ArrayList<>();
        walletEntriesDAL = new WalletEntriesDAL(this);
        walletCategoriesDAL = new WalletCategoriesDAL(this);
        categoriesFileDB_Pojo = new WalletCategoriesFileDB_Pojo();

        constants = new Constants();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        isGridview = walletSharedPreferences.get_ViewByWalletEntry() != 0;

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_top_bar_icon);
        Objects.requireNonNull(getSupportActionBar()).setTitle(WalletCommon.walletCurrentCategoryName);

        Common.applyKitKatTranslucency(this);
        SecurityLocksCommon.IsAppDeactive = true;
        mFab.setVisibility(View.VISIBLE);

        getCurrentCategory();

        setGridview();

        gv_wallet.setOnItemClickListener((adapterView, view, i, j) -> {
            SecurityLocksCommon.IsAppDeactive = false;
            WalletCommon.walletCurrentEntryName = entryFileDB_Pojo.get(i).getEntryFileName();
            startActivity(new Intent(WalletEntriesActivity.this, WalletEntryActivity.class));
            finish();
        });

        mFab.setOnClickListener(view -> {
            SecurityLocksCommon.IsAppDeactive = false;
            WalletCommon.walletCurrentEntryName = "";
            startActivity(new Intent(WalletEntriesActivity.this, WalletEntryActivity.class));
            finish();
        });
    }

    public void getCurrentCategory() {
        StringBuilder sb = new StringBuilder();
        constants.getClass();
        sb.append("SELECT \t     * \t\t\t\t\t\t   FROM  TableWalletCategories WHERE ");
        constants.getClass();
        sb.append("WalletCategoriesFileIsDecoy");
        sb.append(" = ");
        sb.append(SecurityLocksCommon.IsFakeAccount);
        sb.append(" AND ");
        constants.getClass();
        sb.append("WalletCategoriesFileId");
        sb.append(" = ");
        sb.append(WalletCommon.WalletCurrentCategoryId);
        categoriesFileDB_Pojo = walletCategoriesDAL.getCategoryInfoFromDatabase(sb.toString());
    }

    public void setGridview() {
        StringBuilder sb2 = new StringBuilder();
        constants.getClass();
        sb2.append("SELECT \t     * \t\t\t\t\t\t   FROM  TableWalletEntries WHERE ");
        constants.getClass();
        sb2.append("WalletEntryFileIsDecoy");
        sb2.append(" = ");
        sb2.append(SecurityLocksCommon.IsFakeAccount);
        sb2.append(" AND ");
        constants.getClass();
        sb2.append("WalletCategoriesFileId");
        sb2.append(" = ");
        sb2.append(WalletCommon.WalletCurrentCategoryId);
        sb2.append(" ORDER BY ");
        constants.getClass();
        sb2.append("WalletEntryFileModifiedDate");
        sb2.append(" DESC");
        entryFileDB_Pojo = walletEntriesDAL.getAllEntriesInfoFromDatabase(sb2.toString());

        if (entryFileDB_Pojo.size() > 0) {
            gvAdapter = new WalletEntriesAdapter(this, entryFileDB_Pojo);
            gvAdapter.setFocusedPosition(0);
            gvAdapter.setIsEdit(false);
            gvAdapter.setIsGridview(isGridview);
            gv_wallet.setAdapter(gvAdapter);
            gvAdapter.notifyDataSetChanged();
            ll_noWallet.setVisibility(View.GONE);
            return;
        }
        ll_noWallet.setVisibility(View.VISIBLE);
    }

    public void bindSearchResult(List<WalletEntryFileDB_Pojo> list) {
        gvAdapter = new WalletEntriesAdapter(this, list);
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
                List<WalletEntryFileDB_Pojo> arrayList = new ArrayList<>();
                if (str.length() > 0) {
                    for (WalletEntryFileDB_Pojo walletEntryFileDB_Pojo : entryFileDB_Pojo) {
                        if (walletEntryFileDB_Pojo.getEntryFileName().toLowerCase().contains(str)) {
                            arrayList.add(walletEntryFileDB_Pojo);
                        }
                    }
                } else {
                    arrayList = entryFileDB_Pojo;
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
            SecurityLocksCommon.IsAppDeactive = false;
            startActivity(new Intent(this, WalletCategoriesActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        SecurityLocksCommon.IsAppDeactive = false;
        startActivity(new Intent(this, WalletCategoriesActivity.class));
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

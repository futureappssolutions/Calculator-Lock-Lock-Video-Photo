package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Ads.Advertisement;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.R;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.common.Constants;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.AccelerometerListener;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.AccelerometerManager;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchActivityMethods;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.securitylocks.SecurityLocksCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB.ToDoDAL;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.ToDoDB_Pojo;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Adapter.ToDoListAdapter;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.wallet.CommonSharedPreferences;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class ToDoActivity extends AppCompatActivity implements AccelerometerListener, SensorEventListener {
    private ArrayList<ToDoDB_Pojo> toDoList;
    private ToDoListAdapter adapter;
    private Constants constants;
    private LinearLayout ll_emptyToDo;
    private ToDoDAL todoDAL;
    private RecyclerView recList;
    private SensorManager sensorManager;
    private int viewBy = 0;

    @Override
    public void onAccelerationChanged(float f, float f2, float f3) {
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_to_do);



        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBanner(ToDoActivity.this, ll_banner);

        FloatingActionButton fab_AddToDoTask = findViewById(R.id.fab_AddToDoTask);
        recList = findViewById(R.id.toDoCardList);
        ll_emptyToDo = findViewById(R.id.ll_emptyToDo);

        constants = new Constants();
        todoDAL = new ToDoDAL(this);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        CommonSharedPreferences commonSharedPreferences = CommonSharedPreferences.GetObject(this);
        SecurityLocksCommon.IsAppDeactive = true;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("To do Lists");
        toolbar.setNavigationIcon(R.drawable.back_top_bar_icon);

        viewBy = commonSharedPreferences.get_viewByToDoFile();
        fab_AddToDoTask.setOnClickListener(new MyOnClickListeners());
        recList.setHasFixedSize(false);
        setRecyclerView();
    }

    public void setRecyclerView() {
        getData();

        recList.setLayoutManager(new LinearLayoutManager(ToDoActivity.this, RecyclerView.VERTICAL, false));
        if (toDoList.size() > 0) {
            adapter = new ToDoListAdapter(this, toDoList, this);
            adapter.setViewBy(viewBy);
            recList.setAdapter(adapter);
            ll_emptyToDo.setVisibility(View.GONE);
            return;
        }
        ll_emptyToDo.setVisibility(View.VISIBLE);
    }

    public void getData() {
        StringBuilder sb4 = new StringBuilder();
        constants.getClass();
        sb4.append("SELECT \t     * \t\t\t\t\t\t   FROM  TableToDo WHERE ");
        constants.getClass();
        sb4.append("ToDoIsDecoy");
        sb4.append(" = ");
        sb4.append(SecurityLocksCommon.IsFakeAccount);
        sb4.append(" ORDER BY ");
        constants.getClass();
        sb4.append("ToDoName");
        sb4.append(" COLLATE NOCASE ASC");
        toDoList = todoDAL.getAllToDoInfoFromDatabase(sb4.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_cloud_view_sort, menu);
        ((SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search))).setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String str) {
                return true;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public boolean onQueryTextChange(String str) {
                ArrayList<ToDoDB_Pojo> arrayList = new ArrayList<>();
                if (str.length() > 0) {
                    for (ToDoDB_Pojo next : toDoList) {
                        if (next.getToDoFileName().toLowerCase(Locale.getDefault()).contains(str)) {
                            arrayList.add(next);
                        }
                    }
                } else {
                    arrayList = toDoList;
                }
                adapter.setAdapterData(arrayList);
                adapter.notifyDataSetChanged();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            SecurityLocksCommon.IsAppDeactive = false;
            startActivity(new Intent(this, ActivityMain.class));
            finish();
            overridePendingTransition(17432576, 17432577);
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        SecurityLocksCommon.IsAppDeactive = false;
        startActivity(new Intent(this, ActivityMain.class));
        finish();
        overridePendingTransition(17432576, 17432577);
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

    public class MyOnClickListeners implements View.OnClickListener {
        public MyOnClickListeners() {
        }

        @Override
        public void onClick(View view) {
            SecurityLocksCommon.IsAppDeactive = false;
            startActivity(new Intent(ToDoActivity.this, AddToDoActivity.class));
            finish();
            overridePendingTransition(17432576, 17432577);
        }
    }
}

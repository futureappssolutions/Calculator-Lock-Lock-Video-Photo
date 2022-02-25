package com.calculator.vaultlocker.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.calculator.vaultlocker.Model.HackAttemptEntity;
import com.calculator.vaultlocker.R;
import com.calculator.vaultlocker.Adapter.HackAttemptListAdapter;
import com.calculator.vaultlocker.hackattempt.HackAttemptsSharedPreferences;
import com.calculator.vaultlocker.panicswitch.AccelerometerManager;
import com.calculator.vaultlocker.panicswitch.PanicSwitchActivityMethods;
import com.calculator.vaultlocker.panicswitch.PanicSwitchCommon;
import com.calculator.vaultlocker.securitylocks.SecurityLocksCommon;

import java.util.ArrayList;
import java.util.Objects;

public class HackAttemptActivity extends BaseActivity {
    boolean isEditMode = false;
    boolean selectAll = false;
    private ProgressDialog myProgressDialog;
    private ListView HackAttemptListView;
    private ArrayList<HackAttemptEntity> hackAttemptEntitys;
    private HackAttemptListAdapter hackAttemptListAdapter;
    private LinearLayout ll_DeleteAndSelectAll;
    private LinearLayout.LayoutParams ll_DeleteAndSelectAll_Params;
    private LinearLayout.LayoutParams ll_DeleteAndSelectAll_Params_hidden;
    Handler handle = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            if (message.what == 2) {
                hideProgress();
            } else if (message.what == 3) {
                hideProgress();
                ChangeCheckboxVisibility(false);
                Toast.makeText(HackAttemptActivity.this, R.string.toast_more_hack_attempts_deleted, Toast.LENGTH_SHORT).show();
                SecurityLocksCommon.IsAppDeactive = false;
                startActivity(new Intent(HackAttemptActivity.this, HackAttemptActivity.class));
                overridePendingTransition(17432576, 17432577);
                finish();
            }
            super.handleMessage(message);
        }
    };
    private LinearLayout ll_No_hackattempts;
    private LinearLayout ll_hackattempts;
    private SensorManager sensorManager;

    @Override
    public void onAccelerationChanged(float f, float f2, float f3) {
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public void showDeleteProgress() {
        myProgressDialog = ProgressDialog.show(this, null, "Please be patient... this may take a few moments...", true);
    }

    public void hideProgress() {
        if (myProgressDialog != null && myProgressDialog.isShowing()) {
            myProgressDialog.dismiss();
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.hack_attempt_activity);

        SecurityLocksCommon.IsAppDeactive = true;
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Hack Attempts");
        toolbar.setNavigationIcon(R.drawable.ic_top_back_icon);
        toolbar.setNavigationOnClickListener(view -> btnBackonClick());

        LinearLayout ll_Delete = findViewById(R.id.ll_Delete);
        LinearLayout ll_SelectAll = findViewById(R.id.ll_SelectAll);
        ll_DeleteAndSelectAll = findViewById(R.id.ll_DeleteAndSelectAll);
        ll_No_hackattempts = findViewById(R.id.ll_No_hackattempts);
        ll_hackattempts = findViewById(R.id.ll_hackattempts);
        HackAttemptListView = findViewById(R.id.HackAttemptListView);

        ll_DeleteAndSelectAll_Params = new LinearLayout.LayoutParams(-1, -2);
        ll_DeleteAndSelectAll_Params_hidden = new LinearLayout.LayoutParams(-2, 0);
        ll_DeleteAndSelectAll.setVisibility(View.INVISIBLE);
        ll_No_hackattempts.setVisibility(View.VISIBLE);
        ll_hackattempts.setVisibility(View.INVISIBLE);
        ll_DeleteAndSelectAll.setLayoutParams(ll_DeleteAndSelectAll_Params_hidden);
        
        HackAttemptListView.setOnItemClickListener((adapterView, view, i, j) -> {
            if (!isEditMode) {
                SecurityLocksCommon.IsAppDeactive = false;
                Intent intent = new Intent(HackAttemptActivity.this, HackAttemptDetailActivity.class);
                intent.putExtra("HackerImagePath", hackAttemptEntitys.get(i).GetImagePath());
                intent.putExtra("WrongPass", hackAttemptEntitys.get(i).GetWrongPassword());
                intent.putExtra("DateTime", hackAttemptEntitys.get(i).GetHackAttemptTime());
                intent.putExtra("Position", i);
                startActivity(intent);
                finish();
            }
        });

        HackAttemptListView.setOnItemLongClickListener((adapterView, view, i, j) -> {
            hackAttemptEntitys.get(i).SetIsCheck(true);
            ll_DeleteAndSelectAll.setVisibility(View.VISIBLE);
            ll_DeleteAndSelectAll.setLayoutParams(ll_DeleteAndSelectAll_Params);
            hackAttemptListAdapter = new HackAttemptListAdapter(HackAttemptActivity.this, 17367043, hackAttemptEntitys, true, false);
            HackAttemptListView.setAdapter(hackAttemptListAdapter);
            hackAttemptListAdapter.notifyDataSetChanged();
            return true;
        });

        ll_Delete.setOnClickListener(view -> {
            if (hackAttemptEntitys != null) {
                if (IsFileCheck()) {
                    DeleteHackAttept();
                } else {
                    Toast.makeText(HackAttemptActivity.this, R.string.toast_unselectphotomsg_Hackattempts, Toast.LENGTH_SHORT).show();
                }
            }
        });

        ll_SelectAll.setOnClickListener(view -> {
            selectAll = !selectAll;
            for (HackAttemptEntity hackAttemptEntity : hackAttemptEntitys) {
                hackAttemptEntity.SetIsCheck(selectAll);
            }
            hackAttemptListAdapter = new HackAttemptListAdapter(HackAttemptActivity.this, 17367043, hackAttemptEntitys, true, selectAll);
            HackAttemptListView.setAdapter(hackAttemptListAdapter);
            hackAttemptListAdapter.notifyDataSetChanged();
        });
        BindHackAttempsList();
    }

    public void btnBackonClick() {
        SecurityLocksCommon.IsAppDeactive = false;
        startActivity(new Intent(this, ActivityMain.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        btnBackonClick();
    }

    @SuppressLint("SetTextI18n")
    public void DeleteHackAttept() {
        final Dialog dialog = new Dialog(this, R.style.FullHeightDialog);
        dialog.setContentView(R.layout.confirmation_message_box);
        dialog.setCancelable(true);
        TextView textView = dialog.findViewById(R.id.tvmessagedialogtitle);
        textView.setTypeface(Typeface.createFromAsset(getAssets(), "ebrima.ttf"));
        textView.setText("Are you sure you want to delete selected Hack Attempts?");

        dialog.findViewById(R.id.ll_Ok).setOnClickListener(view -> {
            showDeleteProgress();
            new Thread() { 
                @Override 
                public void run() {
                    try {
                        dialog.dismiss();
                        Delete();
                        Message message = new Message();
                        message.what = 3;
                        handle.sendMessage(message);
                    } catch (Exception unused) {
                        Message message2 = new Message();
                        message2.what = 2;
                        handle.sendMessage(message2);
                    }
                }
            }.start();
            dialog.dismiss();
        });

        dialog.findViewById(R.id.ll_Cancel).setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    public void Delete() {
        for (HackAttemptEntity o : new ArrayList<>(hackAttemptEntitys)) {
            if (o.GetIsCheck()) {
                hackAttemptEntitys.remove(o);
            }
        }
        HackAttemptsSharedPreferences.GetObject(this).SetHackAttemptObject(hackAttemptEntitys);
    }

    public boolean IsFileCheck() {
        for (HackAttemptEntity o : new ArrayList<>(hackAttemptEntitys)) {
            if (o.GetIsCheck()) {
                return true;
            }
        }
        return false;
    }

    private void BindHackAttempsList() {
        hackAttemptEntitys = HackAttemptsSharedPreferences.GetObject(getApplicationContext()).GetHackAttemptObject();
        if (hackAttemptEntitys != null) {
            if (hackAttemptEntitys.size() > 0) {
                ll_No_hackattempts.setVisibility(View.INVISIBLE);
                ll_hackattempts.setVisibility(View.VISIBLE);
            }
            hackAttemptListAdapter = new HackAttemptListAdapter(this, 17367043,hackAttemptEntitys, false, false);
            HackAttemptListView.setAdapter(hackAttemptListAdapter);
            hackAttemptListAdapter.notifyDataSetChanged();
            return;
        }
        ll_No_hackattempts.setVisibility(View.VISIBLE);
        ll_hackattempts.setVisibility(View.INVISIBLE);
    }

    public void ChangeCheckboxVisibility(boolean z) {
        if (z) {
            ll_DeleteAndSelectAll.setVisibility(View.VISIBLE);
            ll_DeleteAndSelectAll.setLayoutParams(ll_DeleteAndSelectAll_Params);
        } else {
            ll_DeleteAndSelectAll.setVisibility(View.INVISIBLE);
            ll_DeleteAndSelectAll.setLayoutParams(ll_DeleteAndSelectAll_Params_hidden);
        }
        for (HackAttemptEntity hackAttemptEntity :hackAttemptEntitys) {
            hackAttemptEntity.SetIsCheck(z);
        }
        hackAttemptListAdapter = new HackAttemptListAdapter(this, 17367043,hackAttemptEntitys, z, false);
        HackAttemptListView.setAdapter(hackAttemptListAdapter);
        hackAttemptListAdapter.notifyDataSetChanged();
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
        if (SecurityLocksCommon.IsAppDeactive) {
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
            if (isEditMode) {
                isEditMode = false;
                ChangeCheckboxVisibility(false);
            }
            btnBackonClick();
        }
        return super.onKeyDown(i, keyEvent);
    }
}

package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.HackAttemptEntity;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.R;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.hackattempt.HackAttemptsSharedPreferences;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.common.SimpleGestureFilter;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.AccelerometerListener;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.AccelerometerManager;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchActivityMethods;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.securitylocks.SecurityLocksCommon;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class HackAttemptDetailActivity extends Activity implements AccelerometerListener, SensorEventListener, SimpleGestureFilter.SimpleGestureListener {
    private int Position = 0;
    private String DateTime = "";
    private String HackerImagePath = "";
    private String WrongPass = "";
    private ImageView imghackattempt;
    private TextView txtattempttime;
    private TextView txtwrongpass;
    private SimpleGestureFilter detector;
    private SensorManager sensorManager;
    private ArrayList<HackAttemptEntity> hackAttemptEntitys;

    @Override
    public void onAccelerationChanged(float f, float f2, float f3) {
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override
    public void onDoubleTap() {
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.hackattempt_detail_activity);

        SecurityLocksCommon.IsAppDeactive = true;
        detector = new SimpleGestureFilter(this, this);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        imghackattempt = findViewById(R.id.imghackattempt);
        txtwrongpass = findViewById(R.id.txtwrongpass);
        txtattempttime = findViewById(R.id.txtattempttime);
        hackAttemptEntitys = HackAttemptsSharedPreferences.GetObject(getApplicationContext()).GetHackAttemptObject();

        Intent intent = getIntent();
        HackerImagePath = intent.getStringExtra("HackerImagePath");
        SetHackerImageToImageView(HackerImagePath);
        WrongPass = intent.getStringExtra("WrongPass");
        DateTime = intent.getStringExtra("DateTime");
        Position = intent.getIntExtra("Position", 0);

        if (SecurityLocksCommon.LoginOptions.Password.toString().equals(hackAttemptEntitys.get(Position).GetLoginOption())) {
            txtwrongpass.setText("Wrong Password: " + hackAttemptEntitys.get(Position).GetWrongPassword());
        } else if (SecurityLocksCommon.LoginOptions.Pin.toString().equals(hackAttemptEntitys.get(Position).GetLoginOption())) {
            txtwrongpass.setText("Wrong PIN: " + hackAttemptEntitys.get(Position).GetWrongPassword());
        } else if (SecurityLocksCommon.LoginOptions.Pattern.toString().equals(hackAttemptEntitys.get(Position).GetLoginOption())) {
            txtwrongpass.setText("Wrong Pattern: " + hackAttemptEntitys.get(Position).GetWrongPassword());
        }

        DateTime = DateTime.replace("GMT+05:00", "");
        if (DateTime.length() > 0) {
            txtattempttime.setText(DateTime);
        }
    }

    public void btnBackonClick(View view) {
        SecurityLocksCommon.IsAppDeactive = false;
        startActivity(new Intent(this, HackAttemptActivity.class));
        finish();
    }

    public void SetHackerImageToImageView(String str) {
        try {
            imghackattempt.setImageBitmap(BitmapFactory.decodeStream(new FileInputStream(str), null, null));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        detector.onTouchEvent(motionEvent);
        return super.dispatchTouchEvent(motionEvent);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSwipe(int i) {
        if (i == 3) {
            if (Position == 0) {
                Position = hackAttemptEntitys.size();
            }
            int i2 = Position;
            if (i2 > 0) {
                int i3 = i2 - 1;
                Position = i3;
                HackerImagePath = hackAttemptEntitys.get(i3).GetImagePath();
                WrongPass = hackAttemptEntitys.get(Position).GetWrongPassword();
                DateTime = hackAttemptEntitys.get(Position).GetHackAttemptTime();
                DateTime = DateTime.replace("GMT+05:00", "");

                runOnUiThread(() -> {
                    SetHackerImageToImageView(HackerImagePath);
                    if (WrongPass.length() > 0) {
                        if (SecurityLocksCommon.LoginOptions.Password.toString().equals(hackAttemptEntitys.get(Position).GetLoginOption())) {
                            txtwrongpass.setText("Wrong Password: " + hackAttemptEntitys.get(Position).GetWrongPassword());
                        } else if (SecurityLocksCommon.LoginOptions.Pin.toString().equals(hackAttemptEntitys.get(Position).GetLoginOption())) {
                            txtwrongpass.setText("Wrong PIN: " + hackAttemptEntitys.get(Position).GetWrongPassword());
                        } else if (SecurityLocksCommon.LoginOptions.Pattern.toString().equals(hackAttemptEntitys.get(Position).GetLoginOption())) {
                            txtwrongpass.setText("Wrong Pattern: " + hackAttemptEntitys.get(Position).GetWrongPassword());
                        }
                    }
                    if (DateTime.length() > 0) {
                        txtattempttime.setText(DateTime);
                    }
                });
            }
        } else if (i == 4) {
            if (Position == hackAttemptEntitys.size()) {
                Position = 0;
            }
            int i4 = Position;
            if (i4 >= 0 && i4 < hackAttemptEntitys.size()) {
                HackerImagePath = hackAttemptEntitys.get(Position).GetImagePath();
                WrongPass = hackAttemptEntitys.get(Position).GetWrongPassword();
                DateTime = hackAttemptEntitys.get(Position).GetHackAttemptTime();
                DateTime = DateTime.replace("GMT+05:00", "");

                runOnUiThread(() -> {
                    SetHackerImageToImageView(HackerImagePath);
                    if (WrongPass.length() > 0) {
                        txtwrongpass.setText(WrongPass);
                    }
                    if (DateTime.length() > 0) {
                        txtattempttime.setText(DateTime);
                    }
                });
                Position++;
            }
        }
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
            SecurityLocksCommon.IsAppDeactive = false;
            startActivity(new Intent(getApplicationContext(), HackAttemptActivity.class));
            finish();
        }
        return super.onKeyDown(i, keyEvent);
    }
}

package com.calculator.vaultlocker.Activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.calculator.vaultlocker.R;
import com.calculator.vaultlocker.calculator.CalculatorPin;
import com.calculator.vaultlocker.panicswitch.AccelerometerManager;
import com.calculator.vaultlocker.panicswitch.PanicSwitchActivityMethods;
import com.calculator.vaultlocker.panicswitch.PanicSwitchCommon;
import com.calculator.vaultlocker.securitylocks.SecurityLocksActivity;
import com.calculator.vaultlocker.securitylocks.SecurityLocksCommon;
import com.calculator.vaultlocker.securitylocks.SecurityLocksSharedPreferences;

public class CalculatorPinSetting extends BaseActivity {
    private final StringBuilder builder = new StringBuilder();
    boolean isPinNotMatch = false;
    boolean isSettingDecoy = false;
    boolean isconfirmDisguiseMode = false;
    private CalculatorPin calculator;
    private HorizontalScrollView hsv;
    private TextView displayPrimary;
    private TextView tvPin;
    private String from;
    private SecurityLocksSharedPreferences securityCredentialsSharedPreferences;
    private StringBuilder compringString = new StringBuilder();
    private TextView displaySecondary;
    private SensorManager sensorManager;
    private String confirmCalPin = "";
    private String newCalPin = "";
    private String isconfirmdialog = "";
    private String LoginOption = "";

    @Override
    public void onAccelerationChanged(float f, float f2, float f3) {
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_calculator_pin_setting);

        SecurityLocksCommon.IsAppDeactive = false;
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        securityCredentialsSharedPreferences = SecurityLocksSharedPreferences.GetObject(this);
        LoginOption = securityCredentialsSharedPreferences.GetLoginType();

        tvPin = findViewById(R.id.tv_pin);
        displayPrimary = findViewById(R.id.display_primary);
        displaySecondary = findViewById(R.id.display_secondary);
        hsv = findViewById(R.id.display_hsv);

        Bundle extras = getIntent().getExtras();
        if (getIntent().getStringExtra("isconfirmdialog") != null) {
            isconfirmdialog = getIntent().getStringExtra("isconfirmdialog");
        }

        if (extras != null) {
            from = extras.getString("from");
            if (from == null || !from.equals("SettingFragment")) {
                tvPin.setText(R.string.cal_pin_text);
            } else if (securityCredentialsSharedPreferences.isGetCalModeEnable()) {
                tvPin.setText(R.string.cal_pin_text_conf);
                isconfirmDisguiseMode = true;
            } else {
                tvPin.setText(R.string.cal_pin_text);
            }
        } else {
            tvPin.setText(R.string.cal_pin_text);
        }

        TextView[] textViewArr = {findViewById(R.id.button_0), findViewById(R.id.button_1), findViewById(R.id.button_2), findViewById(R.id.button_3), findViewById(R.id.button_4), findViewById(R.id.button_5), findViewById(R.id.button_6), findViewById(R.id.button_7), findViewById(R.id.button_8), findViewById(R.id.button_9)};
        int i = 0;
        for (int i2 = 10; i < i2; i2 = 10) {
            final String str = (String) textViewArr[i].getText();
            textViewArr[i].setOnClickListener(view -> {
                calculator.digit(str.charAt(0));
                CalculatorPinSetting calculatorPinSetting = CalculatorPinSetting.this;
                StringBuilder sb = calculatorPinSetting.builder;
                sb.append(str.charAt(0));
                calculatorPinSetting.compringString = sb;
                if (isPinNotMatch) {
                    tvPin.setText(R.string.cal_pin_text_confirm);
                }
            });
            i++;
        }

        TextView[] textViewArr2 = {findViewById(R.id.button_sin), findViewById(R.id.button_cos), findViewById(R.id.button_tan), findViewById(R.id.button_ln), findViewById(R.id.button_log), findViewById(R.id.button_factorial), findViewById(R.id.button_pi), findViewById(R.id.button_e), findViewById(R.id.button_exponent), findViewById(R.id.button_start_parenthesis), findViewById(R.id.button_end_parenthesis), findViewById(R.id.button_square_root), findViewById(R.id.button_add), findViewById(R.id.button_subtract), findViewById(R.id.button_multiply), findViewById(R.id.button_divide), findViewById(R.id.button_decimal), findViewById(R.id.button_equals)};
        for (int i3 = 0; i3 < 18; i3++) {
            final String str2 = (String) textViewArr2[i3].getText();
            textViewArr2[i3].setOnClickListener(view -> {
                if (str2.equals("sin")) {
                    calculator.opNum('s');
                }
                if (str2.equals("cos")) {
                    calculator.opNum('c');
                }
                if (str2.equals("tan")) {
                    calculator.opNum('t');
                }
                if (str2.equals("ln")) {
                    calculator.opNum('n');
                }
                if (str2.equals("log")) {
                    calculator.opNum('l');
                }
                if (str2.equals("!")) {
                    Toast.makeText(CalculatorPinSetting.this, R.string.disable_operation_cal, Toast.LENGTH_SHORT).show();
                }
                if (str2.equals("π")) {
                    calculator.num((char) 960);
                }
                if (str2.equals("e")) {
                    calculator.num('e');
                }
                if (str2.equals("^")) {
                    calculator.numOpNum('^');
                }
                if (str2.equals("%")) {
                    ResetorSetCalPin();
                }
                if (str2.equals("(")) {
                    calculator.parenthesisLeft();
                }
                if (str2.equals(")")) {
                    calculator.parenthesisRight();
                }
                if (str2.equals("√")) {
                    Toast.makeText(CalculatorPinSetting.this, R.string.disable_operation_cal, Toast.LENGTH_SHORT).show();
                }
                if (str2.equals("÷")) {
                    Toast.makeText(CalculatorPinSetting.this, R.string.disable_operation_cal, Toast.LENGTH_SHORT).show();
                }
                if (str2.equals("×")) {
                    Toast.makeText(CalculatorPinSetting.this, R.string.disable_operation_cal, Toast.LENGTH_SHORT).show();
                }
                if (str2.equals("−")) {
                    Toast.makeText(CalculatorPinSetting.this, R.string.disable_operation_cal, Toast.LENGTH_SHORT).show();
                }
                if (str2.equals("+")) {
                    Toast.makeText(CalculatorPinSetting.this, R.string.disable_operation_cal, Toast.LENGTH_SHORT).show();
                }
                if (str2.equals(".")) {
                    Toast.makeText(CalculatorPinSetting.this, R.string.disable_operation_cal, Toast.LENGTH_SHORT).show();
                }
                if (str2.equals("=") && !getText().equals("")) {
                    Toast.makeText(CalculatorPinSetting.this, R.string.disable_operation_cal, Toast.LENGTH_SHORT).show();
                    builder.setLength(0);
                    compringString.setLength(0);
                }
            });
        }

        findViewById(R.id.button_delete).setOnClickListener(view -> {
            calculator.delete();
            builder.setLength(0);
            compringString.setLength(0);
        });

        findViewById(R.id.button_delete).setOnLongClickListener(view -> {
            if (!displayPrimary.getText().toString().trim().equals("")) {
                View findViewById = findViewById(R.id.display_overlay);
                Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(findViewById, findViewById.getMeasuredWidth() / 2, findViewById.getMeasuredHeight(), 0.0f, (float) ((int) Math.hypot(findViewById.getWidth(), findViewById.getHeight())));
                createCircularReveal.setDuration(300);
                createCircularReveal.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationRepeat(Animator animator) {
                    }

                    @Override
                    public void onAnimationStart(Animator animator) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        calculator.setText("");
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                        calculator.setText("");
                    }
                });
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(findViewById, "alpha", 0.0f);
                ofFloat.setInterpolator(new DecelerateInterpolator());
                ofFloat.setDuration(200L);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playSequentially(createCircularReveal, ofFloat);
                findViewById.setAlpha(1.0f);
                animatorSet.start();
            }
            return false;
        });

        calculator = new CalculatorPin(this);

        if (bundle != null) {
            setText(bundle.getString("text"));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setText(getText());
        if (AccelerometerManager.isSupported(this)) {
            AccelerometerManager.startListening(this);
        }
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(8), 3);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString("text", getText());
    }

    @Override
    public void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        setText(bundle.getString("text"));
    }

    public String getText() {
        return calculator.getText();
    }

    public void setText(String str) {
        calculator.setText(str);
    }

    public void displayPrimaryScrollLeft(String str) {
        displayPrimary.setText(formatToDisplayMode(str));
        hsv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                hsv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                hsv.fullScroll(17);
            }
        });
    }

    public void displayPrimaryScrollRight(String str) {
        displayPrimary.setText(formatToDisplayMode(str));
        hsv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                hsv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                hsv.fullScroll(66);
            }
        });
    }

    public void displaySecondary(String str) {
        displaySecondary.setText(formatToDisplayMode(str));
    }

    private String formatToDisplayMode(String str) {
        return str.replace("/", "÷").replace("*", "×").replace("-", "−").replace("n ", "ln(").replace("l ", "log(").replace("√ ", "√(").replace("s ", "sin(").replace("c ", "cos(").replace("t ", "tan(").replace(" ", "").replace("∞", "Infinity").replace("NaN", "Undefined");
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
        super.onPause();
    }

    public void ResetorSetCalPin() {
        if (!getText().trim().isEmpty()) {
            String str = from;
            if (str != null && str.equals("SettingFragment")) {
                String confCalPIN = securityCredentialsSharedPreferences.GetSecurityCredential();
                if (getText().contentEquals(confCalPIN)) {
                    SecurityLocksCommon.IsAppDeactive = false;
                    if (SecurityLocksCommon.isBackupPasswordPin) {
                        SecurityLocksCommon.isBackupPasswordPin = false;
                        startActivity(new Intent(this, RecoveryOfCredentialsActivity.class));
                        finish();
                        return;
                    }
                    Intent intent = new Intent(this, SecurityLocksActivity.class);
                    intent.putExtra("isconfirmdialog", "isconfirmdialog");
                    startActivity(intent);
                    finish();
                    return;
                }
                Toast.makeText(this, R.string.lbl_Pin_doesnt_match, Toast.LENGTH_SHORT).show();
            } else if (getText().length() <= 4) {
                Toast.makeText(this, R.string.lbl_Pin_Limit, Toast.LENGTH_SHORT).show();
            } else if (getText().length() > 9) {
                Toast.makeText(this, R.string.lbl_Pin_not_greater_than_8, Toast.LENGTH_SHORT).show();
            } else if (isSettingDecoy) {
            } else {
                if (newCalPin.equals("")) {
                    tvPin.setText(R.string.cal_pin_text_confirm);
                    newCalPin = getText();
                    setText("");
                } else if (confirmCalPin.equals("")) {
                    String text = getText();
                    confirmCalPin = text;
                    if (text.equals(newCalPin)) {
                        securityCredentialsSharedPreferences.SetLoginType(SecurityLocksCommon.LoginOptions.Calculator.toString());
                        securityCredentialsSharedPreferences.SetSecurityCredential(confirmCalPin);
                        Toast.makeText(this, "Calculator PIN set successfully.....", Toast.LENGTH_SHORT).show();
                        securityCredentialsSharedPreferences.RemoveDecoySecurityCredential();
                        securityCredentialsSharedPreferences.isSetCalModeEnable(true);
                        if (SecurityLocksCommon.IsFirstLogin) {
                            SecurityLocksCommon.IsFirstLogin = false;
                            securityCredentialsSharedPreferences.SetIsFirstLogin(false);
                        }
                        if (isSettingDecoy) {
                            Intent intent2 = new Intent(this, ActivityMain.class);
                            SecurityLocksCommon.IsAppDeactive = false;
                            startActivity(intent2);
                            finish();
                        } else if (!isconfirmdialog.equals("isconfirmdialog")) {
                            if (securityCredentialsSharedPreferences.GetShowFirstTimeEmailPopup() && SecurityLocksCommon.LoginOptions.None.toString().equals(LoginOption)) {
                                FirstTimeEmailDialog();
                            } else if (securityCredentialsSharedPreferences.GetSecurityCredential().length() <= 0 || securityCredentialsSharedPreferences.GetEmail().length() <= 0) {
                                FirstTimeEmailDialog();
                            } else {
                                Log.d("Calculator Pin Setting", "Calculator dialog will appear");
                                SecurityLocksCommon.IsAppDeactive = false;
                                startActivity(new Intent(this, ActivityMain.class));
                                finish();
                            }
                        } else if (securityCredentialsSharedPreferences.GetEmail().trim().isEmpty()) {
                            FirstTimeEmailDialog();
                        } else {
                            SecurityLocksCommon.IsAppDeactive = false;
                            Intent intent3 = new Intent(this, ActivityMain.class);
                            if (SecurityLocksCommon.IsFirstLogin) {
                                SecurityLocksCommon.IsFirstLogin = false;
                                securityCredentialsSharedPreferences.SetIsFirstLogin(false);
                            }
                            startActivity(intent3);
                            overridePendingTransition(17432576, 17432577);
                            finish();
                        }
                    } else {
                        Toast.makeText(this, R.string.lbl_Password_doesnt_match, Toast.LENGTH_SHORT).show();
                        confirmCalPin = "";
                        tvPin.setText(R.string.lbl_Pin_doesnt_match);
                        setText("");
                        isPinNotMatch = true;
                    }
                }
            }
        } else {
            Toast.makeText(this, "Enter your PIN", Toast.LENGTH_SHORT).show();
        }
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

    @Override
    public void onBackPressed() {
        SecurityLocksCommon.IsAppDeactive = false;
        if (SecurityLocksCommon.isBackupPasswordPin) {
            Intent intent = new Intent(this, SettingActivity.class);
            overridePendingTransition(17432576, 17432577);
            startActivity(intent);
            SecurityLocksCommon.isBackupPasswordPin = false;
            finish();
            return;
        }

        if (from == null) {
            startActivity(new Intent(this, SecurityLocksActivity.class));
            overridePendingTransition(17432576, 17432577);
            finish();
        } else if (from.equals("SettingFragment")) {
            Intent intent2 = new Intent(this, SettingActivity.class);
            overridePendingTransition(17432576, 17432577);
            startActivity(intent2);
            finish();
        }
    }

    @SuppressLint("ResourceType")
    public void FirstTimeEmailDialog() {
        securityCredentialsSharedPreferences.SetShowFirstTimeEmailPopup(false);
        new AlertDialog.Builder(this).setTitle("Default Password").setMessage("Your deault password is :- 0000").setPositiveButton("Yes Remember", (dialogInterface, i) -> {
            dialogInterface.dismiss();
            if (!isconfirmdialog.equals("isconfirmdialog")) {
                SecurityLocksCommon.IsAppDeactive = false;
                startActivity(new Intent(CalculatorPinSetting.this, ActivityMain.class));
                finish();
                return;
            }
            SecurityLocksCommon.IsAppDeactive = false;
            startActivity(new Intent(CalculatorPinSetting.this, ActivityMain.class));
            finish();
        }).setIcon(17301543).show();
    }
}

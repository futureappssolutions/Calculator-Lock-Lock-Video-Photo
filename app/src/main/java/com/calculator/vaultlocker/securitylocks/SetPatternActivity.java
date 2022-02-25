package com.calculator.vaultlocker.securitylocks;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.calculator.vaultlocker.Activity.LoginActivity;
import com.calculator.vaultlocker.R;
import com.calculator.vaultlocker.Activity.ActivityMain;
import com.calculator.vaultlocker.panicswitch.AccelerometerListener;
import com.calculator.vaultlocker.panicswitch.AccelerometerManager;
import com.calculator.vaultlocker.panicswitch.PanicSwitchActivityMethods;
import com.calculator.vaultlocker.panicswitch.PanicSwitchCommon;
import com.calculator.vaultlocker.common.RecoveryOfCredentialsMethods;
import com.calculator.vaultlocker.Activity.SettingActivity;
import com.calculator.vaultlocker.utilities.Common;

import java.util.ArrayList;
import java.util.List;


public class SetPatternActivity extends Activity implements AccelerometerListener, SensorEventListener {
    public static final String BUNDLE_GRID_LENGTH = "grid_length";
    public static final String BUNDLE_HIGHLIGHT = "highlight";
    public static final String BUNDLE_PATTERN = "pattern";
    public static final String BUNDLE_PATTERN_MAX = "pattern_max";
    public static final String BUNDLE_PATTERN_MIN = "pattern_min";
    public static final int DIALOG_EXITED_HARD = 1;
    public static final int DIALOG_SEPARATION_WARNING = 0;
    public static TextView lblCancel;
    public static TextView lblContinueOrDone;
    public static TextView lbl_setpattern_topbaar_title;
    public static LinearLayout ll_Cancel;
    public static LinearLayout ll_ContinueOrDone;
    public static LinearLayout ll_background;
    public static LinearLayout ll_setpattern_topbaar_bg;
    public static TextView txtdrawpattern;
    public String PatternPassword = "";
    protected int mGridLength;
    protected String mHighlightMode;
    protected int mPatternMax;
    protected int mPatternMin;
    protected SetLockPatternView mPatternView;
    protected ToggleButton mPracticeToggle;
    protected boolean mTactileFeedback;
    boolean isSettingDecoy = false;
    SecurityLocksSharedPreferences securityCredentialsSharedPreferences;
    private List<Point> mEasterEggPattern;
    private SensorManager sensorManager;

    @Override // com.calculator.vaultlocker.panicswitch.AccelerometerListener
    public void onAccelerationChanged(float f, float f2, float f3) {
    }

    @Override // android.hardware.SensorEventListener
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        SecurityLocksCommon.IsAppDeactive = true;
        getWindow().addFlags(128);
        this.sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        ArrayList arrayList = new ArrayList();
        this.mEasterEggPattern = arrayList;
        arrayList.add(new Point(0, 1));
        this.mEasterEggPattern.add(new Point(1, 2));
        this.mEasterEggPattern.add(new Point(2, 1));
        this.mEasterEggPattern.add(new Point(1, 0));
        SecurityLocksCommon.mSiginPattern = new ArrayList();
        SecurityLocksCommon.mSiginPatternConfirm = new ArrayList();
        Typeface createFromAsset = Typeface.createFromAsset(getAssets(), "ebrima.ttf");
        setContentView(R.layout.set_pattern_activity);
        this.mPatternView = (SetLockPatternView) findViewById(R.id.pattern_view);
        ll_background = (LinearLayout) findViewById(R.id.ll_background);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_setpattern_topbaar_bg);
        ll_setpattern_topbaar_bg = linearLayout;
        linearLayout.setBackgroundColor(getResources().getColor(R.color.ColorAppTheme));
        TextView textView = (TextView) findViewById(R.id.lbl_setpattern_topbaar_title);
        lbl_setpattern_topbaar_title = textView;
        textView.setTypeface(createFromAsset);
        lbl_setpattern_topbaar_title.setTextColor(getResources().getColor(R.color.ColorWhite));
        TextView textView2 = (TextView) findViewById(R.id.txtdrawpattern);
        txtdrawpattern = textView2;
        textView2.setTypeface(createFromAsset);
        txtdrawpattern.setText(R.string.lblsetting_SecurityCredentials_Setpattern_Drawnewpattern);
        ll_Cancel = (LinearLayout) findViewById(R.id.ll_Cancel);
        ll_ContinueOrDone = (LinearLayout) findViewById(R.id.ll_ContinueOrDone);
        lblContinueOrDone = (TextView) findViewById(R.id.lblContinueOrDone);
        lblCancel = (TextView) findViewById(R.id.lblCancel);
        lblContinueOrDone.setTypeface(Typeface.createFromAsset(getAssets(), "ebrima.ttf"));
        lblContinueOrDone.setTextColor(getResources().getColor(R.color.black_color));
        lblCancel.setTypeface(Typeface.createFromAsset(getAssets(), "ebrima.ttf"));
        lblCancel.setTextColor(getResources().getColor(R.color.black_color));
        lblContinueOrDone.setText("");
        this.mPatternView.setPracticeMode(true);
        this.mPatternView.invalidate();
        boolean booleanExtra = getIntent().getBooleanExtra("isSettingDecoy", false);
        this.isSettingDecoy = booleanExtra;
        if (booleanExtra) {
            lbl_setpattern_topbaar_title.setText(R.string.lbl_set_decoy_pattern);
            SetLockPatternView.IspatternContinue = false;
            SetLockPatternView.IspatternDone = false;
            txtdrawpattern.setText(R.string.lblsetting_SecurityCredentials_Setpattern_Drawnewpattern);
            ll_Cancel.setBackgroundResource(R.drawable.btn_bottom_baar_album);
            lblCancel.setText(R.string.lblsetting_SecurityCredentials_Setpattern_Cancel);
            ll_ContinueOrDone.setBackgroundResource(R.drawable.btn_bottom_baar_album);
            lblContinueOrDone.setText("");
            this.mPatternView.setPracticeMode(true);
            this.mPatternView.invalidate();
            SecurityLocksCommon.IsAppDeactive = false;
            SecurityLocksCommon.IsConfirmPatternActivity = false;
            SecurityLocksCommon.isBackupPattern = false;
            SecurityLocksCommon.IsSiginPattern = false;
            SecurityLocksCommon.IsSiginPatternConfirm = false;
            SecurityLocksCommon.IsSiginPatternContinue = false;
            SecurityLocksCommon.mSiginPattern.clear();
            SecurityLocksCommon.IsCancel = false;
            SecurityLocksCommon.mSiginPattern.clear();
            SecurityLocksCommon.mSiginPatternConfirm.clear();
        }
        ll_Cancel.setOnClickListener(new View.OnClickListener() { // from class: com.calculator.vaultlocker.securitylocks.SetPatternActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SecurityLocksCommon.IsSiginPattern = false;
                SecurityLocksCommon.IsSiginPatternConfirm = false;
                SecurityLocksCommon.IsSiginPatternContinue = false;
                SecurityLocksCommon.mSiginPattern.clear();
                SecurityLocksCommon.mSiginPatternConfirm.clear();
                if (SecurityLocksCommon.IsCancel) {
                    SecurityLocksCommon.IsCancel = false;
                    SetPatternActivity.txtdrawpattern.setText(R.string.lblsetting_SecurityCredentials_Setpattern_Drawnewpattern);
                    SetPatternActivity.ll_Cancel.setBackgroundResource(R.drawable.btn_bottom_baar_album);
                    SetPatternActivity.lblCancel.setText(R.string.lblsetting_SecurityCredentials_Setpattern_Cancel);
                    SetPatternActivity.ll_ContinueOrDone.setBackgroundResource(R.drawable.btn_bottom_baar_album);
                    SetPatternActivity.lblContinueOrDone.setText("");
                    SetLockPatternView.IspatternContinue = false;
                    SetPatternActivity.this.mPatternView.resetPractice();
                    SetPatternActivity.this.mPatternView.invalidate();
                    return;
                }
                SetLockPatternView.IspatternDone = false;
                SetLockPatternView.IspatternContinue = false;
                SecurityLocksCommon.IsAppDeactive = false;
                Intent intent = new Intent(SetPatternActivity.this, ActivityMain.class);
                if (!SecurityLocksCommon.IsFirstLogin) {
                    intent = new Intent(SetPatternActivity.this, SettingActivity.class);
                } else if (SetPatternActivity.this.isSettingDecoy) {
                    SecurityLocksCommon.IsFirstLogin = false;
                    SetPatternActivity setPatternActivity = SetPatternActivity.this;
                    setPatternActivity.securityCredentialsSharedPreferences = SecurityLocksSharedPreferences.GetObject(setPatternActivity);
                    SetPatternActivity.this.securityCredentialsSharedPreferences.SetIsFirstLogin(false);
                    intent = new Intent(SetPatternActivity.this, ActivityMain.class);
                }
                SetPatternActivity.this.startActivity(intent);
                SetPatternActivity.this.overridePendingTransition(17432576, 17432577);
                SetPatternActivity.this.finish();
            }
        });
        ll_ContinueOrDone.setOnClickListener(new View.OnClickListener() { // from class: com.calculator.vaultlocker.securitylocks.SetPatternActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SetPatternActivity setPatternActivity = SetPatternActivity.this;
                setPatternActivity.securityCredentialsSharedPreferences = SecurityLocksSharedPreferences.GetObject(setPatternActivity);
                if (SetPatternActivity.this.isSettingDecoy && PatternActivityMethods.ConvertPatternToNo(SecurityLocksCommon.mSiginPattern).equals(SetPatternActivity.this.securityCredentialsSharedPreferences.GetSecurityCredential())) {
                    SecurityLocksCommon.IsSiginPattern = false;
                    SecurityLocksCommon.IsSiginPatternConfirm = false;
                    SecurityLocksCommon.IsSiginPatternContinue = false;
                    SecurityLocksCommon.mSiginPattern.clear();
                    SecurityLocksCommon.mSiginPatternConfirm.clear();
                    SecurityLocksCommon.IsCancel = false;
                    SetPatternActivity.txtdrawpattern.setText(R.string.lblsetting_SecurityCredentials_Setpattern_Drawnewpattern);
                    SetPatternActivity.ll_Cancel.setBackgroundResource(R.drawable.btn_bottom_baar_album);
                    SetPatternActivity.lblCancel.setText(R.string.lblsetting_SecurityCredentials_Setpattern_Cancel);
                    SetPatternActivity.ll_ContinueOrDone.setBackgroundResource(R.drawable.btn_bottom_baar_album);
                    SetPatternActivity.lblContinueOrDone.setText("");
                    SetLockPatternView.IspatternContinue = false;
                    SetPatternActivity.this.mPatternView.resetPractice();
                    SetPatternActivity.this.mPatternView.invalidate();
                    Toast.makeText(SetPatternActivity.this.getApplicationContext(), (int) R.string.toast_securitycredentias_set_decoy_fail_pattern, Toast.LENGTH_LONG).show();
                } else if (SecurityLocksCommon.IsSiginPattern) {
                    SetPatternActivity.txtdrawpattern.setText("Draw pattern again to confirm:");
                    SetLockPatternView.IspatternContinue = false;
                    SetPatternActivity.this.mPatternView.resetPractice();
                    SetPatternActivity.this.mPatternView.invalidate();
                    SecurityLocksCommon.IsCancel = false;
                    SecurityLocksCommon.IsSiginPatternConfirm = true;
                    SetPatternActivity.ll_ContinueOrDone.setBackgroundResource(R.drawable.btn_bottom_baar_album);
                    SetPatternActivity.lblContinueOrDone.setText("");
                    SetPatternActivity.ll_Cancel.setBackgroundResource(R.drawable.btn_bottom_baar_album);
                    SetPatternActivity.lblCancel.setText(R.string.lblsetting_SecurityCredentials_Setpattern_Cancel);
                } else if (SecurityLocksCommon.IsSiginPatternConfirm && SecurityLocksCommon.mSiginPatternConfirm.equals(SecurityLocksCommon.mSiginPattern)) {
                    SetPatternActivity.this.mPatternView.resetPractice();
                    SetPatternActivity.this.mPatternView.invalidate();
                    SetPatternActivity.this.PatternPassword = PatternActivityMethods.ConvertPatternToNo(SecurityLocksCommon.mSiginPattern);
                    SetPatternActivity.this.securityCredentialsSharedPreferences.SetLoginType(SecurityLocksCommon.LoginOptions.Pattern.toString());
                    SetLockPatternView.IspatternDone = false;
                    SecurityLocksCommon.IsSiginPattern = false;
                    SecurityLocksCommon.IsSiginPatternConfirm = false;
                    SecurityLocksCommon.IsSiginPatternContinue = false;
                    SecurityLocksCommon.IsCancel = false;
                    SecurityLocksCommon.mSiginPattern.clear();
                    SecurityLocksCommon.mSiginPatternConfirm.clear();
                    if (SetPatternActivity.this.isSettingDecoy) {
                        SetPatternActivity.this.securityCredentialsSharedPreferences.SetDecoySecurityCredential(SetPatternActivity.this.PatternPassword.toString());
                        Toast.makeText(SetPatternActivity.this.getApplicationContext(), (int) R.string.toast_setting_SecurityCredentials_Setpattern_Patternsetsuccesfully_decoy, Toast.LENGTH_LONG).show();
                        SecurityLocksCommon.IsAppDeactive = false;
                        Intent intent = new Intent(SetPatternActivity.this, ActivityMain.class);
                        if (SecurityLocksCommon.IsFirstLogin) {
                            SecurityLocksCommon.IsFirstLogin = false;
                            SetPatternActivity.this.securityCredentialsSharedPreferences.SetIsFirstLogin(false);
                        } else {
                            intent = new Intent(SetPatternActivity.this, SettingActivity.class);
                        }
                        SetPatternActivity.this.startActivity(intent);
                        SetPatternActivity.this.overridePendingTransition(17432576, 17432577);
                        SetPatternActivity.this.finish();
                        return;
                    }
                    SetPatternActivity.this.securityCredentialsSharedPreferences.SetSecurityCredential(SetPatternActivity.this.PatternPassword.toString());
                    Toast.makeText(SetPatternActivity.this.getApplicationContext(), (int) R.string.toast_setting_SecurityCredentials_Setpattern_Patternsetsuccesfully, Toast.LENGTH_LONG).show();
                    if (SetPatternActivity.this.securityCredentialsSharedPreferences.GetSecurityCredential().length() <= 0 || SetPatternActivity.this.securityCredentialsSharedPreferences.GetEmail().length() <= 0) {
                        SetPatternActivity.this.FirstTimeEmailDialog();
                    } else {
                        SetPatternActivity.this.DecoySetPopup();
                    }
                }
            }
        });
        if (bundle != null) {
            this.mPatternView.setPattern(bundle.getParcelableArrayList("pattern"));
        }
    }

    public void DecoySetPopup() {
        final Dialog dialog = new Dialog(this, R.style.FullHeightDialog);
        dialog.setContentView(R.layout.confirmation_message_box);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        TextView textView = (TextView) dialog.findViewById(R.id.tvmessagedialogtitle);
        textView.setTypeface(Typeface.createFromAsset(getAssets(), "ebrima.ttf"));
        textView.setText(R.string.lbl_msg_want_to_set_decoy_pat_ornot);
        ((TextView) dialog.findViewById(R.id.lbl_Ok)).setText("Yes");
        ((TextView) dialog.findViewById(R.id.lbl_Cancel)).setText("No");
        ((LinearLayout) dialog.findViewById(R.id.ll_Ok)).setOnClickListener(new View.OnClickListener() { // from class: com.calculator.vaultlocker.securitylocks.SetPatternActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SetPatternActivity.lbl_setpattern_topbaar_title.setText(R.string.lbl_set_decoy_pattern);
                SetLockPatternView.IspatternContinue = false;
                SetLockPatternView.IspatternDone = false;
                SetPatternActivity.txtdrawpattern.setText(R.string.lblsetting_SecurityCredentials_Setpattern_Drawnewpattern);
                SetPatternActivity.ll_Cancel.setBackgroundResource(R.drawable.btn_bottom_baar_album);
                SetPatternActivity.lblCancel.setText(R.string.lblsetting_SecurityCredentials_Setpattern_Cancel);
                SetPatternActivity.ll_ContinueOrDone.setBackgroundResource(R.drawable.btn_bottom_baar_album);
                SetPatternActivity.lblContinueOrDone.setText("");
                SetPatternActivity.this.mPatternView.setPracticeMode(true);
                SetPatternActivity.this.mPatternView.invalidate();
                SecurityLocksCommon.IsAppDeactive = false;
                SetPatternActivity.this.isSettingDecoy = true;
                SecurityLocksCommon.IsConfirmPatternActivity = false;
                SecurityLocksCommon.isBackupPattern = false;
                SecurityLocksCommon.IsSiginPattern = false;
                SecurityLocksCommon.IsSiginPatternConfirm = false;
                SecurityLocksCommon.IsSiginPatternContinue = false;
                SecurityLocksCommon.mSiginPattern.clear();
                SecurityLocksCommon.IsCancel = false;
                SecurityLocksCommon.mSiginPattern.clear();
                SecurityLocksCommon.mSiginPatternConfirm.clear();
                dialog.dismiss();
            }
        });
        ((LinearLayout) dialog.findViewById(R.id.ll_Cancel)).setOnClickListener(new View.OnClickListener() { // from class: com.calculator.vaultlocker.securitylocks.SetPatternActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SetLockPatternView.IspatternDone = false;
                dialog.dismiss();
                SecurityLocksCommon.IsAppDeactive = false;
                Intent intent = new Intent(SetPatternActivity.this, ActivityMain.class);
                if (SecurityLocksCommon.IsFirstLogin) {
                    SecurityLocksCommon.IsFirstLogin = false;
                    SetPatternActivity.this.securityCredentialsSharedPreferences.SetIsFirstLogin(false);
                } else {
                    intent = new Intent(SetPatternActivity.this, SettingActivity.class);
                }
                SetPatternActivity.this.startActivity(intent);
                SetPatternActivity.this.overridePendingTransition(17432576, 17432577);
                SetPatternActivity.this.finish();
            }
        });
        dialog.show();
    }

    @Override // com.calculator.vaultlocker.panicswitch.AccelerometerListener
    public void onShake(float f) {
        if (PanicSwitchCommon.IsFlickOn || PanicSwitchCommon.IsShakeOn) {
            PanicSwitchActivityMethods.SwitchApp(this);
        }
    }

    @Override // android.hardware.SensorEventListener
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == 8 && sensorEvent.values[0] == 0.0f && PanicSwitchCommon.IsPalmOnFaceOn) {
            PanicSwitchActivityMethods.SwitchApp(this);
        }
    }

    @Override // android.app.Activity
    public void onPause() {
        this.sensorManager.unregisterListener(this);
        if (AccelerometerManager.isListening()) {
            AccelerometerManager.stopListening();
        }
        if (!SecurityLocksCommon.IsFirstLogin) {
            String GetLoginType = SecurityLocksSharedPreferences.GetObject(this).GetLoginType();
            if (SecurityLocksCommon.IsAppDeactive && !SecurityLocksCommon.IsFirstLogin) {
                SetLockPatternView.IspatternContinue = false;
                SecurityLocksCommon.IsSiginPattern = false;
                SecurityLocksCommon.IsSiginPatternConfirm = false;
                SecurityLocksCommon.IsSiginPatternContinue = false;
                SecurityLocksCommon.IsCancel = false;
                SecurityLocksCommon.mSiginPattern.clear();
                SecurityLocksCommon.mSiginPatternConfirm.clear();
                if (!SecurityLocksCommon.LoginOptions.None.toString().equals(GetLoginType)) {
                    if (!SecurityLocksCommon.IsStealthModeOn) {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        overridePendingTransition(17432576, 17432577);
                    }
                    finish();
                }
            }
        }
        if (SecurityLocksCommon.IsAppDeactive) {
            SetLockPatternView.IspatternContinue = false;
            SecurityLocksCommon.IsSiginPattern = false;
            SecurityLocksCommon.IsSiginPatternConfirm = false;
            SecurityLocksCommon.IsSiginPatternContinue = false;
            SecurityLocksCommon.IsCancel = false;
            SecurityLocksCommon.mSiginPattern.clear();
            SecurityLocksCommon.mSiginPatternConfirm.clear();
            finish();
        }
        super.onPause();
    }

    @Override // android.app.Activity
    public void onResume() {
        if (AccelerometerManager.isSupported(this)) {
            AccelerometerManager.startListening(this);
        }
        SensorManager sensorManager = this.sensorManager;
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(8), 3);
        super.onResume();
    }

    @Override // android.app.Activity
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt("grid_length", this.mGridLength);
        bundle.putInt("pattern_max", this.mPatternMax);
        bundle.putInt("pattern_min", this.mPatternMin);
        bundle.putString("highlight", this.mHighlightMode);
        bundle.putParcelableArrayList("pattern", new ArrayList<>(this.mPatternView.getPattern()));
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        Intent intent;
        if (i == 4) {
            SecurityLocksCommon.IsSiginPattern = false;
            SecurityLocksCommon.IsSiginPatternConfirm = false;
            SecurityLocksCommon.IsSiginPatternContinue = false;
            SecurityLocksCommon.IsCancel = false;
            SecurityLocksCommon.mSiginPattern.clear();
            SetLockPatternView.IspatternDone = false;
            SetLockPatternView.IspatternContinue = false;
            SecurityLocksCommon.IsAppDeactive = false;
            if (SecurityLocksCommon.IsFirstLogin) {
                intent = new Intent(this, SecurityLocksActivity.class);
            } else {
                intent = new Intent(this, SettingActivity.class);
            }
            startActivity(intent);
            overridePendingTransition(17432576, 17432577);
            finish();
        }
        return super.onKeyDown(i, keyEvent);
    }

    public void FirstTimeEmailDialog() {
        Typeface createFromAsset = Typeface.createFromAsset(getAssets(), "ebrima.ttf");
        final Dialog dialog = new Dialog(this, R.style.FullHeightDialog);
        dialog.setContentView(R.layout.backup_email_popup);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        final EditText editText = (EditText) dialog.findViewById(R.id.txtemailid);
        ((TextView) dialog.findViewById(R.id.lblEmail_popup_title)).setTypeface(createFromAsset);
        editText.setTypeface(createFromAsset);
        ((TextView) dialog.findViewById(R.id.lbl_email_warning_title)).setTypeface(createFromAsset);
        ((TextView) dialog.findViewById(R.id.lbl_email_warning_desc)).setTypeface(createFromAsset);
        ((TextView) dialog.findViewById(R.id.lblSave)).setTypeface(createFromAsset);
        ((TextView) dialog.findViewById(R.id.lblSkip)).setTypeface(createFromAsset);
        ((LinearLayout) dialog.findViewById(R.id.ll_Save)).setOnClickListener(new View.OnClickListener() { // from class: com.calculator.vaultlocker.securitylocks.SetPatternActivity.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (editText.getText().length() > 0) {
                    String obj = editText.getText().toString();
                    if (new RecoveryOfCredentialsMethods().isEmailValid(obj)) {
                        SetPatternActivity.this.securityCredentialsSharedPreferences.SetEmail(obj);
                        SetPatternActivity.this.securityCredentialsSharedPreferences.SetShowFirstTimeEmailPopup(false);
                        SecurityLocksCommon.IsAppDeactive = false;
                        Toast.makeText(SetPatternActivity.this, (int) R.string.toast_Email_Saved, Toast.LENGTH_LONG).show();
                        SetPatternActivity.this.DecoySetPopup();
                        dialog.dismiss();
                        Common.loginCount = 2;
                        SetPatternActivity.this.securityCredentialsSharedPreferences.SetRateCount(Common.loginCount);
                        return;
                    }
                    Toast.makeText(SetPatternActivity.this, (int) R.string.toast_Invalid_Email, Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(SetPatternActivity.this, (int) R.string.toast_Enter_Email, Toast.LENGTH_SHORT).show();
            }
        });
        ((LinearLayout) dialog.findViewById(R.id.ll_Skip)).setOnClickListener(new View.OnClickListener() { // from class: com.calculator.vaultlocker.securitylocks.SetPatternActivity.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SetPatternActivity.this.securityCredentialsSharedPreferences.SetShowFirstTimeEmailPopup(false);
                SecurityLocksCommon.IsAppDeactive = false;
                Toast.makeText(SetPatternActivity.this, (int) R.string.toast_Skip, Toast.LENGTH_LONG).show();
                SetPatternActivity.this.DecoySetPopup();
                dialog.dismiss();
                Common.loginCount = 1;
                SetPatternActivity.this.securityCredentialsSharedPreferences.SetRateCount(Common.loginCount);
            }
        });
        dialog.show();
    }
}

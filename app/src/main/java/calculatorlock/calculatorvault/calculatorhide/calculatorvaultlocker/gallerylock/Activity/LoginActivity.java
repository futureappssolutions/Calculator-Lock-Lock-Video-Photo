package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Activity;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.R;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.common.FingerprintHandler;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.common.CameraPreview;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.HackAttemptEntity;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.hackattempt.HackAttemptsSharedPreferences;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.AccelerometerListener;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.AccelerometerManager;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchActivityMethods;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchSharedPreferences;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.securitylocks.ConfirmLockPatternViewLogin;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.securitylocks.SecurityLocksCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.securitylocks.SecurityLocksSharedPreferences;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.storageoption.StorageOptionSharedPreferences;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.storageoption.StorageOptionsCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.Common;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.Utilities;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;

public class LoginActivity extends AppCompatActivity implements AccelerometerListener, SensorEventListener {
    public static final String KEY_NAME = "yourKey";
    public static long startTime = 0;
    public static int hackAttemptCount = 0;
    public static Camera mCamera = null;
    public static CameraPreview mCameraPreview = null;
    public static TextView txt_wrong_pttern = null;
    private final Handler customHandler = new Handler();
    public TextView tv_forgot;
    public TextView txt_wrong_password_pin;
    public TextView txtforgotpassword;
    public TextView txtforgotpattern;
    public EditText txtPassword;
    public static String wrongPassword = "";
    public String LoginOption = "";
    public String hackAttemptPath = "";
    private final Camera.PictureCallback mPicture = (bArr, camera) -> {
        File file = new File(SecurityLocksCommon.StoragePath + SecurityLocksCommon.HackAttempts);
        if (!file.exists()) {
            file.mkdirs();
        }
        String uuid = UUID.randomUUID().toString();
        File file2 = new File(SecurityLocksCommon.StoragePath + SecurityLocksCommon.HackAttempts + uuid + "#jpg");
        hackAttemptPath = SecurityLocksCommon.StoragePath + SecurityLocksCommon.HackAttempts + uuid + "#jpg";
        if (!file2.exists()) {
            try {
                file2.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            fileOutputStream.write(bArr);
            fileOutputStream.close();
            AddHackAttempToSharedPreference(LoginActivity.this, wrongPassword, hackAttemptPath);
        } catch (FileNotFoundException unused) {
            Toast.makeText(LoginActivity.this, "File not found exception", Toast.LENGTH_SHORT).show();
        } catch (IOException unused2) {
            Toast.makeText(LoginActivity.this, "IO Exception", Toast.LENGTH_SHORT).show();
        }
        camera.startPreview();
    };
    public String mypass;
    public long timeInMilliseconds = 0;
    public long timeSwapBuff = 0;
    public long updatedTime = 0;
    private final Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {
            Calendar instance = Calendar.getInstance();
            timeInMilliseconds = LoginActivity.startTime - instance.getTimeInMillis();
            updatedTime = timeSwapBuff + timeInMilliseconds;
            int i = (int) (updatedTime / 1000);
            int i2 = (i / 60) / 60;
            int i3 = i % 60;
            if (LoginActivity.startTime > instance.getTimeInMillis()) {
                customHandler.postDelayed(this, 0);
                return;
            }
            Common.HackAttemptCount = 0;
            LoginActivity.hackAttemptCount = 0;
            txtPassword.setEnabled(true);
        }
    };
    private SecurityLocksSharedPreferences securityLocksSharedPreferences;
    private Cipher cipher;
    private KeyStore keyStore;
    private SensorManager sensorManager;

    @Override
    public void onAccelerationChanged(float f, float f2, float f3) {
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public void AddHackAttempToSharedPreference(Context context, String str, String str2) {
        SecurityLocksSharedPreferences GetObject = SecurityLocksSharedPreferences.GetObject(context);
        long currentTimeMillis = System.currentTimeMillis();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        Date date = new Date(currentTimeMillis);
        System.out.println(simpleDateFormat.format(date));
        HackAttemptEntity hackAttemptEntity = new HackAttemptEntity();
        hackAttemptEntity.SetLoginOption(GetObject.GetLoginType());
        hackAttemptEntity.SetWrongPassword(str);
        hackAttemptEntity.SetImagePath(str2);
        hackAttemptEntity.SetHackAttemptTime(date.toString());
        hackAttemptEntity.SetIsCheck(false);
        ArrayList<HackAttemptEntity> hackAttemptEntitys;
        HackAttemptsSharedPreferences GetObject2 = HackAttemptsSharedPreferences.GetObject(context);
        ArrayList<HackAttemptEntity> GetHackAttemptObject = GetObject2.GetHackAttemptObject();
        hackAttemptEntitys = GetHackAttemptObject;
        if (GetHackAttemptObject == null) {
            ArrayList<HackAttemptEntity> arrayList = new ArrayList<>();
            hackAttemptEntitys = arrayList;
            arrayList.add(hackAttemptEntity);
        } else {
            GetHackAttemptObject.add(hackAttemptEntity);
        }
        GetObject2.SetHackAttemptObject(hackAttemptEntitys);
    }

    public void HackAttempt() {
        if (mCamera != null) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        boolean bool = true;
                        while (bool) {
                            if (SecurityLocksCommon.IsPreviewStarted) {
                                Camera.Parameters parameters = LoginActivity.mCamera.getParameters();
                                List<Camera.Size> supportedPreviewSizes = LoginActivity.mCamera.getParameters().getSupportedPreviewSizes();
                                Camera.Size size = supportedPreviewSizes.get(0);
                                for (int i = 1; i < supportedPreviewSizes.size(); i++) {
                                    if (supportedPreviewSizes.get(i).width * supportedPreviewSizes.get(i).height > size.width * size.height) {
                                        size = supportedPreviewSizes.get(i);
                                    }
                                }
                                for (Integer num : parameters.getSupportedPreviewFormats()) {
                                    if (num == 842094169) {
                                        parameters.setPreviewFormat(num);
                                    }
                                }
                                parameters.setPreviewSize(size.width, size.height);
                                parameters.setPictureSize(size.width, size.height);
                                LoginActivity.mCamera.setParameters(parameters);
                                LoginActivity.mCamera.takePicture(null, null, mPicture);
                                bool = false;
                            }
                        }
                    } catch (Exception e) {
                        Log.v("TakePicture Exception", e.toString());
                    }
                }
            }.start();
        }
    }

    public void initCamera(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager.hasSystemFeature("android.hardware.camera")) {
                if (Camera.getNumberOfCameras() == 2) {
                    mCamera = Camera.open(1);
                } else if (Camera.getNumberOfCameras() == 1) {
                    mCamera = Camera.open(0);
                }
                if (mCamera != null) {
                    mCameraPreview = new CameraPreview(context, mCamera);
                    ((FrameLayout) findViewById(R.id.camera_preview)).addView(mCameraPreview);
                    SecurityLocksCommon.IsPreviewStarted = true;
                }
            }
        } catch (Exception unused) {
            SecurityLocksCommon.IsPreviewStarted = false;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initCamera(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_login);

        txtPassword = findViewById(R.id.txtPassword);
        txtforgotpassword = findViewById(R.id.txtforgotpassword);
        txt_wrong_password_pin = findViewById(R.id.txt_wrong_password_pin);
        tv_forgot = findViewById(R.id.tv_forgot);
        txt_wrong_pttern = findViewById(R.id.txt_wrong_pttern);
        txtforgotpattern = findViewById(R.id.lblforgotpattern);

        SecurityLocksCommon.IsAppDeactive = true;
        securityLocksSharedPreferences = SecurityLocksSharedPreferences.GetObject(this);
        LoginOption = securityLocksSharedPreferences.GetLoginType();
        mypass = securityLocksSharedPreferences.GetSecurityCredential();
        StorageOptionsCommon.STORAGEPATH = StorageOptionSharedPreferences.GetObject(this).GetStoragePath();
        SecurityLocksCommon.PatternPassword = securityLocksSharedPreferences.GetSecurityCredential();
        Utilities.CheckDeviceStoragePaths(this);
        Common.initImageLoader(this);

        new Thread() {
            @Override
            public void run() {
                Utilities.changeFileExtention(StorageOptionsCommon.VIDEOS);
                Utilities.changeFileExtention(StorageOptionsCommon.DOCUMENTS);
            }
        }.start();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        PanicSwitchSharedPreferences panicSwitchSharedPreferences = PanicSwitchSharedPreferences.GetObject(this);
        PanicSwitchCommon.IsFlickOn = panicSwitchSharedPreferences.GetIsFlickOn();
        PanicSwitchCommon.IsShakeOn = panicSwitchSharedPreferences.GetIsShakeOn();
        PanicSwitchCommon.IsPalmOnFaceOn = panicSwitchSharedPreferences.GetIsPalmOnScreenOn();
        PanicSwitchCommon.SwitchingApp = panicSwitchSharedPreferences.GetSwitchApp();

        if (securityLocksSharedPreferences.GetFingerprint()) {
            fingerprintmethod();
        }
        if (securityLocksSharedPreferences.GetIsFirstLogin()) {
            SecurityLocksCommon.IsFirstLogin = true;
            SecurityLocksCommon.IsAppDeactive = false;
            startActivity(new Intent(this, CalculatorPinSetting.class));
            finish();
        } else if (SecurityLocksCommon.LoginOptions.Calculator.toString().equals(LoginOption)) {
            SecurityLocksCommon.IsAppDeactive = false;
            startActivity(new Intent(this, MyCalculatorActivity.class));
            finish();
        } else if (SecurityLocksCommon.LoginOptions.Pattern.toString().equals(LoginOption)) {
            setContentView(R.layout.activity_pattern_login);
            txt_wrong_pttern.setVisibility(View.INVISIBLE);
            ConfirmLockPatternViewLogin confirmLockPatternViewLogin = findViewById(R.id.pattern_view);
            txtforgotpattern.setVisibility(View.VISIBLE);
            confirmLockPatternViewLogin.setPracticeMode(true);
            confirmLockPatternViewLogin.invalidate();

            txtforgotpattern.setOnClickListener(view -> {
                if (!Utilities.isNetworkOnline(LoginActivity.this)) {
                    LoginActivity.txt_wrong_pttern.setVisibility(View.VISIBLE);
                    LoginActivity.txt_wrong_pttern.setText(R.string.toast_connection_error);
                } else if (securityLocksSharedPreferences.GetSecurityCredential().length() <= 0 || securityLocksSharedPreferences.GetEmail().length() <= 0) {
                    LoginActivity.txt_wrong_pttern.setVisibility(View.VISIBLE);
                    LoginActivity.txt_wrong_pttern.setText(R.string.toast_forgot_recovery_fail_Pattern);
                } else {
                    new MyAsyncTask().execute(mypass, securityLocksSharedPreferences.GetEmail(), LoginOption);
                    Toast.makeText(LoginActivity.this, R.string.toast_forgot_recovery_Success_Pattern, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            setContentView(R.layout.activity_login);
            getWindow().setSoftInputMode(5);

            txtPassword.setTextColor(getResources().getColor(R.color.ColorWhite));
            txt_wrong_password_pin.setVisibility(View.INVISIBLE);
            tv_forgot.setVisibility(View.INVISIBLE);
            txtforgotpassword.setVisibility(View.VISIBLE);

            txtPassword.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable editable) {
                }

                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    txt_wrong_password_pin.setText(getString(R.string.lbl_Enter_password));
                    if (txtPassword.length() >= 4 && securityLocksSharedPreferences.GetSecurityCredential().equals(txtPassword.getText().toString())) {
                        SignIn();
                    }
                }
            });

            txtPassword.setOnEditorActionListener((textView5, i, keyEvent) -> {
                if (i != 6) {
                    return true;
                }
                SignIn();
                return true;
            });

            txtforgotpassword.setOnClickListener(view -> {
                if (!Utilities.isNetworkOnline(LoginActivity.this)) {
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
                    tv_forgot.setVisibility(View.VISIBLE);
                    tv_forgot.setText(R.string.toast_connection_error);
                } else if (securityLocksSharedPreferences.GetSecurityCredential().length() <= 0 || securityLocksSharedPreferences.GetEmail().length() <= 0) {
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
                    if (SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption)) {
                        tv_forgot.setVisibility(View.VISIBLE);
                        tv_forgot.setText(R.string.toast_forgot_recovery_fail_Pin);
                        return;
                    }
                    tv_forgot.setVisibility(View.VISIBLE);
                    tv_forgot.setText(R.string.toast_forgot_recovery_fail_Password);
                } else {
                    new MyAsyncTask().execute(mypass, securityLocksSharedPreferences.GetEmail(), LoginOption);
                    if (SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption)) {
                        Toast.makeText(LoginActivity.this, R.string.toast_forgot_recovery_Success_Pin, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, R.string.toast_forgot_recovery_Success_Password, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            if (securityLocksSharedPreferences.GetSecurityCredential().length() == 0) {
                if (SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption)) {
                    txtPassword.setHint(R.string.lblsetting_SecurityCredentials_SetyourPin);
                } else {
                    txtPassword.setHint(R.string.lblsetting_SecurityCredentials_SetyourPassword);
                }
            }
            if (SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption)) {
                txtforgotpassword.setText(R.string.lbl_Forgot_pin);
                txtPassword.setHint(R.string.lbl_Enter_pin);
                txtPassword.setInputType(2);
                txtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
        hackAttemptCount = Common.HackAttemptCount;
        CheckHackAttemptCount(false);
        String string = getSharedPreferences("whatsnew", 0).getString("AppVersion", "");
        if (!securityLocksSharedPreferences.GetIconChanged()) {
            Objects.equals(string, "");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void fingerprintmethod() {
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(Context.FINGERPRINT_SERVICE);
        if (!fingerprintManager.isHardwareDetected()) {
            printtoast("Your device doesn't support fingerprint authentication");
        }
        if (ActivityCompat.checkSelfPermission(this, "android.permission.USE_FINGERPRINT") != 0) {
            printtoast("Please enable the fingerprint permission");
        }
        if (!fingerprintManager.hasEnrolledFingerprints()) {
            printtoast("No fingerprint configured. Please register at least one fingerprint in your device's Settings");
        }
        if (!keyguardManager.isKeyguardSecure()) {
            printtoast("Please enable lockscreen security in your device's Settings");
            return;
        }
        try {
            generateKey();
        } catch (FingerprintException e) {
            e.printStackTrace();
        }
        if (initCipher()) {
            FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
            new FingerprintHandler(this).startAuth(fingerprintManager, cryptoObject);
        }
    }

    private void printtoast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void generateKey() throws FingerprintException {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES", "AndroidKeyStore");
            keyStore.load(null);
            keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME, 3).setBlockModes(KeyProperties.BLOCK_MODE_CBC).setUserAuthenticationRequired(true).setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7).build());
            keyGenerator.generateKey();
        } catch (IOException | InvalidAlgorithmParameterException | KeyStoreException | NoSuchAlgorithmException | NoSuchProviderException | CertificateException e) {
            e.printStackTrace();
            throw new FingerprintException(e);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean initCipher() {
        Throwable e;
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            try {
                keyStore.load(null);
                cipher.init(1, keyStore.getKey(KEY_NAME, null));
                return true;
            } catch (KeyPermanentlyInvalidatedException unused) {
                return false;
            } catch (IOException | InvalidKeyException | KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException | CertificateException e2) {
                e = e2;
                throw new RuntimeException("Failed to init Cipher", e);
            }
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e8) {
            throw new RuntimeException("Failed to get Cipher", e8);
        }
    }

    public void SignIn() {
        if (txtPassword.getText().toString().length() > 0) {
            securityLocksSharedPreferences.GetEmail();
            if (securityLocksSharedPreferences.GetSecurityCredential().equals(txtPassword.getText().toString())) {
                SecurityLocksCommon.IsAppDeactive = false;
                Common.HackAttemptCount = 0;
                SecurityLocksCommon.IsFakeAccount = 0;
                Common.loginCount = securityLocksSharedPreferences.GetRateCount();
                Common.loginCount++;
                securityLocksSharedPreferences.SetRateCount(Common.loginCount);
                SecurityLocksCommon.IsnewloginforAd = true;
                SecurityLocksCommon.IsFakeAccount = 0;
                Intent intent = new Intent(this, ActivityMain.class);
                overridePendingTransition(17432576, 17432577);
                startActivity(intent);
                finish();
            } else if (securityLocksSharedPreferences.GetDecoySecurityCredential().equals(txtPassword.getText().toString())) {
                SecurityLocksCommon.IsAppDeactive = false;
                Common.HackAttemptCount = 0;
                Common.loginCount = securityLocksSharedPreferences.GetRateCount();
                Common.loginCount++;
                securityLocksSharedPreferences.SetRateCount(Common.loginCount);
                SecurityLocksCommon.IsFakeAccount = 1;
                Intent intent3 = new Intent(this, ActivityMain.class);
                overridePendingTransition(17432576, 17432577);
                startActivity(intent3);
                finish();
            } else {
                int i = hackAttemptCount + 1;
                hackAttemptCount = i;
                Common.HackAttemptCount = i;
                HackAttempt();
                wrongPassword = txtPassword.getText().toString();
                txtPassword.setText("");
                txt_wrong_password_pin.setVisibility(View.VISIBLE);
                if (SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption)) {
                    txt_wrong_password_pin.setText(R.string.lblsetting_SecurityCredentials_Setpin_Tryagain);
                } else {
                    txt_wrong_password_pin.setText(R.string.lblsetting_SecurityCredentials_Setpasword_Tryagain);
                }
                CheckHackAttemptCount(true);
            }
        } else if (SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption)) {
            txt_wrong_password_pin.setText(R.string.lblsetting_SecurityCredentials_Setpin_Tryagain);
        } else {
            txt_wrong_password_pin.setText(R.string.lblsetting_SecurityCredentials_Setpasword_Tryagain);
        }
    }

    private void CheckHackAttemptCount(boolean z) {
        if (z && hackAttemptCount == 3) {
            Common.IsStart = true;
        }
        if (hackAttemptCount == 3) {
            TimerStart();
        }
    }

    private void TimerStart() {
        if (Common.IsStart) {
            Calendar instance = Calendar.getInstance();
            instance.add(13, 30);
            instance.getTimeInMillis();
            startTime = instance.getTimeInMillis();
            Common.IsStart = false;
        }
        customHandler.postDelayed(updateTimerThread, 0);
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
    public void onStop() {
        super.onStop();
        if (!SecurityLocksCommon.LoginOptions.Pattern.toString().equals(LoginOption)) {
            txtPassword.setText("");
        }
    }

    @Override
    public void onPause() {
        sensorManager.unregisterListener(this);
        if (AccelerometerManager.isListening()) {
            AccelerometerManager.stopListening();
        }
        if (mCamera != null) {
            mCamera.cancelAutoFocus();
            mCamera.stopPreview();
            mCamera.release();
            mCameraPreview = null;
            mCamera = null;
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
            finishAffinity();
        }
        return super.onKeyDown(i, keyEvent);
    }

    public static class FingerprintException extends Exception {
        public FingerprintException(Exception exc) {
            super(exc);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class MyAsyncTask extends AsyncTask<String, Integer, Double> {
        private MyAsyncTask() {

        }

        public void onProgressUpdate(Integer... numArr) {
        }

        public Double doInBackground(String... strArr) {
            postData(strArr[0], strArr[1], strArr[2]);
            return null;
        }

        public void onPostExecute(Double d) {
            if (SecurityLocksCommon.LoginOptions.Password.toString().equals(LoginOption)) {
                Toast.makeText(LoginActivity.this, R.string.toast_forgot_recovery_Success_Password_sent, Toast.LENGTH_LONG).show();
            } else if (SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption)) {
                Toast.makeText(LoginActivity.this, R.string.toast_forgot_recovery_Success_Pin_sent, Toast.LENGTH_LONG).show();
            } else if (SecurityLocksCommon.LoginOptions.Pattern.toString().equals(LoginOption)) {
                Toast.makeText(LoginActivity.this, R.string.toast_forgot_recovery_Success_Pattern_sent, Toast.LENGTH_LONG).show();
            }
        }

        public void postData(String str, String str2, String str3) {
            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(SecurityLocksCommon.ServerAddress);
            try {
                ArrayList<NameValuePair> arrayList = new ArrayList<>(3);
                arrayList.add(new BasicNameValuePair("AppType", "Android"));
                arrayList.add(new BasicNameValuePair("Email", str2));
                arrayList.add(new BasicNameValuePair("Pass", str));
                arrayList.add(new BasicNameValuePair("PassType", str3));
                httpPost.setEntity(new UrlEncodedFormEntity(arrayList));
                inputStreamToString(defaultHttpClient.execute(httpPost).getEntity().getContent()).toString().equalsIgnoreCase("Successfully");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private StringBuilder inputStreamToString(InputStream inputStream) {
            String readLine = null;
            StringBuilder sb = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            while (true) {
                try {
                    readLine = bufferedReader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (readLine == null) {
                    return sb;
                }
                sb.append(readLine);
            }
        }
    }
}

package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Ads.Advertisement;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.R;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.calculator.Calculator;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.securitylocks.SecurityLocksCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.securitylocks.SecurityLocksSharedPreferences;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.Common;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.Utilities;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MyCalculatorActivity extends AppCompatActivity {
    public Calculator calculator;
    public TextView displayPrimary;
    public TextView displaySecondary;
    public HorizontalScrollView hsv;

    public SecurityLocksSharedPreferences securityCredentialsSharedPreferences;
    public StringBuilder builder = new StringBuilder();
    public StringBuilder compringString = new StringBuilder();

    public String LoginOption;
    public String myDecoyPass = "";
    public String mypass = "";
    public String mydefaultpass = "";
    public int counter = 0;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_my_calculator);

        SecurityLocksCommon.IsAppDeactive = true;
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(MyCalculatorActivity.this, ll_banner);

        securityCredentialsSharedPreferences = SecurityLocksSharedPreferences.GetObject(this);
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final SecurityLocksSharedPreferences GetObject = SecurityLocksSharedPreferences.GetObject(this);
        mypass = securityCredentialsSharedPreferences.GetSecurityCredential();
        mydefaultpass = securityCredentialsSharedPreferences.GetDefaultSecurityCredential();
        myDecoyPass = securityCredentialsSharedPreferences.GetDecoySecurityCredential();
        LoginOption = securityCredentialsSharedPreferences.GetLoginType();

        displayPrimary = findViewById(R.id.display_primary);
        displaySecondary = findViewById(R.id.display_secondary);
        hsv = findViewById(R.id.display_hsv);


        TextView[] textViewArr = {findViewById(R.id.button_0), findViewById(R.id.button_1), findViewById(R.id.button_2), findViewById(R.id.button_3), findViewById(R.id.button_4), findViewById(R.id.button_5), findViewById(R.id.button_6), findViewById(R.id.button_7), findViewById(R.id.button_8), findViewById(R.id.button_9)};
        int i = 0;
        for (int i2 = 10; i < i2; i2 = 10) {
            final String str = (String) textViewArr[i].getText();
            textViewArr[i].setOnClickListener(view -> {

                calculator.digit(str.charAt(0));
                compringString = builder;
                compringString.append(str.charAt(0));
                counter = 0;
                if (mydefaultpass.trim().equals(getText().trim())) {

                    SecurityLocksCommon.IsAppDeactive = false;
                    builder.setLength(0);
                    Intent intent = new Intent(MyCalculatorActivity.this, ActivityMain.class);
                    Common.loginCount++;
                    GetObject.SetRateCount(Common.loginCount);
                    startActivity(intent);
                    calculator.setText("");
                    finish();

                }
                if (mypass.trim().equals(getText().trim())) {
                    SecurityLocksCommon.IsAppDeactive = false;
                    builder.setLength(0);
                    Intent intent2 = new Intent(MyCalculatorActivity.this, ActivityMain.class);
                    Common.loginCount++;
                    GetObject.SetRateCount(Common.loginCount);
                    startActivity(intent2);
                    calculator.setText("");
                    finish();

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
                    counter = 0;
                    calculator.numOp('!');
                }
                if (str2.equals("π")) {
                    calculator.num((char) 960);
                }
                if (str2.equals("e")) {
                    calculator.num('e');
                }
                if (str2.equals("%")) {
                    calculator.numOp('%');
                }
                if (str2.equals("(")) {
                    calculator.parenthesisLeft();
                }
                if (str2.equals(")")) {
                    calculator.parenthesisRight();
                }
                if (str2.equals("√")) {
                    calculator.opNum((char) 8730);
                    counter = 0;
                }
                if (str2.equals("÷")) {
                    calculator.numOpNum('/');
                    counter = 0;
                }
                if (str2.equals("×")) {
                    counter = 0;
                    calculator.numOpNum('*');
                }
                if (str2.equals("−")) {
                    counter = 0;
                    calculator.numOpNum('-');
                }
                if (str2.equals("+")) {
                    calculator.numOpNum('+');
                    counter++;
                    if (counter == 5) {
                        if (!Utilities.isNetworkOnline(MyCalculatorActivity.this)) {
                            Toast.makeText(MyCalculatorActivity.this, R.string.toast_connection_error, Toast.LENGTH_SHORT).show();
                        } else if (securityCredentialsSharedPreferences.GetSecurityCredential().length() <= 0 || securityCredentialsSharedPreferences.GetEmail().length() <= 0) {
                            Toast.makeText(MyCalculatorActivity.this, R.string.toast_forgot_recovery_fail_Pattern, Toast.LENGTH_SHORT).show();
                        } else {
                            new MyAsyncTask().execute(mypass, securityCredentialsSharedPreferences.GetEmail(), "Pin");
                            Toast.makeText(MyCalculatorActivity.this, R.string.for_calculator_forgot_pin, Toast.LENGTH_SHORT).show();
                        }
                        counter = 0;
                    }
                }
                if (str2.equals(".")) {
                    counter = 0;
                    calculator.decimal();
                }
                if (str2.equals("=") && !getText().equals("")) {
                    calculator.equal();
                    counter = 0;
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
        calculator = new Calculator(this);
        if (bundle != null) {
            setText(bundle.getString("text"));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setText(getText());
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
                Toast.makeText(MyCalculatorActivity.this, R.string.toast_forgot_recovery_Success_Password_sent, Toast.LENGTH_LONG).show();
            } else if (SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption) || SecurityLocksCommon.LoginOptions.Calculator.toString().equals(LoginOption)) {
                Toast.makeText(MyCalculatorActivity.this, R.string.toast_forgot_recovery_Success_Pin, Toast.LENGTH_LONG).show();
            } else if (SecurityLocksCommon.LoginOptions.Pattern.toString().equals(LoginOption)) {
                Toast.makeText(MyCalculatorActivity.this, R.string.toast_forgot_recovery_Success_Pattern, Toast.LENGTH_LONG).show();
            }
        }

        public void postData(String str, String str2, String str3) {
            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(SecurityLocksCommon.ServerAddress);
            try {
                ArrayList<NameValuePair> arrayList = new ArrayList<>(3);
                arrayList.add(new BasicNameValuePair("AppType", "ACGV - Android"));
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

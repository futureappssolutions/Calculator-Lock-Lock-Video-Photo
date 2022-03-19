package com.calculator.vaultlocker.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.calculator.vaultlocker.Ads.GoogleAds;
import com.calculator.vaultlocker.R;
import com.calculator.vaultlocker.common.Preferences;

public class ActivitySplash extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Preferences.setGoogle_banner("ca-app-pub-3940256099942544/6300978111");
        Preferences.setGoogle_full("ca-app-pub-3940256099942544/1033173712");
        Preferences.setGoogle_native("ca-app-pub-3940256099942544/2247696110");
        Preferences.setGoogle_open("ca-app-pub-3940256099942544/3419835294");

        Preferences.setAppLovin_banner("68adcaa71fe97e91");
        Preferences.setAppLovin_full("bee398d5d02dbfd5");
        Preferences.setAppLovin_native("f44f76ad69d89e87");

        Preferences.setAds_time("1");
        Preferences.setAds_name("g");

        Preferences.setRemove_ads_weekly("weekly_key");
        Preferences.setRemove_ads_monthly("monthly_key");
        Preferences.setRemove_ads_yearly("yearly_key");
        Preferences.setBase_key("base_key");

        try {
            GoogleAds.allcount60 = new CountDownTimer(Integer.parseInt(Preferences.getAds_time()) * 1000L, 1000) {
                public void onTick(long millisUntilFinished) {
                    GoogleAds.adsdisplay = false;
                }

                public void onFinish() {
                    GoogleAds.adsdisplay = true;
                }
            };
            GoogleAds.allcount60.start();
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }

        GoogleAds.preLoadAds(ActivitySplash.this);

        new Handler().postDelayed(() -> {
            startActivity(new Intent(ActivitySplash.this, LoginActivity.class));
            finish();
        }, 3500);
    }
}

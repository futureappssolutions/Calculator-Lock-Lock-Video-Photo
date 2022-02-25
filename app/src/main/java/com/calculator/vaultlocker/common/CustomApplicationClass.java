package com.calculator.vaultlocker.common;

import android.app.Application;

import com.applovin.sdk.AppLovinSdk;
import com.calculator.vaultlocker.Ads.AppOpenManager;
import com.google.android.gms.ads.MobileAds;


public class CustomApplicationClass extends Application {
    public static CustomApplicationClass application;
    AppOpenManager appOpenManager;

    public CustomApplicationClass() {
        application = this;
    }

    public static CustomApplicationClass getApp() {
        if (application == null) {
            application = new CustomApplicationClass();
        }
        return application;
    }
    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();

        application = this;

        AppLovinSdk.getInstance(this).setMediationProvider("max");
        AppLovinSdk.initializeSdk(this, configuration -> {
            // AppLovin SDK is initialized, start loading ads
        });

        MobileAds.initialize(this, initializationStatus -> {
        });

        appOpenManager = new AppOpenManager(this);
    }
}

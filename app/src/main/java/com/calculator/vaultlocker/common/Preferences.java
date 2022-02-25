package com.calculator.vaultlocker.common;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    private static final String remove_ads_weekly = "remove_ads_weekly";
    private static final String remove_ads_monthly = "remove_ads_monthly";
    private static final String remove_ads_yearly = "remove_ads_yearly";
    private static final String active_Weekly = "weekly_key";
    private static final String active_Monthly = "monthly_key";
    private static final String active_Yearly = "yearly_key";
    private static final String base_key = "base_key";

    //Advertisement Key
    private static final String Google_full = "g_full";
    private static final String Google_banner = "g_banner";
    private static final String Google_native = "g_native";
    private static final String Google_open = "g_app_open";
    private static final String AppLovin_full = "AppLovin_full";
    private static final String AppLovin_banner = "AppLovin_banner";
    private static final String AppLovin_native = "AppLovin_native";
    private static final String Ads_time = "ads_time";
    private static final String Ads_name = "Ads_name";

    private static SharedPreferences get() {
        return CustomApplicationClass.getApp().getSharedPreferences("Application", Context.MODE_PRIVATE);
    }

    public static String getRemove_ads_weekly() {
        return get().getString(remove_ads_weekly, "");
    }

    public static void setRemove_ads_weekly(String value) {
        get().edit().putString(remove_ads_weekly, value).apply();
    }

    public static String getRemove_ads_monthly() {
        return get().getString(remove_ads_monthly, "");
    }

    public static void setRemove_ads_monthly(String value) {
        get().edit().putString(remove_ads_monthly, value).apply();
    }

    public static String getRemove_ads_yearly() {
        return get().getString(remove_ads_yearly, "");
    }

    public static void setRemove_ads_yearly(String value) {
        get().edit().putString(remove_ads_yearly, value).apply();
    }

    public static String getGoogle_banner() {
        return get().getString(Google_banner, "");
    }

    public static void setGoogle_banner(String value) {
        get().edit().putString(Google_banner, value).apply();
    }

    public static String getGoogle_full() {
        return get().getString(Google_full, "");
    }

    public static void setGoogle_full(String value) {
        get().edit().putString(Google_full, value).apply();
    }

    public static String getGoogle_native() {
        return get().getString(Google_native, "");
    }

    public static void setGoogle_native(String value) {
        get().edit().putString(Google_native, value).apply();
    }

    public static String getGoogle_open() {
        return get().getString(Google_open, "");
    }

    public static void setGoogle_open(String value) {
        get().edit().putString(Google_open, value).apply();
    }

    public static String getAds_time() {
        return get().getString(Ads_time, "");
    }

    public static void setAds_time(String value) {
        get().edit().putString(Ads_time, value).apply();
    }

    public static String getAds_name() {
        return get().getString(Ads_name, "g");
    }

    public static void setAds_name(String value) {
        get().edit().putString(Ads_name, value).apply();
    }

    public static String getActive_Weekly() {
        return get().getString(active_Weekly, "");
    }

    public static void setActive_Weekly(String value) {
        get().edit().putString(active_Weekly, value).apply();
    }

    public static String getActive_Monthly() {
        return get().getString(active_Monthly, "");
    }

    public static void setActive_Monthly(String value) {
        get().edit().putString(active_Monthly, value).apply();
    }

    public static String getActive_Yearly() {
        return get().getString(active_Yearly, "");
    }

    public static void setActive_Yearly(String value) {
        get().edit().putString(active_Yearly, value).apply();
    }

    public static String getBase_key() {
        return get().getString(base_key, "");
    }

    public static void setBase_key(String value) {
        get().edit().putString(base_key, value).apply();
    }

    public static String getAppLovin_full() {
        return get().getString(AppLovin_full, "");
    }

    public static void setAppLovin_full(String value) {
        get().edit().putString(AppLovin_full, value).apply();
    }

    public static String getAppLovin_banner() {
        return get().getString(AppLovin_banner, "");
    }

    public static void setAppLovin_banner(String value) {
        get().edit().putString(AppLovin_banner, value).apply();
    }

    public static String getAppLovin_native() {
        return get().getString(AppLovin_native, "");
    }

    public static void setAppLovin_native(String value) {
        get().edit().putString(AppLovin_native, value).apply();
    }
}

package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.common;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    private static final String remove_ads_weekly = "remove_ads_weekly";
    private static final String remove_ads_monthly = "remove_ads_monthly";
    private static final String remove_ads_yearly = "remove_ads_yearly";
    private static final String active_Weekly = "weekly_key";
    private static final String active_Monthly = "monthly_key";
    private static final String active_Yearly = "yearly_key";


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
}

package com.calculator.vaultlocker.hackattempt;

import android.content.Context;
import android.content.SharedPreferences;

import com.calculator.vaultlocker.Model.HackAttemptEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class HackAttemptsSharedPreferences {
    static Context context;
    static SharedPreferences myPrefs;
    private static final String _fileName = "HackAttempts";
    private static HackAttemptsSharedPreferences hackAttemptsSharedPreferences;

    private HackAttemptsSharedPreferences() {
    }

    public static HackAttemptsSharedPreferences GetObject(Context context2) {
        if (hackAttemptsSharedPreferences == null) {
            hackAttemptsSharedPreferences = new HackAttemptsSharedPreferences();
        }
        context = context2;
        myPrefs = context2.getSharedPreferences(_fileName, 0);
        return hackAttemptsSharedPreferences;
    }

    public void SetHackAttemptObject(ArrayList<HackAttemptEntity> arrayList) {
        SharedPreferences.Editor edit = myPrefs.edit();
        edit.putString("HackAttemptObject", new Gson().toJson(arrayList));
        edit.apply();
    }

    public ArrayList<HackAttemptEntity> GetHackAttemptObject() {
        new Gson();
        return new Gson().fromJson(myPrefs.getString("HackAttemptObject", ""), new TypeToken<ArrayList<HackAttemptEntity>>() {
        }.getType());
    }
}

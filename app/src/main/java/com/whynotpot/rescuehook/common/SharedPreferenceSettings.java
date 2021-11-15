package com.whynotpot.rescuehook.common;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceSettings {
    private SharedPreferences sharedPreferences;
    private final static String APP_PREFERENCES = "mySettings";
    private final static String APP_PREFERENCES_COINS = "coins";
    private final static String APP_PREFERENCES_TIME = "time";
    private final static String APP_PREFERENCE_FIRST_BOOT = "firstBoot";

    public SharedPreferenceSettings(Context context) {
        sharedPreferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public void saveTime(final int time) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(APP_PREFERENCES_TIME, time);
        editor.apply();
    }

    public void saveCoins(final int coins) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(APP_PREFERENCES_COINS, coins + getCoins());
        editor.apply();
    }

    public boolean getFirstBoot() {
        if (sharedPreferences.contains(APP_PREFERENCE_FIRST_BOOT)) {
            return sharedPreferences.getBoolean(APP_PREFERENCE_FIRST_BOOT, false);
        }
        return false;
    }

    public void setFirstBoot() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(APP_PREFERENCE_FIRST_BOOT, true);
        editor.apply();
    }

    public int getTime() {
        if (sharedPreferences.contains(APP_PREFERENCES_TIME)) {
            return sharedPreferences.getInt(APP_PREFERENCES_TIME, 1);
        }
        return 1;

    }

    public int getCoins() {
        if (sharedPreferences.contains(APP_PREFERENCES_COINS)) {
            return sharedPreferences.getInt(APP_PREFERENCES_COINS, 0);
        }
        return 0;
    }
}
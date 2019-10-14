package io.virtualapp.store;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.virtualapp.VApp;

public class StateStorage {
    public static String PREFIX_PREFERENCES = "configs";

    private static StateStorage sInst = new StateStorage();

    public static StateStorage inst() {
        return sInst;
    }
    private SharedPreferences pref;

    public StateStorage() {
        this(PREFIX_PREFERENCES);
    }

    private StateStorage(String prefix) {
        pref = VApp.getApp().getSharedPreferences(prefix, Context.MODE_PRIVATE);
    }

    public boolean getBoolean(String key) {
        return pref.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return pref.getBoolean(key, defValue);
    }

    public boolean contains(String key) {
        return pref.contains(key);
    }

    public float getFloat(String key, float defValue) {
        return pref.getFloat(key, defValue);
    }

    public float getFloat(String key) {
        return pref.getFloat(key, 1.0f);
    }

    public long getLong(String key, long defValue) {
        return pref.getLong(key, defValue);
    }

    public long getLong(String key) {
        return pref.getLong(key, 0l);
    }

    public int getInt(String key, int defValue) {
        return pref.getInt(key, defValue);
    }

    public int getInt(String key) {
        return pref.getInt(key, 0);
    }

    public String getString(String key, @Nullable String defValue) {
        return pref.getString(key, defValue);
    }

    public String getString(String key) {
        return pref.getString(key, "");
    }

    public Set<String> getStringSet(String key, @Nullable Set<String> defValues) {
        return pref.getStringSet(key, defValues);
    }

    public Map<String, ?> getAll() {
        return pref.getAll();
    }

    public SharedPreferences.Editor edit() {
        return pref.edit();
    }

    public Set<String> getPrefSet(final String key) {
        return pref.getStringSet(key, new HashSet<String>());
    }

    public void setPrefSet(final String key, final Set<String> values) {
        SharedPreferences.Editor edit = pref.edit();
        edit.putStringSet(key, values);
        edit.apply();
    }

    public void setBoolean(final String key, final boolean value) {
        SharedPreferences.Editor edit = pref.edit();
        edit.putBoolean(key, value);
        edit.apply();
    }

    public void setInt(final String key, final int value) {
        SharedPreferences.Editor edit = pref.edit();
        edit.putInt(key, value);
        edit.apply();
    }

    public void setLong(final String key, final long value) {
        SharedPreferences.Editor edit = pref.edit();
        edit.putLong(key, value);
        edit.apply();
    }

    public void setSet(final String key, final Set<String> values) {
        SharedPreferences.Editor edit = pref.edit();
        edit.putStringSet(key, values);
        edit.apply();
    }

    public void setString(final String key, @NonNull final String value) {
        SharedPreferences.Editor edit = pref.edit();
        edit.putString(key, value);
        edit.apply();
    }

    public void remove(String key){
        SharedPreferences.Editor edit = pref.edit();
        edit.remove(key);
        edit.apply();
    }

    public void sync() {
        SharedPreferences.Editor edit = pref.edit();
        edit.commit();
    }
}

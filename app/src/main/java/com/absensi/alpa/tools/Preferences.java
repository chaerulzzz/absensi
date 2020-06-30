package com.absensi.alpa.tools;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Preferences {

    private static final transient String TAG = Preferences.class.getSimpleName();

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor mEditor;
    private static Preferences INSTANCE;

    private Preferences() {
    }

    public static Preferences getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Preferences();
        }

        return INSTANCE;
    }

    @SuppressLint("CommitPrefEdits")
    public void begin() {
        if (this.mEditor == null) {
            this.mEditor = this.sharedPreferences.edit();
        }
    }

    public <T> void put(Constant.CREDENTIALS key, @NonNull T mValue) {
        if (this.mEditor == null) {
            Log.e(TAG, "You must call begin() first.");
            return;
        }

        if (mValue instanceof String) {
            this.mEditor.putString(sharedPreferences.toString(), (String) mValue);
        } else if (mValue instanceof Integer) {
            this.mEditor.putInt(sharedPreferences.toString(), (Integer) mValue);
        } else if (mValue instanceof Float) {
            this.mEditor.putFloat(sharedPreferences.toString(), (Float) mValue);
        } else if (mValue instanceof Long) {
            this.mEditor.putLong(sharedPreferences.toString(), (Long) mValue);
        } else if (mValue instanceof Boolean) {
            this.mEditor.putBoolean(sharedPreferences.toString(), (Boolean) mValue);
        } else {
            Log.e(TAG, "Unsupported mValue type.");
        }
    }

    public void commit() {
        if (this.mEditor == null) {
            Log.e(TAG, "You must call begin() first.");
            return;
        }
        this.mEditor.apply();
    }

    public void setSharedPreferences(SharedPreferences mSharedPreferences) {
        this.sharedPreferences = mSharedPreferences;
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("unchecked")
    public <T> T getValue(String key, Class<T> mClass, T mDefaultValue) {
        if (Objects.equals(String.class, mClass)) {
            return (T) this.sharedPreferences.getString(key, mDefaultValue != null ? (String) mDefaultValue : null);
        } else if (Objects.equals(Integer.class, mClass)) {
            return (T) Integer.valueOf(this.sharedPreferences.getInt(key, mDefaultValue != null ? (Integer) mDefaultValue : -9999));
        } else if (Objects.equals(Float.class, mClass)) {
            return (T) Float.valueOf(this.sharedPreferences.getFloat(key, mDefaultValue != null ? (Float) mDefaultValue : -9999F));
        } else if (Objects.equals(Long.class, mClass)) {
            return (T) Long.valueOf(this.sharedPreferences.getLong(key, mDefaultValue != null ? (Long) mDefaultValue : -9999L));
        } else if (Objects.equals(Boolean.class, mClass)) {
            return (T) Boolean.valueOf(this.sharedPreferences.getBoolean(key, mDefaultValue != null && (Boolean) mDefaultValue));
        } else {
            return null;
        }
    }

    public <T> T getValue(String key, Class<T> mClass) {
        return getValue(key, mClass, null);
    }

    public <T> T getValue(Constant.CREDENTIALS mSharedPreferenceKey, Class<T> mClass, T mDefaultValue) {
        return getValue(mSharedPreferenceKey.toString(), mClass, mDefaultValue);
    }

    public <T> T getValue(Constant.CREDENTIALS mSharedPreferenceKey, Class<T> mClass) {
        return getValue(mSharedPreferenceKey, mClass, null);
    }
}

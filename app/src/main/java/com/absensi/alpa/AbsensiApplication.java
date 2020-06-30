package com.absensi.alpa;

import android.app.Application;
import android.content.Context;

import com.absensi.alpa.tools.Preferences;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AbsensiApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        Preferences.getInstance().setSharedPreferences(this.getSharedPreferences("absensi", Context.MODE_PRIVATE));
        RealmConfiguration mConfiguration = new RealmConfiguration.Builder()
                .name("absensi.realm")
                .build();
        Realm.setDefaultConfiguration(mConfiguration);
    }
}

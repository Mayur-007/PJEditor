package com.freedom.pjeditor;

import android.app.Application;

import com.freedom.pjeditor.realm.RealmController;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Mayur on 6/27/2017.
 */

public class PJEditorApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(getApplicationContext());
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
        RealmController.with(this);
    }
}
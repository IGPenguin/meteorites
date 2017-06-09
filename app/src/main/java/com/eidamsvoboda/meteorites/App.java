package com.eidamsvoboda.meteorites;

import android.app.Application;

import com.orhanobut.hawk.Hawk;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by eidamsvoboda on 05/06/2017.
 */

public class App extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		Realm.init(this);
		RealmConfiguration realmConfig = new RealmConfiguration.Builder()
				.deleteRealmIfMigrationNeeded()
				.build();
		Realm.setDefaultConfiguration(realmConfig);
		Hawk.init(this).build();
	}
}

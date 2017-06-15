package com.eidamsvoboda.meteorites.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.eidamsvoboda.meteorites.tools.DataManager;

import io.realm.Realm;

/**
 * Created by eidamsvoboda on 09/06/2017.
 */

public class SyncBroadcastReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(final Context context, Intent intent) { // This is called every time broadcast is received, if its from boot - skip database update (only schedule next sync)
		if (!(intent.getAction() != null && intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))) {
			Realm realm = Realm.getDefaultInstance();
			DataManager.syncMeteorites(realm, new SyncCallback(context));
		} else {
			SyncScheduler.scheduleSync(context);
		}
	}
}

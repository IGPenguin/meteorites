package com.eidamsvoboda.meteorites;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import io.realm.Realm;

/**
 * Created by eidamsvoboda on 09/06/2017.
 */

public class SyncBroadcastReceiver extends BroadcastReceiver {
	@Override public void onReceive(final Context context, Intent intent) { // This is called every time broadcast is received, if its from boot - skip database update (only schedule next sync)
		if (!(intent.getAction() != null && intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))) {
			Realm realm = Realm.getDefaultInstance();
			DataManager.syncMeteorites(realm, new DataManager.SyncCallback() {
				@Override public void onSyncSuccess() {
					DataManager.setLastSyncDate(System.currentTimeMillis());
					DataManager.setLastSyncResult(context.getString(R.string.toast_sync_succeeded));
				}

				@Override public void onSyncFailed() {
					DataManager.setLastSyncDate(System.currentTimeMillis());
					DataManager.setLastSyncResult(context.getString(R.string.toast_sync_failed));
				}
			});
		}
	}
}

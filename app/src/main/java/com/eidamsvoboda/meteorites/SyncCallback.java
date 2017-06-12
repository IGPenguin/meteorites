package com.eidamsvoboda.meteorites;

import android.content.Context;

/**
 * Created by eidamsvoboda on 12/06/2017.
 */

public class SyncCallback {
	Context context;

	SyncCallback(Context context) {
		this.context=context;
	}

	void onSyncSuccess(){
		DataManager.setLastSyncDate(System.currentTimeMillis());
		DataManager.setLastSyncResult(context.getString(R.string.toast_sync_succeeded));
	}

	void onSyncFailed(){
		DataManager.setLastSyncDate(System.currentTimeMillis());
		DataManager.setLastSyncResult(context.getString(R.string.toast_sync_failed));
	}
}
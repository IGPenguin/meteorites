package com.eidamsvoboda.meteorites.sync;

import android.content.Context;

import com.eidamsvoboda.meteorites.R;
import com.eidamsvoboda.meteorites.tools.DataManager;

/**
 * Created by eidamsvoboda on 12/06/2017.
 */

public class SyncCallback {
	Context context;

	public SyncCallback(Context context) {
		this.context=context;
	}

	public void onSyncSuccess() {
		DataManager.setLastSyncDate(System.currentTimeMillis());
		DataManager.setLastSyncResult(context.getString(R.string.settings_sync_succeeded));
	}

	public void onSyncFailed() {
		DataManager.setLastSyncDate(System.currentTimeMillis());
		DataManager.setLastSyncResult(context.getString(R.string.settings_sync_failed));
	}
}
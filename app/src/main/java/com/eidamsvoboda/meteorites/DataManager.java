package com.eidamsvoboda.meteorites;

import android.util.Log;

import com.orhanobut.hawk.Hawk;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by eidamsvoboda on 09/06/2017.
 */

public class DataManager {

	public static void syncMeteorites(final Realm realm, final SyncCallback syncCallback) {
		final RealmList<Meteorite> meteorites = new RealmList<>();

		Api.get().getMeteorites().enqueue(new Callback<List<Meteorite>>() {

			@Override
			public void onResponse(Call<List<Meteorite>> call, Response<List<Meteorite>> response) {
				if (response.isSuccessful()) {
					if (response.body() != null) {
						meteorites.addAll(response.body());
						realm.executeTransaction(new Realm.Transaction() {
							@Override
							public void execute(Realm realm) {
								realm.copyToRealmOrUpdate(meteorites);
								syncCallback.onSyncSuccess();
							}
						});
					} else {
						Log.w(getClass().getName(), "Response body is null");
						syncCallback.onSyncFailed();
					}
				} else {
					Log.w(getClass().getName(), "Response not successful: " + response.errorBody());
					syncCallback.onSyncFailed();
				}
			}

			@Override
			public void onFailure(Call<List<Meteorite>> call, Throwable t) {
				Log.w(getClass().getName(), "Api call failed: " + t);
				syncCallback.onSyncFailed();
			}
		});
	}

	public static void setSyncFrequency(int frequency) {
		Hawk.put(Constant.Settings.SYNC_FREQUENCY, frequency);
	}

	public static int getSyncFrequency() {
		return Hawk.get(Constant.Settings.SYNC_FREQUENCY, Constant.Settings.SYNC_FREQUENCY_DEFAULT);
	}

	public static void setLastSyncDate(long dateMillis) {
		Hawk.put(Constant.Settings.SYNC_LAST_DATE, dateMillis);
	}

	public static long getLastSyncDate() {
		return Hawk.get(Constant.Settings.SYNC_LAST_DATE, 0L);
	}

	public static void setLastSyncResult(String result) {
		Hawk.put(Constant.Settings.SYNC_LAST_RESULT, result);
	}

	public static String getLastSyncResult() {
		return Hawk.get(Constant.Settings.SYNC_LAST_RESULT, "n/a");
	}
}

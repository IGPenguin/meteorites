package com.eidamsvoboda.meteorites;

import android.util.Log;

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

	public interface SyncCallback {
		void onSyncSuccess();
		void onSyncFailed();
	}
}

package com.eidamsvoboda.meteorites.tools;

import android.util.Log;

import com.eidamsvoboda.meteorites.api.Api;
import com.eidamsvoboda.meteorites.model.Meteorite;
import com.eidamsvoboda.meteorites.sync.SyncCallback;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by eidamsvoboda on 09/06/2017.
 */

public class DataManager {

	public static void syncMeteorites(final Realm realm, final SyncCallback syncCallback) {
		syncCallback.onSyncStarted();
		Api.get().getMeteorites().enqueue(new Callback<List<Meteorite>>() {

			@Override
			public void onResponse(Call<List<Meteorite>> call, final Response<List<Meteorite>> response) {
				if (response.isSuccessful()) {
					if (response.body() != null) {
						realm.executeTransaction(new Realm.Transaction() {
							@Override
							public void execute(Realm realm) {
								realm.copyToRealmOrUpdate(response.body());
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

	public static int getSyncFrequency() {
		return Hawk.get(Constant.Settings.SYNC_FREQUENCY, Constant.Settings.SYNC_FREQUENCY_DEFAULT);
	}

	public static void setSyncFrequency(int frequency) {
		Hawk.put(Constant.Settings.SYNC_FREQUENCY, frequency);
	}

	public static long getLastSyncDate() {
		return Hawk.get(Constant.Settings.SYNC_LAST_DATE, 0L);
	}

	public static void setLastSyncDate(long dateMillis) {
		Hawk.put(Constant.Settings.SYNC_LAST_DATE, dateMillis);
	}

	public static String getLastSyncResult() {
		return Hawk.get(Constant.Settings.SYNC_LAST_RESULT, "n/a");
	}

	public static void setLastSyncResult(String result) {
		Hawk.put(Constant.Settings.SYNC_LAST_RESULT, result);
	}

	public static String getSortField() {
		return Hawk.get(Constant.Settings.SORT_FIELD, "mass");
	}

	public static void setSortField(String sortField) {
		Hawk.put(Constant.Settings.SORT_FIELD, sortField);
	}

	public static Sort getSortOrientation() {
		if (Hawk.get(Constant.Settings.SORT_ORIENTATION, false)) {
			return Sort.ASCENDING;
		} else {
			return Sort.DESCENDING;
		}
	}

	public static void setSortOrientation(Sort sort) {
		boolean ascending = (sort.equals(Sort.ASCENDING));
		Hawk.put(Constant.Settings.SORT_ORIENTATION, ascending);
	}

	public static List<Meteorite> getMeteoriteList(final Realm realm, SyncCallback syncCallback) {
		List<Meteorite> meteoriteList = new ArrayList<>();

		RealmResults<Meteorite> meteoriteRealmResults = realm.where(Meteorite.class)
				.findAllSorted(DataManager.getSortField().toLowerCase(), DataManager.getSortOrientation());

		if (meteoriteRealmResults.size() > 0) {
			meteoriteList.clear();
			meteoriteList.addAll(meteoriteRealmResults);

		} else {
			DataManager.syncMeteorites(realm, syncCallback);
		}

		return meteoriteList;
	}

}

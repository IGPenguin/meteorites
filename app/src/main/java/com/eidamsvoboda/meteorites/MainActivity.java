package com.eidamsvoboda.meteorites;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.security.ProviderInstaller;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity implements MeteoriteAdapter.RecyclerItemClickListener {

	@BindView(R.id.recycler) RecyclerView recyclerView;
	@BindView(R.id.errorTextView) TextView errorTextView;

	MeteoriteAdapter meteoriteAdapter;
	RealmList<Meteorite> meteoriteList = new RealmList<>();
	Realm realm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
		realm = Realm.getDefaultInstance();

		updateAndroidSecurityProvider();

		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(layoutManager);
		meteoriteAdapter = new MeteoriteAdapter(this, meteoriteList, this);
		recyclerView.setAdapter(meteoriteAdapter);

		SyncScheduler.scheduleSync(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		fillRecycler("mass", Sort.DESCENDING);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent settingsActivityIntent = new Intent(this, SettingsActivity.class);
		startActivity(settingsActivityIntent);
		return true;
	}

	@Override
	public void onRecyclerItemClick(Meteorite meteorite) {
		Intent mapActivityIntent = new Intent(this, MapActivity.class);
		mapActivityIntent.putExtra(Constant.Intent.METEORITE_NAME, meteorite.name);
		mapActivityIntent.putExtra(Constant.Intent.METEORITE_LAT, meteorite.reclat);
		mapActivityIntent.putExtra(Constant.Intent.METEORITE_LNG, meteorite.reclong);
		startActivity(mapActivityIntent);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		realm.close();
	}

	public void fillRecycler(final String sortKey, final Sort sortOrder) {
		RealmResults<Meteorite> meteoriteRealmResults = realm.where(Meteorite.class)
				.findAllSorted(sortKey, sortOrder); //Tabs ~ adjusting sort?

		if (meteoriteRealmResults.size() > 0) {
			meteoriteList.clear();
			meteoriteList.addAll(meteoriteRealmResults);
			errorTextView.setVisibility(View.GONE);
			recyclerView.setVisibility(View.VISIBLE);
			meteoriteAdapter.notifyDataSetChanged();
		} else {
			Toast.makeText(this, R.string.toast_sync_start, Toast.LENGTH_SHORT).show();
			DataManager.syncMeteorites(realm, new SyncCallback(this) {
				@Override
				public void onSyncSuccess() {
					super.onSyncSuccess();
					fillRecycler(sortKey, sortOrder);
				}

				@Override
				public void onSyncFailed() {
					super.onSyncFailed();
					Toast.makeText(MainActivity.this, R.string.toast_sync_failed, Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

	private void updateAndroidSecurityProvider() { // Fixes javax.net.ssl.SSLHandshakeException on android < 5.0
		try {
			ProviderInstaller.installIfNeeded(this);
		} catch (GooglePlayServicesRepairableException e) {
			GooglePlayServicesUtil.getErrorDialog(e.getConnectionStatusCode(), this, 0);
		} catch (GooglePlayServicesNotAvailableException e) {
			Log.e("SecurityException", "Google Play Services not available.");
		}
	}
}

package com.eidamsvoboda.meteorites;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(layoutManager);
		meteoriteAdapter = new MeteoriteAdapter(meteoriteList, this);
		recyclerView.setAdapter(meteoriteAdapter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		fillRecycler("mass",Sort.DESCENDING);
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
			Toast.makeText(this,"Local database is empty, syncing.",Toast.LENGTH_SHORT).show();
			DataManager.syncMeteorites(realm, new DataManager.SyncCallback() {
				@Override
				public void onSyncSuccess() {
					fillRecycler(sortKey,sortOrder);
				}

				@Override
				public void onSyncFailed() {
					Toast.makeText(MainActivity.this,"Sync failed, check your connection.",Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
}

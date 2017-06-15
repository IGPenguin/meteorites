package com.eidamsvoboda.meteorites.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.eidamsvoboda.meteorites.R;
import com.eidamsvoboda.meteorites.model.Meteorite;
import com.eidamsvoboda.meteorites.sync.SyncCallback;
import com.eidamsvoboda.meteorites.sync.SyncScheduler;
import com.eidamsvoboda.meteorites.tools.Constant;
import com.eidamsvoboda.meteorites.tools.DataManager;
import com.google.android.gms.security.ProviderInstaller;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity implements MeteoriteAdapter.RecyclerItemClickListener {

	@BindView(R.id.recycler) RecyclerView recyclerView;
	@BindView(R.id.errorTextView) TextView errorTextView;

	MeteoriteAdapter meteoriteAdapter;
	List<Meteorite> meteoriteList = new ArrayList<>();
	Realm realm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
		realm = Realm.getDefaultInstance();

		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(layoutManager);
		meteoriteAdapter = new MeteoriteAdapter(this, meteoriteList, this);
		recyclerView.setAdapter(meteoriteAdapter);

		SyncScheduler.scheduleSync(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		realm.close();
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateAndroidSecurityProvider();
		fillRecycler();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_settings:
				Intent settingsActivityIntent = new Intent(this, SettingsActivity.class);
				startActivity(settingsActivityIntent);
				break;
			case R.id.action_sort:
				if (realm.where(Meteorite.class).findAll().size() > 0) {
					showSortDialog();
				} else {
					Toast.makeText(MainActivity.this, R.string.toast_no_data, Toast.LENGTH_SHORT).show();
				}
				break;
		}
		return true;
	}

	@Override
	public void onRecyclerItemClick(Meteorite meteorite) {
		Intent mapActivityIntent = new Intent(this, MapActivity.class);
		mapActivityIntent.putExtra(Constant.Intent.METEORITE_NAME, meteorite.getName());
		mapActivityIntent.putExtra(Constant.Intent.METEORITE_LAT, meteorite.getReclat());
		mapActivityIntent.putExtra(Constant.Intent.METEORITE_LNG, meteorite.getReclong());
		startActivity(mapActivityIntent);
	}

	public void fillRecycler() {
		RealmResults<Meteorite> meteoriteRealmResults = realm.where(Meteorite.class)
				.findAllSorted(DataManager.getSortField().toLowerCase(), DataManager.getSortOrientation());

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
					fillRecycler();
				}

				@Override
				public void onSyncFailed() {
					super.onSyncFailed();
					Toast.makeText(MainActivity.this, R.string.toast_sync_failed, Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

	private void showSortDialog() {
		MaterialDialog dialog = new MaterialDialog.Builder(this)
				.title(R.string.main_sort_title)
				.positiveText(R.string.main_sort_apply)
				.negativeText(R.string.main_sort_cancel)
				.negativeColor(getResources().getColor(R.color.black))
				.customView(R.layout.view_sort, true)
				.onPositive(new MaterialDialog.SingleButtonCallback() {
					@Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
						Spinner fieldSpinner = (Spinner) dialog.getCustomView().findViewById(R.id.sortFieldSpinner);
						RadioButton radioAscending = (RadioButton) dialog.getCustomView().findViewById(R.id.radioAscending);
						if (radioAscending.isChecked()) {
							DataManager.setSortOrientation(Sort.ASCENDING);
						} else {
							DataManager.setSortOrientation(Sort.DESCENDING);
						}
						DataManager.setSortField(fieldSpinner.getSelectedItem().toString());
						fillRecycler();
					}
				})
				.cancelable(true)
				.build();

		Spinner fieldSpinner = (Spinner) dialog.getCustomView().findViewById(R.id.sortFieldSpinner);
		RadioButton radioAscending = (RadioButton) dialog.getCustomView().findViewById(R.id.radioAscending);
		RadioButton radioDescending = (RadioButton) dialog.getCustomView().findViewById(R.id.radioDescending);

		fieldSpinner.setSelection(((ArrayAdapter) fieldSpinner.getAdapter()).getPosition(DataManager.getSortField()));
		radioAscending.setChecked(DataManager.getSortOrientation() == Sort.ASCENDING);
		radioDescending.setChecked(DataManager.getSortOrientation() == Sort.DESCENDING);

		dialog.show();
	}

	private void updateAndroidSecurityProvider() { // Fixes javax.net.ssl.SSLHandshakeException on android < 5.0
		try {
			ProviderInstaller.installIfNeeded(this);
		} catch (Exception e) {
			new MaterialDialog.Builder(this)
					.title(R.string.dialog_play_services_title)
					.content(e.getMessage())
					.positiveText(R.string.dialog_play_services_repair)
					.negativeColor(getResources().getColor(R.color.black))
					.negativeText(R.string.dialog_play_services_close)
					.onNegative(new MaterialDialog.SingleButtonCallback() {
						@Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
							finish();
						}
					})
					.onPositive(new MaterialDialog.SingleButtonCallback() {
						@Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
							startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.gms")));
						}
					})
					.cancelable(false)
					.show();
		}
	}
}

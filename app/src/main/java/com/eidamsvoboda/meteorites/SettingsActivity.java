package com.eidamsvoboda.meteorites;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class SettingsActivity extends AppCompatActivity {

	@BindView(R.id.lastSyncTextView) TextView lastSyncTextView;
	@BindView(R.id.radioGroup) RadioGroup radioGroup;
	@BindView(R.id.radio24) RadioButton radioButton24;
	@BindView(R.id.radio12) RadioButton radioButton12;
	@BindView(R.id.radio8) RadioButton radioButton8;
	ActionBar actionBar;
	Realm realm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		ButterKnife.bind(this);
		realm = Realm.getDefaultInstance();

		actionBar = getSupportActionBar();
		actionBar.setTitle("Settings");
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		realm.close();
	}

	@Override
	protected void onResume() {
		super.onResume();
		radioGroup.clearCheck();
		switch (DataManager.getSyncFrequency()) {
			case 24:
				radioButton24.setChecked(true);
				break;
			case 12:
				radioButton12.setChecked(true);
				break;
			case 8:
				radioButton8.setChecked(true);
				break;
		}
		updateLastSyncTextView();
	}

	@OnClick({R.id.radio24, R.id.radio12, R.id.radio8})
	public void onRadioClick(RadioButton button) {
		SyncScheduler.cancelSchedule(this);
		switch (button.getId()) {
			case R.id.radio24:
				DataManager.setSyncFrequency(24);
				break;
			case R.id.radio12:
				DataManager.setSyncFrequency(12);
				break;
			case R.id.radio8:
				DataManager.setSyncFrequency(8);
				break;
		}
		SyncScheduler.scheduleSync(this);
	}

	@OnClick(R.id.buttonForceSync)
	public void onForceSync() {
		Toast.makeText(SettingsActivity.this, R.string.toast_sync_start, Toast.LENGTH_SHORT).show();
		DataManager.syncMeteorites(realm, new SyncCallback(this) {
			@Override public void onSyncSuccess() {
				super.onSyncSuccess();
				Toast.makeText(SettingsActivity.this, R.string.toast_sync_succeeded, Toast.LENGTH_SHORT).show();
				updateLastSyncTextView();
			}

			@Override public void onSyncFailed() {
				super.onSyncFailed();
				Toast.makeText(SettingsActivity.this, R.string.toast_sync_failed, Toast.LENGTH_SHORT).show();
				updateLastSyncTextView();
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		onBackPressed();
		return true;
	}

	public void updateLastSyncTextView() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault());
		lastSyncTextView.setText(getString(R.string.settings_last_sync,DataManager.getLastSyncResult()+" "+formatter.format(DataManager.getLastSyncDate())));
	}
}

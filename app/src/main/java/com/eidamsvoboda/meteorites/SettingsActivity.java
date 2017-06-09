package com.eidamsvoboda.meteorites;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class SettingsActivity extends AppCompatActivity {

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
	protected void onResume() {
		super.onResume();
		radioGroup.clearCheck();
		switch (DataManager.getUpdateFrequency()) {
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
	}

	@OnClick({R.id.radio24, R.id.radio12, R.id.radio8})
	public void onRadioClick(RadioButton button) {
		switch (button.getId()) {
			case R.id.radio24:
				DataManager.setUpdateFrequency(24);
				break;
			case R.id.radio12:
				DataManager.setUpdateFrequency(12);
				break;
			case R.id.radio8:
				DataManager.setUpdateFrequency(8);
				break;
		}
	}

	@OnClick(R.id.buttonForceSync)
	public void onForceSync() {
		DataManager.syncMeteorites(realm, new DataManager.SyncCallback() {
			@Override public void onSyncSuccess() {
				Toast.makeText(SettingsActivity.this, R.string.toast_sync_succeeded, Toast.LENGTH_SHORT).show();
			}

			@Override public void onSyncFailed() {
				Toast.makeText(SettingsActivity.this, R.string.toast_sync_failed, Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		onBackPressed();
		return true;
	}
}

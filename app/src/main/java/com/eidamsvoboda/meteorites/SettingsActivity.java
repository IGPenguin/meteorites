package com.eidamsvoboda.meteorites;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {

	@BindView(R.id.radioGroup) RadioGroup radioGroup;
	@BindView(R.id.radio24) RadioButton radioButton24;
	@BindView(R.id.radio12) RadioButton radioButton12;
	@BindView(R.id.radio8) RadioButton radioButton8;
	ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		ButterKnife.bind(this);

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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		onBackPressed();
		return true;
	}

	@OnClick({R.id.radio24, R.id.radio12, R.id.radio8})
	public void pickDoor(RadioButton button) {
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
}

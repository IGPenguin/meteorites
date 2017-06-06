package com.eidamsvoboda.meteorites;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.ButterKnife;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

	ActionBar actionBar;
	String name;
	double lat, lng;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		ButterKnife.bind(this);

		name = getIntent().getStringExtra(Constant.INTENT_METEORITE_NAME);
		lat = Double.parseDouble(getIntent().getStringExtra(Constant.INTENT_METEORITE_LAT));
		lng = Double.parseDouble(getIntent().getStringExtra(Constant.INTENT_METEORITE_LNG));

		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);

		actionBar = getSupportActionBar();
		actionBar.setTitle(name);
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		LatLng latLng = new LatLng(lat, lng);
		googleMap.addMarker(new MarkerOptions()
				.position(latLng)
				.title(name));
		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, Constant.MAP_ZOOM));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		onBackPressed();
		return true;
	}
}

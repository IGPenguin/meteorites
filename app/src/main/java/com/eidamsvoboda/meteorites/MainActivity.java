package com.eidamsvoboda.meteorites;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MeteoriteAdapter.RecyclerItemClickListener {

	@BindView(R.id.recycler) RecyclerView recyclerView;
	MeteoriteAdapter meteoriteAdapter;
	RealmList<Meteorite> meteorites = new RealmList<>();
	Realm realm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
		realm = Realm.getDefaultInstance();

		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(layoutManager);
		meteoriteAdapter = new MeteoriteAdapter(meteorites, this);
		recyclerView.setAdapter(meteoriteAdapter);
	}

	@OnClick(R.id.getButton)
	public void onGet() {
		Api.get().getMeteorites().enqueue(new Callback<List<Meteorite>>() {

			@Override
			public void onResponse(Call<List<Meteorite>> call, Response<List<Meteorite>> response) {
				if (response.isSuccessful()) {
					Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
					meteorites.addAll(response.body());
					meteoriteAdapter.notifyDataSetChanged();
				} else {
					Toast.makeText(MainActivity.this, "Server Fail: " + response.message(), Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Call<List<Meteorite>> call, Throwable t) {
				Toast.makeText(MainActivity.this, "Fail: " + t.toString(), Toast.LENGTH_LONG).show();
			}
		});
	}

	@Override
	public void onRecyclerItemClick(Meteorite meteorite) {
		Toast.makeText(MainActivity.this, "Clicked: " + meteorite.name, Toast.LENGTH_SHORT).show();

		Intent mapActivityIntent = new Intent(this, MapActivity.class);
		mapActivityIntent.putExtra(Constant.INTENT_METEORITE_NAME, meteorite.name);
		mapActivityIntent.putExtra(Constant.INTENT_METEORITE_LAT, meteorite.reclat);
		mapActivityIntent.putExtra(Constant.INTENT_METEORITE_LNG, meteorite.reclong);
		startActivity(mapActivityIntent);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		realm.close();
	}
}

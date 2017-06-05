package com.eidamsvoboda.meteorites;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by eidamsvoboda on 05/06/2017.
 */

public class MeteoriteAdapter extends RecyclerView.Adapter<MeteoriteAdapter.ViewHolder>{

	class ViewHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.name) TextView nameView;
		@BindView(R.id.mass) TextView massView;
		@BindView(R.id.location) TextView locationView;
		@BindView(R.id.date) TextView dateView;

		public ViewHolder(View view){
			super(view);
			ButterKnife.bind(this,view);
		}
	}

	List<Meteorite> meteorites;

	MeteoriteAdapter(List<Meteorite> meteorites){
		this.meteorites=meteorites;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meteorite,parent,false);
		return new ViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		Meteorite meteorite=meteorites.get(position);
		holder.nameView.setText(meteorite.name);
		holder.massView.setText(meteorite.mass);
		holder.locationView.setText(meteorite.geolocation.toString());
		holder.dateView.setText(meteorite.year);
	}

	@Override
	public int getItemCount() {
		return meteorites.size();
	}
}

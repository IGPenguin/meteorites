package com.eidamsvoboda.meteorites.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eidamsvoboda.meteorites.R;
import com.eidamsvoboda.meteorites.model.Meteorite;
import com.eidamsvoboda.meteorites.sync.SyncCallback;
import com.eidamsvoboda.meteorites.tools.DataManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by eidamsvoboda on 05/06/2017.
 */

public class MeteoriteAdapter extends RecyclerView.Adapter<MeteoriteAdapter.ViewHolder> {

	List<Meteorite> meteorites;
	RecyclerItemClickListener recyclerItemClickListener;
	Context context;

	public MeteoriteAdapter(Context context, Realm realm, RecyclerItemClickListener recyclerItemClickListener, SyncCallback syncCallback) {
		this.context = context;
		this.meteorites = DataManager.getMeteoriteList(realm, syncCallback);
		this.recyclerItemClickListener = recyclerItemClickListener;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meteorite, parent, false);
		return new ViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		Meteorite meteorite = meteorites.get(position);
		holder.nameView.setText(meteorite.getName());
		holder.massView.setText(context.getString(R.string.main_mass, meteorite.getMass()));
		holder.locationView.setText(meteorite.getReclat() + ", " + meteorite.getReclong());
		holder.dateView.setText(meteorite.getYear().substring(0, meteorite.getYear().indexOf("-"))); // Show only year, because other data are always the same (1st january 00:00:00)
	}

	@Override
	public int getItemCount() {
		return meteorites.size();
	}

	public interface RecyclerItemClickListener {
		void onRecyclerItemClick(Meteorite meteorite);
	}
	class ViewHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.name) TextView nameView;
		@BindView(R.id.mass) TextView massView;
		@BindView(R.id.location) TextView locationView;
		@BindView(R.id.date) TextView dateView;

		public ViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);
			view.setOnClickListener(new View.OnClickListener() {
				@Override public void onClick(View v) {
					recyclerItemClickListener.onRecyclerItemClick(meteorites.get(getAdapterPosition()));
				}
			});
		}
	}
}

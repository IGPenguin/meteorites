package com.eidamsvoboda.meteorites;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;

/**
 * Created by eidamsvoboda on 05/06/2017.
 */

public class MeteoriteAdapter extends RecyclerView.Adapter<MeteoriteAdapter.ViewHolder> {

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

	public interface RecyclerItemClickListener {
		void onRecyclerItemClick(Meteorite meteorite);
	}

	RealmList<Meteorite> meteorites;
	RecyclerItemClickListener recyclerItemClickListener;
	Context context;

	MeteoriteAdapter(Context context, RealmList<Meteorite> meteorites, RecyclerItemClickListener recyclerItemClickListener) {
		this.context = context;
		this.meteorites = meteorites;
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
		holder.nameView.setText(meteorite.name);
		holder.massView.setText(String.format(context.getString(R.string.main_mass), meteorite.mass));
		holder.locationView.setText(meteorite.reclat + ", " + meteorite.reclong);
		holder.dateView.setText(meteorite.year.substring(0, meteorite.year.indexOf("-"))); // Show only year, because other data are always the same (1st january 00:00:00)
	}

	@Override
	public int getItemCount() {
		return meteorites.size();
	}
}

package com.eidamsvoboda.meteorites.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by eidamsvoboda on 05/06/2017.
 */

public class Meteorite extends RealmObject{
	@PrimaryKey
	private String id;
	private double mass;
	private String name;
	private String reclat;
	private String reclong;
	private String year;

	public double getMass() {
		return mass;
	}

	public String getName() {
		return name;
	}

	public String getReclat() {
		return reclat;
	}

	public String getReclong() {
		return reclong;
	}

	public String getYear() {
		return year;
	}
}

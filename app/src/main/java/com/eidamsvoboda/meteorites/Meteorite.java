package com.eidamsvoboda.meteorites;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by eidamsvoboda on 05/06/2017.
 */

public class Meteorite extends RealmObject{
	@PrimaryKey
	String id;
	double mass;
	String name;
	String reclat;
	String reclong;
	String year;
}

package com.eidamsvoboda.meteorites;

import io.realm.RealmObject;

/**
 * Created by eidamsvoboda on 05/06/2017.
 */

public class Geolocation extends RealmObject {
	String type;
	//RealmList<> coordinates;

	@Override
	public String toString() {
		return type;
	}
}

package com.eidamsvoboda.meteorites;

import java.util.List;

/**
 * Created by eidamsvoboda on 05/06/2017.
 */

public class Geolocation {
	String type;
	List<Float> coordinates;

	@Override
	public String toString() {
		return type+" "+coordinates.toString();
	}
}

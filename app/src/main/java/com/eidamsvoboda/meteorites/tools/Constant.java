package com.eidamsvoboda.meteorites.tools;

/**
 * Created by eidamsvoboda on 06/06/2017.
 */

public class Constant {
	public static class Intent {
		public static final String METEORITE_NAME = "meteorite_name";
		public static final String METEORITE_LAT = "meteorite_lat";
		public static final String METEORITE_LNG = "meteorite_long";
	}
	public static class Map {
		public static final float ZOOM = 3.0f;
	}
	public static class Api {
		public static final String URL = "https://data.nasa.gov/";
		public static final String TOKEN = "xSi7f21Q6jC3R78siFsUrrwj3";
		public static final String SECRET_TOKEN = "RzHFU_TbrLQROPyieqJFBcn5u_UIHivjYwb1";
	}
	public static class Settings {
		public static final int SYNC_FREQUENCY_DEFAULT = 24;
		public static final String SYNC_FREQUENCY = "sync_frequency";
		public static final String SYNC_LAST_DATE = "sync_last_date";
		public static final String SYNC_LAST_RESULT = "sync_last_result";
		public static final String SORT_FIELD = "sort_field";
		public static final String SORT_ORIENTATION = "sort_orientation";
	}

}

package com.eidamsvoboda.meteorites.api;

import com.eidamsvoboda.meteorites.model.Meteorite;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by eidamsvoboda on 15/06/2017.
 */

public interface ServerService {
	@GET("resource/y77d-th95.json?$where=year>='2011-01-01T00:00:00.000'")
	Call<List<Meteorite>> getMeteorites();
}


package com.eidamsvoboda.meteorites;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by eidamsvoboda on 05/06/2017.
 */

public class Api {
	private static ServerService serverService;

	private static final String URL = "https://data.nasa.gov/";
	private static final String TOKEN = "xSi7f21Q6jC3R78siFsUrrwj3";
	private static final String SECRET_TOKEN = "RzHFU_TbrLQROPyieqJFBcn5u_UIHivjYwb1";

	static ServerService get() {
		if (serverService == null) {

			Interceptor tokenInterceptor = new Interceptor() {
				@Override public Response intercept(Chain chain) throws IOException {
					Request original = chain.request();
					HttpUrl originalUrl = original.url();

					HttpUrl tokenUrl = originalUrl.newBuilder()
							.addQueryParameter("$$app_token",TOKEN)
							.build();

					Request.Builder requestBuilder = original.newBuilder()
							.url(tokenUrl);

					Request tokenRequest = requestBuilder.build();
					return  chain.proceed(tokenRequest);
				}
			};

			HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
			loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

			OkHttpClient client = new OkHttpClient.Builder()
					.addInterceptor(loggingInterceptor)
					.addInterceptor(tokenInterceptor)
					.build();

			Retrofit retrofit = new Retrofit.Builder()
					.baseUrl(URL)
					.client(client)
					.addConverterFactory(GsonConverterFactory.create())
					.build();

			serverService = retrofit.create(ServerService.class);
		}
		return serverService;
	}

	interface ServerService {
		@GET("resource/y77d-th95.json?$where=year >= '2011-01-01T00:00:00.000'")
		Call<List<Meteorite>> getMeteorites();
	}
}

package com.eidamsvoboda.meteorites.api;

import com.eidamsvoboda.meteorites.tools.Constant;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by eidamsvoboda on 05/06/2017.
 */

public class Api {

	private static ServerService serverService;

	private Api() {}

	public static ServerService get() {
		if (serverService == null) {

			Interceptor tokenInterceptor = new Interceptor() {
				@Override public Response intercept(Chain chain) throws IOException {
					Request original = chain.request();
					HttpUrl originalUrl = original.url();

					HttpUrl tokenUrl = originalUrl.newBuilder()
							.addQueryParameter("$$app_token", Constant.Api.TOKEN)
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
					.baseUrl(Constant.Api.URL)
					.client(client)
					.addConverterFactory(GsonConverterFactory.create())
					.build();

			serverService = retrofit.create(ServerService.class);
		}
		return serverService;
	}

}

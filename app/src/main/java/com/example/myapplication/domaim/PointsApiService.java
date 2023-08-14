package com.example.myapplication.domaim;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;


public interface PointsApiService {

    @Headers("Content-Type: text/plain")
    @GET("/{id}")
    Call<String> getPointById(@Path("number")int id);

}

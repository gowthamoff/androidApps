package com.kazimasum.retro;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetApi {
  @GET("current.json?key=a6125619b13e4ff1b4e51325232209&q=India")
  Call<WeatherResponse> getWeatherData();
}

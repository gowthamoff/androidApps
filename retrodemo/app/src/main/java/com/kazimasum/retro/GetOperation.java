package com.kazimasum.retro;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetOperation extends AppCompatActivity {
    TextView tv;
    String baseUrl = "https://api.weatherapi.com/v1/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.tv);
        tv.setText("");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GetApi api = retrofit.create(GetApi.class);

        Call<WeatherResponse> call = api.getWeatherData();


        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (!response.isSuccessful()) {
                    Log.e("API Response", "Error: " + response.code());
                    tv.setText("Code: " + response.code());
                    return;
                }

                WeatherResponse weatherResponse = response.body();
//                LocationData locationData = weatherResponse.getLocationData();
                CurrentWeather currentWeather = weatherResponse.getCurrentWeather();

                if ( currentWeather != null) {
                    Log.d("API Response", "Last Updated: " + currentWeather.getLastUpdated());
                    Log.d("API Response", "Temperature (Celsius): " + currentWeather.getTempC() + "°C");
                    Log.d("API Response", "Temperature (Fahrenheit): " + currentWeather.getTempF() + "°F");

                    tv.append("Last Updated: " + currentWeather.getLastUpdated() + "\n");
                    tv.append("Temperature (Celsius): " + currentWeather.getTempC() + "°C\n");
                    tv.append("Temperature (Fahrenheit): " + currentWeather.getTempF() + "°F\n");
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.e("API Error", "Failed to retrieve weather data.", t);
                tv.setText("Failed to retrieve weather data.");
            }
        });
    }
}

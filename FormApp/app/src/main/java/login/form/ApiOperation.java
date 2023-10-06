package login.form;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiOperation extends AppCompatActivity {
    String baseUrl = "http://192.168.1.77:3005/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_operation);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .build();


        GetApi api = retrofit.create(GetApi.class);

        Call<UserResponse> call = api.getUserData();

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (!response.isSuccessful()) {
                    Log.e("API Response", "Error: " + response.code());
                    return;
                }

                UserResponse userResponse = response.body();
                if (userResponse != null) {
                    Log.d("API Response", "Username: " + userResponse.getUsername());
                    Log.d("API Response", "Status Text: " + userResponse.getStatusText());
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("API Response", "Error: " + t.getMessage());
            }
        });
    }
}

package login.form;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetApi {
    @GET("users")
    Call<UserResponse> getUserData();
}

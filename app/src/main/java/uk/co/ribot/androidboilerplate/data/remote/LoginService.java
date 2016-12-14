package uk.co.ribot.androidboilerplate.data.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import uk.co.ribot.androidboilerplate.data.model.LoginResponse;
import uk.co.ribot.androidboilerplate.util.MyGsonTypeAdapterFactory;

/**
 * Created by raul on 10/12/2016.
 */
public interface LoginService {

    String ENDPOINT = "http://52.15.174.153:8080";

    @GET("/pushAWS/rest/users_service/login.do")
    Observable<LoginResponse> getLogin(@Query("email") String email,
                                       @Query("password") String password,
                                       @Query("userId") String userId);

    @GET("/pushAWS/rest/users_service/login.do")
    Call<LoginResponse> getLogin2(@Query("email") String email,
                                  @Query("password") String password,
                                  @Query("userId") String userId);

    /******** Helper class that sets up a new services *******/
    class Creator {

        public static LoginService newLoginService() {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapterFactory(MyGsonTypeAdapterFactory.create())
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(LoginService.ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            return retrofit.create(LoginService.class);
        }
    }
}
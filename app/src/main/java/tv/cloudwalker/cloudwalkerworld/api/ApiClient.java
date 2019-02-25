package tv.cloudwalker.cloudwalkerworld.api;

import android.content.Context;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tv.cloudwalker.cloudwalkerworld.BuildConfig;
import tv.cloudwalker.cloudwalkerworld.Utils.CustomHttpClient;

/**
 * Created by cognoscis on 6/1/18.
 */

public class ApiClient {
    public static final String BASE_URL = BuildConfig.SERVER_URL;
    private static Retrofit retrofit = null;


    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(CustomHttpClient.getHttpClient(context, BASE_URL))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

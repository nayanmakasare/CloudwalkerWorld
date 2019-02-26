package tv.cloudwalker.cloudwalkerworld.api;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import tv.cloudwalker.cloudwalkerworld.module.MovieResponse;
import tv.cloudwalker.cloudwalkerworld.module.RecommendationsResponse;

/**
 * Created by cognoscis on 6/1/18.
 */

public interface ApiInterface {
    @Headers({"Accept-Version: 1.0.0"})
    @GET("data.json")
    Observable<MovieResponse> getHomeScreenData();

    @Headers({"Accept-Version: 1.0.0"})
    @GET("related/{tileId}")
    Observable<RecommendationsResponse> getRecommendations(@Path("tileId") String tileId);
}


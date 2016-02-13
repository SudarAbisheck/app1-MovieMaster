package me.sudar.moviemaster.network;

import me.sudar.moviemaster.models.ApiCallReply;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by sudar on 1/2/16.
 * Email : hey@sudar.me
 */
public class TmDbService {

    private static final String BASE_URL = "http://api.themoviedb.org";

    public static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185";
    public static final String BACKDROP_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w500";

    private TmDbService(){}

    public static TmDbApi getService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(TmDbApi.class);
    }


    public interface TmDbApi{

        String apiKey = "_YOUR_API_KEY_GOES_HERE_";

        @GET("/3/discover/movie?sort_by=popularity.desc&api_key=" + apiKey)
        Observable<ApiCallReply> getPopularMovies();

        @GET("/3/discover/movie?sort_by=vote_average.desc&api_key=" + apiKey)
        Observable<ApiCallReply> getHighRatedMovies();
    }

}

package me.sudar.moviemaster.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
//import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//@Generated("org.jsonschema2pojo")
public class ApiCallReply implements Parcelable {

    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("results")
    @Expose
    private List<Movie> movies = new ArrayList<Movie>();
    @SerializedName("total_results")
    @Expose
    private Integer totalMovies;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;

    /**
     *
     * @return
     * The page
     */
    public Integer getPage() {
        return page;
    }

    /**
     *
     * @param page
     * The page
     */
    public void setPage(Integer page) {
        this.page = page;
    }

    /**
     *
     * @return
     * The results
     */
    public List<Movie> getMovies() {
        return movies;
    }

    /**
     *
     * @param movies
     * The results
     */
    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    /**
     *
     * @return
     * The totalMovies
     */
    public Integer getTotalMovies() {
        return totalMovies;
    }

    /**
     *
     * @param totalMovies
     * The total_results
     */
    public void setTotalMovies(Integer totalMovies) {
        this.totalMovies = totalMovies;
    }

    /**
     *
     * @return
     * The totalPages
     */
    public Integer getTotalPages() {
        return totalPages;
    }

    /**
     *
     * @param totalPages
     * The total_pages
     */
    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.page);
        dest.writeTypedList(movies);
        dest.writeValue(this.totalMovies);
        dest.writeValue(this.totalPages);
    }

    public ApiCallReply() {
    }

    protected ApiCallReply(Parcel in) {
        this.page = (Integer) in.readValue(Integer.class.getClassLoader());
        this.movies = in.createTypedArrayList(Movie.CREATOR);
        this.totalMovies = (Integer) in.readValue(Integer.class.getClassLoader());
        this.totalPages = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<ApiCallReply> CREATOR = new Parcelable.Creator<ApiCallReply>() {
        public ApiCallReply createFromParcel(Parcel source) {
            return new ApiCallReply(source);
        }

        public ApiCallReply[] newArray(int size) {
            return new ApiCallReply[size];
        }
    };
}

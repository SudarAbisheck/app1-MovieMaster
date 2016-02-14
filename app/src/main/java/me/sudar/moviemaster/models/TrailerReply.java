package me.sudar.moviemaster.models;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrailerReply {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<Trailer> trailers = new ArrayList<Trailer>();

    /**
     *
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The trailers
     */
    public List<Trailer> getTrailers() {
        return trailers;
    }

    /**
     *
     * @param trailers
     * The trailers
     */
    public void setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
    }

}
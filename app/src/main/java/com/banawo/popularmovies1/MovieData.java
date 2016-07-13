package com.banawo.popularmovies1;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Jayesh Sukumaran on 7/4/2016.
 */
public class MovieData {

    private static MovieData instance;

    private ArrayList<MovieData> elements;
    private String movieName;
    private String moviePoster;
    private String movieBigPoster;
    private String movieSynopsis;
    private String movieUsrRating;
    private String movieRelDate;


    static {
        instance = null;
    }

    {
        this.elements = new ArrayList<MovieData>();

    }


    private MovieData() {

        this("","","","","","");
    }

    public void CreateMovie(String movieName, String moviePoster, String movieBigPoster, String movieSynopsis, String movieUsrRating, String movieRelDate) {

        MovieData mData = new MovieData(movieName,moviePoster,movieBigPoster,movieSynopsis,movieUsrRating,movieRelDate);
        elements.add(mData);
    }

    private MovieData(String movieName, String moviePoster, String movieBigPoster, String movieSynopsis, String movieUsrRating, String movieRelDate) {
        this.movieName = movieName;
        this.moviePoster = moviePoster;
        this.movieBigPoster = movieBigPoster;
        this.movieSynopsis = movieSynopsis;
        this.movieUsrRating = movieUsrRating;
        this.movieRelDate = movieRelDate;


    }

    public static MovieData GetInstance() {

        if(instance == null) {
            instance = new MovieData();
        }
        return instance;
    }

    public MovieData getElementAtIndex(int index) {
        return elements.get(index);
    }
    public void setElementAtIndex(int index, MovieData data) {
        elements.add(index,data);
    }

    public int getElementCount() {
        return elements.size();
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }
    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }
    public void setMovieBigPoster(String movieBigPoster) {
        this.movieBigPoster = movieBigPoster;
    }

    public void setMovieSynopsis(String movieSynopsis) {
        this.movieSynopsis = movieSynopsis;
    }
    public void setMovieUsrRating(String movieUsrRating) {
        this.movieUsrRating = movieUsrRating;
    }
    public void setMovieRelDate(String movieRelDate) {
        this.movieRelDate = movieRelDate;
    }
    public String getMovieName() {
        return this.movieName;
    }
    public String getMoviePoster() {
        return this.moviePoster;
    }
    public String getMovieBigPoster() {
        return this.movieBigPoster;
    }
    public String getMovieSynopsis() {
        return this.movieSynopsis;
    }
    public String getMovieUsrRating() {
        return this.movieUsrRating;
    }
    public String getMovieRelDate() {
        return this.movieRelDate;
    }
    public void   clearMovieData() {
        elements.clear();
    }

    public final ArrayList<MovieData> getElementList() {
        return elements;
    }

}

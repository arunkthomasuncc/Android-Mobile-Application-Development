package com.example.arun.moviedb;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by Arun on 10/16/2017.
 */

public class Movie implements Serializable {

    String title;
    String overview;

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", overview='" + overview + '\'' +
                ", releasedate='" + releasedate + '\'' +
                ", rating=" + rating +
                ", popularity=" + popularity +
                ", posterpath='" + posterpath + '\'' +
                ", imagebaseURLsmall='" + imagebaseURLsmall + '\'' +
                ", imagebaseURLLarge='" + imagebaseURLLarge + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleasedate() {
        return releasedate;
    }

    public void setReleasedate(String releasedate) {
        this.releasedate = releasedate;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getPosterpath() {
        return posterpath;
    }

    public void setPosterpath(String posterpath) {
        this.posterpath = posterpath;
    }

    public String getImagebaseURLsmall() {
        return imagebaseURLsmall;
    }

    public void setImagebaseURLsmall(String imagebaseURLsmall) {
        this.imagebaseURLsmall = imagebaseURLsmall;
    }

    public String getImagebaseURLLarge() {
        return imagebaseURLLarge;
    }

    public void setImagebaseURLLarge(String imagebaseURLLarge) {
        this.imagebaseURLLarge = imagebaseURLLarge;
    }

    String releasedate;
    int rating;

    public double getRatingnew() {
        return ratingnew;
    }

    public void setRatingnew(double ratingnew) {
        this.ratingnew = ratingnew;
    }

    double ratingnew;
    double popularity;
    String posterpath;
    String imagebaseURLsmall;
    String imagebaseURLLarge;

    public boolean equals(Object obj) {
        boolean isSame=false;
        if (obj == this) {
            isSame= true;
        }

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(obj instanceof Movie)) {
            isSame= false;
        }

        // typecast o to Complex so that we can compare data members
        Movie c = (Movie) obj;

        // Compare the data members and return accordingly
        if(this.getTitle().equals(((Movie) obj).getTitle())&& this.getOverview().equals(((Movie) obj).getOverview()))
        {
            isSame= true;
        }
        return isSame;
    }

    public static Comparator<Movie> sortByRating= new Comparator<Movie>() {
        @Override
        public int compare(Movie track, Movie t1) {
            //return t1.getRating()-(track.getRating());

            if (t1.getRatingnew() < track.getRatingnew())
                return -1;
            if (t1.getRatingnew() > track.getRatingnew())
                return 1;
            return 0;
        }
    };

    public static Comparator<Movie> sortByPopularity= new Comparator<Movie>() {
        @Override
        public int compare(Movie track, Movie t1) {

            if (t1.getPopularity() < track.getPopularity())
                return -1;
            if (t1.getPopularity() > track.getPopularity())
                return 1;
            return 0;
        }
    };




}

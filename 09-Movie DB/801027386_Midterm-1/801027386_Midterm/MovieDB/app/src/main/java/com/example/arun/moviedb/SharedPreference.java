package com.example.arun.moviedb;

/**
 * Created by Arun on 10/16/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Arun on 10/8/2017.
 */

public class SharedPreference {


    public static final String PREFS_NAME = "MOVIE_SEARCH_APP";
    public static final String FAVORITES = "TRACK_FAVORITE";

    public SharedPreference() {
        super();
    }

    // This four methods are used for maintaining favorites.
    public void saveFavorites(Context context, List<Movie> favorites) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(FAVORITES, jsonFavorites);

        editor.commit();
    }

    public void addFavorite(Context context, Movie track) {
        List<Movie> favorites = getFavorites(context);
        if (favorites == null)
            favorites = new ArrayList<Movie>();
        favorites.add(track);
        saveFavorites(context, favorites);
    }
    public void removeFavorite(Context context, Movie track) {
        ArrayList<Movie> favorites = getFavorites(context);
        if (favorites != null) {
            favorites.remove(track);
            saveFavorites(context, favorites);
        }
    }

    public ArrayList<Movie> getFavorites(Context context) {
        SharedPreferences settings;
        List<Movie> favorites;

        settings = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            Movie[] favoriteItems = gson.fromJson(jsonFavorites,
                    Movie[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<Movie>(favorites);
        } else
            return null;

        return (ArrayList<Movie>) favorites;
    }
}


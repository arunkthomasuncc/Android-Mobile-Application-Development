package com.example.arun.moviedb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Arun on 10/16/2017.
 */

public class MovieSearchUtil {


/*    {
        "page": 1,
            "total_results": 97,
            "total_pages": 5,
            "results": [
        {
            "vote_count": 2131,
                "id": 268,
                "video": false,
                "vote_average": 7,
                "title": "Batman",
                "popularity": 16.258939,
                "poster_path": "/kBf3g9crrADGMc2AMAMlLBgSm2h.jpg",
                "original_language": "en",
                "original_title": "Batman",
                "genre_ids": [
            14,
                    28
			],
            "backdrop_path": "/2blmxp2pr4BhwQr74AdCfwgfMOb.jpg",
                "adult": false,
                "overview": "The Dark Knight of Gotham City begins his war on crime with his first major enemy being the clownishly homicidal Joker, who has seized control of Gotham's underworld.",
                "release_date": "1989-06-23"
        },*/


    public static ArrayList<Movie> parser(String ResultJson)
    {
        ArrayList<Movie> movieList = new ArrayList<Movie>();

        try {
            JSONObject movieResult=new JSONObject(ResultJson);
            JSONArray moviearray=movieResult.getJSONArray("results");

            for(int i=0; i<moviearray.length();i++)

            {
                Movie movie= new Movie();

                JSONObject movieObject= moviearray.getJSONObject(i);
                if(movieObject.getString("original_title").length()>0)
                {
                    movie.setTitle(movieObject.getString("original_title"));
                }
                if(movieObject.getString("overview").length()>0)
                {
                    movie.setOverview(movieObject.getString("overview"));
                }
                if(movieObject.getString("release_date").length()>0)
                {
                    movie.setReleasedate(movieObject.getString("release_date"));
                }

                    movie.setRating(movieObject.getInt("vote_average"));

                movie.setRatingnew(movieObject.getDouble("vote_average"));

                    movie.setPopularity(movieObject.getDouble("popularity"));

                if(movieObject.getString("poster_path").length()>0)
                {
                    movie.setPosterpath(movieObject.getString("poster_path"));
                    movie.setImagebaseURLsmall("http://image.tmdb.org/t/p/w154"+movieObject.getString("poster_path"));
                    movie.setImagebaseURLLarge("http://image.tmdb.org/t/p/w342"+movieObject.getString("poster_path"));
                }



                movieList.add(movie);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Log.d("Demo",trackResults.toString());
        return movieList;
    }

}

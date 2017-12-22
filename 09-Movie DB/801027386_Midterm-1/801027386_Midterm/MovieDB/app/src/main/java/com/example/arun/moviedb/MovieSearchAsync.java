package com.example.arun.moviedb;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * Created by Arun on 10/16/2017.
 */

public class MovieSearchAsync extends AsyncTask<String,Void,ArrayList<Movie>> {

   IMusicData moviedata;
    ArrayList<Movie> movieResults= new ArrayList<Movie>();

    public MovieSearchAsync(IMusicData moviedata) {


        this.moviedata = moviedata;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
        super.onPostExecute(movies);

        moviedata.setMovieSearchResult(movieResults);
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... strings) {
//https://api.themoviedb.org/3/search/movie?query=batman&api_key=de4d56ddfc33c9d0a7b55ec707ff0e88&page=1

        StringBuilder sb=new StringBuilder();
        String[] searchdata=strings[0].split(" ");
        for (String x:searchdata
             ) {

            if(sb.length()>0)
            {
                sb.append("+");
            }
            sb.append(x);

        }

        Log.d("query",sb.toString());
        RequestParams requestParams = new RequestParams("GET","https://api.themoviedb.org/3/search/movie");
       // requestParams.addParam("format","json");
      //  requestParams.addParam("method","track.search");
        requestParams.addParam("query",sb.toString());
        requestParams.addParam("api_key","de4d56ddfc33c9d0a7b55ec707ff0e88");
        requestParams.addParam("page","1");
        HttpURLConnection connection = (HttpURLConnection) requestParams.setupConnection();
        try {
            connection.connect();
            StringBuilder sbnew= new StringBuilder();
            String line="";
            if(connection.getResponseCode()==HttpURLConnection.HTTP_OK)
            {
                BufferedReader br= new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while((line=br.readLine())!=null)
                {
                    sbnew.append(line);
                }

                  Log.d("Demo",sbnew.toString());
                movieResults=MovieSearchUtil.parser(sbnew.toString());
                Log.d("MovieResult",movieResults.toString());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return movieResults;


    }


    public  interface IMusicData
    {
        void setMovieSearchResult(ArrayList<Movie> movies);
        // void startProgressbar();
        //void stopProgressbar();
    }
}

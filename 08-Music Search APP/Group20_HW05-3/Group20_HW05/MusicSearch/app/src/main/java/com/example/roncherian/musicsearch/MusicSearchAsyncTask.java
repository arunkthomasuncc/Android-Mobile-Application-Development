package com.example.roncherian.musicsearch;
//Ron Abraham Cherian - 801028678
//Arun Thomas Kunnumpuram
import android.os.AsyncTask;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by roncherian on 07/10/17.
 */

public class MusicSearchAsyncTask extends AsyncTask<String,Void,ArrayList<MusicTrack>> {

    IMusicTrack iMusicTrack;

    public MusicSearchAsyncTask(IMusicTrack iMusicTrack){
        this.iMusicTrack = iMusicTrack;
    }

    @Override
    protected void onPostExecute(ArrayList<MusicTrack> musicTracks) {

        iMusicTrack.showResults(musicTracks);
    }

    @Override
    protected ArrayList<MusicTrack> doInBackground(String... strings) {

        String urlString = "";
        String searchString = "";
        String similarSearchAPI = "";
        String artistName = "";
        String trackName = "";

        for (int i = 0; i<strings.length; i++){

            if (i==0)
                urlString = strings[0];
            else if (i==1)
                searchString = strings[1];
            else if (i==2)
                similarSearchAPI = strings[2];
            else if (i==3)
                artistName = strings[3];
            else if (i==4)
                trackName = strings[4];
        }

        ArrayList<MusicTrack>musicTracks = new ArrayList<MusicTrack>();
        try {
            //URL url = new URL(urlString);
//method=track.search&track=Believe&api_key=b395df93fa2168371596bb1a129d34b1&format=json
            RequestParams requestParams = new RequestParams("GET",urlString);
            if (null != similarSearchAPI && similarSearchAPI.equals(TrackDetailsActivity.SIMILAR_TRACKS)){
                requestParams.addParam("method","track.getsimilar");
                requestParams.addParam("artist",artistName);
                requestParams.addParam("track",trackName);
                requestParams.addParam("limit","10");
            } else {
                requestParams.addParam("method","track.search");
                requestParams.addParam("track",searchString);
                requestParams.addParam("limit","20");
            }


            requestParams.addParam("api_key","b395df93fa2168371596bb1a129d34b1");
            requestParams.addParam("format","json");

            HttpURLConnection connection = (HttpURLConnection) requestParams.setupConnection();
            connection.connect();
            if (connection.getResponseCode()==HttpURLConnection.HTTP_OK){
                StringBuilder stringBuilder = new StringBuilder();
                String line = "";
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line=bufferedReader.readLine()) != null){
                    stringBuilder.append(line);
                }
                if (null != similarSearchAPI && similarSearchAPI.equals(TrackDetailsActivity.SIMILAR_TRACKS)){
                    musicTracks = (ArrayList<MusicTrack>) MusicSearchUtil.MusicTracksJSONParser.parseSimilarMusicTracks(stringBuilder.toString());
                } else {
                    musicTracks = (ArrayList<MusicTrack>) MusicSearchUtil.MusicTracksJSONParser.parseMusicTracks(stringBuilder.toString());
                }

                return musicTracks;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ArrayList<MusicTrack>();
    }

    public  interface IMusicTrack
    {
        void showResults(ArrayList<MusicTrack> results);
        void startProgressbar();
    }

}

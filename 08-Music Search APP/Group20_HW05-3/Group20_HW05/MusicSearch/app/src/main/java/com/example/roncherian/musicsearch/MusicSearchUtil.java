package com.example.roncherian.musicsearch;
//Ron Abraham Cherian - 801028678
//Arun Thomas Kunnumpuram
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by roncherian on 07/10/17.
 */

public class MusicSearchUtil {


    static public class MusicTracksJSONParser {
        static List<MusicTrack> parseMusicTracks(String in) throws JSONException {

            ArrayList<MusicTrack> musicTrackArrayList = new ArrayList<MusicTrack>();
            JSONObject jsonObject = new JSONObject(in);

            if (jsonObject.length() == 0){
                return musicTrackArrayList;
            }
            JSONObject resultJsonObject = jsonObject.getJSONObject("results");
            if (resultJsonObject.length() == 0){
                return musicTrackArrayList;
            }
            JSONObject trackMatchesJsonObject = resultJsonObject.getJSONObject("trackmatches");
            if (trackMatchesJsonObject.length() == 0){
                return musicTrackArrayList;
            }
            JSONArray jsonArray = trackMatchesJsonObject.getJSONArray("track");


            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonMusicTrackObject = jsonArray.getJSONObject(i);

                MusicTrack musicTrack = new MusicTrack();

                musicTrack.setName(jsonMusicTrackObject.getString("name"));
                musicTrack.setArtist(jsonMusicTrackObject.getString("artist"));
                musicTrack.setUrl(jsonMusicTrackObject.getString("url"));
                JSONArray imageJsonArray;
                if (null != jsonMusicTrackObject.getJSONArray("image")){
                    imageJsonArray = jsonMusicTrackObject.getJSONArray("image");
                    for (int j=0; j<imageJsonArray.length(); j++){
                        JSONObject imageJsonObject = imageJsonArray.getJSONObject(j);
                        if (null != imageJsonObject.getString("size") && imageJsonObject.getString("size").equals("large")){
                            musicTrack.setLargeImageUrl(imageJsonObject.getString("#text"));
                        }
                        if (null != imageJsonObject.getString("size") && imageJsonObject.getString("size").equals("small")){
                            musicTrack.setSmallImageUrl(imageJsonObject.getString("#text"));
                        }
                    }
                }
                musicTrackArrayList.add(musicTrack);
            }
            return musicTrackArrayList;
        }

        static List<MusicTrack> parseSimilarMusicTracks(String in) throws JSONException {

            ArrayList<MusicTrack> musicTrackArrayList = new ArrayList<MusicTrack>();
            JSONObject jsonObject = new JSONObject(in);

            JSONObject trackMatchesJsonObject = jsonObject.getJSONObject("similartracks");
            JSONArray jsonArray = trackMatchesJsonObject.getJSONArray("track");


            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonMusicTrackObject = jsonArray.getJSONObject(i);

                MusicTrack musicTrack = new MusicTrack();

                musicTrack.setName(jsonMusicTrackObject.getString("name"));
                JSONObject artistJsonObject = jsonMusicTrackObject.getJSONObject("artist");
                musicTrack.setArtist(artistJsonObject.getString("name"));
                musicTrack.setUrl(jsonMusicTrackObject.getString("url"));
                JSONArray imageJsonArray;
                if (null != jsonMusicTrackObject.getJSONArray("image")){
                    imageJsonArray = jsonMusicTrackObject.getJSONArray("image");
                    for (int j=0; j<imageJsonArray.length(); j++){
                        JSONObject imageJsonObject = imageJsonArray.getJSONObject(j);
                        if (null != imageJsonObject.getString("size") && imageJsonObject.getString("size").equals("large")){
                            musicTrack.setLargeImageUrl(imageJsonObject.getString("#text"));
                        }
                        if (null != imageJsonObject.getString("size") && imageJsonObject.getString("size").equals("small")){
                            musicTrack.setSmallImageUrl(imageJsonObject.getString("#text"));
                        }
                    }
                }
                musicTrackArrayList.add(musicTrack);
            }
            return musicTrackArrayList;
        }

    }

}

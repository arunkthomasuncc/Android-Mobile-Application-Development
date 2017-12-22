package com.example.roncherian.musicsearch;
//Ron Abraham Cherian - 801028678
//Arun Thomas Kunnumpuram
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TrackDetailsActivity extends MenuBarActivity implements MusicSearchAsyncTask.IMusicTrack{

    public static String SIMILAR_TRACKS = "SIMILAR_TRACKS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_details);
        setTitle("Track Details");

        MusicTrack musicTrack = (MusicTrack) getIntent().getExtras().getSerializable(SearchResultsActivity.TRACK_DETAILS_ACTIVITY_INTENT);

        ImageView imageView = (ImageView)findViewById(R.id.imageViewTrackDetails);
        if (null != musicTrack && null != musicTrack.getLargeImageUrl() && musicTrack.getLargeImageUrl().length()>0){
            Picasso.with(this).load(musicTrack.getLargeImageUrl()).into(imageView);
        }


        TextView trackNameTextView = (TextView)findViewById(R.id.textViewTrackName);
        TextView artistNameTextView = (TextView)findViewById(R.id.textViewArtistName);
        final TextView urlNameTextView = (TextView)findViewById(R.id.textViewUrl);
        urlNameTextView.setClickable(true);
        urlNameTextView.setFocusable(false);

        trackNameTextView.setText(musicTrack.getName());
        artistNameTextView.setText(musicTrack.getArtist());
        urlNameTextView.setText(musicTrack.getUrl());
        urlNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = urlNameTextView.getText().toString().trim();
                if (isValidUrl(url)){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                } else {
                    Toast.makeText(TrackDetailsActivity.this,"Not a valid URL",Toast.LENGTH_SHORT).show();
                }

            }
        });


        if (checkInternetPermission()){
            new MusicSearchAsyncTask(TrackDetailsActivity.this).execute("http://ws.audioscrobbler.com/2.0/","",SIMILAR_TRACKS,musicTrack.getArtist(),musicTrack.getName());
        }

    }

    private boolean checkInternetPermission(){

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getActiveNetworkInfo() == null){
            Toast.makeText(TrackDetailsActivity.this,"Please check your internet connection",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    public void showResults(ArrayList<MusicTrack> results) {

        final ArrayList<MusicTrack>musicTrackArrayList = results;
        ListView resultsListView = (ListView)findViewById(R.id.listViewSimilarTracks);



        MusicTracksAdapter musicTracksAdapter = new MusicTracksAdapter(this, R.layout.music_track_listing_row, musicTrackArrayList);
        resultsListView.setAdapter(musicTracksAdapter);
        musicTracksAdapter.setNotifyOnChange(true);

        resultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("demo","Clicked Row Item");
                Intent intent = new Intent(TrackDetailsActivity.this, TrackDetailsActivity.class);
                intent.putExtra(SearchResultsActivity.TRACK_DETAILS_ACTIVITY_INTENT,musicTrackArrayList.get(i));
                startActivity(intent);

            }
        });
    }

    @Override
    public void startProgressbar() {

    }

    private boolean isValidUrl(String url){

        if (url == null){
            return false;
        } else {
            return Patterns.WEB_URL.matcher(url).matches();
        }
    }



}

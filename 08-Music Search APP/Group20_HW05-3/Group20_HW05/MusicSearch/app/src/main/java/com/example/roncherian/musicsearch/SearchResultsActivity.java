package com.example.roncherian.musicsearch;
//Ron Abraham Cherian - 801028678
//Arun Thomas Kunnumpuram
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends MenuBarActivity {

    public static String TRACK_DETAILS_ACTIVITY_INTENT = "Tracke Details Activity Intent";

    public static int SEARCH_ACTIVITY_REQUEST_CODE = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        setTitle("Search Results");

        final ArrayList<MusicTrack>musicTrackArrayList = (ArrayList<MusicTrack>) getIntent().getSerializableExtra(MainActivity.SHOW_RESULTS);
        ListView resultsListView = (ListView)findViewById(R.id.listViewMusicSearchResults);



        MusicTracksAdapter musicTracksAdapter = new MusicTracksAdapter(this, R.layout.music_track_listing_row, musicTrackArrayList);
        resultsListView.setAdapter(musicTracksAdapter);
        musicTracksAdapter.setNotifyOnChange(true);

        resultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("demo","Clicked Row Item");
                Intent intent = new Intent(SearchResultsActivity.this, TrackDetailsActivity.class);
                intent.putExtra(TRACK_DETAILS_ACTIVITY_INTENT,musicTrackArrayList.get(i));
                startActivityForResult(intent,SEARCH_ACTIVITY_REQUEST_CODE);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == SEARCH_ACTIVITY_REQUEST_CODE){
            if (resultCode == RESULT_OK){

                setResult(RESULT_OK);
                finish();
            }
        }
    }
}

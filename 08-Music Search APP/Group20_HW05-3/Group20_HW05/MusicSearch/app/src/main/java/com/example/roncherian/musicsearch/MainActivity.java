package com.example.roncherian.musicsearch;
//Ron Abraham Cherian - 801028678
//Arun Thomas Kunnumpuram
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends MenuBarActivity implements  MusicSearchAsyncTask.IMusicTrack{

    private static SharedPreferences sharedPreferences = null;

    private static SharedPreferences.Editor editor = null;

    public static  String SHOW_RESULTS = "showResults";

    public  static int MAIN_ACTIVITY_REQUEST_CODE = 1000;

    //http://ws.audioscrobbler.com/2.0/?method=track.search&track=Believe&api_key=b395df93fa2168371596bb1a129d34b1&format=json
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = this.getSharedPreferences("FAVORITE", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        setTitle("Music Search");
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

        final EditText searchText = (EditText)findViewById(R.id.editTextSearchMusic);

        final Button searchButton =(Button)findViewById(R.id.buttonSearch);

        final ArrayList<MusicTrack>musics = get("FAVORITE");

        ListView resultsListView = (ListView)findViewById(R.id.listViewFavorites);
        FavoritesListAdapter favoritesListAdapter = new FavoritesListAdapter(this, R.layout.music_track_listing_row, musics);
        resultsListView.setAdapter(favoritesListAdapter);
        favoritesListAdapter.setNotifyOnChange(true);


        resultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("demo","Clicked Row Item");
                Intent intent = new Intent(MainActivity.this, TrackDetailsActivity.class);
                intent.putExtra(SearchResultsActivity.TRACK_DETAILS_ACTIVITY_INTENT,musics.get(i));
                startActivity(intent);

            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchText.getText().toString().length() == 0){
                    Toast.makeText(MainActivity.this,"Please enter a valid search text to search",Toast.LENGTH_SHORT).show();
                }else if (checkInternetPermission()){
                    new MusicSearchAsyncTask(MainActivity.this).execute("http://ws.audioscrobbler.com/2.0/",searchText.getText().toString());
                }
            }
        });


        Log.d("demo","Executed");
    }



    @Override
    public void showResults(ArrayList<MusicTrack> results) {

        Intent intent = new Intent(this, SearchResultsActivity.class);
        intent.putExtra(SHOW_RESULTS,results);
        startActivityForResult(intent,MAIN_ACTIVITY_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == MAIN_ACTIVITY_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                final ArrayList<MusicTrack>musics = get("FAVORITE");

                ListView resultsListView = (ListView)findViewById(R.id.listViewFavorites);
                FavoritesListAdapter favoritesListAdapter = new FavoritesListAdapter(this, R.layout.music_track_listing_row, musics);
                resultsListView.setAdapter(favoritesListAdapter);
                favoritesListAdapter.setNotifyOnChange(true);


                resultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Log.d("demo","Clicked Row Item");
                        Intent intent = new Intent(MainActivity.this, TrackDetailsActivity.class);
                        intent.putExtra(SearchResultsActivity.TRACK_DETAILS_ACTIVITY_INTENT,musics.get(i));
                        startActivity(intent);

                    }
                });
            }
        }
    }

    @Override
    public void startProgressbar() {

    }

    private boolean checkInternetPermission(){

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getActiveNetworkInfo() == null){
            Toast.makeText(MainActivity.this,"Please check your internet connection",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public <T> void setList(String key, List<MusicTrack> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);

        set(key, json);
    }

    public static void set(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public static ArrayList<MusicTrack> get(String key){
        String val = sharedPreferences.getString(key, null);
        if (val==null){
            return  new ArrayList<MusicTrack>();
        }

            /*JSONArray myJson = new JSONArray(val);
            List<MusicTrack>t = (List<MusicTrack>)myJson;*/
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<MusicTrack>>(){}.getType();

        ArrayList<MusicTrack> mMusicObject = gson.fromJson(val, type);

        return mMusicObject;

    }
}

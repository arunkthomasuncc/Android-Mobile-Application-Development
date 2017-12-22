package com.example.arun.moviedb;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements MovieSearchAsync.IMusicData{


    ArrayList<Movie> movieResults= new ArrayList<Movie>();
    //eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJkZTRkNTZkZGZjMzNjOWQwYTdiNTVlYzcwN2ZmMGU4OCIsInN1YiI6IjU5ZTUzMzkzOTI1MTQxMGFlMDAwMjQ1ZCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.JAtybl243BsA61HCUHbdzgFF95yapmFZaE1eSuvoO74
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Search Movies");

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }
    }
    public void search(View view)
    {
        if(!isNetworkConnected())
        {
            Toast.makeText(getApplicationContext(),"Please check your internet connection",Toast.LENGTH_LONG).show();
            return;
        } else {



            EditText searchText = (EditText) findViewById(R.id.editTextSearchMovie);
            if (searchText.length() > 0) {
                String textSearch = searchText.getText().toString().trim();
                // Log.d("Demo","Search Text"+textSearch);
                new MovieSearchAsync(MainActivity.this).execute(textSearch);

            } else {
                Toast.makeText(this, "Please enter a Track", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_raw_layout, menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_sortbyrating:
                // search action
                if(movieResults.size()>0) {
                    Collections.sort(movieResults, Movie.sortByRating);
                    ListView listView = (ListView) findViewById(R.id.listviewResults);


                    // ArrayAdapter<Color> adapter= new ArrayAdapter<Color>(this,android.R.layout.simple_list_item_1,colors);
                    MovieAdapter adapter = new MovieAdapter(this, R.layout.result_raw_layout, movieResults);
                    listView.setAdapter(adapter);
                    adapter.setNotifyOnChange(true);
                }
                return true;
            case R.id.action_sortbypopularity:

                if(movieResults.size()>0) {
                    Collections.sort(movieResults, Movie.sortByPopularity);
                    ListView listView = (ListView) findViewById(R.id.listviewResults);


                    // ArrayAdapter<Color> adapter= new ArrayAdapter<Color>(this,android.R.layout.simple_list_item_1,colors);
                    MovieAdapter adapter = new MovieAdapter(this, R.layout.result_raw_layout, movieResults);
                    listView.setAdapter(adapter);
                    adapter.setNotifyOnChange(true);
                }

                // search action
                return true;
            case R.id.action_showfavorites:
                // search action

                Intent intentforFavorite= new Intent(MainActivity.this,FavoriteMoviesActivity.class);
               // intent.putExtra(TRACK_LIST,tracksResult);
                startActivity(intentforFavorite);
                return true;

            case R.id.action_quit:
                // location found
                // finish();
                //System.exit(0);
                // return true;
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setMovieSearchResult(ArrayList<Movie> movies) {

        movieResults=movies;
        if(movieResults.size()>0) {
            ListView listview = (ListView) findViewById(R.id.listviewResults);
            MovieAdapter adapter = new MovieAdapter(this, R.layout.result_raw_layout, movieResults);
            listview.setAdapter(adapter);
            adapter.setNotifyOnChange(true);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    // Log.d("Demo","clicked"+i);
                    //Log.d("value","Value is "+colors.get(i).toString());

                    Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
                    intent.putExtra("MOVIE", movieResults.get(i));
                    startActivity(intent);

                }
            });

        }
        else
        {
            Toast.makeText(MainActivity.this,"No results found", Toast.LENGTH_LONG).show();

            movieResults=null;
            ListView listview = (ListView) findViewById(R.id.listviewResults);
            listview.setAdapter(null);
          /*  ListView listview = (ListView) findViewById(R.id.listviewResults);
            MovieAdapter adapter = new MovieAdapter(this, R.layout.result_raw_layout, movieResults);
            listview.setAdapter(adapter);
            adapter.setNotifyOnChange(true);*/

        }
    }
}

package com.example.arun.moviedb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Collections;
import java.util.List;

public class FavoriteMoviesActivity extends AppCompatActivity {


    SharedPreference sharedPreference;
    List<Movie> favorites;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_movies);
        setTitle("Favorite Movies");
        sharedPreference = new SharedPreference();
        favorites = sharedPreference.getFavorites(this);
        if (favorites == null) {

        }
        else
        {
            ListView listview= (ListView)findViewById(R.id.listViewFavorites);
            MovieAdapter adapter= new MovieAdapter(this,R.layout.result_raw_layout,favorites,true);
            listview.setAdapter(adapter);
            adapter.setNotifyOnChange(true);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    // Log.d("Demo","clicked"+i);
                    //Log.d("value","Value is "+colors.get(i).toString());

                    Intent intent= new Intent(FavoriteMoviesActivity.this,MovieDetailsActivity.class);
                    intent.putExtra("MOVIE",favorites.get(i));
                    startActivity(intent);

                }
            });
        }


    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.favorite_menu_layout, menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_sortbyrating:
                // search action
                if(favorites.size()>0) {
                    Collections.sort(favorites, Movie.sortByRating);
                    ListView listView = (ListView) findViewById(R.id.listviewResults);


                    // ArrayAdapter<Color> adapter= new ArrayAdapter<Color>(this,android.R.layout.simple_list_item_1,colors);
                    MovieAdapter adapter = new MovieAdapter(this, R.layout.result_raw_layout, favorites);
                    listView.setAdapter(adapter);
                    adapter.setNotifyOnChange(true);
                }
                return true;
            case R.id.action_sortbypopularity:
                // search action
                if(favorites.size()>0) {
                    Collections.sort(favorites, Movie.sortByPopularity);
                    ListView listView = (ListView) findViewById(R.id.listviewResults);


                    // ArrayAdapter<Color> adapter= new ArrayAdapter<Color>(this,android.R.layout.simple_list_item_1,colors);
                    MovieAdapter adapter = new MovieAdapter(this, R.layout.result_raw_layout, favorites);
                    listView.setAdapter(adapter);
                    adapter.setNotifyOnChange(true);
                }
            case R.id.action_home:


                // search action

                Intent intenthome = new Intent(this,MainActivity.class);
                startActivity(intenthome);

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
}

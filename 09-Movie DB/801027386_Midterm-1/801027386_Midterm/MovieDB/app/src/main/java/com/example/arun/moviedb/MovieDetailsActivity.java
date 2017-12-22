package com.example.arun.moviedb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {
Movie movie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

      setTitle("Movie Details");
        movie=(Movie)getIntent().getSerializableExtra("MOVIE");
        ImageView imageView= (ImageView)findViewById(R.id.imageViewMovieBig);
        Picasso.with(MovieDetailsActivity.this).load(movie.getImagebaseURLLarge()).into(imageView);
        TextView textViewNameResult=(TextView)findViewById(R.id.textViewTitleResult);
        textViewNameResult.setText(movie.getTitle());
        TextView overViewNameResult=(TextView)findViewById(R.id.textViewOverViewResult);
        overViewNameResult.setText(movie.getOverview());
        TextView releaseDateResult=(TextView)findViewById(R.id.textViewReleaseDateResult);
        releaseDateResult.setText(movie.getReleasedate());

        TextView rate=(TextView)findViewById(R.id.textViewRatingResult);
        rate.setText(movie.getRatingnew()+"/10");


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.moviedetails_menu_layout, menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
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

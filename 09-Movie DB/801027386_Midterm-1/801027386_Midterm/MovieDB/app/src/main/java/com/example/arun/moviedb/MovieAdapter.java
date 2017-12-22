package com.example.arun.moviedb;

/**
 * Created by Arun on 10/16/2017.
 */

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import static android.R.drawable.btn_star;
import static android.R.drawable.btn_star_big_on;

/**
 * Created by Arun on 10/8/2017.
 */

public class MovieAdapter extends ArrayAdapter<Movie> {

    List<Movie> trackList;
    int resource;
    Context context;
    SharedPreference sharedPreference;
    boolean isfromFavorites;
    //boolean isfromMainActivity;

    public MovieAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Movie> objects) {
        super(context, resource, objects);
        Log.d("Context",""+context);
        this.context = context;
        this.trackList = objects;
        this.resource = resource;
       this.isfromFavorites=false;
    }
    public MovieAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Movie> objects,boolean isfromFavorites) {
        super(context, resource, objects);
        Log.d("Context",""+context);
        this.context = context;
        this.trackList = objects;
        this.resource = resource;
        this.isfromFavorites=isfromFavorites;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final int position1=position;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(resource, parent, false);

        }
        ImageView imageView=convertView.findViewById(R.id.imageViewMovie);
        Picasso.with(context).load(trackList.get(position).getImagebaseURLsmall()).into(imageView);
        //http://image.tmdb.org/t/p/w154/kBf3g9crrADGMc2AMAMlLBgSm2h.jpg

        //Picasso.with(context).load("http://image.tmdb.org/t/p/w154/kBf3g9crrADGMc2AMAMlLBgSm2h.jpg").into(imageView);
        TextView textViewName = convertView.findViewById(R.id.textViewName);
        TextView textViewArtist = convertView.findViewById(R.id.textViewReleased);
        textViewName.setText(trackList.get(position).getTitle());
        if(trackList.get(position).getReleasedate().length()>0) {

            String[] year=trackList.get(position).getReleasedate().split("-");

            textViewArtist.setText("Released in " + year[0]);
        }
        final ImageButton favoriteButton =convertView.findViewById(R.id.imageButtonFavorite);
        sharedPreference=new SharedPreference();

        if (checkFavoriteItem(trackList.get(position))) {
            favoriteButton.setTag("gold");
            favoriteButton.setImageResource(btn_star_big_on);
        } else {
            favoriteButton.setTag("silver");
            favoriteButton.setImageResource(btn_star);
        }
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(favoriteButton.getTag().equals("silver"))
                {
                    favoriteButton.setTag("gold");
                    favoriteButton.setImageResource(btn_star_big_on);
                    sharedPreference.addFavorite(context, trackList.get(position1));
                    Toast.makeText(context,"Marked as favorite",Toast.LENGTH_SHORT).show();
                    //favoriteButton.setBackgroundResource(btn_star_big_on);
                }
                else
                {
                    favoriteButton.setTag("silver");
                    favoriteButton.setImageResource(btn_star);
                    sharedPreference.removeFavorite(context, trackList.get(position1));

                   if(isfromFavorites)
                    {
                        trackList.remove(position);
                        notifyDataSetChanged();
                    }

                    Toast.makeText(context,"Removed from favorite",Toast.LENGTH_SHORT).show();
                }

            }
        });
        return convertView;

    }
    public boolean checkFavoriteItem(Movie checkTrack) {
        boolean check = false;
        List<Movie> favorites = sharedPreference.getFavorites(context);
        if (favorites != null) {
            for (Movie track : favorites) {
                if (track.equals(checkTrack)) {
                    check = true;
                    break;
                }
            }
        }
        return check;
    }

}


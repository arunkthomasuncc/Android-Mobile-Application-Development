package com.example.roncherian.musicsearch;
//Ron Abraham Cherian - 801028678
//Arun Thomas Kunnumpuram
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by roncherian on 07/10/17.
 */

public class MusicTracksAdapter extends ArrayAdapter<MusicTrack> {

    private static SharedPreferences sharedPreferences = null;

    private static SharedPreferences.Editor editor = null;

    Context mContext;
    int mResource;
    ArrayList<MusicTrack> mObjects;

    public MusicTracksAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<MusicTrack> objects) {
        super(context, resource, objects);
        this.mContext = context;//context.getClass().getSimpleName().equals("SearchResultsActivity")
        this.mResource = resource;
        this.mObjects = objects;
        sharedPreferences = context.getSharedPreferences("FAVORITE", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(mResource,parent,false);
        }

        final MusicTrack musicTrack = (MusicTrack)mObjects.get(position);
        TextView artistNameTextView = (TextView)convertView.findViewById(R.id.textViewArtistOfTrack);
        artistNameTextView.setText(musicTrack.getArtist());
        TextView trackNameTextView = (TextView)convertView.findViewById(R.id.textViewNameOfTrack);
        trackNameTextView.setText(musicTrack.getName());

        ImageView musicTrackImageView = (ImageView)convertView.findViewById(R.id.imageViewMusicTrack);
        if (null!=musicTrack && null!=musicTrack.getSmallImageUrl() &&musicTrack.getSmallImageUrl().length()>0){
            Picasso.with(this.mContext).load(musicTrack.getSmallImageUrl()).into(musicTrackImageView);
        }


        final ImageButton favoriteImageButton = (ImageButton)convertView.findViewById(R.id.imageButtonFavorite);

        List<MusicTrack>favoriteMusicTracks = get("FAVORITE");
        if (favoriteMusicTracks.contains(musicTrack)){
            musicTrack.setFavorite(true);
            favoriteImageButton.setImageResource(android.R.drawable.btn_star_big_on);
        } else {
            favoriteImageButton.setImageResource(android.R.drawable.btn_star_big_off);
            musicTrack.setFavorite(false);
        }
        mObjects.set(position,musicTrack);


        favoriteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("demo","Image Button Click recognized");
                boolean isFavorite = musicTrack.getFavorite();
                musicTrack.setFavorite(!isFavorite);
                if (isFavorite){
                    favoriteImageButton.setImageResource(android.R.drawable.btn_star_big_off);
                    List<MusicTrack>musics = get("FAVORITE");
                    musics.remove(musicTrack);
                    setList("FAVORITE",musics);
                    Toast.makeText(mContext,"You have removed the track: '"+musicTrack.getName()+"' from favorites list",Toast.LENGTH_SHORT).show();
                } else {
                    favoriteImageButton.setImageResource(android.R.drawable.btn_star_big_on);
                    List<MusicTrack>musics = get("FAVORITE");
                    if (!musics.contains(musicTrack)){
                        musics.add(musicTrack);
                        setList("FAVORITE",musics);
                    }
                    Toast.makeText(mContext,"You have favorited the track: "+musicTrack.getName(),Toast.LENGTH_SHORT).show();
                }


            }
        });
        return convertView;
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

    public static List<MusicTrack> get(String key){
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

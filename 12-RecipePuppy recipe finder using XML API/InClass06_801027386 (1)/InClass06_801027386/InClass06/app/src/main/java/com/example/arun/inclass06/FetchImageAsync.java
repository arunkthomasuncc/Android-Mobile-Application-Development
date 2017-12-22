package com.example.arun.inclass06;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Arun on 10/2/2017.
 */

public class FetchImageAsync extends AsyncTask<String, Void,Bitmap> {


    IDataImage activity;
    int count;

    public FetchImageAsync(IDataImage activity) {
        this.activity = activity;
    }
    @Override
    protected Bitmap doInBackground(String... params) {
        //RequestParams requestParams=new RequestParams("GET",params[0]);
        //HttpURLConnection httpURLConnection=null;
        Bitmap bitmap=null;
        try {
            URL url = new URL(params[0]);
            HttpURLConnection connection= (HttpURLConnection)url.openConnection();
            bitmap= BitmapFactory.decodeStream(connection.getInputStream());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        activity.setUpImage(bitmap);
    }

    static public interface IDataImage
    {
        public void setUpImage(Bitmap upImage);

    }
}
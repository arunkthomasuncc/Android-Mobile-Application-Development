package com.example.arun.trivia;
//Ron Abraham CHerina - 801028678
//Arun Thomas
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by roncherian on 02/10/17.
 */

public class FetchImageAsync extends AsyncTask<String, Void, byte[]>{

    IQuestionImage iData;

    String index = "";

    public FetchImageAsync(IQuestionImage iData) {
        this.iData = iData;
    }

    @Override
    protected void onPostExecute(byte[] bytes) {

        iData.showImage(bytes, index);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        iData.startProgressBar();
    }

    @Override
    protected byte[] doInBackground(String... strings) {

        try {
            URL url= new URL(strings[0]);
            index = strings[1];
            HttpURLConnection connection= (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            if(connection.getResponseCode()==HttpURLConnection.HTTP_OK)
            {
                    InputStream inputStream = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100, byteArrayOutputStream);
                    return byteArrayOutputStream.toByteArray();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
        }
        return null;
    }

    public  interface IQuestionImage
    {
        void showImage(byte[] bytes, String index);
        void startProgressBar();
    }
}

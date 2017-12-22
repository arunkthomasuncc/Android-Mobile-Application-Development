package com.example.arun.trivia;
//Ron Abraham CHerina - 801028678
//Arun Thomas
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Arun on 9/28/2017.
 */

public class FetchQuestionAsync extends AsyncTask<String,Void,ArrayList<Question>> {

   IQuestion mainActivity;

    public FetchQuestionAsync(IQuestion question) {
        this.mainActivity=question;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mainActivity.startProgressbar();

    }

    @Override
    protected void onPostExecute(ArrayList<Question> questions) {
       // super.onPostExecute(questions);
       // mainActivity.stopProgressbar();
        mainActivity.setupQuestions(questions);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected ArrayList<Question> doInBackground(String... strings) {

        ArrayList<Question> questions=new ArrayList<Question>();
        try {
            URL url= new URL(strings[0]);
            HttpURLConnection connection= (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            if(connection.getResponseCode()==HttpURLConnection.HTTP_OK)
            {
                StringBuilder sb= new StringBuilder();
                String line="";
                BufferedReader br= new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while((line=br.readLine())!= null)
                {
                     sb.append(line);

                }
               // Log.d("response",sb.toString());
                questions=QuestionUtil.parser(sb.toString());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return questions;
    }
    public  interface IQuestion
    {
        void setupQuestions(ArrayList<Question> questions);
        void startProgressbar();
        //void stopProgressbar();
    }
}

package com.example.roncherian.recipes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by roncherian on 25/09/17.
 */

public class GetDataAsyncTask extends AsyncTask<List<String>,List<Recipe>,List<Recipe>> {

    MainActivity activity;
    public GetDataAsyncTask(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        /*activity.progressBar = new ProgressBar(activity);
        activity.progressBar.setIndeterminate(true);
        activity.progressBar.setVisibility(View.VISIBLE);*/
        activity.dialog = new ProgressDialog(activity);
        activity.dialog.setCancelable(true);
        activity.dialog.setIndeterminate(true);
        activity.dialog.setMessage("Loading...");
        activity.dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        activity.dialog.show();
    }

    @Override
    protected void onPostExecute(List<Recipe> recipes) {
        super.onPostExecute(recipes);

        /*activity.progressBar.setMax(10);
        for (int i=0; i<10; i++){
            activity.progressBar.setProgress(i);

        }*/
        ArrayList<Recipe>newRecipeList = new ArrayList<Recipe>();
        newRecipeList = (ArrayList<Recipe>) recipes;

        if (newRecipeList.size() == 0){
            activity.dialog.dismiss();
            Toast.makeText(activity,"There were no recipes found",Toast.LENGTH_LONG).show();

            return;
        }else {
            Intent intent = new Intent(activity, RecipeActivity.class);
            intent.putExtra("RecipeList",newRecipeList);
            activity.dialog.dismiss();
            activity.startActivity(intent);
            //return recipes;
        }


    }

    @Override
    protected void onProgressUpdate(List<Recipe>... values) {

        super.onProgressUpdate(values);
    }

    @Override
    protected List<Recipe> doInBackground(List<String>... lists) {

        {

            List<String>recipeList = lists[0];
            String dish = recipeList.get(0);
            StringBuilder ingredients = new StringBuilder();
            recipeList.remove(0);
            for(String recipe: recipeList){
                if (ingredients.length() != 0)
                    ingredients.append(",").append(recipe);
                else{
                    ingredients.append(recipe);
                }
            }
            Log.d("demoFound",ingredients.toString() + dish);


            RequestParams requestParams = new RequestParams("GET","http://www.recipepuppy.com/api");
            requestParams.addParam("q",dish);
            requestParams.addParam("i",ingredients.toString());

            HttpURLConnection connection = (HttpURLConnection) requestParams.setupConnection();
            BufferedReader bufferedReader = null;
            StringBuilder stringBuilder = new StringBuilder();
            try {


                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line = "";
                    while (null != (line = bufferedReader.readLine())){
                        stringBuilder.append(line);
                    }

                    Log.d("demo",stringBuilder.toString());

                    return RecipeUtil.RecipeJSONParser.parseRecipe(stringBuilder.toString());

                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }
    }
}

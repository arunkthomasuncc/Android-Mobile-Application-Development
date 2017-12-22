package com.example.arun.inclass06;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arun on 10/2/2017.
 */

public class RecipeUtil {

    static public class RecipeJSONParser{
        static List<Recipe> parseRecipe(String in) throws JSONException {

            ArrayList<Recipe> recipeList = new ArrayList<Recipe>();
            JSONObject jsonObject = new JSONObject(in);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int i=0; i< jsonArray.length(); i++){

                JSONObject jsonRecipeObject = jsonArray.getJSONObject(i);

                Recipe recipe = new Recipe();

                recipe.setTitle(jsonRecipeObject.getString("title"));
                recipe.setUrl(jsonRecipeObject.getString("href"));
                recipe.setIngredients(jsonRecipeObject.getString("ingredients"));

                String thumbnail = jsonRecipeObject.getString("thumbnail");

                if (!thumbnail.equals("")){
                    try {

                        URL url = new URL(thumbnail);
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.setRequestMethod("GET");
                        httpURLConnection.connect();
                        if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            InputStream inputStream = httpURLConnection.getInputStream();
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);


                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                            byte[] bytes = byteArrayOutputStream.toByteArray();

                            recipe.setImageByteArray(bytes);
                        }
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                /*try {
                    URL url = new URL(jsonObject.getString("thumbnailUrl"));
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.connect();
                    if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                        InputStream inputStream = httpURLConnection.getInputStream();
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream,null,options);


                        album.setPicture(bitmap);
                        inputStream.close();
                    }


                    BitmapDrawable bitmapDrawable = (BitmapDrawable) contactImageView.getDrawable();
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] bytes = byteArrayOutputStream.toByteArray();
                    contact.setContactImage(bytes);



                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

                recipeList.add(recipe);


            }
            return  recipeList;
        }


    }
}


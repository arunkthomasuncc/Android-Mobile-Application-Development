package com.example.arun.inclass06;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Arun on 10/2/2017.
 */

public class RecipeXMLUtil {
    static public class PullParser {



        static public ArrayList<Recipe> parsePerson(InputStream inputStream)
        {
            Recipe recipe=null;
            ArrayList<Recipe> recipeList=new ArrayList<Recipe>();
            try {
                XmlPullParser parser= XmlPullParserFactory.newInstance().newPullParser();
                parser.setInput(inputStream,"UTF=8");
                int eventType=parser.getEventType();
                while(eventType != XmlPullParser.END_DOCUMENT) {

                    switch (eventType)
                    {

                        case XmlPullParser.START_TAG :
                            if(parser.getName().equals("recipe"))
                            {
                                recipe= new Recipe();
                            }
                            if(parser.getName().equals("title"))
                            {
                                recipe.setTitle(parser.nextText().toString().trim());
                            }
                            if(parser.getName().equals("href"))
                            {
                                recipe.setUrl(parser.nextText().toString().trim());
                            }
                            if(parser.getName().equals("ingredients"))
                            {
                                recipe.setIngredients(parser.nextText().toString().trim());

                            }

                            break;
                        case XmlPullParser.END_TAG:

                            if(parser.getName().equals("recipe"))
                            {
                               Log.d("end","Hello");
                                Log.d("recipe",recipe.toString());
                                recipeList.add(recipe);
                                recipe=null;
                            }
                            break;

                    }
                    eventType=parser.next();
                }

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

Log.d("Demo","Hii");
            Log.d("list",recipeList.toString());
            return recipeList;
        }


    }


}

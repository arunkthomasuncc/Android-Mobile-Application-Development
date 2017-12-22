package com.example.arun.trivia;
//Ron Abraham CHerina - 801028678
//Arun Thomas
import android.text.style.QuoteSpan;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Arun on 9/28/2017.
 */

public class QuestionUtil {

    public static ArrayList<Question>  parser(String questionJSON)
    {
        ArrayList<Question> questionArray= new ArrayList<Question>();
        try {
            int i;
            JSONObject jsonObject= new JSONObject(questionJSON);
            JSONArray questions=jsonObject.getJSONArray("questions");
            for(i=0;i<questions.length();i++)
            {
                Question question= new Question();
                JSONObject questionJsonObject=  questions.getJSONObject(i);
                question.setId(questionJsonObject.getInt("id"));
                question.setText(questionJsonObject.getString("text"));
                if(questionJsonObject.has("image")) {
                    question.setImageURL(questionJsonObject.getString("image"));
                }
                JSONObject choices=questionJsonObject.getJSONObject("choices");
                question.setAns(choices.getInt("answer"));
                JSONArray choicesList=choices.getJSONArray("choice");
                ArrayList<String> quechoices= new ArrayList<String>();
                for(int j=0;j<choicesList.length();j++)
                {
                    quechoices.add((String)choicesList.get(j));
                }
                question.setChoices(quechoices);
                questionArray.add(question);
            }
            Log.d("Ivalue",i +"");
            Log.d("hi","hiji");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return questionArray;

    }

}

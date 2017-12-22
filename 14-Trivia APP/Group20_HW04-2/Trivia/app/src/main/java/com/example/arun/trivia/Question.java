package com.example.arun.trivia;
//Ron Abraham CHerina - 801028678
//Arun Thomas
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arun on 9/28/2017.
 */

public class Question implements Serializable{
    int id;
    String text;
    String imageURL;
    int ans;
    int clickedAnswer;

    byte[] imageByteArray;

    public byte[] getImageByteArray() {
        return imageByteArray;
    }

    public void setImageByteArray(byte[] imageByteArray) {
        this.imageByteArray = imageByteArray;
    }

    public ArrayList<String> getChoices() {
        return choices;
    }

    public void setChoices(ArrayList<String> choices) {
        this.choices = choices;
    }

    ArrayList<String> choices;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getAns() {
        return ans;
    }

    public void setAns(int ans) {
        this.ans = ans;
    }

    public int getClickedAnswer() {
        return clickedAnswer;
    }

    public void setClickedAnswer(int clickedAnswer) {
        this.clickedAnswer = clickedAnswer;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", ans=" + ans +
                ", choices=" + choices +
                '}';
    }
}




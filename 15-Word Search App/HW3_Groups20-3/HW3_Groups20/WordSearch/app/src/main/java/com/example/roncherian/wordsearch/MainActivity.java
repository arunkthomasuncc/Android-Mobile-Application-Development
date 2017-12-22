package com.example.roncherian.wordsearch;
//Ron Abraham Cherian
//Arun Thomas
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    int count = 0;
    List<String>textListToSearch = new ArrayList<String>();
    final static String FOUND_WORDS_ARRAY = "foundWordsArray";
    boolean matchCase = false;

    ProgressBar progressBar;

    static  final  String YES = "YES";
    static  final  String NO = "NO";


    static enum  BUTTON_TYPE  { ADD, REMOVE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Search Words");

        final LinearLayout parentView = (LinearLayout) findViewById(R.id.editPage);

        LayoutInflater inflator1 = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        LinearLayout linearLayout = (LinearLayout) inflator1.inflate(R.layout.search_word_add_layout, null);


        Button button = (Button) linearLayout.findViewById(R.id.buttonAdd);
        button.setTag(BUTTON_TYPE.ADD);
        parentView.addView(linearLayout);


        addOrRemoveView(parentView,button);

        findViewById(R.id.checkBoxMatchCases).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                matchCase = !matchCase;
            }
        });

        Button searchButton = (Button)findViewById(R.id.buttonSearch);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textListToSearch = new ArrayList<String>();
                if (matchCase){
                    textListToSearch.add(YES);
                } else {
                    textListToSearch.add(NO);
                }

                for (int i = 0; i<=count; i++){
                    LinearLayout layout = (LinearLayout) parentView.getChildAt(i+1);
                    EditText editText = (EditText)layout.findViewById(R.id.editTextSearch);
                    String text = editText.getText().toString().trim();

                    if (text.length() > 0){
                        //Log.d("demo",text);
                        textListToSearch.add(text);
                    }

                }
                new SearchString().execute(textListToSearch);
            }
        });

    }

    private class SearchString extends AsyncTask<List<String>,Integer,List<SpannableStringBuilder>>{

        @Override
        protected void onProgressUpdate(Integer... values) {
            //super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);


        }


        @Override
        protected List<SpannableStringBuilder> doInBackground(List<String>... lists) {

            List<String>stringListToSearch = lists[0];
            List<SpannableStringBuilder>foundStrings = new ArrayList<SpannableStringBuilder>();
            String searchingText = readStringFromFile();
            char[] searchingTextCharacterArray = searchingText.toCharArray();
            String matchCase = stringListToSearch.get(0);
            stringListToSearch.remove(0);
            float count = 0;
            int[] colorArray = {Color.RED, Color.GREEN, Color.BLUE};
            int colorCount=0;
            for (String searchString : stringListToSearch){
                count++;
                String patternToMatch = "\\s"+searchString+"\\s";
                Pattern stringPattern = null;
                if (matchCase.equals(YES)){
                    stringPattern = Pattern.compile(patternToMatch);
                } else {
                    stringPattern = Pattern.compile(patternToMatch, Pattern.CASE_INSENSITIVE);
                }

                Matcher matcher;
                matcher = stringPattern.matcher(searchingText);

                while (matcher.find()){
                    if (matcher.start()< 11){
                        SpannableStringBuilder builder = new SpannableStringBuilder();
                        SpannableString str1= new SpannableString(searchingText.substring(matcher.start(),matcher.start()+32));
                        str1.setSpan(new ForegroundColorSpan(colorArray[colorCount]),0,searchString.length()+1,0);
                        builder.append(str1);
                        foundStrings.add(builder);
                        /*String stringToGive = searchString+searchingText.substring(matcher.end(),matcher.end()+30);
                        foundStrings.add(stringToGive);*/
                    } else if((searchingText.length() - matcher.end() < 11)){
                        //String stringToGive = searchingText.substring(matcher.start() - 30, matcher.end());
                        String stringToGive = searchingText.substring(matcher.end() - 32,matcher.end());
                        SpannableStringBuilder builder = new SpannableStringBuilder();
                        SpannableString str1= new SpannableString(searchingText.substring(matcher.start() - 30,matcher.end()));
                        str1.setSpan(new ForegroundColorSpan(colorArray[colorCount]),30,searchString.length()+31,0);
                        builder.append(str1);
                        foundStrings.add(builder);
                        //foundStrings.add(stringToGive);
                    } else {
                        String stringToGive = "";
                        if (searchString.length()<=21){
                            stringToGive = "..."+searchingText.substring(matcher.start()-9,matcher.end()+19-searchString.length())+"...";
                        } else {
                            stringToGive = "..."+searchingText.substring(matcher.start()-9,matcher.end());
                        }

                        //stringToGive = "..."+searchingText.substring(matcher.start() - 10,matcher.end() + 10)+"...";
                        SpannableStringBuilder builder = new SpannableStringBuilder();
                        SpannableString str1= new SpannableString(stringToGive);
                        str1.setSpan(new ForegroundColorSpan(colorArray[colorCount]),13,searchString.length()+14,0);
                        builder.append(str1);
                        foundStrings.add(builder);

                        //foundStrings.add(stringToGive);
                    }

                }
                publishProgress(Math.round((count/stringListToSearch.size())*100));
                colorCount++;
                if (colorCount == 3){
                    colorCount = 0;
                }
            }
            return foundStrings;
        }

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            progressBar = (ProgressBar)findViewById(R.id.progressBar);
            progressBar.setMax(100);
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(List<SpannableStringBuilder> strings) {
            //super.onPostExecute(strings);
            /*for (int i=0;i<strings.size();i++){
                Log.d("demoFound",strings.get(i));
            }*/
            Intent intent = new Intent(MainActivity.this, WordsFound.class);
            intent.putExtra(FOUND_WORDS_ARRAY, (ArrayList<SpannableStringBuilder>)strings);
            //intent.putStringArrayListExtra(FOUND_WORDS_ARRAY,(ArrayList<SpannableStringBuilder>) strings);
            progressBar.setVisibility(View.INVISIBLE);
            progressBar.setProgress(0);
            startActivity(intent);

        }


    }

    private void  addOrRemoveView(final LinearLayout parentView, Button button){

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button parentButton = (Button)view;


                if (parentButton.getTag() == BUTTON_TYPE.ADD){

                    parentButton.setTag(BUTTON_TYPE.REMOVE);
                    parentButton.setBackgroundResource(R.drawable.remove);
                    LinearLayout parentView = (LinearLayout) findViewById(R.id.editPage);
                    LayoutInflater inflator1 = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    LinearLayout linearLayout = (LinearLayout) inflator1.inflate(R.layout.search_word_add_layout, null);
                    linearLayout.setTag(++count);
                    EditText searchText = (EditText)linearLayout.findViewById(R.id.editTextSearch);
                    Button addOrRemoveButton = (Button)linearLayout.findViewById(R.id.buttonAdd);


                    if (count >= 19){
                        addOrRemoveButton.setTag(BUTTON_TYPE.REMOVE);
                        addOrRemoveButton.setBackgroundResource(R.drawable.remove);
                    } else {
                        addOrRemoveButton.setTag(BUTTON_TYPE.ADD);
                    }

                    parentView.addView(linearLayout);
                    addOrRemoveView(parentView,addOrRemoveButton);

                } else if (parentButton.getTag() == BUTTON_TYPE.REMOVE){
                    parentButton.setTag(BUTTON_TYPE.ADD);
                    parentButton.setBackgroundResource(R.drawable.add);
                    LinearLayout layout = (LinearLayout) parentButton.getParent();
                    parentView.removeView(layout);
                    if (count == 19){
                        LinearLayout layout2 = (LinearLayout) parentView.getChildAt(count);
                        Button newButton = layout2.findViewById(R.id.buttonAdd);
                        newButton.setTag(BUTTON_TYPE.ADD);
                        newButton.setBackgroundResource(R.drawable.add);
                    }
                    count--;
                }
            }
        });
    }


    private String readStringFromFile(){
        try{
            InputStream inputStream = getAssets().open("textfile.txt");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String text = new String(buffer);
            text = text.trim().replaceAll("\n"," ");
            //System.out.print(text);
            //Log.d("demo",text);
            return text;
        }catch (Exception e){
            e.printStackTrace();
        }

        return "";

    }


}

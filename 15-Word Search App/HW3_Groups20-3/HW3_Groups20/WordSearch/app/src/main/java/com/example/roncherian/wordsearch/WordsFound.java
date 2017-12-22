package com.example.roncherian.wordsearch;
//Ron Abraham Cherian
//Arun Thomas
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class WordsFound extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_found);
        setTitle("Words Found");

        //ArrayList<String> foundWordsArrayList = getIntent().getExtras().getStringArrayList(MainActivity.FOUND_WORDS_ARRAY);
        ArrayList<SpannableStringBuilder> foundWordsArrayList = (ArrayList<SpannableStringBuilder>) getIntent().getExtras().getSerializable(MainActivity.FOUND_WORDS_ARRAY);
        Log.d("demo",foundWordsArrayList.size()+"");

        LinearLayout layout = (LinearLayout) findViewById(R.id.lineralLayoutForText);
        LayoutInflater inflator1 = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        /*for (SpannableStringBuilder string: foundWordsArrayList){

            LinearLayout linearLayout = (LinearLayout) inflator1.inflate(R.layout.found_words_layout, null);
            TextView textView = (TextView) linearLayout.findViewById(R.id.textViewFoundWords);
            textView.setText(string);
            layout.addView(linearLayout);
        }*/
        if (foundWordsArrayList.size() == 0){
            LinearLayout linearLayout = (LinearLayout) inflator1.inflate(R.layout.found_words_layout, null);
            TextView textView = (TextView) linearLayout.findViewById(R.id.textViewFoundWords);
            textView.setText("NO WORDS FOUND");
            layout.addView(linearLayout);
        }else {
            for (int i=0; i<foundWordsArrayList.size();i++){
                SpannableString string = SpannableString.valueOf(foundWordsArrayList.get(i));
                LinearLayout linearLayout = (LinearLayout) inflator1.inflate(R.layout.found_words_layout, null);
                TextView textView = (TextView) linearLayout.findViewById(R.id.textViewFoundWords);
                textView.setText(string);
                layout.addView(linearLayout);

            }
        }


        final Button finish = (Button)findViewById(R.id.buttonFinish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}

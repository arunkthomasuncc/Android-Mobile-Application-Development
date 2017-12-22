package com.example.arun.trivia;
//Ron Abraham CHerina - 801028678
//Arun Thomas
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.net.InetAddress;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FetchQuestionAsync.IQuestion{

    static String QUESTIONS_LIST = "QUESTIONS_LIST";

    static int MAIN_ACTIVITY_INTENT_REQUEST_CODE = 100;

    static String ACTIVITY_TO_GO = "ACTIVITY_TO_GO";

    static String MAIN_ACTIVITY = "MAIN";

    ArrayList<Question> questionList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        String url = "http://dev.theappsdr.com/apis/trivia_json/index.php";

        Button buttonStart = (Button)findViewById(R.id.buttonStart);
        Button buttonExit = (Button)findViewById(R.id.buttonExit);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent questionsIntent = new Intent(MainActivity.this, QuestionsActivity.class);
                questionsIntent.putExtra(QUESTIONS_LIST,questionList);
                startActivityForResult(questionsIntent,MAIN_ACTIVITY_INTENT_REQUEST_CODE);
            }
        });

        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(1);
            }
        });

        if(!isNetworkConnected())
        {
            Toast.makeText(getApplicationContext(),"Please check your connection",Toast.LENGTH_LONG).show();
            return;
        } else {
            new FetchQuestionAsync(MainActivity.this).execute(url);
        }


    }


    public void stopProgressbar() {
        ProgressBar pb= (ProgressBar)findViewById(R.id.progressBar);
        pb.setVisibility(ProgressBar.INVISIBLE);
        Button startButton=(Button)findViewById(R.id.buttonStart);
        startButton.setEnabled(true);
        ImageView img=(ImageView)findViewById(R.id.imageView);
        img.setVisibility(View.VISIBLE);
        TextView tv= (TextView)findViewById(R.id.textViewLoading);
        tv.setText("Trivia Ready");
        //tv.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MAIN_ACTIVITY_INTENT_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                String value = data.getExtras().getString(MainActivity.ACTIVITY_TO_GO);
                Log.d("demo","Passed bac value: "+value);

                if (value.equals(MAIN_ACTIVITY)){

                }
            } else if (resultCode == RESULT_CANCELED){
                Log.d("demo","No values passed");
            }
        }
    }

    @Override
    public void setupQuestions(ArrayList<Question> questions) {
        stopProgressbar();

        questionList = questions;


    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }


    @Override
    public void startProgressbar() {
        ProgressBar pb= (ProgressBar)findViewById(R.id.progressBar);
        pb.setVisibility(ProgressBar.VISIBLE);
        pb.setIndeterminate(true);
        TextView tv= (TextView)findViewById(R.id.textViewLoading);
        tv.setVisibility(View.VISIBLE);

    }

}

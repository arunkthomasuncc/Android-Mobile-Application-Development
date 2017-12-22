package com.example.arun.trivia;
//Ron Abraham CHerina - 801028678
//Arun Thomas
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class StatsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        float result = (float)getIntent().getFloatExtra(QuestionsActivity.PERCENTAGE, 0.0f);
        Log.d("Result", ""+result);

        TextView percentageTextView = (TextView)findViewById(R.id.textViewPercentage);
        percentageTextView.setText(result+"%");

        ProgressBar percentageProgressBar = (ProgressBar)findViewById(R.id.progressBarPercentage);

        percentageProgressBar.setProgress((int)result);
        percentageProgressBar.setVisibility(View.VISIBLE);

        Button quitButton = (Button)findViewById(R.id.buttonQuitFromStats);
        Button tryAgainButton = (Button)findViewById(R.id.buttonTryAgain);

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //finishActivity(MainActivity.MAIN_ACTIVITY_INTENT_REQUEST_CODE);
                Intent intent = new Intent();
                intent.putExtra(MainActivity.ACTIVITY_TO_GO,MainActivity.MAIN_ACTIVITY);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //finishActivity(QuestionsActivity.QUESTIONS_ACTIVITY_INTENT_REQUEST_CODE);
                Intent intent = new Intent();
                intent.putExtra(MainActivity.ACTIVITY_TO_GO,QuestionsActivity.QUESTION_ACTIVITY);
                setResult(RESULT_OK,intent);
                finish();
            }
        });


    }
}

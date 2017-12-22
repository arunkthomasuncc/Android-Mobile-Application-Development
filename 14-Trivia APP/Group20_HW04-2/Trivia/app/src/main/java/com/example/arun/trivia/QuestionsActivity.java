package com.example.arun.trivia;
//Ron Abraham CHerina - 801028678
//Arun Thomas
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.CountDownTimer;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class QuestionsActivity extends AppCompatActivity implements FetchImageAsync.IQuestionImage {

    int count = 0;
    int numberOfCorrectAnswers = 0;
    static String PERCENTAGE = "PERCENTAGE";

    static int QUESTIONS_ACTIVITY_INTENT_REQUEST_CODE = 200;

    static String QUESTION_ACTIVITY = "QUESTION";

    CountDownTimer countDownTimer = null;
    ArrayList<Question> questionArrayList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        questionArrayList = (ArrayList<Question>) getIntent().getSerializableExtra(MainActivity.QUESTIONS_LIST);
        for (Question que : questionArrayList) {
            Log.d("Result", que.toString());

        }
        addRadioButtons(questionArrayList.get(count),  count);

        Button quitButton = (Button)findViewById(R.id.buttonQuit);
        Button nextButton = (Button)findViewById(R.id.buttonNext);



        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView imageView = (ImageView)findViewById(R.id.imageViewQuestion);
                imageView.setImageDrawable(null);
                RadioGroup radioGroup = (RadioGroup)findViewById(R.id.RadioGroupAnswers);
                //questionArrayList.get(count).setClickedAnswer(radioGroup.getCheckedRadioButtonId());
                if (radioGroup.getCheckedRadioButtonId()+1 == questionArrayList.get(count).getAns()){
                    numberOfCorrectAnswers++;
                }
                count++;
                if (count<questionArrayList.size()){
                    addRadioButtons(questionArrayList.get(count),  count);
                } else  {
                    Intent intent = new Intent(QuestionsActivity.this,StatsActivity.class);
                    intent.putExtra(PERCENTAGE,((float)numberOfCorrectAnswers/questionArrayList.size())*100);
                    startActivityForResult(intent,QUESTIONS_ACTIVITY_INTENT_REQUEST_CODE);
                }


            }
        });

        activateTimer();
    }

    private void activateTimer(){
        final TextView timerTextView = (TextView)findViewById(R.id.textViewTimer);
        if (countDownTimer != null){
            cancelTimer();
        }
        countDownTimer = new CountDownTimer(120000, 1000) {

            public void onTick(long millisUntilFinished) {
                timerTextView.setText("Time Left: " + (millisUntilFinished / 1000) + " seconds");
            }

            public void onFinish() {
                Intent intent = new Intent(QuestionsActivity.this,StatsActivity.class);
                intent.putExtra(PERCENTAGE,((float)numberOfCorrectAnswers/questionArrayList.size())*100);
                startActivityForResult(intent,QUESTIONS_ACTIVITY_INTENT_REQUEST_CODE);
            }

        }.start();
    }

    private void cancelTimer(){
        countDownTimer.cancel();
    }

    public void addRadioButtons(Question question, int index) {
        stopProgressBar();
        if (questionArrayList.get(index).getImageURL() != null){
            new FetchImageAsync(QuestionsActivity.this).execute(questionArrayList.get(index).getImageURL(), index+"");
        }

        int number = question.getChoices().size();
        LinearLayout parentView = (LinearLayout) findViewById(R.id.linearLayoutAnswers);
        LayoutInflater inflator1 = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        LinearLayout answerListLinearLayout = (LinearLayout) inflator1.inflate(R.layout.answer_list, null);
        RadioGroup radioGroup = (RadioGroup) answerListLinearLayout.findViewById(R.id.RadioGroupAnswers);
        parentView.removeAllViews();
        RadioButton[] radioButtons = new RadioButton[number];
        LinearLayout layout = (LinearLayout) radioGroup.getParent();
        layout.removeAllViews();
        for (int i = 0; i < number; i++) {

            radioButtons[i] = new RadioButton(this);
            radioButtons[i].setId(i);

            radioButtons[i].setText(question.getChoices().get(i));
            radioGroup.addView(radioButtons[i]);
        }
        parentView.addView(radioGroup);

        TextView textViewQuestion = (TextView)findViewById(R.id.textViewQuestion);
        textViewQuestion.setText(question.getText().trim());

        TextView textViewQuestionNumber = (TextView)findViewById(R.id.textViewQuestionNumber);
        textViewQuestionNumber.setText("Q"+(question.getId()+1)+"");

        //((ViewGroup) findViewById(R.id.RadioGroupAnswers)).addView(radioGroup);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == QUESTIONS_ACTIVITY_INTENT_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                String value = data.getExtras().getString(MainActivity.ACTIVITY_TO_GO);
                Log.d("demo","Passed bac value: "+value);

                if (value.equals(QUESTION_ACTIVITY)){
                    cancelTimer();
                    ImageView imageView = (ImageView)findViewById(R.id.imageViewQuestion);
                    imageView.setImageDrawable(null);
                    count = 0;
                    numberOfCorrectAnswers = 0;
                    addRadioButtons(questionArrayList.get(count),  count);


                    activateTimer();

                } else if (value.equals(MainActivity.MAIN_ACTIVITY)){

                    finish();
                }
            } else if (resultCode == RESULT_CANCELED){
                Log.d("demo","No values passed");
            }
        } else if (requestCode == MainActivity.MAIN_ACTIVITY_INTENT_REQUEST_CODE){
            finish();
        }
    }

    @Override
    public void showImage(byte[] bytes, String index) {
        ImageView imageView = (ImageView)findViewById(R.id.imageViewQuestion);

        if (bytes == null){
            stopProgressBar();
        }
        if (!(count+"").equals(index)){
            return;
        }
        if (bytes != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            imageView.setImageBitmap(bitmap);
            stopProgressBar();
        }

    }

    private void stopProgressBar(){
        ProgressBar pb= (ProgressBar)findViewById(R.id.progressBarQuestionLoading);
        pb.setVisibility(ProgressBar.INVISIBLE);
    }

    @Override
    public void startProgressBar() {
        ProgressBar pb= (ProgressBar)findViewById(R.id.progressBarQuestionLoading);
        pb.setVisibility(ProgressBar.VISIBLE);
        pb.setIndeterminate(true);
    }


}

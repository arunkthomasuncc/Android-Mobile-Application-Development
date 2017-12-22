package com.example.arun.inclass03group20;
//Ron Abraham CHerian 801028678
//Arun Kunnumpuram Thomas
//INClass03Group20.zip
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        Person person = new Person();

        if(null != getIntent().getExtras()){
            person = (Person)getIntent().getExtras().getSerializable("PERSON_OBJECT");
            ImageView profilePicImage =(ImageView)findViewById(R.id.imageView);
            TextView name = (TextView)findViewById(R.id.textViewName);
            TextView email = (TextView)findViewById(R.id.textViewEmail);
            TextView department = (TextView)findViewById(R.id.textViewDepartment);
            TextView moodName = (TextView)findViewById(R.id.textViewMood);
            ImageView moodImage =(ImageView)findViewById(R.id.imageView2);

            name.setText(getResources().getString(R.string.displayName)+person.getName());
            email.setText(getResources().getString(R.string.displayEmail)+person.getEmail());
            department.setText(getResources().getString(R.string.displayDepartment)+person.getDepartment());
            if (person.moodType == 2001){
                moodImage.setImageResource(R.drawable.angry);
                moodName.setText("I am Angry!");
            } else if (person.moodType == 2002){
                moodImage.setImageResource(R.drawable.sad);
                moodName.setText("I am Sad!");
            } else if (person.moodType == 2003){
                moodImage.setImageResource(R.drawable.happy);
                moodName.setText("I am Happy!");
            } else if (person.moodType == 2004){
                moodImage.setImageResource(R.drawable.awesome);
                moodName.setText("I am Awesome!");
            } else {

            }

            if (person.getAvatarType() == 1001){
               profilePicImage.setImageResource(R.drawable.avatar_f_1);
            } else if (person.getAvatarType() == 1002){
                profilePicImage.setImageResource(R.drawable.avatar_m_3);
            } else if (person.getAvatarType() == 1003){
                profilePicImage.setImageResource(R.drawable.avatar_f_2);
            } else if (person.getAvatarType() == 1004){
                profilePicImage.setImageResource(R.drawable.avatar_m_2);
            } else if (person.getAvatarType() == 1005){
                profilePicImage.setImageResource(R.drawable.avatar_f_3);
            } else  if (person.getAvatarType() == 1006){
                profilePicImage.setImageResource(R.drawable.avatar_m_1);
            } else {

            }
        }
    }
}

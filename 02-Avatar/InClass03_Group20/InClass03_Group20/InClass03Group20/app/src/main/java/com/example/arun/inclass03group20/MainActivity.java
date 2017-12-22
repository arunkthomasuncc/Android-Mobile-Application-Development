package com.example.arun.inclass03group20;
//Ron Abraham CHerian 801028678
//Arun Kunnumpuram Thomas
//INClass03Group20.zip
import android.content.Intent;
import android.hardware.input.InputManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    final static int PIC_SELECT_REQ_CODE=100;
    final static  String PIC_SELECTOR="PIC SELECTOR";

     ImageView profilePhoto= null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

       if(data.getExtras()==null)
       {
           Log.d("demo","value is null");
       }

        if(requestCode==PIC_SELECT_REQ_CODE)
        {
            if(resultCode==RESULT_OK)
            {

                String value=data.getExtras().getString(PIC_SELECTOR).toString();
                if(value.equals("1"))
                {
                    profilePhoto.setImageResource(R.drawable.avatar_f_1);
                    profilePhoto.setTag(1001);

                }else if(value.equals("2"))
                {
                    profilePhoto.setImageResource(R.drawable.avatar_m_3);
                    profilePhoto.setTag(1002);
                }else if(value.equals("3"))
                {
                    profilePhoto.setImageResource(R.drawable.avatar_f_2);
                    profilePhoto.setTag(1003);
                }else if(value.equals("4"))
                {
                    profilePhoto.setImageResource(R.drawable.avatar_m_2);
                    profilePhoto.setTag(1004);
                }else if(value.equals("5"))
                {
                    profilePhoto.setImageResource(R.drawable.avatar_f_3);
                    profilePhoto.setTag(1005);
                }else
                {
                    profilePhoto.setImageResource(R.drawable.avatar_m_1);
                    profilePhoto.setTag(1006);
                }

            }
            if(resultCode==0)
            {
                Log.d("Demo","No Avatar Selected");
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profilePhoto= (ImageView)findViewById(R.id.imageViewProfilePic);


        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(MainActivity.this,SelectAvatarActivity.class);
                startActivityForResult(intent,PIC_SELECT_REQ_CODE);

            }
        });

        final EditText nameText = (EditText)findViewById(R.id.editTextName);
        final EditText emailText = (EditText)findViewById(R.id.editTextEmail);
        final ImageView profilePic = (ImageView)findViewById(R.id.imageViewProfilePic);
        final RadioGroup departmentChosen = (RadioGroup)findViewById(R.id.radioGroup);
        final ImageView moodTypeImage = (ImageView)findViewById(R.id.imageViewEmotion);
        final TextView moodTextView = (TextView)findViewById(R.id.textViewMood);


        Button submitButton = (Button)(findViewById(R.id.buttonSubmit));

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Person person = new Person();
                if (nameText.getText().length() == 0){
                    Toast.makeText(getApplicationContext(),"Please enter your name", Toast.LENGTH_LONG).show();
                    return;
                }
                person.setName(nameText.getText().toString());
                if (emailText.getText().length() == 0 || !isValidEmail(emailText.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Please enter a valid email", Toast.LENGTH_LONG).show();
                    return;
                }
                person.setEmail(emailText.getText().toString());

                RadioButton radioButton = (RadioButton) findViewById(departmentChosen.getCheckedRadioButtonId());

                person.setDepartment(radioButton.getText().toString());
                if (null == profilePic.getTag() || 0 == (int)profilePic.getTag()){
                    Toast.makeText(getApplicationContext(),"Please choose an avatar", Toast.LENGTH_LONG).show();
                    return;
                }
                person.setAvatarType((int)profilePic.getTag());
                if (null == moodTypeImage.getTag() || 0==(int)moodTypeImage.getTag()){
                    person.setMoodType(2003);
                }else {
                    person.setMoodType((int) moodTypeImage.getTag());
                }

                Log.d("demo",profilePic.getTag()+"");
                Log.d("demo",person.toString());

                Intent intent = new Intent(MainActivity.this,DisplayActivity.class);
                intent.putExtra("PERSON_OBJECT",person);

                startActivity(intent);
            }
        });

        SeekBar seekBar = (SeekBar)findViewById(R.id.seekBar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0){
                    moodTypeImage.setImageResource(R.drawable.angry);
                    moodTypeImage.setTag(2001);
                    moodTextView.setText(R.string.AngryMood);
                }else  if (progress == 1){
                    moodTypeImage.setImageResource(R.drawable.sad);
                    moodTypeImage.setTag(2002);
                    moodTextView.setText(R.string.SadMood);

                } else  if (progress == 2){

                    moodTypeImage.setImageResource(R.drawable.happy);
                    moodTypeImage.setTag(2003);
                    moodTextView.setText(R.string.HappyMood);
                } else {

                    moodTypeImage.setImageResource(R.drawable.awesome);
                    moodTypeImage.setTag(2004);
                    moodTextView.setText(R.string.AwesomMood);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

}

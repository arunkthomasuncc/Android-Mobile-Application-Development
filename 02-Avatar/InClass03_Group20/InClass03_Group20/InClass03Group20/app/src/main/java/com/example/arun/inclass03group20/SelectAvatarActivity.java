package com.example.arun.inclass03group20;
//Ron Abraham CHerian 801028678
//Arun Kunnumpuram Thomas
//INClass03Group20.zip
import android.content.Intent;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SelectAvatarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_avatar);


        ImageView avatar1=(ImageView)findViewById(R.id.Avatar1);
        ImageView avatar2=(ImageView)findViewById(R.id.Avatar2);
        ImageView avatar3=(ImageView)findViewById(R.id.Avatar3);
        ImageView avatar4=(ImageView)findViewById(R.id.Avatar4);
        ImageView avatar5=(ImageView)findViewById(R.id.Avatar5);
        ImageView avatar6=(ImageView)findViewById(R.id.Avatar6);

        avatar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent();
                intent.putExtra(MainActivity.PIC_SELECTOR,"1");
                setResult(RESULT_OK,intent);
                finish();

            }
        });
        avatar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent();
                intent.putExtra(MainActivity.PIC_SELECTOR,"2");
                setResult(RESULT_OK,intent);
                finish();

            }
        });
        avatar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent();
                intent.putExtra(MainActivity.PIC_SELECTOR,"3");
                setResult(RESULT_OK,intent);
                finish();

            }
        });
        avatar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent();
                intent.putExtra(MainActivity.PIC_SELECTOR,"4");
                setResult(RESULT_OK,intent);
                finish();

            }
        });
        avatar5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent();
                intent.putExtra(MainActivity.PIC_SELECTOR,"5");
                setResult(RESULT_OK,intent);
                finish();

            }
        });
        avatar6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent();
                intent.putExtra(MainActivity.PIC_SELECTOR,"6");
                setResult(RESULT_OK,intent);
                finish();

            }
        });

    }
}

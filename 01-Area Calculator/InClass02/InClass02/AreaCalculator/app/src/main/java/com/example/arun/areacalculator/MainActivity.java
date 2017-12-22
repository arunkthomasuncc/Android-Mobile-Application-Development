package com.example.arun.areacalculator;
//Assignment #: InClass02
//FileName: InCLass02.zip
//Full Name: Ron Abraham Cherian and Arun Kunnupuram Thomas (Group 20)
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    static int shapeValue=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText length1 = (EditText) findViewById(R.id.Length1);
        final EditText length2 = (EditText) findViewById(R.id.length2);
        final TextView shape = (TextView) findViewById(R.id.shapeTextView);
        final TextView length2Text = (TextView) findViewById(R.id.length2TextView);
        final TextView areaTextView = (TextView) findViewById(R.id.areaText);

        ImageView triangleIv = (ImageView) findViewById(R.id.triangleImageView);
        ImageView squareIv = (ImageView) findViewById(R.id.squareImageView);
        ImageView circleIv = (ImageView) findViewById(R.id.circleImageView);

        Button calculateButton = (Button) findViewById(R.id.CalculateButton);
        Button clearButton = (Button) findViewById(R.id.clearButton);

        triangleIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                shape.setText("Triangle");
                if (length2.getVisibility() == View.INVISIBLE) {
                    length2.setVisibility(View.VISIBLE);
                    length2Text.setVisibility(View.VISIBLE);

                }
                shapeValue=1;
                Log.d("shape is","triangle");

            }
        });


        squareIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shape.setText("Square");
                if (length2.getVisibility() == View.VISIBLE) {
                    length2.setVisibility(View.INVISIBLE);
                    length2Text.setVisibility(View.INVISIBLE);

                }
                shapeValue=2;
                Log.d("shape is","square");
            }
        });

        circleIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shape.setText("Circle");
                if (length2.getVisibility() == View.VISIBLE) {
                    length2.setVisibility(View.INVISIBLE);
                    length2Text.setVisibility(View.INVISIBLE);
                }
                shapeValue=3;
                Log.d("shape is","Circle");
            }
        });

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shapeValue == 0) {
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.selectShape),Toast.LENGTH_LONG).show();
                    return;
                } else if (shapeValue == 1){
                    if (length1.getText().length() == 0 ){
                        Toast.makeText(getApplicationContext(),getResources().getString(R.string.enterLength1),Toast.LENGTH_LONG).show();
                        return;
                    } else if(length2.getText().length() == 0){
                        Toast.makeText(getApplicationContext(),getResources().getString(R.string.enterLength2),Toast.LENGTH_LONG).show();
                        return;
                    }else {
                        Double doubleLength1 = Double.parseDouble(length1.getText().toString().trim());
                        Double doubleLength2 = Double.parseDouble(length2.getText().toString().trim());

                        Double area = 0.5*doubleLength1*doubleLength2;

                        area = Math.round(area*10d)/10d;
                        areaTextView.setText(area+"");
                    }
                } else  if (shapeValue ==2){
                    if (length1.getText().length() == 0){
                        Toast.makeText(getApplicationContext(),getResources().getString(R.string.enterLength1),Toast.LENGTH_LONG).show();
                    } else {
                        Double doubleLength1 = Double.parseDouble(length1.getText().toString().trim());

                        Double area = Math.pow(doubleLength1,2);
                        area = Math.round(area*10d)/10d;
                        areaTextView.setText(area+"");
                    }
                }else if (shapeValue == 3){
                    if (length1.getText().length() == 0){
                        Toast.makeText(getApplicationContext(),getResources().getString(R.string.enterLength1),Toast.LENGTH_LONG).show();
                    } else {
                        Double doubleLength1 = Double.parseDouble(length1.getText().toString().trim());

                        Double area = Math.PI*(Math.pow(doubleLength1,2));
                        area = Math.round(area*100d)/100d;
                        areaTextView.setText(area+"");
                    }
                }
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                length1.setText("");
                length2.setText("");
                shapeValue = 0;
                shape.setText(getResources().getString(R.string.selectShape));
                areaTextView.setText("");
                length2Text.setVisibility(View.VISIBLE);
                length2.setVisibility(View.VISIBLE);
            }
        });
    }
}

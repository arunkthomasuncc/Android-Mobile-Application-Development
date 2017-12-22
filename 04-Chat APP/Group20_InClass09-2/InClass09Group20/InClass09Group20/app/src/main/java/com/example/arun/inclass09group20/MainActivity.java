package com.example.arun.inclass09group20;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    static int SIGNUP=100;
    static String staticToken="";
    static int staticuserId=0;
    static String staticfname="";
    static String staticlname="";
    String errorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Chat Room");

        Button signUP=(Button)findViewById(R.id.buttonSignup);
        signUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent= new Intent(MainActivity.this,SignupActivity.class);
                startActivityForResult(intent,SIGNUP);
            }
        });

        Button login=(Button)findViewById(R.id.buttonLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText email=(EditText)findViewById(R.id.editTextEmail);
                EditText password=(EditText)findViewById(R.id.editTextPassword);
                if(email.getText().length()>0&& password.getText().length()>0)
                {

                    OkHttpClient client = new OkHttpClient();
                    RequestBody formBody = new FormBody.Builder()
                            .add("email", email.getText().toString())
                            .add("password",password.getText().toString())
                            .build();
                    Request request = new Request.Builder()
                            .url("http://ec2-54-164-74-55.compute-1.amazonaws.com/api/login")
                            .post(formBody)
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {


                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                            String result=response.body().string();
                            try {
                                JSONObject obj=new JSONObject(result);
                                String status=obj.getString("status");

                                if(status.equals("ok")) {
                                    String token = obj.getString("token");
                                    String userEmail = obj.getString("user_email");
                                    int userId = obj.getInt("user_id");
                                    Log.d("token", token);
                                /*Intent intent= new Intent();
                                intent.putExtra("token",token);
                                finish();*/

                                /*"status": "ok",
                                        "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE1MTAwMTQwNDIsImV4cCI6MTU0MTU1MDA0MiwianRpIjoiNm9QYno2TTBSaEJnc044MFdWcG1CQyIsInVzZXIiOjM4fQ.RNwvB-hm8PtuAKKGipKjsUMsXE8lyKxM0PsVqo7PV-E",
                                        "user_id": 38,
                                        "user_email": "arun@gmail.com",
                                        "user_fname": "arun",
                                        "user_lname": "thomas",
                                        "user_role": "USER"*/
                                SharedPreferences sp= getApplicationContext().getSharedPreferences("inclass09",Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor;

                                editor = sp.edit();
                                    staticToken = token;
                                    staticuserId = userId;
                                    staticfname = obj.getString("user_fname");
                                    staticlname = obj.getString("user_lname");


                                editor.putString("token", token);
                                editor.putString("email", userEmail);
                                editor.putInt("userid", userId);


                                editor.commit();

                                    Intent intent = new Intent(MainActivity.this, MessageThreads.class);
                                    startActivity(intent);

                                }
                                else
                                {
                                    errorMessage=obj.getString("message");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(),errorMessage,Toast.LENGTH_LONG).show();
                                        }
                                    });

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    });


                }else
                {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Please give email and password",Toast.LENGTH_LONG).show();
                        }
                    });

                }

            }
        });
    }
}

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

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setTitle("Sign Up");

        Button cancel=(Button)findViewById(R.id.buttonCancelFromSignUP);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button signUP=(Button)findViewById(R.id.buttonSignUPfromSignUP);
        signUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText firstName=(EditText)findViewById(R.id.editTextFirstNameSignup);
                EditText LastName=(EditText)findViewById(R.id.editTextLastNameSignUP);
                EditText email=(EditText)findViewById(R.id.editTextEmailSignUP);
                EditText password=(EditText)findViewById(R.id.editTextChoosePassword);
                EditText repeatpw=(EditText)findViewById(R.id.editTextRepeatPassword);



                if(password.getText().toString().equals(repeatpw.getText().toString()))
                {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody formBody = new FormBody.Builder()
                            .add("email", email.getText().toString())
                            .add("password",password.getText().toString())
                            .add("fname",firstName.getText().toString())
                            .add("lname",LastName.getText().toString())
                            .build();
                    Request request = new Request.Builder()
                            .url("http://ec2-54-164-74-55.compute-1.amazonaws.com/api/signup")
                            .post(formBody)
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                            Toast.makeText(getApplicationContext(),"Failed to signup",Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                            String result=response.body().string();



                            try {
                                JSONObject obj = new JSONObject(result);
                                if(obj.getString("status").equals("ok"))
                                {
                                String token = obj.getString("token");
                                int user_id = obj.getInt("user_id");


                                MainActivity.staticToken = token;
                                MainActivity.staticuserId = user_id;
                                Log.d("token", token);
                                    SharedPreferences sp= getApplicationContext().getSharedPreferences("inclass09", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor;
                                    editor = sp.edit();
                                    editor.putString("token", token);


                                    editor.commit();
                                Intent intent = new Intent(SignupActivity.this, MessageThreads.class);
                                startActivity(intent);
                                /*Intent intent= new Intent();
                                intent.putExtra("token",token);
                                finish();*/
                            }
                                else
                                {
                                    final String errorMessage=obj.getString("message");
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
                    Toast.makeText(getApplicationContext(), "passwords not match", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}

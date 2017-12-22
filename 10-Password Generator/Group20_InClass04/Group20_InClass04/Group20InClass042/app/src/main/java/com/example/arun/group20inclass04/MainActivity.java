package com.example.arun.group20inclass04;
//Ron Abraham Cherian (801028678)
//Arun
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    static String[] passwordList= new String[5];
    Handler handler;
    ExecutorService threadPool;

    ProgressDialog progressDialog;
    ProgressDialog progressDialog2;
    AlertDialog.Builder passwordListDialog;
    TextView newPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Password Generator");
        final Button buttonThread = (Button)findViewById(R.id.buttonThread);
        final Button buttonAsync = (Button)findViewById(R.id.buttonAsync);
        final EditText name=(EditText)findViewById(R.id.editTextName);
        final EditText dept=(EditText)findViewById(R.id.editTextDept);

        final EditText age=(EditText)findViewById(R.id.editTextAge);

        final EditText zip=(EditText)findViewById(R.id.editTextZIP);
        newPassword= (TextView)findViewById(R.id.textViewNewPassword);




        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Generating Passwords");
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        passwordListDialog= new AlertDialog.Builder(this);


        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {

                switch (msg.what){

                    case ThreadPassword.STATUS_START:
                        progressDialog.show();
                        break;
                    case ThreadPassword.STATUS_STEP:

                         progressDialog.setProgress((Integer)msg.obj *20);
                        break;
                    case ThreadPassword.STATUS_DONE:
                        progressDialog.dismiss();
                        Log.d("Passwords",passwordList.toString());
                       // newPassword.setText(passwordList[0]+"hello"+passwordList[1]+"hello"+passwordList[2]+"hello"+passwordList[3]+"hello"+passwordList[4]+"hello");
                        passwordListDialog.setTitle("Choose a Password").setCancelable(false).setItems(passwordList, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                newPassword.setText(passwordList[which]);
                                //passwordList = null;
                            }
                        }).show();
                        break;

                }

                return false;
            }
        });

        threadPool= Executors.newFixedThreadPool(2);
        buttonThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (name.getText().length() == 0){
                    Toast.makeText(MainActivity.this,"Enter a valid name",Toast.LENGTH_SHORT).show();
                    return;
                }
                String nameValue=name.getText().toString().trim();
                if (nameValue.length() == 0 || nameValue.matches(".*\\d+.*")){
                    Toast.makeText(MainActivity.this,"Name can't contain Numbers",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (dept.getText().length() == 0){
                    Toast.makeText(MainActivity.this,"Enter a valid department",Toast.LENGTH_SHORT).show();
                    return;
                }
                String deptValue=dept.getText().toString().trim();
                if (age.getText().length() == 0 || age.getText().length() > 5){
                    Toast.makeText(MainActivity.this,"Enter a valid age",Toast.LENGTH_SHORT).show();
                    return;
                }
                int ageValue= Integer.parseInt(age.getText().toString().trim());
                if (ageValue <= 0){
                    Toast.makeText(MainActivity.this,"Age must be greater than zero",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (zip.getText().length() == 0 || zip.getText().length() > 5){
                    Toast.makeText(MainActivity.this,"Enter a valid zip",Toast.LENGTH_SHORT).show();
                    return;
                }
                int zipValue=Integer.parseInt(zip.getText().toString().trim());
                if (zipValue < 10000 || zipValue >99999){
                    Toast.makeText(MainActivity.this,"Zip should be a 5 digit number",Toast.LENGTH_SHORT).show();
                    return;
                }

                threadPool.execute(new ThreadPassword(nameValue,ageValue,deptValue,zipValue));


            }
        });

        buttonAsync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (name.getText().length() == 0){
                    Toast.makeText(MainActivity.this,"Enter a valid name",Toast.LENGTH_SHORT).show();
                    return;
                }
                String nameValue=name.getText().toString().trim();
                if (nameValue.length() == 0 || nameValue.matches(".*\\d+.*")){
                    Toast.makeText(MainActivity.this,"Name can't contain Numbers",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (dept.getText().length() == 0){
                    Toast.makeText(MainActivity.this,"Enter a valid department",Toast.LENGTH_SHORT).show();
                    return;
                }
                String deptValue=dept.getText().toString().trim();
                if (age.getText().length() == 0 || age.getText().length() > 5){
                    Toast.makeText(MainActivity.this,"Enter a valid age",Toast.LENGTH_SHORT).show();
                    return;
                }
                int ageValue= Integer.parseInt(age.getText().toString().trim());
                if (ageValue <= 0){
                    Toast.makeText(MainActivity.this,"Age must be greater than zero",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (zip.getText().length() == 0 || zip.getText().length() > 5){
                    Toast.makeText(MainActivity.this,"Enter a valid zip",Toast.LENGTH_SHORT).show();
                    return;
                }
                int zipValue=Integer.parseInt(zip.getText().toString().trim());
                if (zipValue < 10000 || zipValue >99999){
                    Toast.makeText(MainActivity.this,"Zip should be a 5 digit number",Toast.LENGTH_SHORT).show();
                    return;
                }


                Profile profile = new Profile(nameValue,deptValue,ageValue,zipValue);

                new PasswordGeneratorAsync().execute(profile);
            }
        });

        Button clearButton = (Button)findViewById(R.id.buttonClear);

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setText("");
                age.setText("");
                dept.setText("");
                zip.setText("");
                newPassword.setText("");
                name.requestFocus();
            }
        });

        Button closeButton = (Button)findViewById(R.id.buttonClose);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private class ThreadPassword implements Runnable
    {
        String name;
        int age;
        String dept;
        int zip;
        static final int STATUS_START=1;
        static final int STATUS_STEP=2;
        static final int STATUS_DONE=3;
        Message msg;
        public ThreadPassword(String name, int age, String dept, int zip) {
            this.name = name;
            this.age = age;
            this.dept = dept;
            this.zip = zip;
        }

        @Override
        public void run() {

            msg= new Message();
            msg.what=STATUS_START;
            handler.sendMessage(msg);
            for(int i=0;i<5;i++)
            {
                Util util= new Util();
                 String password= util.getPassword(name,dept,age,zip);
                passwordList[i]=password;
               msg= new Message();
                msg.what=STATUS_STEP;
                msg.obj=i+1;
                handler.sendMessage(msg);
            }



            msg= new Message();
            msg.what=STATUS_DONE;
            handler.sendMessage(msg);

        }
    }

    class PasswordGeneratorAsync extends AsyncTask<Profile,Integer,String[]>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog2 = new ProgressDialog(MainActivity.this);
            progressDialog2.setCancelable(false);
            progressDialog2.setTitle("Generating Passwords");
            progressDialog2.setMax(100);
            progressDialog2.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog2.show();
        }

        @Override
        protected String[] doInBackground(Profile... params) {

            Profile profile = params[0];
            for(int i=0;i<5;i++)
            {
                Util util= new Util();
                String password= util.getPassword(profile.getName(),profile.getDept(),profile.getAge(),profile.getZip());
                passwordList[i]=password;
                publishProgress((i+1));

            }
            return passwordList;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            newPassword = (TextView)findViewById(R.id.textViewNewPassword);
            passwordListDialog= new AlertDialog.Builder(MainActivity.this);
            passwordListDialog.setTitle("Choose a Password").setCancelable(false).setItems(passwordList, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    newPassword.setText(passwordList[which]);
                    //passwordList = null;
                }
            }).show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (values[0] > 4){
                progressDialog2.dismiss();
            } else {
                progressDialog2.setProgress(values[0]*20);
            }

        }

    }


}

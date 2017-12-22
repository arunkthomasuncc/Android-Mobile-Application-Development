package com.example.arun.inclass09group20;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MessageThreads extends AppCompatActivity implements ThreadsAdapter.ICallBack {
    ArrayList<ThreadsObj> threads = new ArrayList<ThreadsObj>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_threads);
        setTitle("Message Threads");
        TextView username=(TextView) findViewById(R.id.textViewLoggedInUserName);
        username.setText(MainActivity.staticfname+" "+MainActivity.staticlname);
        getThreads();
       SharedPreferences sp=getApplicationContext().getSharedPreferences("inclass09", Context.MODE_PRIVATE);
       // int userTokenId=sp.get
       // if(sp.contains("token")) {*/

        // String token=sp.getString("token",null);




        ///////////////////////////////////////////////
        ImageButton exitButton = (ImageButton) findViewById(R.id.imageButtonExit);

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //MainActivity.staticToken="";
                Intent intentExit = new Intent(getApplicationContext(), MainActivity.class);
                intentExit.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentExit);
            }
        });

        ImageButton addThreadButton = (ImageButton) findViewById(R.id.imageButtonAddThread);

        addThreadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                EditText editTextThreadName= (EditText)findViewById(R.id.editTextThreadName);

                if(editTextThreadName.getText().length()>0) {
                    // Call API



                    OkHttpClient client = new OkHttpClient();
                    RequestBody formBody = new FormBody.Builder()
                            .add("title", editTextThreadName.getText().toString())
                            .build();
                    editTextThreadName.setText(" ");
                    Request request = new Request.Builder()
                            .url("http://ec2-54-164-74-55.compute-1.amazonaws.com/api/thread/add")
                            .post(formBody)
                            .addHeader("Authorization", "BEARER " + MainActivity.staticToken)
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                            getThreads();

                        }
                    });
                }



            }
        });

         /*  mRecyclerView=(RecyclerView)findViewById(R.id.threadsRecyclerView);
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            mRecyclerView.setHasFixedSize(true);

            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
            mRecyclerView.setLayoutManager(mLayoutManager);




            // specify an adapter (see also next example)
            mAdapter = new ThreadsAdapter(threads,this);
            mRecyclerView.setAdapter(mAdapter);*/

    }


    @Override
    public void threadViewClicked(ThreadsObj threadsObj) {
        Intent intent = new Intent(MessageThreads.this, ChatRoomActivity.class);
        intent.putExtra("THREAD_OBJECT", threadsObj);
        startActivityForResult(intent, 100);

    }

    public void getThreads()
    {
        OkHttpClient client = new OkHttpClient();
        String token = MainActivity.staticToken;
        Request request = new Request.Builder()
                .url("http://ec2-54-164-74-55.compute-1.amazonaws.com/api/thread")
                .addHeader("Authorization", "BEARER " + token)
                .build();
        client.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                threads.removeAll(threads);

                String result = response.body().string();

                try {
                    JSONObject obj = new JSONObject(result);

                    JSONArray array = obj.getJSONArray("threads");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject threadobj = array.getJSONObject(i);
                        ThreadsObj threadObject = new ThreadsObj();

                        if (threadobj.getString("user_fname").length() > 0) {
                            threadObject.setfName(threadobj.getString("user_fname"));
                        }
                        if (threadobj.getInt("user_id") > 0) {
                            threadObject.setUserId(threadobj.getInt("user_id"));
                        }
                        if (threadobj.getInt("id") > 0) {
                            threadObject.setId(threadobj.getInt("id"));
                        }
                        if (threadobj.getString("title").length() > 0) {
                            threadObject.setTitle(threadobj.getString("title"));
                        }
                        if (threadobj.getString("created_at").length() > 0) {
                            threadObject.setCreatedAt(threadobj.getString("created_at"));

                        }
                        threads.add(threadObject);


                    }
                    Collections.sort(threads,ThreadsObj.sortById);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRecyclerView=(RecyclerView)findViewById(R.id.threadsRecyclerView);
                            // use this setting to improve performance if you know that changes
                            // in content do not change the layout size of the RecyclerView
                            mRecyclerView.setHasFixedSize(true);

                            // use a linear layout manager
                            mLayoutManager = new LinearLayoutManager(MessageThreads.this, LinearLayoutManager.VERTICAL,false);
                            mRecyclerView.setLayoutManager(mLayoutManager);



                            // specify an adapter (see also next example)
                            mAdapter = new ThreadsAdapter(threads,MessageThreads.this,MessageThreads.this);
                            mRecyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();

                        }
                    });

                } catch (JSONException e) {

                }
                    /*{
                        "threads": [
                        {
                            "user_fname": "salman",
                                "user_lname": "mujtaba",
                                "user_id": "32",
                                "id": "29",
                                "title": "Salman's thread",
                                "created_at": "2017-11-07 00:27:19"
                        },*/

            }
        });
    }
}

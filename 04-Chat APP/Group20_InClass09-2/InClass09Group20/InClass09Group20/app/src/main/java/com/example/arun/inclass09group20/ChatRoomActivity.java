package com.example.arun.inclass09group20;

import android.content.Intent;
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

public class ChatRoomActivity extends AppCompatActivity implements ChatRoomActivityAdapter.ICallBack{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Messages> msgs= new ArrayList<Messages>();
    ThreadsObj obj;
    int threadId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        setTitle("Chatroom");

        obj= (ThreadsObj) getIntent().getSerializableExtra("THREAD_OBJECT");

        TextView textViewThreadName=(TextView)findViewById(R.id.textViewThreadName);
        textViewThreadName.setText(obj.getTitle());


        threadId=obj.getId();
        getMessages();

        ImageButton createMessageButton=(ImageButton)findViewById(R.id.imageButtonSendMessage);
        createMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText editTextNewMessage= (EditText)findViewById(R.id.editTextNewMessage);
                if(editTextNewMessage.getText().length()>0) {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody formBody = new FormBody.Builder()
                            .add("message", editTextNewMessage.getText().toString())
                            .add("thread_id",threadId+"")
                            .build();
                    editTextNewMessage.setText("");
                    Request request = new Request.Builder()
                            .url("http://ec2-54-164-74-55.compute-1.amazonaws.com/api/message/add")
                            .post(formBody)
                            .addHeader("Authorization", "BEARER " + MainActivity.staticToken)
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                            getMessages();

                        }
                    });
                }







            }
        });

        ImageButton imageButtonHome = (ImageButton)findViewById(R.id.imageButtonHome);

        imageButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatRoomActivity.this, MainActivity.class);

                finish();
            }
        });



    }

    @Override
    public void messageViewClicked(Messages messages) {




    }

    public void getMessages()
    {
        OkHttpClient client = new OkHttpClient();
        // String token=sp.getString("token",null);

        String token = MainActivity.staticToken;
        Request request = new Request.Builder()
                .url("http://ec2-54-164-74-55.compute-1.amazonaws.com/api/messages/"+threadId)
                .addHeader("Authorization", "BEARER " + token)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                String result = response.body().string();
                msgs.removeAll(msgs);

                try {
                    JSONObject obj = new JSONObject(result);

                    JSONArray array = obj.getJSONArray("messages");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject threadobj = array.getJSONObject(i);
                        Messages messages = new Messages();

                        if (threadobj.getString("user_fname").length() > 0) {
                            messages.setfName(threadobj.getString("user_fname"));
                        }

                        if (threadobj.getString("user_lname").length() > 0) {
                            messages.setlName(threadobj.getString("user_lname"));
                        }







                        if (threadobj.getInt("user_id") > 0) {
                            messages.setUserId(threadobj.getInt("user_id")+"");
                        }
                        if (threadobj.getInt("id") > 0) {
                            messages.setId(threadobj.getInt("id")+"");
                        }
                        if (threadobj.getString("message").length() > 0) {
                            messages.setMessage(threadobj.getString("message"));
                        }
                        if (threadobj.getString("created_at").length() > 0) {
                            messages.setCreatedAt(threadobj.getString("created_at"));

                        }
                        msgs.add(messages);


                    }
                    Collections.sort(msgs,Messages.sortById);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRecyclerView=(RecyclerView)findViewById(R.id.recyclerViewChatRoom);
                            // use this setting to improve performance if you know that changes
                            // in content do not change the layout size of the RecyclerView
                            mRecyclerView.setHasFixedSize(true);

                            // use a linear layout manager
                            mLayoutManager = new LinearLayoutManager(ChatRoomActivity.this, LinearLayoutManager.VERTICAL,false);
                            mRecyclerView.setLayoutManager(mLayoutManager);




                            // specify an adapter (see also next example)
                            mAdapter = new ChatRoomActivityAdapter(msgs,ChatRoomActivity.this);
                            mRecyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();

                        }
                    });

                } catch (JSONException e) {

                }

            }
        });




        /*mRecyclerView=(RecyclerView)findViewById(R.id.recyclerViewChatRoom);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ArrayList<Messages> messages = new ArrayList<>();


        // specify an adapter (see also next example)
        mAdapter = new ChatRoomActivityAdapter(messages,this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();*/
    }

    @Override
    public void deleteMessage(String messageId) {
        // Call /api/delete/message/messageId

        OkHttpClient client = new OkHttpClient();
        // String token=sp.getString("token",null);

        String token = MainActivity.staticToken;
        Request request = new Request.Builder()
                .url("http://ec2-54-164-74-55.compute-1.amazonaws.com/api/message/delete/"+messageId)
                .addHeader("Authorization", "BEARER " + token)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                                              mAdapter.notifyDataSetChanged();

                    }
                });

            }
        });

    }
}

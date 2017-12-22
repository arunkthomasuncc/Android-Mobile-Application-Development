package com.example.arun.inclass09group20;

/**
 * Created by Arun on 11/6/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by roncherian on 06/11/17.
 */

public class ThreadsAdapter extends RecyclerView.Adapter<ThreadsAdapter.ViewHolder>{


    static ICallBack callback;
    static ArrayList<ThreadsObj> mData;
    // Context context;

    static MainActivity mainActivity;
    static Context context;

    public ThreadsAdapter(ArrayList<ThreadsObj> mData,Context context, ICallBack callback) {
        this.mData = mData;
        this.context=context;
        this.mainActivity=mainActivity;
        this.callback = callback;

    }


    @Override
    public ThreadsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.threads_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ThreadsAdapter.ViewHolder holder, int position) {


        ThreadsObj threadsObject = mData.get(position);
        holder.threadsObj = threadsObject;
        holder.threadName.setText(threadsObject.getTitle());
        if(threadsObject.getUserId()==MainActivity.staticuserId)
        {
            holder.delete.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.delete.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    public int getItemCount() {

        return mData.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {

        TextView threadName;

        ThreadsObj threadsObj;
        ImageButton delete=(ImageButton)itemView.findViewById(R.id.imageButton2);

        public ViewHolder(View itemView) {
            super(itemView);
            threadName = (TextView)itemView.findViewById(R.id.textViewThreadName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (callback != null){
                        callback.threadViewClicked(threadsObj);
                    }

                }
            });


            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int threadId=threadsObj.getId();
                    deleteThread(threadId);
                    mData.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    notifyItemRangeChanged(getAdapterPosition(), mData.size());

                }
            });



        }
    }

    public  void deleteThread(int threadId)
    {
        OkHttpClient client= new OkHttpClient();
        String token = MainActivity.staticToken;
        Request request = new Request.Builder()
                .url("http://ec2-54-164-74-55.compute-1.amazonaws.com/api/thread/delete/"+threadId)
                .addHeader("Authorization", "BEARER " + token)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });

    }

    public interface ICallBack {
        void threadViewClicked(ThreadsObj threadsObj);
    }
}

package com.example.arun.inclass09group20;

/**
 * Created by Arun on 11/6/2017.
 */

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.ocpsoft.prettytime.PrettyTime;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by roncherian on 06/11/17.
 */

public class ChatRoomActivityAdapter extends RecyclerView.Adapter<ChatRoomActivityAdapter.ViewHolder>{


    static ChatRoomActivityAdapter.ICallBack callback;
    static ArrayList<Messages> mData;
    // Context context;

    static MainActivity mainActivity;

    public ChatRoomActivityAdapter(ArrayList<Messages> mData, ChatRoomActivityAdapter.ICallBack callback) {
        this.mData = mData;
        this.mainActivity=mainActivity;
        callback = callback;

    }


    @Override
    public ChatRoomActivityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View emailView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_layout, parent, false);
        ChatRoomActivityAdapter.ViewHolder viewHolder = new ChatRoomActivityAdapter.ViewHolder(emailView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ChatRoomActivityAdapter.ViewHolder holder, int position) {

        Messages messages = mData.get(position);
        holder.messages = messages;


       // String myDate = "2014/10/29 18:10:45";

        // "2017-11-07 06:41:22"
        String createdDate=messages.getCreatedAt();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        Date date = new Date();

        try {
            date = sdf.parse(createdDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        PrettyTime p = new PrettyTime();


         long millis = date.getTime();
        //There is 5 hour difference between current time and created time from server. created time from server is 5 hr ahead than us.
        millis=millis-5*60*60*1000;
        String datetime= p.format(new Date(millis));
        holder.createdTime.setText(datetime);
       // System.out.println(p.format(new Date(millis)));

        holder.message.setText(messages.getMessage());
        if(Integer.parseInt(messages.getUserId())==MainActivity.staticuserId)
        {
            holder.nameOfMessenger.setText("Me");

        }else
        holder.nameOfMessenger.setText(messages.getfName()+" "+messages.getlName());


        if (Integer.parseInt(messages.getUserId()) == MainActivity.staticuserId)
        {
            holder.deleteButton.setVisibility(View.VISIBLE);
        } else {
           holder.deleteButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {

        return mData.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameOfMessenger, message, createdTime;

        ImageButton deleteButton;
        Messages messages;


        public ViewHolder(View itemView) {
            super(itemView);
            nameOfMessenger = (TextView)itemView.findViewById(R.id.textViewNameOfMessenger);
            message = (TextView)itemView.findViewById(R.id.textViewMessage);
            createdTime = (TextView)itemView.findViewById(R.id.textViewMessSentTime);


            deleteButton = (ImageButton) itemView.findViewById(R.id.imageButtonDelete);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int messageId=Integer.parseInt(messages.getId());
                    deleteMessage(messageId);
                    mData.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    notifyItemRangeChanged(getAdapterPosition(), mData.size());


                }
            });




            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    if (callback != null){
                        callback.messageViewClicked(messages);
                    }
                    return true;
                }
            });





        }
    }
    public interface ICallBack {
        void messageViewClicked(Messages messages);
        void deleteMessage(String messageId);
    }

    public void deleteMessage(int messageId)
    { OkHttpClient client= new OkHttpClient();
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

            }
        });

    }
}

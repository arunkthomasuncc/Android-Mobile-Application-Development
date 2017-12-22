package com.example.arun.homework07v1;

/**
 * Created by Arun on 11/17/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.ocpsoft.prettytime.PrettyTime;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Arun on 11/13/2017.
 */

public class PostListRecyclerAdapter extends RecyclerView.Adapter<PostListRecyclerAdapter.ViewHolder>{



    static ArrayList<Post> mData;
    // Context context;

    //static MainActivity mainActivity;
    static Context context;
    FirebaseAuth mAuth;
    FirebaseUser user;
    private DatabaseReference mDatabase;

    public PostListRecyclerAdapter(ArrayList<Post> mData,Context context) {
        this.mData = mData;
        this.context=context;
        this.mAuth=FirebaseAuth.getInstance();
        this.user=mAuth.getCurrentUser();
        this.mDatabase=FirebaseDatabase.getInstance().getReference();

    }


    @Override
    public PostListRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final PostListRecyclerAdapter.ViewHolder holder, final int position) {

        final Post threadsObject = mData.get(position);
        holder.threadsObj = threadsObject;




        FirebaseUser user=mAuth.getCurrentUser();

        mDatabase.child("Users").child(threadsObject.getUserID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);


                holder.authorName.setText(user.getFirstName());
                holder.postHighlight.setText(threadsObject.getPostBody());
                String createdDate=threadsObject.getCreatedAt();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
                Date date = new Date();
                try {
                    date = sdf.parse(createdDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                PrettyTime p = new PrettyTime();
                long millis = date.getTime();
                String datetime= p.format(new Date(millis));
                holder.postTime.setText(datetime);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

    @Override
    public int getItemCount() {

        return mData.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {

        TextView authorName;
        TextView postTime;
        TextView postHighlight;

        Post threadsObj;

        public ViewHolder(View itemView) {
            super(itemView);

            authorName = (TextView)itemView.findViewById(R.id.textViewPostAuthor);
            postHighlight= (TextView)itemView.findViewById(R.id.textViewPostHighlight);
            postTime=(TextView)itemView.findViewById(R.id.textViewPostTime);

            authorName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    //Toast.makeText(context,"userId"+threadsObj.getUserID(),Toast.LENGTH_SHORT).show();
                    if(threadsObj.getUserID().equals(user.getUid()))
                    {
                        Intent intent = new Intent(context, LoggedinUserProfileActivity.class);
                        context.startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(context, UserProfileDetailActivity.class);
                        intent.putExtra("userId", threadsObj.getUserID());
                        intent.putExtra("username", threadsObj.getAuthorName());
                        context.startActivity(intent);
                    }



                }
            });

        }
    }

}
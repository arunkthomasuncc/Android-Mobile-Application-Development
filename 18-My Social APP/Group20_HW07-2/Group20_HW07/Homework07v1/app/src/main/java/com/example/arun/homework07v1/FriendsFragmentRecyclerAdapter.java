package com.example.arun.homework07v1;

/**
 * Created by Arun on 11/18/2017.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;




public class FriendsFragmentRecyclerAdapter extends RecyclerView.Adapter<FriendsFragmentRecyclerAdapter.ViewHolder>{



    static ArrayList<Friend> mData;
    // Context context;

    //static MainActivity mainActivity;
    static Context context;
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference mDatabase;
    ManageFriendsActivity activity;
    public FriendsFragmentRecyclerAdapter(ArrayList<Friend> mData,Context context) {
        this.mData = mData;
        this.context=context;
        this.mAuth=FirebaseAuth.getInstance();
        this.user=mAuth.getCurrentUser();
        this.mDatabase=FirebaseDatabase.getInstance().getReference();

    }


    @Override
    public FriendsFragmentRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_raw_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final FriendsFragmentRecyclerAdapter.ViewHolder holder, int position) {


        final Friend threadsObject = mData.get(position);
        holder.threadsObj = threadsObject;

        mDatabase.child("Users").child(threadsObject.getFriendId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);


                holder.friendName.setText(user.getFirstName());
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

        TextView friendName;
        ImageButton delete;
        Friend threadsObj;

        public ViewHolder(View itemView) {
            super(itemView);

            friendName = (TextView)itemView.findViewById(R.id.textViewFriendName);
            delete=(ImageButton)itemView.findViewById(R.id.imageButtonRemoveFriend);

            friendName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                  //  Toast.makeText(context,"userId"+threadsObj.getUserID(),Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(context.getApplicationContext(), UserProfileDetailActivity.class);
                        intent.putExtra("userId", threadsObj.getFriendId());
                        intent.putExtra("username", threadsObj.getFriendName());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);




                }
            });


            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                            mData.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            notifyItemRangeChanged(getAdapterPosition(), mData.size());
                            DatabaseReference myFriends= FirebaseDatabase.getInstance().getReference().child("user-friend").child(mAuth.getCurrentUser().getUid());
                            myFriends.child(threadsObj.getFriendshipId()).removeValue();
                            DatabaseReference friendFriends= FirebaseDatabase.getInstance().getReference().child("user-friend").child(threadsObj.getFriendId());
                            friendFriends.child(threadsObj.getFriendshipId()).removeValue();





                }
            });


        }
    }

}
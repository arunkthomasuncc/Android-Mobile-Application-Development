package com.example.arun.homework07v1;

/**
 * Created by Arun on 11/18/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RequestPendingRecyclerViewAdapter extends RecyclerView.Adapter<RequestPendingRecyclerViewAdapter.ViewHolder> {


    static ArrayList<Request> mData;
    // Context context;

    //static MainActivity mainActivity;
    static Context context;
    FirebaseAuth mAuth;
    FirebaseUser user;
    private DatabaseReference mDatabase;

    public RequestPendingRecyclerViewAdapter(ArrayList<Request> mData, Context context) {
        this.mData = mData;
        this.context = context;
        this.mAuth = FirebaseAuth.getInstance();
        this.user = mAuth.getCurrentUser();
        this.mDatabase= FirebaseDatabase.getInstance().getReference();

    }


    @Override
    public RequestPendingRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_pending_raw_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RequestPendingRecyclerViewAdapter.ViewHolder holder, int position) {


        Request threadsObject = mData.get(position);
        holder.threadsObj = threadsObject;
        if(threadsObject.getRequestType()==0) {

            mDatabase.child("Users").child(threadsObject.getRequesterId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    holder.requesterName.setText(user.getFirstName());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
        else
        {

            mDatabase.child("Users").child(threadsObject.getReceiverId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user1 = dataSnapshot.getValue(User.class);
                    holder.requesterName.setText(user1.getFirstName());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
        //request type 0 means reciver, 1 means sent
        if(threadsObject.getRequestType()==1)
        {
         holder.accept.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {

        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView requesterName;
        ImageButton accept;
        ImageButton reject;


        Request threadsObj;

        public ViewHolder(View itemView) {
            super(itemView);

            requesterName = (TextView) itemView.findViewById(R.id.textViewFriendName);
            accept=(ImageButton)itemView.findViewById(R.id.imageButtonAccept);
            reject=(ImageButton)itemView.findViewById(R.id.imageButtonReject);


            requesterName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(threadsObj.requestType==0)

                    {


                        Intent intent = new Intent(context, UserProfileDetailActivity.class);
                        intent.putExtra("userId", threadsObj.getRequesterId());
                        intent.putExtra("username", threadsObj.getRequesterName());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(context, UserProfileDetailActivity.class);
                        intent.putExtra("userId", threadsObj.getReceiverId());
                        intent.putExtra("username", threadsObj.getReceiverName());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);

                    }


                }
            });

            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //make friends in both users

                    DatabaseReference firstName= mDatabase.child("Users").child(mAuth.getCurrentUser().getUid());
                    firstName.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                            User userObj=dataSnapshot.getValue(User.class);
                            String friendshipId=mDatabase.child("user-friend").child(mAuth.getCurrentUser().getUid()).push().getKey();

                            Friend friend=new Friend();
                            friend.setFriendshipId(friendshipId);
                            friend.setFriendName(threadsObj.getRequesterName());
                            friend.setFriendId(threadsObj.getRequesterId());

                            mDatabase.child("user-friend").child(mAuth.getCurrentUser().getUid()).child(friendshipId).setValue(friend);
                            friend.setFriendName(threadsObj.getReceiverName());
                            friend.setFriendId(threadsObj.receiverId);
                            mDatabase.child("user-friend").child(threadsObj.getRequesterId()).child(friendshipId).setValue(friend);

//remove request from both place

                            mDatabase.child("user-request").child(mAuth.getCurrentUser().getUid()).child(threadsObj.getRequestId()).removeValue();

                            mDatabase.child("user-request").child(threadsObj.getRequesterId()).child(threadsObj.getRequestId()).removeValue();
                           //remove from list

                            mData.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            notifyItemRangeChanged(getAdapterPosition(), mData.size());




                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    //remove request from both users

                    //remove from list


                }
            });
            reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //check deleting the send request or receive request

                    if(threadsObj.requestType==0)

                    {
                        mDatabase.child("user-request").child(mAuth.getCurrentUser().getUid()).child(threadsObj.getRequestId()).removeValue();

                        mDatabase.child("user-request").child(threadsObj.getRequesterId()).child(threadsObj.getRequestId()).removeValue();
                    }
                    else
                    {
                        mDatabase.child("user-request").child(mAuth.getCurrentUser().getUid()).child(threadsObj.getRequestId()).removeValue();

                        mDatabase.child("user-request").child(threadsObj.getReceiverId()).child(threadsObj.getRequestId()).removeValue();

                    }
                    //remove from list

                    mData.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    notifyItemRangeChanged(getAdapterPosition(), mData.size());


                }
            });
        }
    }
}


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

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AddNewFriendRecyclerViewAdapter extends RecyclerView.Adapter<AddNewFriendRecyclerViewAdapter.ViewHolder>{



    static ArrayList<User> mData;
    // Context context;

    //static MainActivity mainActivity;
    static Context context;
    FirebaseAuth mAuth;
    FirebaseUser user;
    private DatabaseReference mDatabase;

    public AddNewFriendRecyclerViewAdapter(ArrayList<User> mData,Context context) {
        this.mData = mData;
        this.context=context;
        this.mAuth=FirebaseAuth.getInstance();
        this.mDatabase= FirebaseDatabase.getInstance().getReference();
        this.user=mAuth.getCurrentUser();

    }


    @Override
    public AddNewFriendRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_new_friend_raw_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final AddNewFriendRecyclerViewAdapter.ViewHolder holder, int position) {


        User threadsObject = mData.get(position);
        holder.threadsObj = threadsObject;

        mDatabase.child("Users").child(threadsObject.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                holder.userName.setText(user.getFirstName()+" ");
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

        TextView userName;
        ImageButton addNewFriendButton;

        User threadsObj;

        public ViewHolder(View itemView) {
            super(itemView);

            userName= (TextView)itemView.findViewById(R.id.textViewFriendName);
            addNewFriendButton=(ImageButton)itemView.findViewById(R.id.imageButtonAddFriend);



            userName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                        Intent intent = new Intent(context, UserProfileDetailActivity.class);
                        intent.putExtra("userId", threadsObj.getUserId());
                      //  intent.putExtra("username", threadsObj.getFirstName()+" "+threadsObj.getLastName());
                    intent.putExtra("username", threadsObj.getFirstName()+" ");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);




                }
            });

            addNewFriendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    //create two request object one with request type =0 and one with request type =1
                    //store it in uesr-request/loggedin userid/request id/store the object with request 1
                    //store it in uesr-request/the freinduserid/request id/store the object with request 0



                    DatabaseReference firstName= mDatabase.child("Users").child(mAuth.getCurrentUser().getUid());
                    firstName.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                                 User userObj=dataSnapshot.getValue(User.class);
                            String requestId=mDatabase.child("user-request").child(mAuth.getCurrentUser().getUid()).push().getKey();
                            Request request= new Request();
                            request.setRequestId(requestId);
                            request.setRequesterId(mAuth.getCurrentUser().getUid());
                           // request.setRequesterName(userObj.getFirstName()+" "+userObj.getLastName());
                            request.setRequesterName(userObj.getFirstName()+" ");
                            request.setReceiverId(threadsObj.getUserId());
                         //   request.setReceiverName(threadsObj.getFirstName()+" "+threadsObj.getLastName());
                            request.setReceiverName(threadsObj.getFirstName()+" ");
                            request.setRequestType(1); //1 for sent

                            mDatabase.child("user-request").child(mAuth.getCurrentUser().getUid()).child(requestId).setValue(request);
                            request.setRequestType(0);
                            mDatabase.child("user-request").child(threadsObj.getUserId()).child(requestId).setValue(request);


                            mData.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            notifyItemRangeChanged(getAdapterPosition(), mData.size());


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

             //       User user=currentUser.get(0);




                }
            });

        }
    }

}

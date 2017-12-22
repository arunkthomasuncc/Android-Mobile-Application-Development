package com.example.arun.homework07v1;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewFriendFragment extends Fragment {
    private DatabaseReference mDatabase;
    FirebaseAuth mAuth;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    static ArrayList<User> allUsersList= new ArrayList<User>();
    static ArrayList<User> friendsList=new ArrayList<User>();
    static ArrayList<User> requestFriendList=new ArrayList<User>();

    boolean isLoadOnce=false;

    public AddNewFriendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       // getActivity().setTitle("add new friend");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_new_friend, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

findAllUsers();
//setFinalFriendList();



    }

public void setFinalFriendList()
{
    if(allUsersList.size()>0)
    {
      if(friendsList.size()>0)
        allUsersList.removeAll(friendsList);
      if(requestFriendList.size()>0)
        allUsersList.removeAll(requestFriendList);
    }

    mRecyclerView=(RecyclerView)getActivity().findViewById(R.id.newFriendsFragmentRecyclerView);
    // use this setting to improve performance if you know that changes
    // in content do not change the layout size of the RecyclerView
    mRecyclerView.setHasFixedSize(true);

    // use a linear layout manager
    mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL,false);
    mRecyclerView.setLayoutManager(mLayoutManager);
    // specify an adapter (see also next example)
    mAdapter = new AddNewFriendRecyclerViewAdapter(allUsersList,getActivity().getApplicationContext());
    mRecyclerView.setAdapter(mAdapter);
    mAdapter.notifyDataSetChanged();

}
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

          getFragmentManager().beginTransaction().detach(this).attach(this).commit();
            //findAllUsers();
           // setFinalFriendList();
        }
    }

    void findAllUsers()
    {
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        final DatabaseReference mAllUsers= (DatabaseReference)mDatabase.child("Users");

        mAllUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allUsersList.clear();
                for (DataSnapshot artistSnapShot:dataSnapshot.getChildren()) {

                    User userObj=artistSnapShot.getValue(User.class);
                    if(userObj.getUserId().equals(mAuth.getCurrentUser().getUid()))
                    {

                    }else {
                        allUsersList.add(userObj);
                    }

                }
                //setFinalFriendList();
                findAllFriends();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    void findAllFriends()
    {
        DatabaseReference mUserFriendsList= (DatabaseReference)mDatabase.child("user-friend").child(mAuth.getCurrentUser().getUid());
        mUserFriendsList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friendsList.clear();
                for (DataSnapshot artistSnapShot:dataSnapshot.getChildren()) {

                    Friend friend=artistSnapShot.getValue(Friend.class);

                    User userObj=new User();
                    userObj.setUserId(friend.getFriendId());
                    userObj.setFirstName(friend.getFriendName());
                    friendsList.add(userObj);


                }
                // setFinalFriendList();
                findAllRequestUsers();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    void findAllRequestUsers()
    {
        DatabaseReference mUserRequestPendingList= (DatabaseReference)mDatabase.child("user-request").child(mAuth.getCurrentUser().getUid());


        mUserRequestPendingList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                requestFriendList.clear();
                for (DataSnapshot artistSnapShot:dataSnapshot.getChildren()) {

                    Request request= artistSnapShot.getValue(Request.class);
                    User user= new User();
                    if(request.requesterId.equals(mAuth.getCurrentUser().getUid())) {
                        user.setUserId(request.getReceiverId());
                        user.setFirstName(request.getReceiverName());

                    }else {
                        user.setUserId(request.requesterId);
                        user.setFirstName(request.getRequesterName());
                    }

                    requestFriendList.add(user);

                }

                setFinalFriendList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

package com.example.arun.homework07v1;


import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
public class RequestPendingFragment extends Fragment {
    private DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    EditText postEditText;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Request> requestsList= new ArrayList<Request>();


    public RequestPendingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       // getActivity().setTitle("request pending");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request_pending, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Toast.makeText(getActivity().getApplicationContext(),"Request Pendng",Toast.LENGTH_SHORT).show();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        DatabaseReference mPosts= (DatabaseReference)mDatabase.child("user-request").child(mAuth.getCurrentUser().getUid());

        mPosts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                requestsList.removeAll(requestsList);
                for(DataSnapshot requestsSnapshot:dataSnapshot.getChildren())
                {
                    Request request=requestsSnapshot.getValue(Request.class);
                    requestsList.add(request);
                    setFinalRequestList();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    void setFinalRequestList()
    {
        mRecyclerView=(RecyclerView)getActivity().findViewById(R.id.requestPendingRecyclerView);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        mAdapter = new RequestPendingRecyclerViewAdapter(requestsList,getActivity().getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
   @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }
}

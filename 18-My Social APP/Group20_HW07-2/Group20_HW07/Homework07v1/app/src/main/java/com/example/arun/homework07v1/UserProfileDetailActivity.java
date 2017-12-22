package com.example.arun.homework07v1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class UserProfileDetailActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    EditText postEditText;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Post> postList= new ArrayList<Post>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_detail);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.app_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        setTitle("My Social APP");
        String userId=getIntent().getExtras().getString("userId");
        String username=getIntent().getExtras().getString("username");

        android.support.v7.widget.Toolbar toolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.friendProfileToolbar);




        //  getCurrentUser();
       // toolbar.setTitle(username);
        toolbar.inflateMenu(R.menu.friend_profile_toolbar);
        TextView friendname=(TextView)toolbar.findViewById(R.id.friendName);
        friendname.setText(username);

        toolbar.setOnMenuItemClickListener(new android.support.v7.widget.Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if(item.getItemId()==R.id.menu_home)
                {
                    Intent intent= new Intent(UserProfileDetailActivity.this,HomePageActivity.class);
                    startActivity(intent);
                }
                return false;

            }
        });

        TextView authorName=(TextView)findViewById(R.id.textViewPosts);
        authorName.setText(username+"'s posts");

        mDatabase= FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        DatabaseReference mPosts= (DatabaseReference)mDatabase.child("user-posts").child(userId);

        mPosts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                postList.clear();

                for (DataSnapshot artistSnapShot:dataSnapshot.getChildren()) {

                    Post postObj=artistSnapShot.getValue(Post.class);
                    postList.add(postObj);

                }


                if(postList.size()>0)
                {
                    Collections.sort(postList, new Comparator<Post>() {
                        DateFormat f = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
                        @Override
                        public int compare(Post o1, Post o2) {
                            try {
                                return f.parse(o2.getCreatedAt()).compareTo(f.parse(o1.getCreatedAt()));
                            } catch (ParseException e) {
                                throw new IllegalArgumentException(e);
                            }
                        }
                    });
                }
                mRecyclerView=(RecyclerView)findViewById(R.id.friendPostRecyclerView);
                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                mRecyclerView.setHasFixedSize(true);

                // use a linear layout manager
                mLayoutManager = new LinearLayoutManager(UserProfileDetailActivity.this, LinearLayoutManager.VERTICAL,false);
                mRecyclerView.setLayoutManager(mLayoutManager);
                // specify an adapter (see also next example)
                mAdapter = new UserDetailPostActivityRecyclerViewAdapter(postList,UserProfileDetailActivity.this);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_page_main_action_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.menu_logout:
                // implement logout
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

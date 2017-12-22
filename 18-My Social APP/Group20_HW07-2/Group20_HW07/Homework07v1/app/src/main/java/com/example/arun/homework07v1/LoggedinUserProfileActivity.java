package com.example.arun.homework07v1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class LoggedinUserProfileActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_loggedin_user_profile);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.app_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        setTitle("My Social APP");

        mDatabase= FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        getCurrentUser();
        DatabaseReference mPosts= (DatabaseReference)mDatabase.child("user-posts").child(mAuth.getCurrentUser().getUid());

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
                mRecyclerView=(RecyclerView)findViewById(R.id.myPostRecyclerView);
                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                mRecyclerView.setHasFixedSize(true);

                // use a linear layout manager
                mLayoutManager = new LinearLayoutManager(LoggedinUserProfileActivity.this, LinearLayoutManager.VERTICAL,false);
                mRecyclerView.setLayoutManager(mLayoutManager);
                // specify an adapter (see also next example)
                mAdapter = new LoggedinuserPostListAdapter(postList,LoggedinUserProfileActivity.this);
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

    void getCurrentUser()
    {

        FirebaseUser user=mAuth.getCurrentUser();

        mDatabase.child("Users").child(user.getUid()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

                        // [START_EXCLUDE]
                        if (user == null) {
                            // User is null, error out
                            Log.e("homePageActivity", "User is unexpectedly null");
                        } else {
                            // Write new post
                            // HomePageActivity.username=user.getFirstName()+" "+user.getLastName();

                            android.support.v7.widget.Toolbar toolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.myProfileToolbar);




                            //  getCurrentUser();
                            //

                            toolbar.inflateMenu(R.menu.my_profile_toolbar);
                           // toolbar.setTitle(user.getFirstName()+" "+user.getLastName());
                            TextView title=toolbar.findViewById(R.id.myProfileToolBarTitle);
                          //  title.setText(user.getFirstName()+" "+user.getLastName());
                            title.setText(user.getFirstName()+" ");
                            ImageView editProfile=(ImageView)toolbar.findViewById(R.id.editProfileImage);
                            editProfile.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                   // Toast.makeText(getApplicationContext(),"Hey Boyy",Toast.LENGTH_SHORT).show();

                                    Intent intent=new Intent(LoggedinUserProfileActivity.this,UserEditProfileActivity.class);
                                    startActivity(intent);
                                }
                            });

                           // setSupportActionBar(toolbar);
                          //  toolbar.setLogo(R.drawable.edit_profile);



                            toolbar.setOnMenuItemClickListener(new android.support.v7.widget.Toolbar.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {

                                    if(item.getItemId()==R.id.menu_friendsList)
                                    {
                                        Intent intent= new Intent(LoggedinUserProfileActivity.this,ManageFriendsActivity.class);
                                        startActivity(intent);
                                    }
                                    return false;

                                }
                            });

                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("homepageActivity", "getUser:onCancelled", databaseError.toException());
                        // [START_EXCLUDE]
                        //  setEditingEnabled(true);
                        // [END_EXCLUDE]
                    }
                });
    }
}

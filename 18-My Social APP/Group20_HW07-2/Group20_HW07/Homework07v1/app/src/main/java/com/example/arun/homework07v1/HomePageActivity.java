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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

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
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;


public class HomePageActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    EditText postEditText;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Post> postList= new ArrayList<Post>();
    static String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

       getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.app_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

       setTitle("My Social APP");
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        DatabaseReference mPosts= (DatabaseReference)mDatabase.child("posts");

        getCurrentUser();

        // Toolbar toolbar=(Toolbar)findViewById(R.id.secondNormalToolbarHomePage);
/*
               Button friendsButton=(Button)findViewById(R.id.buttonfriendsNav);
        friendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent= new Intent(HomePageActivity.this,ManageFriendsActivity.class);
                startActivity(intent);

            }
        });*/

        mPosts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                postList.clear();

                for (DataSnapshot artistSnapShot:dataSnapshot.getChildren()) {

                    Post postObj=artistSnapShot.getValue(Post.class);
                    GregorianCalendar cal=new GregorianCalendar();;
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
                    Date date = new Date();
                    try {
                        date = df.parse(postObj.getCreatedAt().toString());

                      // cal.setTime(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long millis = date.getTime();

                    Calendar minAdultAge = new GregorianCalendar();
                    minAdultAge.add(Calendar.DATE, -2);
                    Date currentdate=new Date();

                    if((currentdate.getTime()-millis)<2L*86400*1000)
                    {
                        postList.add(postObj);
                    }



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
                mRecyclerView=(RecyclerView)findViewById(R.id.allPostRecyclerView);
                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                mRecyclerView.setHasFixedSize(true);

                // use a linear layout manager
                mLayoutManager = new LinearLayoutManager(HomePageActivity.this, LinearLayoutManager.VERTICAL,false);
                mRecyclerView.setLayoutManager(mLayoutManager);
                // specify an adapter (see also next example)
                mAdapter = new PostListRecyclerAdapter(postList,HomePageActivity.this);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        postEditText=(EditText)findViewById(R.id.editTextPost);

        final ImageButton postButton=(ImageButton)findViewById(R.id.imageButtonPost);

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(postEditText.getText().length()>0)
                {

                    if(postEditText.getText().toString().length()<200) {
                        FirebaseUser user = mAuth.getCurrentUser();


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
                                            writeNewPost(user.getFirstName(), user.getLastName());
                                            postEditText.setText("");
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
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Max 200 characters allowed",Toast.LENGTH_SHORT).show();
                    }
                }

                else
                {
                    postEditText.setError("Required");
                }
            }
        });
    }

    public void  writeNewPost(String firstName,String lastName)
    {
        String postBody=postEditText.getText().toString();
        FirebaseUser user =mAuth.getCurrentUser();
        String postId= mDatabase.child("posts").push().getKey();
        Post post=new Post();
        post.setPostID(postId);
        post.setUserID(user.getUid());
        post.setPostBody(postBody);
       // post.setAuthorName(firstName+" "+lastName);
        post.setAuthorName(firstName+" ");
        Date curDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        String DateToStr = format.format(curDate);
        // System.out.println(DateToStr);
        post.setCreatedAt(DateToStr);
       // mDatabase.child("Posts").child(user.getUid()).child(postId).setValue(post);



   //     String key = mDatabase.child("posts").push().getKey();
        //Post post = new Post(userId, username, title, body);
       // Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + postId, post);
        childUpdates.put("/user-posts/" + user.getUid() + "/" + postId, post);

        mDatabase.updateChildren(childUpdates);
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
Log.d("Testing", user.getFirstName());
                        // [START_EXCLUDE]
                        if (user == null) {
                            // User is null, error out
                            Log.e("homePageActivity", "User is unexpectedly null");
                        } else {
                            // Write new post
                           // HomePageActivity.username=user.getFirstName()+" "+user.getLastName();

                            android.support.v7.widget.Toolbar toolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.HomePageSecondToolbar);

                            HomePageActivity.username=user.getFirstName();

                            Log.d("testing after setting",HomePageActivity.username);


                          //  getCurrentUser();
                         //   toolbar.setTitle(user.getFirstName()+" "+user.getLastName());
                           toolbar.inflateMenu(R.menu.homepage_second_toolbar_menu);
                            TextView name=(TextView)toolbar.findViewById(R.id.loggedinUserName);
                           // name.setText(user.getFirstName()+" "+user.getLastName()+" ");
                            name.setText(user.getFirstName()+" ");

                            name.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(HomePageActivity.this, LoggedinUserProfileActivity.class);
                                    startActivity(intent);

                                }
                            });

                            toolbar.setOnMenuItemClickListener(new android.support.v7.widget.Toolbar.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {

                                    if(item.getTitle().equals("friends List"))
                                    {
                                        Intent intent= new Intent(HomePageActivity.this,ManageFriendsActivity.class);
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

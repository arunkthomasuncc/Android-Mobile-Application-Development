package com.example.arun.homework07v1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class UserEditProfileActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    Calendar userAge;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    EditText age;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit_profile);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.app_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        setTitle("My Social APP");
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        DatabaseReference mPosts= (DatabaseReference)mDatabase.child("Users");
        mDatabase.child("Users").child(user.getUid()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User loggedInuser = dataSnapshot.getValue(User.class);

                        // [START_EXCLUDE]
                        if (loggedInuser == null) {
                            // User is null, error out
                            Log.e("usereditprofile", "User is unexpectedly null");
                        } else {
                            // Write new post

                            EditText firstName=(EditText)findViewById(R.id.editTextFirstNameProfile);
                            firstName.setText(loggedInuser.getFirstName());
                            EditText lastName=(EditText)findViewById(R.id.editTextLastnameProfile);
                            lastName.setText(loggedInuser.getLastName());
                           age=(EditText)findViewById(R.id.editTextAgeProfile);
                            age.setText(loggedInuser.getDob());
                            TextView email=(TextView) findViewById(R.id.textViewEmailProfile);
                            email.setText(loggedInuser.getEmail());

                          myCalendar= Calendar.getInstance();

                            date= new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                      int dayOfMonth) {
                                    // TODO Auto-generated method stub

                                    userAge = new GregorianCalendar(year,monthOfYear,dayOfMonth);
                                    myCalendar.set(Calendar.YEAR, year);
                                    myCalendar.set(Calendar.MONTH, monthOfYear);
                                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                    updateLabel();
                                }

                            };

                            age.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    new DatePickerDialog(UserEditProfileActivity.this, date, myCalendar
                                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
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

        Button save=(Button)findViewById(R.id.buttonSubmitProfile);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar minAdultAge = new GregorianCalendar();
                minAdultAge.add(Calendar.YEAR, -13);
                EditText firstName=(EditText)findViewById(R.id.editTextFirstNameProfile);

                EditText lastName=(EditText)findViewById(R.id.editTextLastnameProfile);

                age=(EditText)findViewById(R.id.editTextAgeProfile);

                TextView email=(TextView) findViewById(R.id.textViewEmailProfile);


                if (age.getText().length()>0&&firstName.getText().length()>0 && lastName.getText().length()>0 && email.getText().length()>0 )
                {
                    if (minAdultAge.before(userAge)) {
                        Toast.makeText(UserEditProfileActivity.this, "Age must be older than 18", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        String userEmail=email.getText().toString();
                        final String userFirstName=firstName.getText().toString();
                        final String userLastName=lastName.getText().toString();
                        final String userDOB= age.getText().toString();


                        User userObj = new User();
                        FirebaseUser fuser=mAuth.getCurrentUser();

                        userObj.setUserId(fuser.getUid());
                        userObj.setFirstName(userFirstName);
                        userObj.setLastName(userLastName);
                        userObj.setEmail(userEmail);
                        userObj.setDob(userDOB);


                        mDatabase.child("Users").child(fuser.getUid()).setValue(userObj);
                        Toast.makeText(getApplicationContext(),"Profile Updated Successfully",Toast.LENGTH_SHORT).show();
                        Intent intent =new Intent(UserEditProfileActivity.this,LoggedinUserProfileActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please provide values",Toast.LENGTH_LONG).show();
                }



            }
        });

    }
    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        age.setText(sdf.format(myCalendar.getTime()));
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

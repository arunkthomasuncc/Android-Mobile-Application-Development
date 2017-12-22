package com.example.arun.homework07v1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class SignupActivity extends AppCompatActivity {
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    EditText age;
    Calendar userAge;
    FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.app_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        setTitle("My Social APP");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
         age= (EditText)findViewById(R.id.editTextAgeSignup);

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
                new DatePickerDialog(SignupActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        Button cancel=(Button)findViewById(R.id.buttonCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button signUp= (Button)findViewById(R.id.buttonSignup);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar minAdultAge = new GregorianCalendar();
                minAdultAge.add(Calendar.YEAR, -13);
                EditText firstName=(EditText)findViewById(R.id.editTextFirstNameSignup);
                EditText lastName=(EditText)findViewById(R.id.editTextLastnameSignup);
                EditText email=(EditText)findViewById(R.id.editTextEmailSignup);
                EditText password=(EditText)findViewById(R.id.editTextPassword);
                EditText confirmPassword=(EditText)findViewById(R.id.editTextConfirmpassword);

                if (age.getText().length()>0&&firstName.getText().length()>0 && lastName.getText().length()>0 && email.getText().length()>0 && password.getText().length()>0 &&confirmPassword.getText().length()>0)
                {
                    if (minAdultAge.before(userAge)) {
                        Toast.makeText(SignupActivity.this, "Age must be older than 13", Toast.LENGTH_SHORT).show();
                    }
                    else if(password.getText().toString().equals(confirmPassword.getText().toString()))
                    {
                        String userEmail=email.getText().toString();
                        String userPassword=password.getText().toString();
                        final String userFirstName=firstName.getText().toString();
                        final String userLastName=lastName.getText().toString();
                        final String userDOB= age.getText().toString();
                        mAuth.createUserWithEmailAndPassword(userEmail,userPassword)
                                .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d("signup", "createUserWithEmail:success");
                                            FirebaseUser user = mAuth.getCurrentUser();

                                            createNewUser(task.getResult().getUser(),userFirstName,userLastName,userDOB);
                                            //updateUI(user);
                                        } else {
                                            // If sign in fails, display a message to the user.
                                           Log.w("signup", "createUserWithEmail:failure", task.getException());
                                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();
                                            //updateUI(null);
                                        }

                                        // ...
                                    }
                                });
                    }
                    else
                    {
                        Toast.makeText(SignupActivity.this, "Passwords are not matching", Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please provide values",Toast.LENGTH_LONG).show();
                }



            }
        });

    }

    private void createNewUser(FirebaseUser user,String firstName,String lastName,String dob) {
        String email = user.getEmail();
        User userObj = new User();

        userObj.setUserId(user.getUid());
        userObj.setFirstName(firstName);
        userObj.setLastName(lastName);
        userObj.setEmail(email);
        userObj.setDob(dob);


        mDatabase.child("Users").child(user.getUid()).setValue(userObj);
        Toast.makeText(getApplicationContext(),"User Created Successfully",Toast.LENGTH_SHORT).show();
      //  FirebaseAuth.getInstance().signOut();
     //   startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        finish();

    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        age.setText(sdf.format(myCalendar.getTime()));
    }


}

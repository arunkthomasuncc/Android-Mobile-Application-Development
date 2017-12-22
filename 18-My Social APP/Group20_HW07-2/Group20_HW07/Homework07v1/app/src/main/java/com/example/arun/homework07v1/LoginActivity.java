package com.example.arun.homework07v1;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    EditText userName;
    EditText password;
    FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 9001;
    private DatabaseReference mDatabase;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.app_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        setTitle("My Social APP");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        userName = (EditText) findViewById(R.id.editTextLoginUserName);
        password = (EditText) findViewById(R.id.editTextLoginPassword);


        TextView signup = (TextView) findViewById(R.id.textViewSignup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        Button login = (Button) findViewById(R.id.buttonLogin);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                normalLogin();
            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        SignInButton signInButton = findViewById(R.id.buttonGoogleLogin);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText("Sign in with Google");
            }
        }
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        if (mAuth.getCurrentUser() != null) {

            Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
            startActivity(intent);
        }


    }

    private void normalLogin() {

        if (userName.getText().length() > 0 && password.getText().length() > 0) {

            String username = userName.getText().toString().trim();
            String pw = password.getText().toString().trim();
            mAuth.signInWithEmailAndPassword(username, pw)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("homework07login", "signInWithEmail:success");
                                //  FirebaseUser user = mAuth.getCurrentUser();

                                Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                                startActivity(intent);

                                // updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("homework07login", "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }

                            // ...
                        }
                    });


        } else {
            Toast.makeText(getApplicationContext(), "Please give username and password", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("Google signin", "firebaseAuthWithGoogle:" + acct.getId());
        final GoogleSignInAccount googleAccount = acct;
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Google signin", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();


                            //Create a user here
                            createNewUser(googleAccount, user);

                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Google signin", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }

                        // ...
                    }
                });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("google sign in", "Google sign in failed", e);
                // ...
            }
        }
    }

    private void createNewUser(GoogleSignInAccount acct, FirebaseUser user) {
        final FirebaseUser fuser = user;
        String email = user.getEmail();
        final User userObj = new User();

        userObj.setUserId(user.getUid());
        userObj.setFirstName(acct.getGivenName());
        userObj.setLastName(acct.getFamilyName());
        userObj.setEmail(user.getEmail());
        userObj.setDob("");


        mDatabase.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                boolean found = false;
                for (DataSnapshot users : dataSnapshot.getChildren()) {
                    User user = users.getValue(User.class);
                    if (user.getUserId().equals(fuser.getUid())) {
                        found = true;
                        Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                        startActivity(intent);

                    }


                }
                if (!found) {
                    mDatabase.child("Users").child(fuser.getUid()).setValue(userObj);
                    Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                    startActivity(intent);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
                });


        // Toast.makeText(getApplicationContext(),"User Created Successfully",Toast.LENGTH_SHORT).show();


    }
}


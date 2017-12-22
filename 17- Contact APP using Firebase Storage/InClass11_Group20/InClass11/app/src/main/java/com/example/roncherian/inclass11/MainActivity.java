package com.example.roncherian.inclass11;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class MainActivity extends AppCompatActivity  implements GoogleApiClient.OnConnectionFailedListener {

    private StorageReference mStorageRef;

    public static final String MY_PREFS = "MyPrefs";
    private GoogleApiClient mGoogleApiCLient;
    DatabaseReference mRef;
    private FirebaseAuth mAuth;

    public static String LOGGED_IN_USER_ID = "user_id";
    public static String LOGGED_IN_USER_NAME = "user_name";
    public static String IS_GOOGLE_USER = "is_google_user";
    public static String CONTACTS = "contacts";
    public static String USERS = "Users";
    public static String USER_IDS = "user_ids";


    public static int SIGN_UP_REQUEST_CODE = 100;
    public static int GOOGLE_SIGN_IN_REQUEST_CODE = 101;
    public static int HOME_SCREEN_REQUEST_CODE = 102;


    private ValueEventListener valueEventListener, singleValueEventListener;
    private ChildEventListener childEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Login");

        mStorageRef = FirebaseStorage.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference(USERS);
        //final FirebaseUser user = mAuth.getCurrentUser();

        final EditText emailText = (EditText)findViewById(R.id.editTextUserName);
        final EditText passwordText = (EditText)findViewById(R.id.editTextPassword);
        Button signInButton = (Button)findViewById(R.id.buttonSignIn);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (emailText.getText().toString().length() == 0){
                    Toast.makeText(MainActivity.this,"Enter email",Toast.LENGTH_SHORT).show();
                    return;
                } else if (passwordText.getText().toString().length() == 0){
                    Toast.makeText(MainActivity.this,"Enter password",Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.signInWithEmailAndPassword(emailText.getText().toString(), passwordText.getText().toString())
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("Login", "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();



                                    Intent intent = new Intent(MainActivity.this, ContactActivity.class);
                                    intent.putExtra(LOGGED_IN_USER_ID,user.getUid());
                                    intent.putExtra(LOGGED_IN_USER_NAME,user.getDisplayName());
                                    startActivityForResult(intent,HOME_SCREEN_REQUEST_CODE);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("login", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(MainActivity.this, task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                    // updateUI(null);
                                }

                                // ...
                            }
                        });
            }
        });

        TextView signUpEdittext = (TextView)findViewById(R.id.textViewSignUp);

        signUpEdittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivityForResult(intent,SIGN_UP_REQUEST_CODE);
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();


        mGoogleApiCLient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        SignInButton signinButton = (SignInButton)findViewById(R.id.signInWithGoogle);
        TextView textView = (TextView)signinButton.getChildAt(0);
        textView.setText("Sign in with Google");
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithGoogle();
            }
        });



    }

    private void signInWithGoogle(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiCLient);
        startActivityForResult(signInIntent,GOOGLE_SIGN_IN_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_UP_REQUEST_CODE){

        } else if (requestCode == GOOGLE_SIGN_IN_REQUEST_CODE){

            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (googleSignInResult.isSuccess()){
                GoogleSignInAccount account = googleSignInResult.getSignInAccount();

                firebaseAuthWithGoogle(account);

            }else {
                Toast.makeText(this,googleSignInResult.getStatus().zzagl(),Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        //Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();

                            User user = new User();
                            user.setFirstName(acct.getGivenName());
                            user.setLastName(acct.getFamilyName());
                            user.setUserId(firebaseUser.getUid());
                            user.setGoogleUser(true);
                            user.setEmail(acct.getEmail());
                            user.setImageURI(acct.getPhotoUrl().toString());
                            //mRef.child(account.getId()).setValue(user);
                            goToHomePage(firebaseUser.getUid(), user);
                            Intent intent = new Intent(MainActivity.this, ContactActivity.class);
                            intent.putExtra(LOGGED_IN_USER_ID,firebaseUser.getUid());
                            intent.putExtra(LOGGED_IN_USER_NAME,firebaseUser.getDisplayName());
                            intent.putExtra(IS_GOOGLE_USER,true);
                            startActivityForResult(intent,HOME_SCREEN_REQUEST_CODE);

                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void goToHomePage(final String accountId, final User user){

        singleValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(accountId)) {
                    // run some code
                    mRef.child(accountId).setValue(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mRef.addListenerForSingleValueEvent(singleValueEventListener);

    }

    private void uploadFile(){


        Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
        StorageReference riversRef = mStorageRef.child("images/rivers.jpg");

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

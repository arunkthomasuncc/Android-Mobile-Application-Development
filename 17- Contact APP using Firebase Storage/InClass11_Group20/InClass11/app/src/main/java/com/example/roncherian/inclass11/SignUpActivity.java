package com.example.roncherian.inclass11;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

public class SignUpActivity extends AppCompatActivity {

    private static final int REQUEST_GALLERY = 101;
    static  String userChoosenTask;
    static int REQUEST_CAMERA=100;
    FirebaseStorage storage;
    StorageReference storageRef;
    ImageView profilePic;

    FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        storage = FirebaseStorage.getInstance();
        setTitle("Sign Up");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        profilePic=(ImageView)findViewById(R.id.imageViewContactProfilePic);
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final CharSequence[] items = {"Camera", "Gallery"};
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                builder.setTitle("Profile Photo").setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        boolean result=Utility.checkPermission(SignUpActivity.this);
                        if (i == 0) {
                            userChoosenTask="camera";
                            if(result) {


                                cameraIntent();
                            }
                        } else {
                            userChoosenTask="gallery";
                            if(result) {

                                galleryIntent();
                            }
                        }
                    }
                }).show();
            }


        });

        Button cancel=(Button)findViewById(R.id.buttonSignupCancel) ;
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Button signUp=(Button)findViewById(R.id.buttonContactCreate);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText firstName=(EditText)findViewById(R.id.editTextContactFirstName);
                final EditText lastName=(EditText)findViewById(R.id.editTextContactLastName);
                final EditText userName=(EditText)findViewById(R.id.editTextContactEmail);
                final EditText password=(EditText)findViewById(R.id.editTextContacthone);
                EditText repeatpw=(EditText)findViewById(R.id.editTextConfirmPassword);

                if(password.getText().toString().length()<8)
                {
                    Toast.makeText(getApplicationContext(),"Password should be min 8 characters",Toast.LENGTH_SHORT).show();
                }
                else if(!Utility.validateEmail(userName.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Enter valid Email",Toast.LENGTH_SHORT).show();
                }

                else if(password.getText().toString().equals(repeatpw.getText().toString()))
                {
                    if(userName.getText().toString().length()>0&&firstName.getText().toString().length()>0&&password.getText().toString().length()>0&&repeatpw.getText().toString().length()>0)

                    {
                        profilePic.setDrawingCacheEnabled(true);
                        profilePic.buildDrawingCache();
                        Bitmap bitmap = profilePic.getDrawingCache();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();

                        StorageReference storageReference = storage.getReference();
                        final String userUrl = UUID.randomUUID()+"";
                        StorageReference spaceRef = storageReference.child("profilepic/" + userUrl + ".jpg");
                        UploadTask uploadTask = spaceRef.putBytes(data);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                                final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                String userEmail=userName.getText().toString();
                                String userPassword=password.getText().toString();
                                mAuth.createUserWithEmailAndPassword(userEmail,userPassword)
                                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    // Sign in success, update UI with the signed-in user's information
                                                    Log.d("signup", "createUserWithEmail:success");
                                                    FirebaseUser user = mAuth.getCurrentUser();

                                                    User userobj=new User();
                                                    userobj.setFirstName(firstName.getText().toString());
                                                    userobj.setLastName(lastName.getText().toString());
                                                    userobj.setEmail(userName.getText().toString());
                                                    userobj.setUserId(user.getUid());
                                                    userobj.setImageURI(downloadUrl.toString());
                                                    userobj.setUserUrl(userUrl);

                                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(firstName.getText().toString()+" "+lastName.getText().toString()).build();
                                                    user.updateProfile(profileUpdates);

                                                    mDatabase.child(MainActivity.USERS).child(user.getUid()).setValue(userobj);
                                                    Toast.makeText(getApplicationContext(),"User Created Successfully",Toast.LENGTH_SHORT).show();
                                                    // startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                                    Intent intent = new Intent(SignUpActivity.this, ContactActivity.class);
                                                    intent.putExtra(MainActivity.LOGGED_IN_USER_ID,user.getUid());
                                                    intent.putExtra(MainActivity.LOGGED_IN_USER_NAME,firstName.getText().toString()+" "+lastName.getText().toString());
                                                    startActivity(intent);
                                                    finish();


                                                    //updateUI(user);
                                                } else {
                                                    // If sign in fails, display a message to the user.
                                                    Log.w("signup", "createUserWithEmail:failure", task.getException());
                                                    Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                                            Toast.LENGTH_SHORT).show();
                                                    //updateUI(null);
                                                }

                                                // ...
                                            }
                                        });


                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Please provide all values",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Password not matching",Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

    public void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQUEST_CAMERA);
    }

    public void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),REQUEST_GALLERY);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK)
        {
            if(requestCode==REQUEST_CAMERA)
            {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                //byte[] imageData=bytes.toByteArray();
                profilePic.setImageBitmap(thumbnail);

               /* StorageReference cameraReference=storageRef.child("profilepic/abcd");
                UploadTask uploadTask = cameraReference.putBytes(imageData);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Picasso.with(MainActivity.this).load(downloadUrl).into(camera);

                    }
                });*/

            }
            else if(requestCode==REQUEST_GALLERY) {
                Bitmap bm = null;

                try {
                    bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                    profilePic.setImageBitmap(bm);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
    }
}

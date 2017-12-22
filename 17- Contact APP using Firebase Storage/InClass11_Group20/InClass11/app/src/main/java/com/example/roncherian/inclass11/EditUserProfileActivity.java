package com.example.roncherian.inclass11;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.UUID;

public class EditUserProfileActivity extends AppCompatActivity {


    private DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    String userProficlePicUrl = "";
    ImageView profilePic;
    private static final int REQUEST_GALLERY = 101;
    static String userChoosenTask;
    static int REQUEST_CAMERA = 100;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);
        setTitle("Edit My Profile");

        mDatabase= FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user=mAuth.getCurrentUser();
        DatabaseReference mPosts= (DatabaseReference)mDatabase.child("Users");

        storageReference = FirebaseStorage.getInstance().getReference();
        profilePic=(ImageView)findViewById(R.id.imageViewEditUserProfilePic);

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

                            EditText firstName=(EditText)findViewById(R.id.editTextUserEditFirstName);
                            firstName.setText(loggedInuser.getFirstName());
                            EditText lastName=(EditText)findViewById(R.id.editTextUserEditLastName);
                            lastName.setText(loggedInuser.getLastName());

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(firstName.getText().toString()+" "+lastName.getText().toString()).build();
                            user.updateProfile(profileUpdates);

                            TextView email=(TextView) findViewById(R.id.editTextUserEditEmail);
                            email.setText(loggedInuser.getEmail());

                            ImageView profilePic = (ImageView)findViewById(R.id.imageViewEditUserProfilePic);
                            Picasso.with(EditUserProfileActivity.this).load(loggedInuser.getImageURI()).into(profilePic);

                            userProficlePicUrl = loggedInuser.getUserUrl();


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

        Button cancel=(Button)findViewById(R.id.buttonEditUserCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] items = {"Camera", "Gallery"};
                AlertDialog.Builder builder = new AlertDialog.Builder(EditUserProfileActivity.this);
                builder.setTitle("Profile Photo").setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        boolean result = Utility.checkPermission(EditUserProfileActivity.this);
                        if (i == 0) {
                            userChoosenTask = "camera";
                            if (result) {


                                cameraIntent();
                            }
                        } else {
                            userChoosenTask = "gallery";
                            if (result) {

                                galleryIntent();
                            }
                        }
                    }
                }).show();
            }
        });


        Button save=(Button)findViewById(R.id.buttonUserEditSave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText firstName=(EditText)findViewById(R.id.editTextUserEditFirstName);

                final EditText lastName=(EditText)findViewById(R.id.editTextUserEditLastName);

                final TextView email=(TextView) findViewById(R.id.editTextUserEditEmail);

                final String userEmail=email.getText().toString();
                final String userFirstName=firstName.getText().toString();
                final String userLastName=lastName.getText().toString();

                final User userObj = new User();
                final FirebaseUser fuser=mAuth.getCurrentUser();

                profilePic.setDrawingCacheEnabled(true);
                profilePic.buildDrawingCache();
                Bitmap bitmap = profilePic.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                StorageReference deletereference = storageReference.child("profilepic/"+userProficlePicUrl+".jpg");
                deletereference.delete().addOnSuccessListener(EditUserProfileActivity.this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
                final String contactUrl = UUID.randomUUID()+"";
                StorageReference spaceRef = storageReference.child("profilepic/" + contactUrl + ".jpg");
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

                        userObj.setUserId(fuser.getUid());
                        userObj.setFirstName(userFirstName);
                        userObj.setLastName(userLastName);
                        userObj.setEmail(userEmail);
                        userObj.setUserUrl(userProficlePicUrl);
                        userObj.setImageURI(downloadUrl.toString());


                        // String contactId = mDatabase.child("contacts").child(mAuth.getCurrentUser().getUid()).push().getKey();
                        // contact.setContactId(contactId);

                        mDatabase.child("Users").child(fuser.getUid()).setValue(userObj);
                        Toast.makeText(getApplicationContext(),"Profile Updated Successfully",Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    ;

                });




            }





        });


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
}
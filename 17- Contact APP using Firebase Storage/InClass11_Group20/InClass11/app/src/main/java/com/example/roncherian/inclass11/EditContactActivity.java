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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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
import java.util.ArrayList;
import java.util.UUID;

public class EditContactActivity extends AppCompatActivity {


    private static final int REQUEST_GALLERY = 101;
    static String userChoosenTask;
    static int REQUEST_CAMERA = 100;
    FirebaseStorage storage;
    StorageReference storageRef;
    ImageView profilePic;
    FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    ArrayList<Contact> contacts = new ArrayList<>();
    ValueEventListener valueEventListener;
    DatabaseReference mPostRef;

    Contact currentCOntact = new Contact();

    String contactID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        storage = FirebaseStorage.getInstance();
        setTitle("Edit Contact");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        Button cancel = (Button) findViewById(R.id.buttonContactEditCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Contact contact=(Contact)getIntent().getExtras().get("contact");
        contactID=contact.getContactId();
        EditText fname=(EditText)findViewById(R.id.editTextContactEditFirstName);
        EditText lname=(EditText)findViewById(R.id.editTextContactEditLastName);
        EditText email=(EditText)findViewById(R.id.editTextContactEditEmail);
        EditText phone=(EditText)findViewById(R.id.editTextContacEditPthone);
        profilePic=(ImageView)findViewById(R.id.imageViewEditContactProfilePic);

        fname.setText(contact.getFirstName());
        lname.setText(contact.getLastName());
        email.setText(contact.getEmail());
        phone.setText(contact.getPhone());

        Picasso.with(EditContactActivity.this).load(contact.getImageUrl()).into(profilePic);

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] items = {"Camera", "Gallery"};
                AlertDialog.Builder builder = new AlertDialog.Builder(EditContactActivity.this);
                builder.setTitle("Profile Photo").setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        boolean result = Utility.checkPermission(EditContactActivity.this);
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
        Button signUp = (Button) findViewById(R.id.buttonContactEditSave);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText firstName = (EditText) findViewById(R.id.editTextContactEditFirstName);
                final EditText lastName = (EditText) findViewById(R.id.editTextContactEditLastName);
                final EditText email = (EditText) findViewById(R.id.editTextContactEditEmail);
                final EditText phone = (EditText) findViewById(R.id.editTextContacEditPthone);

                if (firstName.getText().toString().length() > 0 && phone.getText().toString().length() > 0 && lastName.getText().toString().length() > 0 && email.getText().toString().length() > 0)

                {
                    profilePic.setDrawingCacheEnabled(true);
                    profilePic.buildDrawingCache();
                    Bitmap bitmap = profilePic.getDrawingCache();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();

                    StorageReference storageReference = storage.getReference();
                    final String contactUrl = UUID.randomUUID()+"";
                    StorageReference spaceRef = storageReference.child("contactpic/" + contactUrl + ".jpg");
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

                            Contact contact = new Contact();
                            contact.setFirstName(firstName.getText().toString());
                            contact.setLastName(lastName.getText().toString());
                            contact.setEmail(email.getText().toString());
                            contact.setPhone(phone.getText().toString());
                            contact.setUserId(mAuth.getCurrentUser().getUid());
                            contact.setImageUrl(downloadUrl.toString());
                            contact.setContactId(contactID);
                            contact.setContactUrl(contactUrl);

                            // String contactId = mDatabase.child("contacts").child(mAuth.getCurrentUser().getUid()).push().getKey();
                            // contact.setContactId(contactId);

                            mDatabase.child("contacts").child(mAuth.getCurrentUser().getUid()).child(contactID).setValue(contact);
                            finish();
                        }

                        ;

                    });

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
}
package com.example.roncherian.inclass11;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ContactActivity extends MenuBarActivity implements ContactsRecyclerAdapter.OnClick {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    DatabaseReference mPostRef;
    FirebaseAuth mAuth;

    StorageReference storageReference;
    ArrayList<Contact> contacts = new ArrayList<>();

    ChildEventListener childEventListener;
    ValueEventListener valueEventListener;

    String loggedInUserId, loggedInUserName;
    boolean isGoogleUser;

    static int EDIT_CONTACT_REQUEST_CODE = 500;
    static int ADD_CONTACT_REQUEST_CODE = 501;
    static int EDIT_MY_PROFILE_REQUEST_CODE = 502;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        setTitle("Contacts");

        ImageButton addContactButton = (ImageButton)findViewById(R.id.imageButtonAddContact);
        ImageButton editMyProfile = (ImageButton)findViewById(R.id.imageButtonEditMyProfile);
        TextView nameText = (TextView)findViewById(R.id.textViewName);

        storageReference = FirebaseStorage.getInstance().getReference();


        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent addContactIntent = new Intent(ContactActivity.this, ContactCreateActivity.class);
                startActivityForResult(addContactIntent, ADD_CONTACT_REQUEST_CODE);
            }
        });

        editMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editContactIntent = new Intent(ContactActivity.this, EditUserProfileActivity.class);
                startActivityForResult(editContactIntent, EDIT_MY_PROFILE_REQUEST_CODE);
            }
        });

        mPostRef = FirebaseDatabase.getInstance().getReference(MainActivity.CONTACTS);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        if (user.getDisplayName() == null){
            String name = getIntent().getExtras().getString(MainActivity.LOGGED_IN_USER_NAME);
            nameText.setText(name);
        } else {
            nameText.setText(user.getDisplayName());
        }


        loggedInUserId = getIntent().getExtras().getString(MainActivity.LOGGED_IN_USER_ID);
        loggedInUserName = getIntent().getExtras().getString(MainActivity.LOGGED_IN_USER_NAME);
        isGoogleUser = getIntent().getExtras().getBoolean(MainActivity.IS_GOOGLE_USER);

        mRecyclerView=(RecyclerView)findViewById(R.id.recyclerViewPosts);

        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(ContactActivity.this, LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);


        // specify an adapter (see also next example)
        mAdapter = new ContactsRecyclerAdapter(contacts,ContactActivity.this,loggedInUserId, ContactActivity.this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onStart() {
        super.onStart();
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //Contact contact = dataSnapshot.getValue(Contact.class);
                //contacts.add(contact);
                //mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //Contact contact = dataSnapshot.getValue(Contact.class);
                //contacts.remove(contact);
                //mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mPostRef.addChildEventListener(childEventListener);

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contacts.clear();

                for (DataSnapshot postSnapshot:dataSnapshot.getChildren()) {

                    Contact contact=postSnapshot.getValue(Contact.class);
                    contacts.add(contact);

                }

                mRecyclerView=(RecyclerView)findViewById(R.id.recyclerViewPosts);

                mRecyclerView.setHasFixedSize(true);

                // use a linear layout manager
                mLayoutManager = new LinearLayoutManager(ContactActivity.this, LinearLayoutManager.VERTICAL,false);
                mRecyclerView.setLayoutManager(mLayoutManager);


                // specify an adapter (see also next example)
                mAdapter = new ContactsRecyclerAdapter(contacts,ContactActivity.this,loggedInUserId,ContactActivity.this);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mPostRef.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(valueEventListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mPostRef.removeEventListener(childEventListener);
        mPostRef.removeEventListener(valueEventListener);
    }

    @Override
    public void onEditContactButtonClicked(int position, Contact contact) {

        Intent editContactIntent = new Intent(ContactActivity.this, EditContactActivity.class);
        editContactIntent.putExtra("contact", contact);
        editContactIntent.putExtra("position",position);
        startActivityForResult(editContactIntent, EDIT_CONTACT_REQUEST_CODE);

    }

    @Override
    public void OnDeleteItemClicked(int position, final Contact contact) {

        AlertDialog.Builder builder =new AlertDialog.Builder(ContactActivity.this);
        builder.setMessage("Delete the Contact?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                mPostRef.child(mAuth.getCurrentUser().getUid()).child(contact.getContactId()).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                        String url = contact.getContactId();

                        //storageReference.child()

                        StorageReference reference = FirebaseStorage.getInstance().getReference();
                        reference.child("contactpic/"+contact.getContactUrl()+".jpg").delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                contacts.remove(contact);
                                mAdapter.notifyDataSetChanged();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ContactActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog dialog=builder.create();
        dialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_CONTACT_REQUEST_CODE){
            if (resultCode == RESULT_OK){

            }
        } else if (requestCode == ADD_CONTACT_REQUEST_CODE){
            if (resultCode == RESULT_OK){

            }
        } else if (requestCode == EDIT_MY_PROFILE_REQUEST_CODE){
            if (resultCode == RESULT_OK){

            }
        }
    }
}

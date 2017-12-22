package com.example.roncherian.homework2;
//Ron Abraham Cherian - 801028678
//Arun Kunnumpuram Thomas
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final static int NEW_CONTACT_REQUEST_CODE = 100;
    final static int EDIT_CONTACT_REQUEST_CODE = 102;
    final static int DELETE_CONTACT_REQUEST_CODE = 103;
    final static int VIEW_CONTACT_REQUEST_CODE = 104;
    final static String NEW_CONTACT = "new_contact";
    final static String CONTACT_ARRAY = "contact_array";
    final static String CONTACT_LIST_TYPE_STRING = "contact_list_type";
    final static String INDEX = "index";
    static enum  CONTACT_LIST_TYPE  { VIEW, EDIT, DELETE, CREATE};
    List<Contact>myContacts = new ArrayList<Contact>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Contacts");
        findViewById(R.id.createNewContactButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CreateNewContact.class);
                //startActivity(intent);
                intent.putExtra(CONTACT_LIST_TYPE_STRING, MainActivity.CONTACT_LIST_TYPE.CREATE.ordinal());
                startActivityForResult(intent, NEW_CONTACT_REQUEST_CODE);
            }
        });

        findViewById(R.id.displayContactButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ContactList.class);
                intent.putExtra(CONTACT_ARRAY, (Serializable) myContacts);
                intent.putExtra(CONTACT_LIST_TYPE_STRING,CONTACT_LIST_TYPE.VIEW.ordinal());
                startActivity(intent);
            }
        });

        findViewById(R.id.editContactButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ContactList.class);
                intent.putExtra(CONTACT_LIST_TYPE_STRING, CONTACT_LIST_TYPE.EDIT.ordinal());
                intent.putExtra(CONTACT_ARRAY, (Serializable) myContacts);
                startActivityForResult(intent, EDIT_CONTACT_REQUEST_CODE);
            }
        });

        findViewById(R.id.deleteContactButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ContactList.class);
                intent.putExtra(CONTACT_LIST_TYPE_STRING, CONTACT_LIST_TYPE.DELETE.ordinal());
                intent.putExtra(CONTACT_ARRAY, (Serializable) myContacts);
                startActivityForResult(intent, DELETE_CONTACT_REQUEST_CODE);
            }
        });

        findViewById(R.id.finishButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Bye", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == NEW_CONTACT_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                Contact contact = (Contact) data.getExtras().getSerializable(NEW_CONTACT);
                myContacts.add(contact);
                //List<Contact>myContacts = new ArrayList<Contact>();
                //myContacts.add(contact);
                Intent intent1 = new Intent(MainActivity.this,ContactList.class);
                intent1.putExtra(MainActivity.CONTACT_ARRAY, (Serializable) myContacts);
                intent1.putExtra(MainActivity.CONTACT_LIST_TYPE_STRING, MainActivity.CONTACT_LIST_TYPE.VIEW.ordinal());
                startActivity(intent1);
                Log.d("Demo","My new Contact"+contact.toString());

            }
        } else if (requestCode == DELETE_CONTACT_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                myContacts = (List<Contact>)data.getExtras().getSerializable(MainActivity.CONTACT_ARRAY);
                Intent intent1 = new Intent(MainActivity.this,ContactList.class);
                intent1.putExtra(MainActivity.CONTACT_ARRAY, (Serializable) myContacts);
                intent1.putExtra(MainActivity.CONTACT_LIST_TYPE_STRING, MainActivity.CONTACT_LIST_TYPE.VIEW.ordinal());
                startActivity(intent1);
            }
        } else if (requestCode == EDIT_CONTACT_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                myContacts = (List<Contact>)data.getExtras().getSerializable(MainActivity.CONTACT_ARRAY);

            }
        }
    }
}

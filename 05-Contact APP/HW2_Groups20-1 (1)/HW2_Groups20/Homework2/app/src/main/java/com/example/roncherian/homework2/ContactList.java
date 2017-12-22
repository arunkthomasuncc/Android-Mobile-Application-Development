package com.example.roncherian.homework2;
//Ron Abraham Cherian - 801028678
//Arun Kunnumpuram Thomas
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ContactList extends AppCompatActivity {

    //int contactListType = -1;
    List<Contact>myContactsList = null;
    int globalIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        setTitle("Contact List");

        myContactsList = (List<Contact>) getIntent().getExtras().getSerializable(MainActivity.CONTACT_ARRAY);
        populateContactList(myContactsList);


    }

    private void populateContactList(final List<Contact>myContactList){

        LinearLayout parentView = (LinearLayout) findViewById(R.id.editPage);
        parentView.removeAllViews();
        if (myContactList.size() == 0){

            //finish();
        }
        for (int i = 0; i < myContactList.size(); i++) {

            Contact contact = myContactList.get(i);
            LayoutInflater inflator1 = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            RelativeLayout relativeLayout = (RelativeLayout) inflator1.inflate(R.layout.contacts_list, null);

            TextView name = (TextView) relativeLayout.findViewById(R.id.listNameTextView);
            TextView phone = (TextView) relativeLayout.findViewById(R.id.listMobileTextView);
            ImageView imageView = (ImageView) relativeLayout.findViewById(R.id.contactProfImageView);
            name.setText(contact.getFirstName() + " " + contact.getLastName());
            phone.setText(contact.getPhone());
            Bitmap bitmap = BitmapFactory.decodeByteArray(contact.getContactImage(),0,contact.getContactImage().length);
            imageView.setImageBitmap(bitmap);
            relativeLayout.setTag(1000 + i);
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("demo","Relactive Layout clicked");
                    int tag = (int) ((RelativeLayout)view).getTag();
                    int index = tag - 1000;
                    globalIndex = index;
                    int contactListType = -1;
                    if (null != getIntent() && null!= getIntent().getExtras()){
                        contactListType = (int)getIntent().getExtras().getInt(MainActivity.CONTACT_LIST_TYPE_STRING);
                    }
                    if (contactListType == MainActivity.CONTACT_LIST_TYPE.EDIT.ordinal()){
                        Intent intent = new Intent(ContactList.this,CreateNewContact.class);
                        intent.putExtra(MainActivity.CONTACT_ARRAY, (Serializable) myContactList);
                        intent.putExtra(MainActivity.INDEX,  index);
                        intent.putExtra(MainActivity.CONTACT_LIST_TYPE_STRING,MainActivity.CONTACT_LIST_TYPE.EDIT.ordinal());
                        startActivityForResult(intent, MainActivity.EDIT_CONTACT_REQUEST_CODE);
                    } else if (contactListType == MainActivity.CONTACT_LIST_TYPE.DELETE.ordinal()){
                        AlertDialog.Builder builder = new AlertDialog.Builder(ContactList.this);

                        builder.setTitle("Confirm Delete").
                                setCancelable(false).
                                setMessage("Do you really want to delete this?").
                                setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Log.d("demo","Clicked Ok");
                                        myContactList.remove(globalIndex);

                                        Intent intent = new Intent();
                                        intent.putExtra(MainActivity.CONTACT_ARRAY, (Serializable) myContactList);
                                        setResult(RESULT_OK,intent);
                                        finish();
                                    }
                                }).
                                setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Log.d("demo","Clicked No");
                                    }
                        });
                        builder.show();

                    } else if (contactListType == MainActivity.CONTACT_LIST_TYPE.VIEW.ordinal()){
                        Intent intent = new Intent(ContactList.this,CreateNewContact.class);
                        intent.putExtra(MainActivity.CONTACT_ARRAY, (Serializable) myContactList);
                        intent.putExtra(MainActivity.INDEX,  index);
                        intent.putExtra(MainActivity.CONTACT_LIST_TYPE_STRING,MainActivity.CONTACT_LIST_TYPE.VIEW.ordinal());
                        startActivityForResult(intent, MainActivity.VIEW_CONTACT_REQUEST_CODE);
                    }

                }
            });
            parentView.addView(relativeLayout);


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == MainActivity.EDIT_CONTACT_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                if (null != data.getExtras() && 0 <= data.getExtras().getInt(MainActivity.INDEX)){
                    myContactsList.set(data.getExtras().getInt(MainActivity.INDEX), (Contact) data.getExtras().getSerializable(MainActivity.NEW_CONTACT));
                    populateContactList(myContactsList);

                    /*Intent intent = new Intent();
                    intent.putExtra(MainActivity.CONTACT_ARRAY,(Serializable) myContactsList);
                    setResult(RESULT_OK,intent);
                    finish();*/
                }

            }
        }
    }

}

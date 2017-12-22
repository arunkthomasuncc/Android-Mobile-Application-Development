package com.example.roncherian.homework2;
//Ron Abraham Cherian - 801028678
//Arun Kunnumpuram Thomas
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CreateNewContact extends AppCompatActivity {

    final static int CAMERA_REQUEST_CODE = 101;
    boolean imageTaken = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_contact);
        setTitle("Create New Contact");

        final EditText firstNameET = (EditText)findViewById(R.id.firstNameEditText);
        final EditText lastNameET = (EditText)findViewById(R.id.lastNameEditText);
        final EditText companyET = (EditText)findViewById(R.id.CompanyEditText);
        final EditText phoneET = (EditText)findViewById(R.id.phoneEditText);
        final EditText emailET = (EditText)findViewById(R.id.emailEditText);
        final EditText urlET = (EditText)findViewById(R.id.urlEditText);
        final EditText addressET = (EditText)findViewById(R.id.addressEditText);
        final EditText birthdayET = (EditText)findViewById(R.id.birthdayEditText);
        final EditText nicknameET = (EditText)findViewById(R.id.nicknameEditText);
        final EditText fbProfileET = (EditText)findViewById(R.id.fbProfileURLEditText);
        final EditText twitterProfileTE = (EditText)findViewById(R.id.twitterProfURLEditText);
        final EditText skypeET = (EditText)findViewById(R.id.SkypeEditText);
        final EditText youtubeChannelET = (EditText)findViewById(R.id.youtubeChannelEditText);

        final ImageView contactImageView = (ImageView)findViewById(R.id.captureImageView);

        int forEditContactType = -1;
        if (null != getIntent() && null != getIntent().getExtras()){
            forEditContactType = getIntent().getExtras().getInt(MainActivity.CONTACT_LIST_TYPE_STRING);
            KeyListener variable;
            //variable = editText.getKeyListener();

            if (forEditContactType == MainActivity.CONTACT_LIST_TYPE.EDIT.ordinal() || forEditContactType == MainActivity.CONTACT_LIST_TYPE.VIEW.ordinal()){
                int index = (int)getIntent().getExtras().getInt(MainActivity.INDEX);
                List<Contact>myContactsList = (ArrayList<Contact>)getIntent().getExtras().getSerializable(MainActivity.CONTACT_ARRAY);
                Contact contact = myContactsList.get(index);
                firstNameET.setText(contact.getFirstName());
                lastNameET.setText(contact.getLastName());
                companyET.setText(contact.getCompany());
                phoneET.setText(contact.getPhone());
                emailET.setText(contact.getEmail());
                urlET.setText(contact.getUrl());
                addressET.setText(contact.getAddress());
                birthdayET.setText(contact.getBirthday());
                nicknameET.setText(contact.getNickname());
                fbProfileET.setText(contact.getFacebookProfileUrl());
                twitterProfileTE.setText(contact.getTwitterProfileUrl());
                skypeET.setText(contact.getSkype());
                youtubeChannelET.setText(contact.getYoutubeChannel());
                Bitmap bitmap = BitmapFactory.decodeByteArray(contact.getContactImage(),0,contact.getContactImage().length);
                contactImageView.setImageBitmap(bitmap);
            }

            if (forEditContactType == MainActivity.CONTACT_LIST_TYPE.VIEW.ordinal()){
                //firstNameET.setFocusable(false);
                firstNameET.setKeyListener(null);
                lastNameET.setKeyListener(null);
                companyET.setKeyListener(null);
                phoneET.setKeyListener(null);
                emailET.setKeyListener(null);

                urlET.setFocusable(false);
                urlET.setClickable(true);
                urlET.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String url = urlET.getText().toString();
                        Intent i = new Intent(Intent.ACTION_VIEW);

                        if (url.startsWith("https://") || url.startsWith("http://"))
                            i.setData(Uri.parse(url));
                        else if (url.length() > 0)
                            i.setData(Uri.parse("http://" + url));
                        else
                            return;
                        startActivity(i);
                    }
                });

                addressET.setKeyListener(null);
                nicknameET.setKeyListener(null);


                fbProfileET.setFocusable(false);
                fbProfileET.setClickable(true);
                fbProfileET.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String url = fbProfileET.getText().toString();
                        Intent i = new Intent(Intent.ACTION_VIEW);

                        if (url.startsWith("https://") || url.startsWith("http://"))
                            i.setData(Uri.parse(url));
                        else if (url.length() > 0)
                            i.setData(Uri.parse("http://" + url));
                        else
                            return;
                        startActivity(i);
                    }
                });

                twitterProfileTE.setFocusable(false);
                twitterProfileTE.setClickable(true);
                twitterProfileTE.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String url = twitterProfileTE.getText().toString();
                        Intent i = new Intent(Intent.ACTION_VIEW);

                        if (url.startsWith("https://") || url.startsWith("http://"))
                            i.setData(Uri.parse(url));
                        else if (url.length() > 0)
                            i.setData(Uri.parse("http://" + url));
                        else
                            return;
                        startActivity(i);
                    }
                });

                skypeET.setFocusable(false);
                skypeET.setClickable(true);
                skypeET.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String url = skypeET.getText().toString();
                        Intent i = new Intent(Intent.ACTION_VIEW);

                        if (url.startsWith("https://") || url.startsWith("http://"))
                            i.setData(Uri.parse(url));
                        else if (url.length() > 0)
                            i.setData(Uri.parse("http://" + url));
                        else
                            return;
                        startActivity(i);
                    }
                });

                youtubeChannelET.setFocusable(false);
                youtubeChannelET.setClickable(true);
                youtubeChannelET.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String url = youtubeChannelET.getText().toString();
                        Intent i = new Intent(Intent.ACTION_VIEW);

                        if (url.startsWith("https://") || url.startsWith("http://"))
                            i.setData(Uri.parse(url));
                        else if (url.length() > 0)
                            i.setData(Uri.parse("http://" + url));
                        else
                            return;
                        startActivity(i);
                    }
                });

                //birthdayET.setFocusable(false);
                //contactImageView.setOnKeyListener(null);
                contactImageView.setOnClickListener(null);

                findViewById(R.id.saveContactButton).setVisibility(View.GONE);
            }

        }


        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                birthdayET.setText(sdf.format(myCalendar.getTime()));
            }

        };


        if (forEditContactType == MainActivity.CONTACT_LIST_TYPE.EDIT.ordinal() || forEditContactType == MainActivity.CONTACT_LIST_TYPE.CREATE.ordinal()){
            birthdayET.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(CreateNewContact.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.getDatePicker().setMinDate(-3786807600000l);
                    datePickerDialog.show();
                }
            });
        }

        findViewById(R.id.saveContactButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String emailAddress = emailET.getText().toString().trim();
                if (emailAddress.length() > 0 && !android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()){
                    Toast.makeText(getApplicationContext(), "Enter valid Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (firstNameET.getText().toString().trim().length() == 0){
                    Toast.makeText(getApplicationContext(), "Enter valid first name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (lastNameET.getText().toString().trim().length() == 0){
                    Toast.makeText(getApplicationContext(), "Enter valid last name", Toast.LENGTH_SHORT).show();
                    return;
                }

                String phoneNumberEntered = phoneET.getText().toString().trim();
                String MobilePattern = "[+]{0,1}[0-9]+";
                if (phoneNumberEntered.length() == 0 || !phoneNumberEntered.matches(MobilePattern)){
                    Toast.makeText(getApplicationContext(), "Enter valid Phone Number", Toast.LENGTH_SHORT).show();
                    return;
                }

                Contact contact = new Contact();
                contact.setFirstName(firstNameET.getText().toString());
                contact.setLastName(lastNameET.getText().toString());
                contact.setCompany(companyET.getText().toString());
                contact.setPhone(phoneET.getText().toString());
                contact.setEmail(emailET.getText().toString());
                contact.setUrl(urlET.getText().toString());
                contact.setAddress(addressET.getText().toString());
                //contact.setbirt
                contact.setNickname(nicknameET.getText().toString());
                contact.setFacebookProfileUrl(fbProfileET.getText().toString());
                contact.setTwitterProfileUrl(twitterProfileTE.getText().toString());
                contact.setSkype(skypeET.getText().toString());
                contact.setYoutubeChannel(youtubeChannelET.getText().toString());
                contact.setImageTaken(imageTaken);


                contact.setBirthday(birthdayET.getText().toString().trim());

                if (imageTaken && null != contactImageView && null != contactImageView.getDrawable()){
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) contactImageView.getDrawable();
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] bytes = byteArrayOutputStream.toByteArray();
                    contact.setContactImage(bytes);
                } else {
                    ImageView imageView = (ImageView) findViewById(R.id.contactProfImageView);
                    Drawable drawable = getResources().getDrawable(R.drawable.default_image);
                    BitmapDrawable bitmapDrawable = (BitmapDrawable)drawable;
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] bytes = byteArrayOutputStream.toByteArray();
                    contact.setContactImage(bytes);
                }

                Intent intent = new Intent();

                int contactType = -1;
                if (null != getIntent() && null != getIntent().getExtras()){
                    contactType = getIntent().getExtras().getInt(MainActivity.CONTACT_LIST_TYPE_STRING);
                }

                if (contactType == MainActivity.CONTACT_LIST_TYPE.CREATE.ordinal()){
                    intent.putExtra(MainActivity.NEW_CONTACT,contact);
                } else if (contactType == MainActivity.CONTACT_LIST_TYPE.EDIT.ordinal()){

                    int index = (int)getIntent().getExtras().getInt(MainActivity.INDEX);
                    intent.putExtra(MainActivity.NEW_CONTACT,contact);
                    intent.putExtra(MainActivity.INDEX,index);
                }

                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    public void captureImageClick(View view){

        ImageView imageView = (ImageView)view;
        findViewById(R.id.captureImageView);

        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent,CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAMERA_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                Log.d("demp",data.toString());
                Bitmap image = (Bitmap) data.getExtras().get("data");
                ImageView imageView = (ImageView) findViewById(R.id.captureImageView);
                imageView.setImageBitmap(image);
                imageTaken = true;
            }


        }
    }
}

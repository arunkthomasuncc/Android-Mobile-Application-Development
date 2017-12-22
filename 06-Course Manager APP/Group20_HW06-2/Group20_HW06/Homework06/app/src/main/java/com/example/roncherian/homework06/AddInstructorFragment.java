package com.example.roncherian.homework06;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import io.realm.Realm;

import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddInstructorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class AddInstructorFragment extends MenuBarFragments {

    private OnFragmentInteractionListener mListener;

    String instructorId = "";

    InstructorObj instructorObj = null;

    byte[] imageByteArray = null;

    public AddInstructorFragment() {
        // Required empty public constructor
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button resetButton = (Button)getActivity().findViewById(R.id.buttonInstrReset);
        Button submitButton = (Button)getActivity().findViewById(R.id.buttonInstrAdd);

        final TextView firstName = (TextView)getActivity().findViewById(R.id.editTextFN);
        final TextView lastName = (TextView)getActivity().findViewById(R.id.editTextLN);
        final TextView email = (TextView)getActivity().findViewById(R.id.editTextInstrEmail);
        final TextView website = (TextView)getActivity().findViewById(R.id.editTextInstrWebsite);
        final ImageView addInstructorPhoto = (ImageView)getActivity().findViewById(R.id.imageViewAddInstPhoto);

        AlertDialog.Builder builderPhotoSelection = new AlertDialog.Builder(getActivity());
        builderPhotoSelection.setTitle("Add Photo").setCancelable(true).
                setItems(new CharSequence[]{"Gallery","Camera"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0){
                            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                                ActivityCompat.requestPermissions( getActivity(), new String[] {  Manifest.permission.READ_EXTERNAL_STORAGE  },
                                        301 );
                            } else if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                                ActivityCompat.requestPermissions( getActivity(), new String[] {  Manifest.permission.WRITE_EXTERNAL_STORAGE  },
                                        302 );
                            } else {
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                //intent.putExtra(MediaStore.EXTRA_OUTPUT,pictureUri);
                                startActivityForResult(intent, MainActivity.GALLERY_REQUEST_CODE);
                            }

                        } else if (i == 1){
                            if ( ContextCompat.checkSelfPermission( getActivity(), Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED ) {

                                ActivityCompat.requestPermissions( getActivity(), new String[] {  Manifest.permission.CAMERA  },
                                        300 );
                            } else {
                                Log.d("demo","Clicked List Dialog selected: Camera");
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent,MainActivity.CAMERA_REQUEST_CODE);
                            }

                        }
                    }
                });

        final AlertDialog alertDialogSimpleDialog = builderPhotoSelection.create();

        addInstructorPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogSimpleDialog.show();
            }
        });
        if (null!= instructorId && !instructorId.equals("")){
            instructorObj = new InstructorObj();
            resetButton.setVisibility(View.INVISIBLE);
            submitButton.setVisibility(View.INVISIBLE);
            addInstructorPhoto.setEnabled(false);
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    Instructor instructor = realm.where(Instructor.class).equalTo("id",instructorId).findFirst();
                    instructorObj.setEmail(instructor.getEmail());
                    instructorObj.setId(instructor.getId());
                    instructorObj.setWebsite(instructor.getWebsite());
                    instructorObj.setFirstName(instructor.getFirstName());
                    instructorObj.setLastName(instructor.getLastName());
                    instructorObj.setImageArray(instructor.getImageArray());
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    firstName.setText(instructorObj.getFirstName());
                    lastName.setText(instructorObj.getLastName());
                    email.setText(instructorObj.getEmail());
                    website.setText(instructorObj.getWebsite());
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(instructorObj.getImageArray(), 0, instructorObj.getImageArray().length, options);
                    addInstructorPhoto.setImageBitmap(bitmap);
                }
            });
            return;
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirm reset all the fields to empty").
                setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                firstName.setText("");
                lastName.setText("");
                email.setText("");
                website.setText("");
                imageByteArray=null;
                addInstructorPhoto.setImageResource(R.drawable.add_photo);
            }
        }).setCancelable(false);

        final AlertDialog alertDialog =  builder.create();


        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (imageByteArray==null){
                    Toast.makeText(getActivity(),"Add a profile Image", Toast.LENGTH_SHORT).show();
                } else if (firstName.getText().toString().length()==0){
                    Toast.makeText(getActivity(),"Enter First Name", Toast.LENGTH_SHORT).show();
                } else if (lastName.getText().toString().length()==0){
                    Toast.makeText(getActivity(),"Enter Last Name", Toast.LENGTH_SHORT).show();
                } else if (email.getText().toString().length()==0){
                    Toast.makeText(getActivity(),"Enter Email", Toast.LENGTH_SHORT).show();
                } else if (!MainActivity.isValidEmail(email.getText().toString())){
                    Toast.makeText(getActivity(),"Enter a valid email", Toast.LENGTH_SHORT).show();
                } else if (website.getText().toString().length()==0){
                    Toast.makeText(getActivity(),"Enter Website", Toast.LENGTH_SHORT).show();
                } else if (!MainActivity.isValidWebUrl(website.getText().toString())){
                    Toast.makeText(getActivity(),"Enter a valid Website", Toast.LENGTH_SHORT).show();
                } else {
                    Realm realm = Realm.getDefaultInstance();

                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            Instructor instructor = realm.createObject(Instructor.class, UUID.randomUUID().toString());
                            instructor.setEmail(email.getText().toString().trim());
                            instructor.setFirstName(firstName.getText().toString().trim());
                            instructor.setLastName(lastName.getText().toString().trim());
                            instructor.setWebsite(website.getText().toString().trim());
                            instructor.setUsername(MainActivity.LOGGED_IN_USERNAME);
                            instructor.setImageArray(imageByteArray);
                            onButtonPressed();
                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {

                        }
                    });
                }
            }
        });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            instructorId = bundle.getString(MainActivity.INSTRUCTOR_ID, "");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (instructorId == null || instructorId.equals(""))
            getActivity().setTitle("Add Instructor");
        else {
            getActivity().setTitle("Instructor Information");
        }
        View view = inflater.inflate(R.layout.fragment_add_instructor,container,false);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.orRegisterInstructor();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void orRegisterInstructor();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView addPhotoButton = (ImageView) getActivity().findViewById(R.id.imageViewAddInstPhoto);
        switch(requestCode) {
            case MainActivity.GALLERY_REQUEST_CODE:
                if(resultCode == RESULT_OK){

                    Bitmap bm=null;
                    if (data != null) {
                        try {
                            Uri selectedImage = data.getData();

                            bm = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), selectedImage);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            imageByteArray = stream.toByteArray();
                            addPhotoButton.setImageBitmap(bm);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }



                }

                break;
            case MainActivity.CAMERA_REQUEST_CODE:
                if(resultCode == RESULT_OK){
                    Bitmap bmp = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    imageByteArray = stream.toByteArray();
                    addPhotoButton.setImageBitmap(bmp);
                }
                break;
        }
    }
}

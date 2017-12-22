package com.example.roncherian.homework06;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class RegisterFragment extends ExitMenuFragment {

    private OnFragmentInteractionListener mListener;

    byte[] imageByteArray = null;
    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Register");
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(User user) {
        if (mListener != null) {
            mListener.onRegisterUserPressed(user.getUsername());
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


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ImageButton addPhotoButton = (ImageButton) getActivity().findViewById(R.id.imageButtonAddPhoto);

        Button registerButton = (Button)getActivity().findViewById(R.id.buttonRegister);

        final EditText firstName = (EditText)getActivity().findViewById(R.id.editTextFirstName);
        final EditText lastName = (EditText)getActivity().findViewById(R.id.editTextLastName);
        final EditText username = (EditText)getActivity().findViewById(R.id.editTextUsername);
        final EditText password = (EditText)getActivity().findViewById(R.id.editTextPassword);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo").setCancelable(true).
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
                                Log.d("demo","Clicked List Dialog selected: Gallery");
                                //Uri pictureUri=getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
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

        final AlertDialog alertDialogSimpleDialog = builder.create();
        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogSimpleDialog.show();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fn = firstName.getText().toString();
                String ln = lastName.getText().toString();
                String un = username.getText().toString();
                String pass = password.getText().toString();

                if (imageByteArray==null){
                    Toast.makeText(getActivity(),"Please add an profile image",Toast.LENGTH_SHORT).show();
                    return;
                }else if (null==fn || fn.isEmpty()){
                    Toast.makeText(getActivity(),"Enter your first name",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (null==ln || ln.isEmpty()){
                    Toast.makeText(getActivity(),"Enter your last name",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (null==un || un.isEmpty()){
                    Toast.makeText(getActivity(),"Enter your user name",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (null==pass || pass.isEmpty()){
                    Toast.makeText(getActivity(),"Enter your password",Toast.LENGTH_SHORT).show();
                    return;
                } else if (pass.length()<8){
                    Toast.makeText(getActivity(),"Password should be minimum 8 characters",Toast.LENGTH_SHORT).show();
                    return;
                }
                Realm realm = Realm.getDefaultInstance();

                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        User user = realm.createObject(User.class, username.getText().toString().trim());
                        user.setFirstName(firstName.getText().toString().trim());
                        user.setLastName(lastName.getText().toString().trim());
                        user.setPassword(password.getText().toString().trim());
                        if (imageByteArray!=null){
                            user.setImageArray(imageByteArray);
                        }
                        mListener.onRegisterUserPressed(user.getUsername());
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {

                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        Log.d("demo",error.getMessage());
                        if (error instanceof RealmPrimaryKeyConstraintException){
                            Toast.makeText(getActivity(),"Username Alreday Registered",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageButton addPhotoButton = (ImageButton) getActivity().findViewById(R.id.imageButtonAddPhoto);
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
                    //Uri selectedImage = data.getData();
                    //addPhotoButton.setImageURI(selectedImage);
                    Bitmap bmp = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    imageByteArray = stream.toByteArray();
                    addPhotoButton.setImageBitmap(bmp);
                }
                break;
        }
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
        void onRegisterUserPressed(String username);
    }

    private File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new   File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    private Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

}

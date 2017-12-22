package com.example.roncherian.homework06;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.exceptions.RealmException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LoginFragment extends ExitMenuFragment {

    private OnFragmentInteractionListener mListener;

    public LoginFragment() {
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
        getActivity().setTitle("Course Manager");
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onSignUpButtonPressed();
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

        TextView signUpTextView = (TextView)getActivity().findViewById(R.id.textViewSignUp);
        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onSignUpButtonPressed();
            }
        });

        Button loginButton = (Button)getActivity().findViewById(R.id.buttonLogin);

        final EditText username = (EditText)getActivity().findViewById(R.id.editTextUsername);
        final EditText password = (EditText)getActivity().findViewById(R.id.editTextPassword);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (username.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(),"Please enter username",Toast.LENGTH_SHORT).show();
                    return;
                } else if (password.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(),"Please enter password",Toast.LENGTH_SHORT).show();
                    return;
                }
                Realm realm = Realm.getDefaultInstance();
                final User[] user = new User[1];

                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        user[0] = realm.where(User.class).equalTo("username",username.getText().toString()).equalTo("password",password.getText().toString()).findFirst();
                        //realm.commitTransaction();
                        if (null!= user[0]){
                            Log.d("demo", user[0].toString());
                            if (mListener!=null){
                                mListener.onLoginButtonPressed(user[0].getUsername());
                            }
                        } else {
                            throw new RealmException("Incorrect username/password.");
                        }
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        Log.d("Sucess","hoooray");

                        //refreshData();
                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

                /*try{
                    realm.beginTransaction();
                    user[0] = realm.where(User.class).equalTo("username",username.getText().toString()).equalTo("password",password.getText().toString()).findFirst();
                    realm.commitTransaction();
                    if (null!= user[0]){
                        Log.d("demo", user[0].toString());
                        if (mListener!=null){
                            mListener.onLoginButtonPressed(user[0]);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    if (realm.isInTransaction()){
                        realm.commitTransaction();
                    }
                }*/
            }
        });
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
        void onSignUpButtonPressed();
        void onLoginButtonPressed(String username);
    }
}

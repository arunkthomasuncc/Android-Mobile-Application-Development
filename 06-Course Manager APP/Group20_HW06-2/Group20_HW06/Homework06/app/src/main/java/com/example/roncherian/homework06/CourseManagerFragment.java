package com.example.roncherian.homework06;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import io.realm.Realm;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CourseManagerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class CourseManagerFragment extends MenuBarFragments implements CourseListRecyclerViewAdapter.ResultsInterface{

    private OnFragmentInteractionListener mListener;

    User loggedInUser = null;

    String loggedInUsername = "";

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    public CourseManagerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String userId = bundle.getString(MainActivity.USERNAME, "");
            loggedInUsername = userId;
            if (!userId.equals("")){
                /*Realm realm = Realm.getDefaultInstance();
                try {
                    loggedInUser = realm.where(User.class).equalTo("username", userId).findFirst();
                    // do something with the person ...
                } finally {
                    realm.close();
                }*/
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        getActivity().setTitle("Course Manager");
        View view = inflater.inflate(R.layout.fragment_course_manager, container, false);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /*if (loggedInUser == null){
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    loggedInUser = realm.where(User.class).equalTo("username", loggedInUsername).findFirst();
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {

                }
            });
            return;
        }*/


        mRecyclerView=(RecyclerView)getActivity().findViewById(R.id.recyclerViewCourseList);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);


        // specify an adapter (see also next example)
        //ingredientList.add("");
        mAdapter = new CourseListRecyclerViewAdapter( getActivity(),CourseManagerFragment.this);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();



        ImageButton addCourseButton = (ImageButton)getActivity().findViewById(R.id.imageButtonAddCourse);

        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onCreateNewCourseButtonPressed(loggedInUsername);
            }
        });

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(User user) {
        if (mListener != null) {
            mListener.onCreateNewCourseButtonPressed(loggedInUsername);
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
    public void courseItemLongClicked(String courseId) {

        final String myCourseId = courseId;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete Course").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Realm realm = Realm.getDefaultInstance();
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        User user = realm.where(User.class).equalTo("username",MainActivity.LOGGED_IN_USERNAME).findFirst();

                        for (int i =0; i<user.getCourses().size();i++){
                            if (user.getCourses().get(i).getId().equals(myCourseId)){
                                user.getCourses().remove(i);
                            }
                        }
                        Courses courses = realm.where(Courses.class).equalTo("id",myCourseId).findFirst();
                        courses.deleteFromRealm();
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {

                        mAdapter = new CourseListRecyclerViewAdapter( getActivity(),CourseManagerFragment.this);
                        mRecyclerView.setAdapter(mAdapter);

                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create();

        final AlertDialog dialog = builder.create();

        dialog.show();
    }

    @Override
    public void courseItemPressed(int position) {

        if (mListener != null){
            mListener.viewCourseItemPressed(position,MainActivity.LOGGED_IN_USERNAME);
        }

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onCreateNewCourseButtonPressed(String username);
        void viewCourseItemPressed(int position, String username);
    }
}

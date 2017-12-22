package com.example.roncherian.homework06;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import io.realm.Realm;
import io.realm.RealmResults;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InstructorsListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class InstructorsListFragment extends MenuBarFragments implements InstructorDetailListingRVAdapter.ResultsInterface{

    private OnFragmentInteractionListener mListener;

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    public InstructorsListFragment() {
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
        getActivity().setTitle("Instructors");
        View view = inflater.inflate(R.layout.fragment_instructors_list, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        mRecyclerView=(RecyclerView)getActivity().findViewById(R.id.recyclerViewInstructorsList);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);


        // specify an adapter (see also next example)
        //ingredientList.add("");
        mAdapter = new InstructorDetailListingRVAdapter(getActivity(),InstructorsListFragment.this);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();



        ImageButton addInstructorButton = (ImageButton)getActivity().findViewById(R.id.imageButtonAddInstructor);

        addInstructorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonPressed();
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onAddInstructorButtonPressed();
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
    public void instructorItemLongClicked(String instructorId) {

        final String instructId = instructorId;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Are you sure you want to to delete the instructor").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Instructor instructorR = realm.where(Instructor.class).equalTo("id", instructId).findFirst();
                        RealmResults<Courses> courses = realm.where(Courses.class).equalTo("instructor.id",instructId).findAll();
                        for (Courses course: courses){
                            course.setInstructor(null);
                        }
                        if (instructorR!=null){
                            instructorR.deleteFromRealm();
                        }
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        mAdapter = new InstructorDetailListingRVAdapter(getActivity(),InstructorsListFragment.this);
                        mRecyclerView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create();

        final AlertDialog alertDialogSimpleDialog = builder.create();
        alertDialogSimpleDialog.show();
    }

    @Override
    public void instructorItemClicked(String instructorId) {

        if (mListener!=null){
            mListener.onViewInstructor(instructorId);
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
        void onAddInstructorButtonPressed();
        void onViewInstructor(String instructorId);
    }
}

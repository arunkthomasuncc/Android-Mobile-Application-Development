package com.example.roncherian.inclass08recipes;
//Ron Abraham Cherian - 801028678
//Arun Thomas

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SecondFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SecondFragment extends Fragment implements recipeResultAdapter.ResultsInterface{

    private OnFragmentInteractionListener mListener;

    ArrayList<Recipe> recipeArrayList;
    public SecondFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_second, container, false);

        recipeArrayList=mListener.resultFetch();
        //recipeArrayList = (ArrayList<Recipe>) getArguments().getSerializable("serialiazed");



        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecyclerView mRecyclerView;
        RecyclerView.Adapter mAdapter;
        RecyclerView.LayoutManager mLayoutManager;

        mRecyclerView=(RecyclerView)getActivity().findViewById(R.id.my_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);


        // specify an adapter (see also next example)
        //ingredientList.add("");
        mAdapter = new recipeResultAdapter(recipeArrayList, getActivity(),SecondFragment.this);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();

        Button finishButton = (Button)getActivity().findViewById(R.id.buttonFinish);

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getFragmentManager().getBackStackEntryCount()>0)
                    getFragmentManager().popBackStack();

                //getFragmentManager().popBackStack(getFragmentManager().getBackStackEntryAt(1).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);

            }
        });


    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

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
    public void navigateIntent(String url) {


        Intent i = new Intent(Intent.ACTION_VIEW);
        try {
            i.setData(Uri.parse(url));
            startActivity(i);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public void finishPressed() {

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
        void onFragmentInteraction(Uri uri);
        ArrayList<Recipe> resultFetch();

    }
}

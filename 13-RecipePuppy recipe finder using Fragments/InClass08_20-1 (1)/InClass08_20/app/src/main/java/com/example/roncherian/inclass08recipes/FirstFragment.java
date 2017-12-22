package com.example.roncherian.inclass08recipes;
//Ron Abraham Cherian - 801028678
//Arun Thomas

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FirstFragment extends Fragment {


    ApiCallback callback;

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    ArrayList<String>ingredientList = new ArrayList<String>();

    public FirstFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first, container, false);


        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        /*

        mRecyclerView=(RecyclerView)getActivity().findViewById(R.id.recyclerView);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);


        // specify an adapter (see also next example)
        ingredientList.add("");
        mAdapter = new RecipeAdapter(ingredientList, FirstFragment.this);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();*/


        if (ingredientList.size() > 0){
            mRecyclerView=(RecyclerView)getActivity().findViewById(R.id.recyclerView);
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            mRecyclerView.setHasFixedSize(true);


            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
            mRecyclerView.setLayoutManager(mLayoutManager);

            mAdapter = new RecipeAdapter(ingredientList, FirstFragment.this);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
        final EditText editTextDish = (EditText)getActivity().findViewById(R.id.editTextDish);
        final EditText editTextingredient = (EditText)getActivity().findViewById(R.id.editTextIngredientSearch);
        Button searchButton = (Button)getActivity().findViewById(R.id.buttonSearch);
        final ArrayList<String>list = new ArrayList<String>();

        Button addButton = (Button)getActivity().findViewById(R.id.buttonAdd);



        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ingredientList.isEmpty() || ingredientList == null){
                    mRecyclerView=(RecyclerView)getActivity().findViewById(R.id.recyclerView);
                    // use this setting to improve performance if you know that changes
                    // in content do not change the layout size of the RecyclerView
                    mRecyclerView.setHasFixedSize(true);


                    // use a linear layout manager
                    mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                } else if (ingredientList.size() >= 4){
                    return;
                }

                // specify an adapter (see also next example)
                ingredientList.add(editTextingredient.getText().toString());
                editTextingredient.setText("");
                mAdapter = new RecipeAdapter(ingredientList, FirstFragment.this);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String dish = editTextDish.getText().toString();
                if (dish.isEmpty()){
                    Toast.makeText(getActivity(),"Please Enter a dish",Toast.LENGTH_SHORT).show();
                    return;
                }

                list.add(editTextingredient.getText().toString());

                if (mRecyclerView != null){
                    int noOfChildern = mRecyclerView.getChildCount();
                    for (int i = 0; i< noOfChildern; i++){
                        LinearLayout layout = (LinearLayout) mRecyclerView.getChildAt(i);
                        EditText editText = (EditText) layout.findViewById(R.id.editTextIngredientSearchRV);
                        String ingredientName = editText.getText().toString();
                        if (!ingredientName.equals(""))
                            list.add(editText.getText().toString());
                    }
                }


                callback.callAPI(editTextDish.getText().toString(),list);
            }
        });

    }

    public void onAttach(Context context) {

        super.onAttach(context);

        if (context instanceof ApiCallback) {
            callback = (ApiCallback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnTextChanged");
        }

    }


    public void refresh(ArrayList<String> ingredientList) {


        /*RecyclerView mRecyclerView;
        RecyclerView.Adapter mAdapter;
        RecyclerView.LayoutManager mLayoutManager;*/
        mRecyclerView=(RecyclerView)getActivity().findViewById(R.id.recyclerView);

        // specify an adapter (see also next example)
        ingredientList.add("");
        mAdapter = new RecipeAdapter(ingredientList, FirstFragment.this);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();
    }

    public interface ApiCallback{

        void callAPI(String dish, ArrayList<String>ingredientList);
    }

}

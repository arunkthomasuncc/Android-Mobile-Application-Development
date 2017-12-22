package com.example.roncherian.inclass08recipes;
//Ron Abraham Cherian - 801028678
//Arun Thomas

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by roncherian on 30/10/17.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder>{


    static MyCallback mRefresh;

    static FirstFragment fragment;
    static ArrayList<String> mData;

    public RecipeAdapter(ArrayList<String> mData, FirstFragment callback) {
        if (mData == null){
            this.mData = new ArrayList<String>();
        } else {
            this.mData = mData;
        }

        //this.mRefresh = callback;
        this.fragment=callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_list_rv, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String ingredient=mData.get(position);

        holder.ingredient.setText(ingredient);
        holder.pos = position;
        //holder.emailData=mData.get(position);
        /*holder.email.setText(email.getEmail());
        holder.subject.setText(email.getSubject());
        holder.summary.setText(email.getSummary());*/



    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        EditText ingredient;

        int pos;


        String ingredientName;
        public ViewHolder(View itemView) {
            super(itemView);

            ingredient=(EditText)itemView.findViewById(R.id.editTextIngredientSearchRV);



            Button removeButton = (Button)itemView.findViewById(R.id.buttonRemove);

            ingredient.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (!hasFocus){
                        mData.set(pos,ingredient.getText().toString());
                    }
                }
            });

            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    RecipeAdapter.mData.remove(pos);
                    RecipeAdapter adapter = new RecipeAdapter(RecipeAdapter.mData, fragment);
                    RecyclerView rv = (RecyclerView) fragment.getView().findViewById(R.id.recyclerView);
                    rv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

//rv.findViewHolderForPosition(pos,false)

                    //fragment.refresh(RecipeAdapter.mData);

                }
            });
            /*email=(TextView)itemView.findViewById(R.id.textViewEmail);
            subject=(TextView)itemView.findViewById(R.id.textViewSubject);


            itemView.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.d("Demo","Clicked button"+ emailData.getSubject());
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.d("Dmemo","clicked on"+emailData.getSubject());

                }
            });*/
        }
        // each data item is just a string in this case

    }


    public interface MyCallback{
        void refresh(ArrayList<String> ingredientList);
    }
}
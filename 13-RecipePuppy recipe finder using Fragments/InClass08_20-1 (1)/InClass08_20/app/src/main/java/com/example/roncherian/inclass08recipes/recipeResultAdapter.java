package com.example.roncherian.inclass08recipes;
//Ron Abraham Cherian - 801028678
//Arun Thomas
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by roncherian on 30/10/17.
 */

public class recipeResultAdapter extends RecyclerView.Adapter<recipeResultAdapter.ViewHolder> {



    static ResultsInterface mNavigate;

    ArrayList<Recipe> mData;
    static Context context;
    public recipeResultAdapter(ArrayList<Recipe> mData, Context context, ResultsInterface resultsInterface) {
        this.mData = mData;
        this.context=context;
        this.mNavigate = resultsInterface;
    }
    @Override
    public recipeResultAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(recipeResultAdapter.ViewHolder holder, int position) {

        if (null == mData ||mData.size() == 0){
            holder.isRecipesEmpty = true;
            holder.textViewTitleResult.setVisibility(View.INVISIBLE);
            holder.textViewIngredientResults.setVisibility(View.INVISIBLE);
            holder.textViewURLResult.setVisibility(View.INVISIBLE);
            holder.imageViewRecipe.setVisibility(View.INVISIBLE);
            holder.recipesFound.setVisibility(View.VISIBLE);
            holder.linarLayoutRecipeDetails.setVisibility(View.INVISIBLE);
            holder.linarLayoutRecipeTitle.setVisibility(View.INVISIBLE);
        } else {
            Recipe note=mData.get(position);
            //  holder.noteData=mData.get(position);
            holder.textViewIngredientResults.setText(note.getIngredients()+"");
            holder.textViewTitleResult.setText(note.getTitle()+"");
            holder.textViewURLResult.setText(note.getUrl()+"");
            //holder.imageViewRecipe.setImageBitmap(note.getBitmap());
            Picasso.with(context).load(note.getImageUrl()).into(holder.imageViewRecipe);
            holder.isRecipesEmpty = false;


            holder.textViewTitleResult.setVisibility(View.VISIBLE);
            holder.textViewIngredientResults.setVisibility(View.VISIBLE);
            holder.textViewURLResult.setVisibility(View.VISIBLE);
            holder.imageViewRecipe.setVisibility(View.VISIBLE);
            holder.recipesFound.setVisibility(View.INVISIBLE);
            holder.linarLayoutRecipeDetails.setVisibility(View.VISIBLE);
            holder.linarLayoutRecipeTitle.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        if (null == mData || mData.size() == 0){
            return  1;
        } else {
            return mData.size();
        }

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitleResult;
        ImageView imageViewRecipe;
        TextView textViewIngredientResults;
        TextView textViewURLResult;
        TextView recipesFound;



        LinearLayout linarLayoutRecipeTitle;
        LinearLayout linarLayoutRecipeDetails;

        boolean isRecipesEmpty;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewTitleResult=(TextView)itemView.findViewById(R.id.textViewTitleResult);
            textViewIngredientResults=(TextView)itemView.findViewById(R.id.textViewIngredientResults);
            textViewURLResult=(TextView)itemView.findViewById(R.id.textViewURLResult);
            imageViewRecipe=(ImageView)itemView.findViewById(R.id.imageViewRecipe);
            recipesFound = (TextView)itemView.findViewById(R.id.textViewNoRecipesFound);

            linarLayoutRecipeTitle = (LinearLayout)itemView.findViewById(R.id.linearLayoutForRecipeTitle);
            linarLayoutRecipeDetails = (LinearLayout)itemView.findViewById(R.id.linearLayoutRecipeDetails);


            textViewURLResult.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recipeResultAdapter.mNavigate.navigateIntent(textViewURLResult.getText().toString());
                }
            });


            /*if (isRecipesEmpty){
                textViewTitleResult.setVisibility(View.INVISIBLE);
                textViewIngredientResults.setVisibility(View.INVISIBLE);
                textViewURLResult.setVisibility(View.INVISIBLE);
                imageViewRecipe.setVisibility(View.INVISIBLE);
                recipesFound.setVisibility(View.VISIBLE);

            } else {
                textViewTitleResult.setVisibility(View.VISIBLE);
                textViewIngredientResults.setVisibility(View.VISIBLE);
                textViewURLResult.setVisibility(View.VISIBLE);
                imageViewRecipe.setVisibility(View.VISIBLE);
                recipesFound.setVisibility(View.INVISIBLE);
            }*/


        }

    }

    public interface ResultsInterface {
        void navigateIntent(String url);
        void finishPressed();
    }

}
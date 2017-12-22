package com.example.arun.inclass06;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.arun.inclass06.R.id.imageViewRecipe;

public class RecipeActivity extends AppCompatActivity implements FetchImageAsync.IDataImage {

    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        Bitmap imagebitMap;



        setTitle("Recipes");
        final ArrayList<Recipe> recipes = (ArrayList<Recipe>) getIntent().getSerializableExtra("RecipeList");

        final TextView textViewTitle = (TextView)findViewById(R.id.textViewTitleResult);

        final TextView textViewIngredients = (TextView)findViewById(R.id.textViewIngredientResults);
        final TextView textViewUrl = (TextView)findViewById(R.id.textViewURLResult);
        final ImageView imageViewRecipe = (ImageView)findViewById(R.id.imageViewRecipe);

        if (recipes.size() == 0){
            Toast.makeText(RecipeActivity.this,"No Recipes Found",Toast.LENGTH_LONG).show();
            return;
        }else {
            textViewTitle.setText(recipes.get(0).getTitle());
            textViewIngredients.setText(recipes.get(0).getIngredients());
            textViewUrl.setText(recipes.get(0).getUrl());
            new FetchImageAsync(RecipeActivity.this).execute("https://c1.staticflickr.com/5/4286/35513985750_2690303c8b_z.jpg");

            textViewUrl.setFocusable(false);
            textViewUrl.setClickable(true);
            textViewUrl.setMovementMethod(LinkMovementMethod.getInstance());
            textViewUrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = textViewUrl.getText().toString();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });

            /*if (recipes.get(index).getImageByteArray() != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(recipes.get(0).getImageByteArray(), 0, recipes.get(0).getImageByteArray().length);

                imageViewRecipe.setImageBitmap(bitmap);
            } else {
                imageViewRecipe.setImageBitmap(null);
            }*/

        }

        Button buttonFirst = (Button)findViewById(R.id.buttonFirst);
        Button buttonPrev = (Button)findViewById(R.id.buttonPrev);
        Button buttonNext = (Button)findViewById(R.id.buttonNext);
        Button buttonLast = (Button)findViewById(R.id.buttonLast);

        Button buttonFinish = (Button)findViewById(R.id.buttonFinish);

        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //System.exit(0);
            }
        });

        buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (index == 0){
                    Toast.makeText(RecipeActivity.this,"No more previous recipes",Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    index = 0;
                    textViewTitle.setText(recipes.get(0).getTitle());
                    textViewIngredients.setText(recipes.get(0).getIngredients());
                    textViewUrl.setText(recipes.get(0).getUrl());

                    new FetchImageAsync(RecipeActivity.this).execute("https://c1.staticflickr.com/5/4286/35513985750_2690303c8b_z.jpg");
                    textViewUrl.setFocusable(false);
                    textViewUrl.setClickable(true);
                    textViewUrl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String url = textViewUrl.getText().toString();
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            startActivity(i);
                        }
                    });
                   /* if (recipes.get(index).getImageByteArray() != null) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(recipes.get(0).getImageByteArray(), 0, recipes.get(0).getImageByteArray().length);

                        imageViewRecipe.setImageBitmap(bitmap);
                    } else {
                        imageViewRecipe.setImageBitmap(null);
                    }*/
                }

            }
        });

        buttonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (index == 0){
                    Toast.makeText(RecipeActivity.this,"No more previous recipes",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    index--;
                    textViewTitle.setText(recipes.get(index).getTitle());
                    textViewIngredients.setText(recipes.get(index).getIngredients());
                    textViewUrl.setText(recipes.get(index).getUrl());
                    new FetchImageAsync(RecipeActivity.this).execute("https://c1.staticflickr.com/5/4286/35513985750_2690303c8b_z.jpg");
                    textViewUrl.setFocusable(false);
                    textViewUrl.setClickable(true);
                    textViewUrl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String url = textViewUrl.getText().toString();
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            startActivity(i);
                        }
                    });
                   /* if (recipes.get(index).getImageByteArray() != null) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(recipes.get(index).getImageByteArray(), 0, recipes.get(index).getImageByteArray().length);

                        imageViewRecipe.setImageBitmap(bitmap);
                    } else {
                        imageViewRecipe.setImageBitmap(null);
                    }*/
                }

            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (index  == recipes.size()-1){
                    Toast.makeText(RecipeActivity.this,"No more recipes",Toast.LENGTH_SHORT).show();
                    return;
                } else{
                    index++;
                    textViewTitle.setText(recipes.get(index).getTitle());
                    textViewIngredients.setText(recipes.get(index).getIngredients());
                    imageViewRecipe.setImageBitmap(null);
                    new FetchImageAsync(RecipeActivity.this).execute("https://c1.staticflickr.com/5/4286/35513985750_2690303c8b_z.jpg");

                    textViewUrl.setText(recipes.get(index).getUrl());
                    textViewUrl.setFocusable(false);
                    textViewUrl.setClickable(true);

                    textViewUrl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String url = textViewUrl.getText().toString();
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            startActivity(i);
                        }
                    });
                   /* if (recipes.get(index).getImageByteArray() != null){
                        Bitmap bitmap = BitmapFactory.decodeByteArray(recipes.get(index).getImageByteArray(),0, recipes.get(index).getImageByteArray().length);
                        imageViewRecipe.setImageBitmap(bitmap);
                    } else {
                        imageViewRecipe.setImageBitmap(null);
                    }*/


                }

            }
        });

        buttonLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (index == recipes.size()-1){
                    Toast.makeText(RecipeActivity.this,"No more recipes",Toast.LENGTH_SHORT).show();
                    return;
                }
                index = recipes.size()-1;
                textViewTitle.setText(recipes.get(index).getTitle());
                new FetchImageAsync(RecipeActivity.this).execute("https://c1.staticflickr.com/5/4286/35513985750_2690303c8b_z.jpg");
                textViewIngredients.setText(recipes.get(index).getIngredients());
                textViewUrl.setText(recipes.get(index).getUrl());
                textViewUrl.setFocusable(false);
                textViewUrl.setClickable(true);
                textViewUrl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String url = textViewUrl.getText().toString();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });
               /* if (recipes.get(index).getImageByteArray() != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(recipes.get(index).getImageByteArray(), 0, recipes.get(index).getImageByteArray().length);

                    imageViewRecipe.setImageBitmap(bitmap);
                } else {
                    imageViewRecipe.setImageBitmap(null);
                }*/

            }
        });


    }

    @Override
    public void setUpImage(Bitmap upImage) {
        ImageView imageView= (ImageView)findViewById(R.id.imageViewRecipe);
        imageView.setImageBitmap(upImage);
    }
}


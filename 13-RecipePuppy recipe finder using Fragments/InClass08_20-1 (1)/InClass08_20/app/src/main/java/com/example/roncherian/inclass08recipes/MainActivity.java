package com.example.roncherian.inclass08recipes;
//Ron Abraham Cherian - 801028678
//Arun Thomas
import android.app.Fragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FirstFragment.ApiCallback, SecondFragment.OnFragmentInteractionListener, ProgressFragment.OnFragmentInteractionListener{

    static String APP_LIST_FRAGMENT = "app_list_fragment";
    ArrayList<Recipe> results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Recipe Puppy");
        getFragmentManager().beginTransaction().add(R.id.containerView, new FirstFragment(), APP_LIST_FRAGMENT).commit();



    }

    @Override
    public void callAPI(String dish, ArrayList<String> ingredientList) {

        ArrayList<String>lists = new ArrayList<String>();
        lists.add(dish);
        lists.addAll(ingredientList);
        new GetDataAsyncTask(MainActivity.this).execute(lists);
        setTitle("Recipes");
        getFragmentManager().beginTransaction().replace(R.id.containerView,new ProgressFragment(),"progressFragment").addToBackStack(null).commit();
    }

    public void getRecipesList(ArrayList<Recipe>recipes){

        //Log.d("demo",recipes.toString());
        results=recipes;

        if (results == null || results.size() == 0){

            Toast.makeText(MainActivity.this,"No Recipes Found",Toast.LENGTH_SHORT).show();

            Fragment fragment = getFragmentManager().findFragmentByTag("progressFragment");
            getFragmentManager().beginTransaction().remove(fragment).commit();
            if (getFragmentManager().getBackStackEntryCount()>0)
                getFragmentManager().popBackStack();
            //getFragmentManager().popBackStack(getFragmentManager().getBackStackEntryAt(1).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            return;
        }
        SecondFragment secondFragment = new SecondFragment();
        //Bundle bundle = new Bundle();
        //bundle.putSerializable("serialiazed",recipes);

        //secondFragment.setArguments(bundle);
        setTitle("Recipes");

        Fragment fragment = getFragmentManager().findFragmentByTag("progressFragment");
        getFragmentManager().beginTransaction().remove(fragment).commit();

        if(getFragmentManager().getBackStackEntryCount() > 0){
            getFragmentManager().popBackStack();
        }

        setTitle("Recipes");

        getFragmentManager().beginTransaction().replace(R.id.containerView,new SecondFragment(),"editResult").addToBackStack(null).commit();

    }


    public ArrayList<Recipe> getresults()
    {
        return results;
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public ArrayList<Recipe> resultFetch() {
        return getresults();
    }

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() > 1){
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public void popOutProgressBar() {
        if(getFragmentManager().getBackStackEntryCount() > 0){
            getFragmentManager().popBackStack();
        }
    }
}

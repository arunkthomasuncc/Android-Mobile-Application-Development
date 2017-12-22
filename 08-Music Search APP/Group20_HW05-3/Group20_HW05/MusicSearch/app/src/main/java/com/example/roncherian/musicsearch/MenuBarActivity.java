package com.example.roncherian.musicsearch;
//Ron Abraham Cherian - 801028678
//Arun Thomas Kunnumpuram
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MenuBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_bar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bar_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.homeItem:
                /*Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);*/
                if (!this.getClass().equals(MainActivity.class)){
                    //setResult(Activity.RESULT_OK);
                    //finish();
                    Intent intent = new Intent(this, MainActivity.class);
                    //intent.putExtra(SearchResultsActivity.TRACK_DETAILS_ACTIVITY_INTENT,musics.get(i));
                    startActivity(intent);
                }

                return true;

            case R.id.quitItem:
                //finish();
                //android.os.Process.killProcess(android.os.Process.myPid());
                //System.exit(1);
              /*  Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory( Intent.CATEGORY_HOME );
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);*/

                Intent intentExit = new Intent(getApplicationContext(), MainActivity.class);
                intentExit.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentExit.putExtra("EXIT", true);
                startActivity(intentExit);
                //return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

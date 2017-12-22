package com.example.roncherian.wordsearch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by roncherian on 24/09/17.
 */

public class ListItemUI extends LinearLayout {

    public EditText word;
    public Button addOrRemoveButton;


    public ListItemUI(Context context) {
        super(context);
        inflateXML(context);
    }

    private void inflateXML(Context context){
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.search_word_add_layout, null);
        this.word = (EditText) findViewById(R.id.editTextSearch);
        this.addOrRemoveButton = (Button) findViewById(R.id.buttonAdd);
    }
}

package com.example.roncherian.homework06;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

/**
 * Created by roncherian on 05/11/17.
 */

public abstract class MyEditTextValidator implements TextWatcher {

    private final TextView textView;

    public MyEditTextValidator(TextView textView) {
        this.textView = textView;
    }

    public abstract void validate(TextView textView, String text);

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

        String text = textView.getText().toString();
        validate(textView, text);
    }
}

package com.farm.widget;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

/**
 * Created by ${hmj} on 2016/5/19.
 */
public class CustomTextWatcher implements TextWatcher
{
    TextView textView;

    public void CustomTextWatcher(TextView textView)
    {
        this.textView = textView;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {

    }

    @Override
    public void afterTextChanged(Editable s)
    {
        textView.setText(s.toString());
    }
}

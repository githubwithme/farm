package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;

import com.farm.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

/**
 * Created by ${hmj} on 2016/1/11.
 */
@EActivity(R.layout.addevent)
public class AddEvent extends Activity
{

    @AfterViews
    void afterOncreate()
    {
    }

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();

    }

    ;


}

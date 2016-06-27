package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;

import com.farm.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

/**
 * Created by ${hmj} on 2016/6/27.
 */
@EActivity(R.layout.recoverydetail)
public class RecoveryDetail extends Activity
{
    @AfterViews
    void afterOncreate()
    {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
    }
}

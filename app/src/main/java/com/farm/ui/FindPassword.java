package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;

import com.farm.R;

import org.androidannotations.annotations.EActivity;

/**
 * Created by ${hmj} on 2015/12/14.
 */
@EActivity(R.layout.findpassword)
public class FindPassword extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
    }
}

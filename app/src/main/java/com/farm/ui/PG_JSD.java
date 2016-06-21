package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;

import com.farm.R;

import org.androidannotations.annotations.EActivity;

/**
 * Created by hasee on 2016/6/21.
 */
@EActivity(R.layout.pg_jsd)
public class PG_JSD extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
    }
}

package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;

import com.farm.R;

import org.androidannotations.annotations.EActivity;

/**
 * Created by user on 2016/4/12.
 */
@EActivity(R.layout.ncz_eventhandledetail)
public class NCZ_EventHandleDetail extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
    }
}

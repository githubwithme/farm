package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.farm.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by hasee on 2016/6/14.
 */
@EActivity(R.layout.pg_workplan)
public class PG_Workplan extends Activity
{

    @ViewById
    LinearLayout ll_bz;
    @ViewById
    EditText et_fbz;
    @ViewById
    RadioButton rb_bz;
    @ViewById
    RadioButton rb_fbz;
    @ViewById
    TextView tv_bz;
    @Click
    void rb_bz()
    {
        ll_bz.setVisibility(View.VISIBLE);
        et_fbz.setVisibility(View.GONE);
    }
    @Click
    void rb_fbz()
    {
        ll_bz.setVisibility(View.GONE);
        et_fbz.setVisibility(View.VISIBLE);
    }
    @Click
    void tv_bz()
    {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
}

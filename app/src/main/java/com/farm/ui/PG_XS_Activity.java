package com.farm.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.farm.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by hasee on 2016/7/1.
 */
@EActivity(R.layout.pg_xs_activity)
public class PG_XS_Activity extends Activity
{


    @ViewById
    Button btn_createorders;
    @ViewById
    Button btn_orders;

    @Click
    void btn_createorders()
    {
        Intent intent=new Intent(this,PG_CreateOrder_.class);
        startActivity(intent);
    }
    @Click
    void btn_orders()
    {
        Intent intent=new Intent(this,PG_OrderManager_.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
    }
}

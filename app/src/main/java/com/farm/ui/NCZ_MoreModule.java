package com.farm.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.farm.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by ${hmj} on 2016/5/25.
 */
@EActivity(R.layout.ncz_moremodule)
public class NCZ_MoreModule extends Activity
{
    @ViewById
    RelativeLayout rl_planmap;
    @ViewById
    RelativeLayout rl_salemap;


    @Click
    void rl_planmap()
    {
        Intent intent = new Intent(NCZ_MoreModule.this, MakeLayer_Farm_.class);
        startActivity(intent);
    }

    @Click
    void rl_salemap()
    {
        Intent intent = new Intent(NCZ_MoreModule.this, NCZ_SaleMap_.class);
        startActivity(intent);
    }


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

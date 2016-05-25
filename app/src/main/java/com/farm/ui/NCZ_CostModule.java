package com.farm.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.farm.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by ${hmj} on 2016/5/25.
 */
@EActivity(R.layout.ncz_costmodule)
public class NCZ_CostModule extends Activity
{
    @ViewById
    LinearLayout ll_specialcost;
    @ViewById
    LinearLayout ll_largecost;
    @ViewById
    LinearLayout ll_smallcost;


    @Click
    void ll_specialcost()
    {
        Intent intent = new Intent(NCZ_CostModule.this, SpecialCostFragment_.class);
        startActivity(intent);
    }

    @Click
    void ll_largecost()
    {
        Intent intent = new Intent(NCZ_CostModule.this, LargeCostFragment_.class);
        startActivity(intent);
    }

    @Click
    void ll_smallcost()
    {
        Intent intent = new Intent(NCZ_CostModule.this, SmallCostFragment_.class);
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

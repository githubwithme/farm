package com.farm.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.farm.R;
import com.farm.chart.FinanceAnalysis_;
import com.farm.chart.GoodsAnalysisActivity_;
import com.farm.chart.SaleAnalysis_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by ${hmj} on 2016/5/27.
 */
@EActivity(R.layout.ncz_analysismodule)
public class NCZ_AnalysisModule extends Activity
{
    @ViewById
    RelativeLayout rl_kc;

    @Click
    void rl_kc()
    {
        Intent intent = new Intent(NCZ_AnalysisModule.this, GoodsAnalysisActivity_.class);
        startActivity(intent);
    }

    @Click
    void rl_xs()
    {
        Intent intent = new Intent(NCZ_AnalysisModule.this, SaleAnalysis_.class);
        startActivity(intent);
    }

    @Click
    void rl_cw()
    {
        Intent intent = new Intent(NCZ_AnalysisModule.this, FinanceAnalysis_.class);
        startActivity(intent);
    }

    @Click
    void rl_sj()
    {
        Intent intent = new Intent(NCZ_AnalysisModule.this, FinanceAnalysis_.class);
        startActivity(intent);
    }

    @Click
    void rl_gz()
    {
        Intent intent = new Intent(NCZ_AnalysisModule.this, FinanceAnalysis_.class);
        startActivity(intent);
    }

    @Click
    void rl_mq()
    {
        Intent intent = new Intent(NCZ_AnalysisModule.this, FinanceAnalysis_.class);
        startActivity(intent);
    }

    @Click
    void btn_back()
    {
        finish();
    }

    @AfterViews
    void oncreateview()
    {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
    }
}

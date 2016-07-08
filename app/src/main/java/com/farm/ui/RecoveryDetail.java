package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.SellOrder_New;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.w3c.dom.Text;

/**
 * Created by ${hmj} on 2016/6/27.
 */
@EActivity(R.layout.recoverydetail)
public class RecoveryDetail extends Activity
{
    @ViewById
    TextView bz_fzr;
    @ViewById
    TextView by_fzr;
    @ViewById
    TextView cl;


    SellOrder_New sellOrder_new = new SellOrder_New();

    @AfterViews
    void afterOncreate()
    {
        showdata();
    }

    private void showdata()
    {
        if (!sellOrder_new.getContractorId().equals(""))
        {
            bz_fzr.setText("包装工：已落实" + ";" + sellOrder_new.getContractorName());
        } else
        {
            bz_fzr.setText("包装工：未落实" + ";");
        }
        if (!sellOrder_new.getPickId().equals(""))
        {
            by_fzr.setText("搬运工：已落实" + ";" + sellOrder_new.getPickName());
        } else
        {
            by_fzr.setText("搬运工：未落实" + ";");

        }
        cl.setText("车辆："+sellOrder_new.getPlateNumber());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        sellOrder_new = getIntent().getParcelableExtra("zbstudio");
    }
}

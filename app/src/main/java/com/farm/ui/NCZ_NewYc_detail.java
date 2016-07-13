package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.WZ_YCxx;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by hasee on 2016/7/13.
 */

@EActivity(R.layout.ncz_newyc_detail)
public class NCZ_NewYc_detail extends Activity
{
    String parknames;
    WZ_YCxx wz_yCxx = new WZ_YCxx();


    @ViewById
    TextView goodsname;
    @ViewById
    TextView parkName;
    @ViewById
    TextView storehouseName;
    @ViewById
    TextView type;
    @ViewById
    TextView batchName;
    @ViewById
    TextView expDate;
    @ViewById
    TextView quantity;
    @ViewById
    TextView allzhongliang;

    @ViewById
    LinearLayout llview1;
    @ViewById
    View ltview1;
    @ViewById
    LinearLayout llview3;
    @ViewById
    View ltview2;
    @ViewById
    LinearLayout ll_gone1;
    @ViewById
    View view_1;

    @AfterViews
    void afterview()
    {
        shouData();
    }

    private void shouData()
    {
        goodsname.setText(wz_yCxx.getGoodsName());
        parkName.setText(parknames);
        storehouseName.setText(wz_yCxx.getStorehouseName());

        if (wz_yCxx.getType().equals("0"))
        {
            type.setText("即将过期");
            ll_gone1.setVisibility(View.GONE);
            view_1.setVisibility(View.GONE);
        } else if (wz_yCxx.getType().equals("1"))
        {
            llview1.setVisibility(View.GONE);
            ltview1.setVisibility(View.GONE);
            llview3.setVisibility(View.GONE);
            ltview2.setVisibility(View.GONE);
            type.setText("库存过低");
        } else if (wz_yCxx.getType().equals("2"))
        {
            type.setText("即将过期|库存过低");
        } else if (wz_yCxx.getType().equals("3"))
        {
            ll_gone1.setVisibility(View.GONE);
            view_1.setVisibility(View.GONE);
            type.setText("已经过期");
        }
        batchName.setText(wz_yCxx.getBatchName());
        if (!wz_yCxx.getExpDate1().equals(""))
        {
            expDate.setText(wz_yCxx.getExpDate1().substring(0, wz_yCxx.getExpDate1().length() - 8));
        }

        String danwei;
        if (!wz_yCxx.getThree().equals(""))
        {
            quantity.setText(wz_yCxx.getThreeNum() + wz_yCxx.getThree());
            danwei=wz_yCxx.getThree();
        } else if (wz_yCxx.getThree().equals("") && !wz_yCxx.getSec().equals(""))
        {
            quantity.setText(wz_yCxx.getSecNum() + wz_yCxx.getSec());
            danwei=wz_yCxx.getSec();
        } else
        {
            quantity.setText(wz_yCxx.getFirsNum() + wz_yCxx.getFirs());
            danwei=wz_yCxx.getFirs();
        }


        allzhongliang.setText(wz_yCxx.getLevelOfWarning()+danwei);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        parknames = getIntent().getStringExtra("parkname");
        wz_yCxx = getIntent().getParcelableExtra("wz_rKxx");
    }
}

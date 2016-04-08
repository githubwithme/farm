package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.WZ_YCxx;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by user on 2016/4/8.
 */
@EActivity(R.layout.ncz_wz_ycdetail)
public class NCZ_WZ_YCDetail extends Activity{
    WZ_YCxx wz_yCxx;


    @ViewById
    LinearLayout llview1;
    @ViewById
    View ltview1;
    @ViewById
    LinearLayout llview2;
    @ViewById
    View ltview2;
    @ViewById
    LinearLayout llview3;
    @ViewById
    View ltview3;
    @ViewById
    TextView goodsname;
    @ViewById
    TextView parkName;
    @ViewById
    TextView storehouseName;
    @ViewById
    TextView nowDate;
    @ViewById
    TextView type;
    @ViewById
    TextView batchName;
    @ViewById
    TextView expDate;
    @ViewById
    TextView expQuantity;
    @ViewById
    TextView quantity;
    @ViewById
    ImageButton imgbtn_back;

    @Click
    void imgbtn_back()
    {
        finish();
    }

    @AfterViews
    void afteroncreate()
    {
        goodsname.setText(wz_yCxx.getGoodsName());
        parkName.setText(wz_yCxx.getParkName());
        storehouseName.setText(wz_yCxx.getStorehouseName());
        nowDate.setText(wz_yCxx.getNowDate());

        if (wz_yCxx.getType().equals("0"))
        {
            type.setText("将要过期");
//            type.setText(wz_yCxx.getGoodsName()+"将要过期");
            batchName.setText(wz_yCxx.getBatchName());
            expDate.setText(wz_yCxx.getExpDate()+"天");
            expQuantity.setText(wz_yCxx.getExpQuantity());
            quantity.setText(wz_yCxx.getQuantity());
        }else if (wz_yCxx.getType().equals("2"))
        {
            type.setText("将要过期|库存过低");
//            type.setText(wz_yCxx.getGoodsName()+"将要过期|库存过低");
            batchName.setText(wz_yCxx.getBatchName());
            expDate.setText(wz_yCxx.getExpDate()+"天");
            expQuantity.setText(wz_yCxx.getExpQuantity());
            quantity.setText(wz_yCxx.getQuantity());
        }else
        {
            llview1.setVisibility(View.GONE);
            llview2.setVisibility(View.GONE);
            llview3.setVisibility(View.GONE);
            ltview1.setVisibility(View.GONE);
            ltview2.setVisibility(View.GONE);
            ltview3.setVisibility(View.GONE);
            type.setText("库存过低");
//            type.setText(wz_yCxx.getGoodsName()+"库存过低");
            quantity.setText(wz_yCxx.getQuantity());
        }




    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        wz_yCxx=getIntent().getParcelableExtra("wz_yCxx");
    }
}

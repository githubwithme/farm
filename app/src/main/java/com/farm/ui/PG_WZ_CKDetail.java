package com.farm.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.WZ_RKxx;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by user on 2016/5/21.
 */
@EActivity(R.layout.pg_wz_ckdetail)
public class PG_WZ_CKDetail extends FragmentActivity
{

    private WZ_RKxx wz_rKxx;
    private String inType;
    private String indate;
    @ViewById
    RelativeLayout ll_add;

    @ViewById
    TextView goodsname;
    @ViewById
    TextView parkName;
    @ViewById
    TextView storehouseName;
    @ViewById
    TextView batchName;
    @ViewById
    TextView inDate;
    @ViewById
    TextView outType;
    @ViewById
    EditText quantity;
    @ViewById
    TextView outGoodsvalue;
    @ViewById
    TextView note;
    @ViewById
    ImageButton imgbtn_back;

    @Click
    void imgbtn_back() {
        finish();
    }

    @AfterViews
    void afteroncreate() {
        quantity.setFocusable(false);
        goodsname.setText(wz_rKxx.getGoodsname());
        parkName.setText(wz_rKxx.getParkName());
        storehouseName.setText(wz_rKxx.getStorehouseName());
        outType.setText(inType);

        String indates = indate.substring(0, indate.length() - 8);
        inDate.setText(indate);
        batchName.setText(wz_rKxx.getBatchName());
        quantity.setText(wz_rKxx.getQuantity());
        outGoodsvalue.setText(wz_rKxx.getOutGoodsvalue() + "元");
        if (wz_rKxx.getNote().equals("")) {
            note.setText("无");
        } else {
            note.setText(wz_rKxx.getNote());
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        wz_rKxx = getIntent().getParcelableExtra("wz_rKxx");
        inType = getIntent().getStringExtra("inType");
        indate = getIntent().getStringExtra("indate");
    }

}

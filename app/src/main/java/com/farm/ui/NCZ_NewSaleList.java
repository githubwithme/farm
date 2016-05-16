package com.farm.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Button;
import android.widget.ListView;

import com.farm.R;
import com.farm.adapter.Adapter_NewSaleList;
import com.farm.bean.SellOrderDetail_New;
import com.farm.common.utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${hmj} on 2016/5/15.
 */
@EActivity(R.layout.ncz_newsalelist)
public class NCZ_NewSaleList extends Activity
{
    List<SellOrderDetail_New> list_SellOrderDetail;
    @ViewById
    Button btn_createorder;    @ViewById
ListView lv;

    @Click
    void btn_createorder()
    {
        Intent intent = new Intent(NCZ_NewSaleList.this, CreateOrder_.class);
        intent.putExtra("batchtime","2016-05-11");
        intent.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) list_SellOrderDetail);
        startActivity(intent);
    }
    @AfterViews
    void afterOncreate()
    {
        Adapter_NewSaleList adapter_sellOrderDetail = new Adapter_NewSaleList(this, list_SellOrderDetail);
        lv.setAdapter(adapter_sellOrderDetail);
        utils.setListViewHeight(lv);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        list_SellOrderDetail = getIntent().getParcelableArrayListExtra("list");
    }
}

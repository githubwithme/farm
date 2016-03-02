package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.farm.R;
import com.farm.bean.sellOrderDetailTab;
import com.farm.bean.sellOrderTab;
import com.farm.common.SqliteDb;
import com.farm.common.utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import java.util.List;


/**
 * 1、填写订单信息：订单信息、销售明细
 * 2、确定订单后更新产品批次表（productbatch）
 * 3、刷新产品页面
 */
@EActivity(R.layout.addorder)
public class AddOrder extends Activity
{

    //	@ViewById
//	FrameLayout fl_new;
//
//
    @Click
    void btn_sure()
    {
        createOrder();
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

    public void createOrder()
    {
        String orderid=java.util.UUID.randomUUID().toString();
        sellOrderTab sellOrderTab=new sellOrderTab();
        sellOrderTab.setid(orderid);
        sellOrderTab.setDataofOrder(utils.getTime());
        sellOrderTab.setSellAmount("30000");
        sellOrderTab.setSellType("主营销售");
        sellOrderTab.setSN("第一批次");
        sellOrderTab.setStatus("销售中");
        List<sellOrderDetailTab> list_sellOrderDetailTab=SqliteDb.getAllsellOrderDetailTab(AddOrder.this,sellOrderDetailTab.class);
        for (int i = 0; i <list_sellOrderDetailTab.size(); i++)
        {
            list_sellOrderDetailTab.get(i).setOrderId(orderid);
            SqliteDb.updatesellOrderDetailTab(AddOrder.this, list_sellOrderDetailTab.get(i));
        }
        finish();
        Toast.makeText(AddOrder.this,"订单创建成功！",Toast.LENGTH_SHORT).show();

    }


}

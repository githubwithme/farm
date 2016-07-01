package com.farm.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.Adapter_FarmSaleData;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.SellOrderDetail_New;
import com.farm.bean.commembertab;
import com.farm.bean.parktab;
import com.farm.common.utils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2016/4/25.
 */
@EActivity(R.layout.ncz_farmsaledata)
public class NCZ_FarmSaleData extends Activity
{
    String parkid;
    String batchTime;
    String parkname;
    List<Map<String, String>> uuids;
    List<SellOrderDetail_New> list_sell;
    Adapter_FarmSaleData adapter_farmSaleData;
    @ViewById
    ExpandableListView expandableListView;
    @ViewById
    TextView tv_note;

    @Click
    void btn_back()
    {
        finish();
    }
    @Click
    void btn_createorders()
    {
        Intent intent = new Intent(NCZ_FarmSaleData.this, NCZ_CreateNewOrder_.class);
        startActivity(intent);
    }


    @Click
    void btn_orders()
    {
        Intent intent = new Intent(NCZ_FarmSaleData.this, NCZ_OrderManager_.class);
        startActivity(intent);
    }

    @Click
    void btn_customer()
    {
//        Intent intent = new Intent(NCZ_FarmSaleData.this, NCZ_OrderManager_.class);
//        startActivity(intent);
    }


    @AfterViews
    void afterOncreate()
    {
        parkid = getIntent().getStringExtra("parkid");
        parkname = getIntent().getStringExtra("parkname");
        batchTime = getIntent().getStringExtra("batchTime");
        IntentFilter intentfilter_update = new IntentFilter(AppContext.BROADCAST_FINISH);
        registerReceiver(receiver_update, intentfilter_update);
        tv_note.setText("各分场销售情况");
        getSaleDataOfArea();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();

    }

    BroadcastReceiver receiver_update = new BroadcastReceiver()// 从扩展页面返回信息
    {
        @SuppressWarnings("deprecation")
        @Override
        public void onReceive(Context context, Intent intent)
        {
            finish();
        }
    };


    private void getSaleDataOfArea()
    {
        commembertab commembertab = AppContext.getUserInfo(NCZ_FarmSaleData.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("year", utils.getYear());
        params.addQueryStringParameter("action", "getBatchTimeByUid");//jobGetList1
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<parktab> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), parktab.class);
                        adapter_farmSaleData = new Adapter_FarmSaleData(NCZ_FarmSaleData.this, listNewData, expandableListView);
                        expandableListView.setAdapter(adapter_farmSaleData);
                        utils.setListViewHeight(expandableListView);
//                        for (int i = 0; i < listNewData.size(); i++)
//                        {
//                            expandableListView.expandGroup(i);//展开
//                        }

                    } else
                    {
                        listNewData = new ArrayList<parktab>();
                    }

                } else
                {
                    AppContext.makeToast(NCZ_FarmSaleData.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_FarmSaleData.this, "error_connectServer");
            }
        });
    }

}

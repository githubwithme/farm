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
import com.farm.adapter.NCZ_AreaSaleData_Adapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.SellOrderDetail_New;
import com.farm.bean.areatab;
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
@EActivity(R.layout.ncz_areasaledata)
public class NCZ_AreaSaleData extends Activity
{
    String parkid;
    String batchTime;
    String parkname;
    List<Map<String, String>> uuids;
    List<SellOrderDetail_New> list_sell;
    NCZ_AreaSaleData_Adapter ncz_areaSaleData_adapter;
    @ViewById
    ExpandableListView expandableListView;
    @ViewById
    TextView tv_note;

    @Click
    void btn_back()
    {
        finish();
    }


    @AfterViews
    void afterOncreate()
    {
        parkid = getIntent().getStringExtra("parkid");
        parkname = getIntent().getStringExtra("parkname");
        batchTime = getIntent().getStringExtra("batchTime");
        IntentFilter intentfilter_update = new IntentFilter(AppContext.BROADCAST_FINISH);
        registerReceiver(receiver_update, intentfilter_update);
        tv_note.setText(parkname + "***" + batchTime + "批次***" + "销售情况");
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
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("parkid", parkid);
        params.addQueryStringParameter("year", utils.getYear());
        params.addQueryStringParameter("batchTime", batchTime);
        params.addQueryStringParameter("action", "getSaleDataOfArea");//jobGetList1
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<areatab> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), areatab.class);
                        ncz_areaSaleData_adapter = new NCZ_AreaSaleData_Adapter(NCZ_AreaSaleData.this, listNewData, expandableListView);
                        expandableListView.setAdapter(ncz_areaSaleData_adapter);
                        utils.setListViewHeight(expandableListView);
//                        for (int i = 0; i < listNewData.size(); i++)
//                        {
//                            expandableListView.expandGroup(i);//展开
//                        }

                    } else
                    {
                        listNewData = new ArrayList<areatab>();
                    }

                } else
                {
                    AppContext.makeToast(NCZ_AreaSaleData.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_AreaSaleData.this, "error_connectServer");
            }
        });
    }

}

package com.farm.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ExpandableListView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.Adapter_AreaSale_CZ;
import com.farm.adapter.Adapter_BatchTimeSale_CZ;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.BatchTime;
import com.farm.bean.Result;
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

/**
 * Created by ${hmj} on 2016/5/26.
 */
@EActivity(R.layout.cz_saleactivity)
public class CZ_SaleActivity extends Activity
{
    Adapter_AreaSale_CZ adapter_areaSale_cz;
    Adapter_BatchTimeSale_CZ adapter_batchTimeSale_cz;
    @ViewById
    ExpandableListView expandableListView_areasaledata;
    @ViewById
    ExpandableListView expandableListView_batchtimesaledata;


    @Click
    void btn_createorders()
    {
        Intent intent = new Intent(CZ_SaleActivity.this, NCZ_CreateNewOrder_.class);
        startActivity(intent);
    }


    @Click
    void btn_orders()
    {
        Intent intent = new Intent(CZ_SaleActivity.this, NCZ_OrderManager_.class);
        startActivity(intent);
    }

    @Click
    void btn_customer()
    {
        Intent intent = new Intent(CZ_SaleActivity.this, NCZ_CustomerContract_.class);
        startActivity(intent);
    }


    @AfterViews
    void afterOncreate()
    {
        getActionBar().hide();
        getAreaSaleData();
        getBatchTimeByUid();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }


    private void getBatchTimeByUid()
    {
        commembertab commembertab = AppContext.getUserInfo(CZ_SaleActivity.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("parkid", commembertab.getuId());
        params.addQueryStringParameter("year", utils.getYear());
        params.addQueryStringParameter("action", "CZ_getBatchTimeContractData");//jobGetList1
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<BatchTime> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), BatchTime.class);
                        adapter_batchTimeSale_cz = new Adapter_BatchTimeSale_CZ(CZ_SaleActivity.this, listNewData, expandableListView_batchtimesaledata);
                        expandableListView_batchtimesaledata.setAdapter(adapter_batchTimeSale_cz);
                        utils.setListViewHeight(expandableListView_batchtimesaledata);
//                        for (int i = 0; i < listNewData.size(); i++)
//                        {
//                            expandableListView.expandGroup(i);//展开
//                        }

                    } else
                    {
                        listNewData = new ArrayList<BatchTime>();
                    }

                } else
                {
                    AppContext.makeToast(CZ_SaleActivity.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(CZ_SaleActivity.this, "error_connectServer");
            }
        });
    }

    private void getAreaSaleData()
    {
        commembertab commembertab = AppContext.getUserInfo(CZ_SaleActivity.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("year", utils.getYear());
        params.addQueryStringParameter("action", "getAreaSaleData");//jobGetList1
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
                        adapter_areaSale_cz = new Adapter_AreaSale_CZ(CZ_SaleActivity.this, listNewData, expandableListView_areasaledata);
                        expandableListView_areasaledata.setAdapter(adapter_areaSale_cz);
                        utils.setListViewHeight(expandableListView_areasaledata);

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
                    AppContext.makeToast(CZ_SaleActivity.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(CZ_SaleActivity.this, "error_connectServer");
            }
        });
    }
}

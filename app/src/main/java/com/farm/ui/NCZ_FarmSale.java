package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.NCZ_FarmSale_Adapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.bean.parktab;
import com.farm.common.FileHelper;
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
 * Created by user on 2016/4/25.
 */
@EActivity(R.layout.ncz_farmsale)
public class NCZ_FarmSale extends Activity
{
    NCZ_FarmSale_Adapter ncz_farmSale_adapter;
    @ViewById
    ExpandableListView expandableListView;

    @Click
    void btn_shoppingcart()
    {
        //                    Intent intent = new Intent(context, NCZ_Todayjob_Common_.class);
//                    intent.putExtra("bean", batchTime);//
//                    context.startActivity(intent);
    }

    @AfterViews
    void afterOncreate()
    {
        getBatchTimeByUid_test();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
    }

    private void getBatchTimeByUid_test()
    {
        List<parktab> listNewData = FileHelper.getAssetsData(NCZ_FarmSale.this, "getBatchTimeByUid", parktab.class);
        ncz_farmSale_adapter = new NCZ_FarmSale_Adapter(NCZ_FarmSale.this, listNewData, expandableListView);
        expandableListView.setAdapter(ncz_farmSale_adapter);
        utils.setListViewHeight(expandableListView);
        for (int i = 0; i < listNewData.size(); i++)
        {
            expandableListView.expandGroup(i);//展开
        }
    }

    private void getBatchTimeByUid()
    {
        commembertab commembertab = AppContext.getUserInfo(NCZ_FarmSale.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
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
                        ncz_farmSale_adapter = new NCZ_FarmSale_Adapter(NCZ_FarmSale.this, listNewData, expandableListView);
                        expandableListView.setAdapter(ncz_farmSale_adapter);
                        utils.setListViewHeight(expandableListView);
                        for (int i = 0; i < listNewData.size(); i++)
                        {
                            expandableListView.expandGroup(i);//展开
//                                  expandableListView.collapseGroup(i);//关闭
                        }

                    } else
                    {
                        listNewData = new ArrayList<parktab>();
                    }

                } else
                {
                    AppContext.makeToast(NCZ_FarmSale.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_FarmSale.this, "error_connectServer");

            }
        });
    }


}

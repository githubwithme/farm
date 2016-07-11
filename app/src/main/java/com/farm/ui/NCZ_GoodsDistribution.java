package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.Adapter_Goods_NCZ;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.ParkGoodsBean;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.common.FileHelper;
import com.farm.common.utils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${hmj} on 2016/7/11.
 */
@EActivity(R.layout.ncz_goodsdistribution)
public class NCZ_GoodsDistribution extends Activity
{

    @ViewById
    ExpandableListView expandableListView;
    Adapter_Goods_NCZ adapter_goods_ncz;


    @AfterViews
    void afterOncreate()
    {
//        getGoods();
        getNewSaleList_test();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }


    private void getNewSaleList_test()
    {
        List<ParkGoodsBean> listNewData = FileHelper.getAssetsData(NCZ_GoodsDistribution.this, "getgoods", ParkGoodsBean.class);
        if (listNewData != null)
        {
            adapter_goods_ncz = new Adapter_Goods_NCZ(NCZ_GoodsDistribution.this, listNewData, expandableListView);
            expandableListView.setAdapter(adapter_goods_ncz);
            utils.setListViewHeight(expandableListView);
        }
        for (int i = 0; i < listNewData.size(); i++)
        {
            expandableListView.expandGroup(i);//展开
        }

    }

    private void getGoods()
    {
        commembertab commembertab = AppContext.getUserInfo(NCZ_GoodsDistribution.this);
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
                List<ParkGoodsBean> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), ParkGoodsBean.class);
                        adapter_goods_ncz = new Adapter_Goods_NCZ(NCZ_GoodsDistribution.this, listNewData, expandableListView);
                        expandableListView.setAdapter(adapter_goods_ncz);
                        utils.setListViewHeight(expandableListView);
//                        for (int i = 0; i < listNewData.size(); i++)
//                        {
//                            expandableListView.expandGroup(i);//展开
//                        }

                    } else
                    {
                        listNewData = new ArrayList<ParkGoodsBean>();
                    }

                } else
                {
                    AppContext.makeToast(NCZ_GoodsDistribution.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_GoodsDistribution.this, "error_connectServer");
            }
        });
    }


}

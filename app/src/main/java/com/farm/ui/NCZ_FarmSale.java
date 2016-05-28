package com.farm.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.NCZ_FarmSale_Adapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.BatchTime;
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
    List<parktab> listNewData = null;
    //    List<SellOrderDetail_New> list_newsale = null;
    NCZ_FarmSale_Adapter ncz_farmSale_adapter;
    @ViewById
    ExpandableListView expandableListView;
    @ViewById
    TextView tv_numberofnewsale;
    @ViewById
    Button btn_orders;

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Click
    void btn_newsalelist()
    {
        if (tv_numberofnewsale.isShown())
        {
            Intent intent = new Intent(NCZ_FarmSale.this, NCZ_NewSaleList_.class);
            startActivity(intent);
        } else
        {
            Toast.makeText(this, "暂无待发布订单", Toast.LENGTH_SHORT).show();
        }

    }

    @Click
    void btn_back()
    {
        finish();
    }

    @Click
    void btn_orders()
    {
        Intent intent = new Intent(NCZ_FarmSale.this, NCZ_OrderManager_.class);
        startActivity(intent);

    }


    @AfterViews
    void afterOncreate()
    {
        getBatchTimeByUid();
        iSExistNewSale();
//        getBatchTimeByUid_test();
//        getNewSaleList_test();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        IntentFilter intentfilter_update = new IntentFilter(AppContext.BROADCAST_UPDATESELLORDER);
        registerReceiver(receiver_update, intentfilter_update);
    }

    BroadcastReceiver receiver_update = new BroadcastReceiver()// 从扩展页面返回信息
    {
        @SuppressWarnings("deprecation")
        @Override
        public void onReceive(Context context, Intent intent)
        {
            getBatchTimeByUid();
            iSExistNewSale();
        }
    };

    private void getBatchTimeByUid_test()
    {
        listNewData = FileHelper.getAssetsData(NCZ_FarmSale.this, "getBatchTimeByUid", parktab.class);
        ncz_farmSale_adapter = new NCZ_FarmSale_Adapter(NCZ_FarmSale.this, listNewData, expandableListView);
        expandableListView.setAdapter(ncz_farmSale_adapter);
        utils.setListViewHeight(expandableListView);
        for (int i = 0; i < listNewData.size(); i++)
        {
            expandableListView.expandGroup(i);//展开
        }
    }

//    private void getNewSaleList_test()
//    {
//        list_newsale = FileHelper.getAssetsData(NCZ_FarmSale.this, "getNewSaleList", SellOrderDetail_New.class);
//        if (list_newsale != null)
//        {
//            tv_numberofnewsale.setVisibility(View.VISIBLE);
//            tv_numberofnewsale.setText("1");
//        }
//
//    }

    private void getBatchTimeByUid()
    {
        commembertab commembertab = AppContext.getUserInfo(NCZ_FarmSale.this);
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

                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), parktab.class);
                        ncz_farmSale_adapter = new NCZ_FarmSale_Adapter(NCZ_FarmSale.this, listNewData, expandableListView);
                        expandableListView.setAdapter(ncz_farmSale_adapter);
                        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
                        {

                            @Override
                            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
                            {
                                parktab parktab = listNewData.get(groupPosition);
                                BatchTime batchTime = parktab.getBatchTimeList().get(childPosition);
                                Intent intent = new Intent(NCZ_FarmSale.this, NCZ_FarmSale_BatchDetail_.class);
                                intent.putExtra("parkid", parktab.getid());
                                intent.putExtra("batchTime", batchTime.getBatchTime());
                                NCZ_FarmSale.this.startActivity(intent);
                                return true;
                            }
                        });
                        utils.setListViewHeight(expandableListView);
//                        for (int i = 0; i < listNewData.size(); i++)
//                        {
//                            expandableListView.expandGroup(i);//展开
//                                  expandableListView.collapseGroup(i);//关闭
//                        }

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

    private void iSExistNewSale()
    {
        commembertab commembertab = AppContext.getUserInfo(NCZ_FarmSale.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("year", utils.getYear());
        params.addQueryStringParameter("action", "iSExistNewSale");//jobGetList1
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        tv_numberofnewsale.setVisibility(View.VISIBLE);
                        tv_numberofnewsale.setText("1");
                    } else
                    {
                        tv_numberofnewsale.setVisibility(View.GONE);
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

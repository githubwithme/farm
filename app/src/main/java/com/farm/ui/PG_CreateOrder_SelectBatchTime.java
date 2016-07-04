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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.SelectBatchTime_Adapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.BatchTime;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.bean.parktab;
import com.farm.common.FileHelper;
import com.farm.common.utils;
import com.farm.widget.MyDialog;
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
 * Created by hasee on 2016/7/4.
 */
@EActivity(R.layout.ncz_createorder_selectbatchtime)
public class PG_CreateOrder_SelectBatchTime extends Activity
{
    MyDialog myDialog;
    List<parktab> listNewData = null;
    SelectBatchTime_Adapter selectBatchTime_adapter;
    @ViewById
    ExpandableListView expandableListView;
    @ViewById
    Button btn_orders;

    @Override
    protected void onResume()
    {
        super.onResume();
    }



    @Click
    void btn_back()
    {
        finish();
    }


    @AfterViews
    void afterOncreate()
    {
        getBatchTimeByUid();
//        getBatchTimeByUid_test();
//        getNewSaleList_test();
//        deleNewSaleAddsalefor();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        IntentFilter intentfilter_update = new IntentFilter(AppContext.BROADCAST_UPDATESELLORDER);
        registerReceiver(receiver_update, intentfilter_update);

        IntentFilter intentfilter_finish = new IntentFilter(AppContext.BROADCAST_FINISHSELECTBATCHTIME);
        registerReceiver(receiver_finish, intentfilter_finish);
    }

    BroadcastReceiver receiver_finish = new BroadcastReceiver()// 从扩展页面返回信息
    {
        @SuppressWarnings("deprecation")
        @Override
        public void onReceive(Context context, Intent intent)
        {
            finish();
        }
    };
    BroadcastReceiver receiver_update = new BroadcastReceiver()// 从扩展页面返回信息
    {
        @SuppressWarnings("deprecation")
        @Override
        public void onReceive(Context context, Intent intent)
        {
            getBatchTimeByUid();
        }
    };

    private void getBatchTimeByUid_test()
    {
        listNewData = FileHelper.getAssetsData(PG_CreateOrder_SelectBatchTime.this, "getBatchTimeByUid", parktab.class);
        selectBatchTime_adapter = new SelectBatchTime_Adapter(PG_CreateOrder_SelectBatchTime.this, listNewData, expandableListView);
        expandableListView.setAdapter(selectBatchTime_adapter);
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
        commembertab commembertab = AppContext.getUserInfo(PG_CreateOrder_SelectBatchTime.this);
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
                        List<parktab> list = new ArrayList<>();
                        for (int i = 0; i < listNewData.size(); i++)
                        {
                            parktab parktab = listNewData.get(i);
                            if (!parktab.getAllsalefor().equals("0"))
                            {
                                List<BatchTime> list_newBatchTime = new ArrayList<>();
                                List<BatchTime> list_batchtime = parktab.getBatchTimeList();
                                for (int j = 0; j < list_batchtime.size(); j++)
                                {
                                    BatchTime batchtime = list_batchtime.get(j);
                                    if (!batchtime.getAllsalefor().equals("0"))
                                    {
                                        list_newBatchTime.add(batchtime);
                                    }
                                }
                                parktab.setBatchTimeList(list_newBatchTime);
                                list.add(parktab);
                            }
                        }
                        selectBatchTime_adapter = new SelectBatchTime_Adapter(PG_CreateOrder_SelectBatchTime.this, list, expandableListView);
                        expandableListView.setAdapter(selectBatchTime_adapter);
                        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
                        {
                            @Override
                            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
                            {
                                parktab parktab = listNewData.get(groupPosition);
                                BatchTime batchTime = parktab.getBatchTimeList().get(childPosition);
                                Intent intent = new Intent(PG_CreateOrder_SelectBatchTime.this, NCZ_CreateOrder_SelectProduct_.class);
                                intent.putExtra("parkid", parktab.getid());
                                intent.putExtra("parkname", parktab.getparkName());
                                intent.putExtra("batchTime", batchTime.getBatchTime());
                                PG_CreateOrder_SelectBatchTime.this.startActivity(intent);
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
                    AppContext.makeToast(PG_CreateOrder_SelectBatchTime.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(PG_CreateOrder_SelectBatchTime.this, "error_connectServer");
            }
        });
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event)
//    {
//        if (keyCode == KeyEvent.KEYCODE_BACK)
//        {
//            cancleOrder();
//        }
//        return false;
//    }

    private void cancleOrder()
    {
        View dialog_layout = (LinearLayout) getLayoutInflater().inflate(R.layout.customdialog_callback, null);
        myDialog = new MyDialog(PG_CreateOrder_SelectBatchTime.this, R.style.MyDialog, dialog_layout, "取消订单", "取消订单吗？", "取消", "不取消", new MyDialog.CustomDialogListener()
        {
            @Override
            public void OnClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.btn_sure:
                        myDialog.dismiss();
                        Toast.makeText(PG_CreateOrder_SelectBatchTime.this, "已取消", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    case R.id.btn_cancle:
                        myDialog.dismiss();
                        break;
                }
            }
        });
        myDialog.show();
    }

    private void deleNewSaleAddsalefor()
    {
        commembertab commembertab = AppContext.getUserInfo(this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("action", "deleNewSaleAddsalefor");//jobGetList1
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
               /* if (result.getAffectedRows() != 0)
                {
                    listData = JSON.parseArray(result.getRows().toJSONString(), SellOrder_New.class);

                } else
                {
                    listData = new ArrayList<SellOrder_New>();
                }*/

                } else
                {
                    AppContext.makeToast(PG_CreateOrder_SelectBatchTime.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(PG_CreateOrder_SelectBatchTime.this, "error_connectServer");

            }
        });
    }
}

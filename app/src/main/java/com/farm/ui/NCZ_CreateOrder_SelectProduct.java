package com.farm.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.Adapter_CreateOrder_SelectProduct;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.SellOrderDetail_New;
import com.farm.bean.areatab;
import com.farm.bean.commembertab;
import com.farm.bean.contractTab;
import com.farm.common.FileHelper;
import com.farm.common.utils;
import com.farm.widget.CustomGridview;
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
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2016/4/25.
 */
@EActivity(R.layout.ncz_createorder_selectproduct)
public class NCZ_CreateOrder_SelectProduct extends Activity
{
    String parkid;
    String batchTime;
    List<Map<String, String>> uuids;
    List<SellOrderDetail_New> list_sell;
    Adapter_CreateOrder_SelectProduct adapter_createOrder_selectProduct;
    @ViewById
    ExpandableListView expandableListView;
    @ViewById
    Button btn_addmore;
    @ViewById
    Button btn_cancleorder;
    @ViewById
    TextView tv_salenumber;
    @ViewById
    RelativeLayout pb_upload;

    @Click
    void btn_back()
    {
        finish();
    }

    @Click
    void btn_cancleorder()
    {
        finish();
    }

    @Click
    void btn_createorder()
    {
        setData();
        Intent intent = new Intent(NCZ_CreateOrder_SelectProduct.this, NCZ_DirectCreateOrder_.class);
        intent.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) list_sell);
        Bundle bundle = new Bundle();
        ArrayList arrayList = new ArrayList();
        arrayList.add(uuids);
        String aa = JSON.toJSONString(uuids);
        bundle.putParcelableArrayList("list_uuid", arrayList);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Click
    void btn_addmore()
    {
        pb_upload.setVisibility(View.VISIBLE);
        setData();
        StringBuilder builder = new StringBuilder();
        builder.append("{\"SellOrderDetailList\": ");
        builder.append(JSON.toJSONString(list_sell));
        builder.append(", \"uuids\": ");
        builder.append(JSON.toJSONString(uuids));
        builder.append("} ");
        addNewSale(builder.toString());
    }

    @AfterViews
    void afterOncreate()
    {
//        getBatchTimeByUid_test();
        getSaleDataOfArea();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        parkid = getIntent().getStringExtra("parkid");
        batchTime = getIntent().getStringExtra("batchTime");
        IntentFilter intentfilter_update = new IntentFilter(AppContext.BROADCAST_FINISH);
        registerReceiver(receiver_finish, intentfilter_update);
        IntentFilter intentfilter_updatesalenumber = new IntentFilter(AppContext.BROADCAST_UPDATESALENUMBER);
        registerReceiver(receiver_updatesalenumber, intentfilter_updatesalenumber);
    }

    BroadcastReceiver receiver_updatesalenumber = new BroadcastReceiver()// 从扩展页面返回信息
    {
        @SuppressWarnings("deprecation")
        @Override
        public void onReceive(Context context, Intent intent)
        {
            countSaleNumber();
        }
    };
    BroadcastReceiver receiver_finish = new BroadcastReceiver()// 从扩展页面返回信息
    {
        @SuppressWarnings("deprecation")
        @Override
        public void onReceive(Context context, Intent intent)
        {
            finish();
        }
    };

    public void setData()
    {
        list_sell = new ArrayList<>();
        uuids = new ArrayList<>();
        int grountCount = adapter_createOrder_selectProduct.getGroupCount();
        for (int i = 0; i < grountCount; i++)
        {
            int childrenCount = adapter_createOrder_selectProduct.getChildrenCount(i);
            for (int j = 0; j < childrenCount; j++)
            {
                LinearLayout linearlayout = (LinearLayout) adapter_createOrder_selectProduct.getChildView(i, j, false, null, null);
                CustomGridview gv = (CustomGridview) linearlayout.findViewById(R.id.gv);
                int childCount = gv.getChildCount();
                for (int k = 0; k < childCount; k++)
                {
                    LinearLayout ll = (LinearLayout) gv.getChildAt(k);
                    CheckBox cb_selectall = (CheckBox) ll.findViewById(R.id.cb_selectall);
                    Button btn_number = (Button) ll.findViewById(R.id.btn_number);
                    if (cb_selectall.isChecked())
                    {
                        Bundle bundle = (Bundle) cb_selectall.getTag(R.id.tag_view);
                        contractTab contractTab = bundle.getParcelable("bean");
                        String number_newsale = btn_number.getText().toString();
                        String number_salefor = contractTab.getAllsalefor();
                        String number_left = String.valueOf(Integer.valueOf(number_salefor) - Integer.valueOf(number_newsale));
                        HashMap hashMap = new HashMap();
                        hashMap.put("contractid", contractTab.getid());
                        hashMap.put("year", utils.getYear());
                        hashMap.put("batchTime", batchTime);
                        hashMap.put("number_difference", number_left);
                        uuids.add(hashMap);

                        String uuid = java.util.UUID.randomUUID().toString();
                        SellOrderDetail_New sellorderdetail_newsale = new SellOrderDetail_New();
                        sellorderdetail_newsale.setuid(contractTab.getuId());
                        sellorderdetail_newsale.setUuid(uuid);
                        sellorderdetail_newsale.setactuallat("");
                        sellorderdetail_newsale.setactuallatlngsize("");
                        sellorderdetail_newsale.setactuallng("");
                        sellorderdetail_newsale.setactualnote("");
                        sellorderdetail_newsale.setactualnumber("");
                        sellorderdetail_newsale.setactualprice("");
                        sellorderdetail_newsale.setactualweight("");
                        sellorderdetail_newsale.setareaid(contractTab.getAreaId());
                        sellorderdetail_newsale.setareaname(contractTab.getareaName());
                        sellorderdetail_newsale.setBatchTime(batchTime);
                        sellorderdetail_newsale.setcontractid(contractTab.getid());
                        sellorderdetail_newsale.setcontractname(contractTab.getContractNum());
                        sellorderdetail_newsale.setisSoldOut("0");
                        sellorderdetail_newsale.setparkid(contractTab.getparkId());
                        sellorderdetail_newsale.setparkname(contractTab.getparkName());
                        sellorderdetail_newsale.setPlanlat("");
                        sellorderdetail_newsale.setplanlng("");
                        sellorderdetail_newsale.setplanlatlngsize("");
                        sellorderdetail_newsale.setplannote("");
                        sellorderdetail_newsale.setplannumber(number_newsale);
                        sellorderdetail_newsale.setplanprice("");
                        sellorderdetail_newsale.setplanweight("");
                        sellorderdetail_newsale.setreg(utils.getTime());
                        sellorderdetail_newsale.setstatus("0");
                        sellorderdetail_newsale.setType("newsale");
                        sellorderdetail_newsale.setsaleid("");
                        sellorderdetail_newsale.setXxzt("0");
                        sellorderdetail_newsale.setYear(utils.getYear());
                        list_sell.add(sellorderdetail_newsale);
                    }
                }
            }
        }
    }

    public void countSaleNumber()
    {
        int number_select = 0;
        list_sell = new ArrayList<>();
        uuids = new ArrayList<>();
        int grountCount = adapter_createOrder_selectProduct.getGroupCount();
        for (int i = 0; i < grountCount; i++)
        {
            int childrenCount = adapter_createOrder_selectProduct.getChildrenCount(i);
            for (int j = 0; j < childrenCount; j++)
            {
                LinearLayout linearlayout = (LinearLayout) adapter_createOrder_selectProduct.getChildView(i, j, false, null, null);
                CustomGridview gv = (CustomGridview) linearlayout.findViewById(R.id.gv);
                int childCount = gv.getChildCount();
                for (int k = 0; k < childCount; k++)
                {
                    LinearLayout ll = (LinearLayout) gv.getChildAt(k);
                    CheckBox cb_selectall = (CheckBox) ll.findViewById(R.id.cb_selectall);
                    Button btn_number = (Button) ll.findViewById(R.id.btn_number);
                    if (cb_selectall.isChecked())
                    {
                        Bundle bundle = (Bundle) cb_selectall.getTag(R.id.tag_view);
                        contractTab contractTab = bundle.getParcelable("bean");
                        number_select = number_select + Integer.valueOf(btn_number.getText().toString());
                    }
                    tv_salenumber.setText(number_select + "株");
                }
            }
        }
    }

    private void getBatchTimeByUid_test()
    {
        List<areatab> listNewData = FileHelper.getAssetsData(NCZ_CreateOrder_SelectProduct.this, "getSellOrderDetailByBatchtime", areatab.class);
        adapter_createOrder_selectProduct = new Adapter_CreateOrder_SelectProduct(NCZ_CreateOrder_SelectProduct.this, listNewData, expandableListView);
        expandableListView.setAdapter(adapter_createOrder_selectProduct);
        utils.setListViewHeight(expandableListView);
//        for (int i = 0; i < listNewData.size(); i++)
//        {
//            expandableListView.expandGroup(i);//展开
//        }
    }

    private void getBatchTimeByUid()
    {
        commembertab commembertab = AppContext.getUserInfo(NCZ_CreateOrder_SelectProduct.this);
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
                List<areatab> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), areatab.class);
                        adapter_createOrder_selectProduct = new Adapter_CreateOrder_SelectProduct(NCZ_CreateOrder_SelectProduct.this, listNewData, expandableListView);
                        expandableListView.setAdapter(adapter_createOrder_selectProduct);
                        utils.setListViewHeight(expandableListView);
                        for (int i = 0; i < listNewData.size(); i++)
                        {
                            expandableListView.expandGroup(i);//展开
//                                  expandableListView.collapseGroup(i);//关闭
                        }

                    } else
                    {
                        listNewData = new ArrayList<areatab>();
                    }

                } else
                {
                    AppContext.makeToast(NCZ_CreateOrder_SelectProduct.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_CreateOrder_SelectProduct.this, "error_connectServer");
            }
        });
    }

    private void getNewSalelList()
    {
        commembertab commembertab = AppContext.getUserInfo(NCZ_CreateOrder_SelectProduct.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("year", utils.getYear());
        params.addQueryStringParameter("action", "getSellOrderDetailList");//jobGetList1
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<SellOrderDetail_New> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), SellOrderDetail_New.class);

                        int number = 0;
                        for (int i = 0; i < listNewData.size(); i++)
                        {
                            number = number + Integer.valueOf(listNewData.get(i).getplannumber());
                        }
                    } else
                    {
                    }

                } else
                {
                    AppContext.makeToast(NCZ_CreateOrder_SelectProduct.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_CreateOrder_SelectProduct.this, "error_connectServer");
            }
        });
    }

    private void addNewSale(String data)
    {
        RequestParams params = new RequestParams();
        params.setContentType("application/json");
        try
        {
            params.setBodyEntity(new StringEntity(data, "utf-8"));
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        params.addQueryStringParameter("action", "saveSellOrderDetailList");
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
                        pb_upload.setVisibility(View.GONE);
                        Toast.makeText(NCZ_CreateOrder_SelectProduct.this, "保存成功", Toast.LENGTH_SHORT).show();
                        Intent intent2 = new Intent();
                        intent2.setAction(AppContext.BROADCAST_UPDATESELLORDER);
                        sendBroadcast(intent2);
                        finish();
                    }
                } else
                {
                    pb_upload.setVisibility(View.GONE);
                    AppContext.makeToast(NCZ_CreateOrder_SelectProduct.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                pb_upload.setVisibility(View.GONE);
                AppContext.makeToast(NCZ_CreateOrder_SelectProduct.this, "error_connectServer");
            }
        });
    }

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
                        adapter_createOrder_selectProduct = new Adapter_CreateOrder_SelectProduct(NCZ_CreateOrder_SelectProduct.this, listNewData, expandableListView);
                        expandableListView.setAdapter(adapter_createOrder_selectProduct);
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
                    AppContext.makeToast(NCZ_CreateOrder_SelectProduct.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_CreateOrder_SelectProduct.this, "error_connectServer");
            }
        });
    }

}
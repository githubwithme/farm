package com.farm.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.Charge_Settlement_Adapter;
import com.farm.adapter.PG_JSD_Adapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.SellOrderDetail_New;
import com.farm.bean.SellOrder_New;
import com.farm.bean.commembertab;
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
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hasee on 2016/7/26.
 */
@EActivity(R.layout.charge_settlement)
public class Charge_UpdateSettlement extends Activity
{
    SellOrder_New sellOrder_new = new SellOrder_New();
    String jsdId;
    Charge_Settlement_Adapter charge_settlement_adapter;
    @ViewById
    ListView frame_listview_news;
    String batchTime;

    List<SellOrderDetail_New> listdata = new ArrayList<SellOrderDetail_New>();
    @ViewById
    Button charge_save;
    @ViewById
    Button charge_load;
    @ViewById
    EditText plateNumber;//车牌号
    @ViewById
    EditText zp_ds_zhong;//正品带水重
    @ViewById
    EditText cp_ds_zhong;//次品带水重
    @ViewById
    EditText zp_bds_zhong;//正品净重
    @ViewById
    EditText cp_jingzhong;//次品净重
    String isSave="1";



    @Click
    void  charge_load()
    {
        if (plateNumber.getText().toString().equals(""))
        {
            Toast.makeText(Charge_UpdateSettlement.this, "请填写车牌号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (zp_bds_zhong.getText().equals(""))
        {
            Toast.makeText(Charge_UpdateSettlement.this, "请填正品净重", Toast.LENGTH_SHORT).show();
            return;
        }
        SellOrder_New sellOrder = new SellOrder_New();
        sellOrder=sellOrder_new;
        sellOrder.setInfoId(sellOrder_new.getUuid());
        sellOrder.setUid(sellOrder_new.getUid());
        sellOrder.setIsNeedAudit("2");
        sellOrder.setSettlestatus("2");
        sellOrder.setPlateNumber(plateNumber.getText().toString());//车车牌号
        sellOrder.setQualityWaterWeight(zp_ds_zhong.getText().toString());//正品带水重
        sellOrder.setQualityNetWeight(zp_bds_zhong.getText().toString());//正品净重
        sellOrder.setDefectWaterWeight(cp_ds_zhong.getText().toString());//次品带水重
        sellOrder.setDefectNetWeight(cp_jingzhong.getText().toString());//次品净重
        updatesellOrderSettlement(sellOrder);

/*    "  qualityWaterWeight=" + mSellOrder.getQualityWaterWeight() + " , qualityNetWeight=" + mSellOrder.getQualityNetWeight() +
            " , defectWaterWeight=" + mSellOrder.getDefectWaterWeight()+",defectNetWeight="+mSellOrder.getDefectNetWeight() + " , plateNumber='" + mSellOrder.getPlateNumber()*/

    }
    @AfterViews
    void afterview()
    {
        charge_save.setVisibility(View.GONE);
        charge_load.setVisibility(View.VISIBLE);
        getSellOrderDetailSec();
        showData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        sellOrder_new = getIntent().getParcelableExtra("bean");

        IntentFilter intentfilter_update = new IntentFilter(AppContext.UPDATEMESSAGE_NEW_JSD);
        registerReceiver(receiver_update, intentfilter_update);
        jsdId = sellOrder_new.getid();

    }
    BroadcastReceiver receiver_update = new BroadcastReceiver()// 从扩展页面返回信息
    {
        @SuppressWarnings("deprecation")
        @Override
        public void onReceive(Context context, Intent intent)
        {
            getSellOrderDetailSec();
        }
    };
    private void showData()
    {
        plateNumber.setText(sellOrder_new.getPlateNumber());
        zp_ds_zhong.setText(sellOrder_new.getQualityWaterWeight());
        cp_ds_zhong.setText(sellOrder_new.getDefectWaterWeight());
        zp_bds_zhong.setText(sellOrder_new.getQualityNetWeight());
        cp_jingzhong.setText(sellOrder_new.getDefectNetWeight());
    }

    //获取销售明细子表
    public void getSellOrderDetailSec()
    {
        commembertab commembertab = AppContext.getUserInfo(Charge_UpdateSettlement.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("strWhere", "uid=" + commembertab.getuId() + " and settlementId=" + jsdId);
        params.addQueryStringParameter("action", "getSellOrderDetailSec");
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

                  /*  if (result.getAffectedRows() != 0)
                    {*/
                    listNewData = JSON.parseArray(result.getRows().toJSONString(), SellOrderDetail_New.class);
                    listdata.addAll(listNewData);
                    if (listNewData.size() > 0)
                    {
                        charge_settlement_adapter = new Charge_Settlement_Adapter(Charge_UpdateSettlement.this, listNewData, sellOrder_new.getQualityNetWeight(), sellOrder_new.getDefectNetWeight(), isSave);
                        frame_listview_news.setAdapter(charge_settlement_adapter);
                        utils.setListViewHeight(frame_listview_news);
                    } else
                    {
                        getsellOrderDetailBySaleId();
                    }

              /*      } else
                    {*/
//                        listNewData = new ArrayList<SellOrderDetail_New>();

//                    }

                } else
                {
                    AppContext.makeToast(Charge_UpdateSettlement.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(Charge_UpdateSettlement.this, "error_connectServer");
            }
        });
    }

    //查销售明细表 同时生成销售明细子表
    private void getsellOrderDetailBySaleId()
    {

        commembertab commembertab = AppContext.getUserInfo(Charge_UpdateSettlement.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("saleId", sellOrder_new.getInfoId());
        params.addQueryStringParameter("action", "getsellOrderDetailBySaleId");
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
                        if (listNewData.size() > 0)
                        {
                            batchTime = listNewData.get(0).getBatchTime();
                        }


                            List<SellOrderDetail_New> listAddSellData = new ArrayList<SellOrderDetail_New>();
                            for (int i = 0; i < listNewData.size(); i++)
                            {
                                SellOrderDetail_New sellOrderDetail_new = new SellOrderDetail_New();
                                sellOrderDetail_new.setSettlementId(jsdId);
                                sellOrderDetail_new.setInfoId(listNewData.get(i).getUuid());
                                sellOrderDetail_new.setuid(listNewData.get(i).getuid());
                                sellOrderDetail_new.setBatchTime(listNewData.get(i).getBatchTime());
                                sellOrderDetail_new.setYear(listNewData.get(i).getYear());
                                sellOrderDetail_new.setparkid(listNewData.get(i).getparkid());
                                sellOrderDetail_new.setparkname(listNewData.get(i).getparkname());
                                sellOrderDetail_new.setareaid(listNewData.get(i).getareaid());
                                sellOrderDetail_new.setareaname(listNewData.get(i).getareaname());
                                sellOrderDetail_new.setcontractid(listNewData.get(i).getcontractid());
                                sellOrderDetail_new.setcontractname(listNewData.get(i).getcontractname());
                                listAddSellData.add(sellOrderDetail_new);
                            }
                            StringBuilder builder = new StringBuilder();
                            builder.append("{\"SellOrderDetailSeclist\": ");
                            builder.append(JSON.toJSONString(listAddSellData));
                            builder.append("} ");
                            addSellOrderDetailSec(builder.toString());



                    } else
                    {
                        listNewData = new ArrayList<SellOrderDetail_New>();
                    }

                } else
                {
                    AppContext.makeToast(Charge_UpdateSettlement.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(Charge_UpdateSettlement.this, "error_connectServer");
            }
        });
    }

    //添加销售明显子表
    private void addSellOrderDetailSec(String data)
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("action", "addSellOrderDetailSec");
        params.setContentType("application/json");
        try
        {
            params.setBodyEntity(new StringEntity(data, "utf-8"));
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        HttpUtils http = new HttpUtils();
        http.configTimeout(60000);
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

                        getSellOrderDetailSec();
                    }

                } else
                {
                    AppContext.makeToast(Charge_UpdateSettlement.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(Charge_UpdateSettlement.this, "error_connectServer");
            }
        });
    }

    //修改
    private void updatesellOrderSettlement(SellOrder_New mSellOrder)
    {


        RequestParams params = new RequestParams();
        params.addQueryStringParameter("id", mSellOrder.getid());
        params.addQueryStringParameter("settlestatus", "2");
        params.addQueryStringParameter("isNeedAudit", "2");
        params.addQueryStringParameter("uid", mSellOrder.getUid());
/*        params.addQueryStringParameter("carryPrice", mSellOrder.getCarryPrice());
        params.addQueryStringParameter("packPrice", mSellOrder.getPackPrice());
        params.addQueryStringParameter("infoId", mSellOrder.getInfoId());
        params.addQueryStringParameter("plateNumber", mSellOrder.getPlateNumber());*/
        params.addQueryStringParameter("qualityWaterWeight", mSellOrder.getQualityWaterWeight());
        params.addQueryStringParameter("qualityNetWeight", mSellOrder.getQualityNetWeight());
        params.addQueryStringParameter("defectWaterWeight", mSellOrder.getDefectNetWeight());
        params.addQueryStringParameter("defectNetWeight", mSellOrder.getDefectNetWeight());
        params.addQueryStringParameter("isReady","True");
        params.addQueryStringParameter("action", "changesellOrderSettlement");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<SellOrder_New> listData = new ArrayList<SellOrder_New>();
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        listData = JSON.parseArray(result.getRows().toJSONString(), SellOrder_New.class);

                        if (listdata.size()==0)
                        {
                            getsellOrderDetailBySaleId();
                        }
                    } else
                    {
                        listData = new ArrayList<SellOrder_New>();
                    }

                } else
                {
                    AppContext.makeToast(Charge_UpdateSettlement.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(Charge_UpdateSettlement.this, "error_connectServer");

            }
        });
    }
}

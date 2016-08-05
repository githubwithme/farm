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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.Charge_Settlement_Adapter;
import com.farm.adapter.PG_JSD_Adapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Purchaser;
import com.farm.bean.Result;
import com.farm.bean.SellOrderDetail_New;
import com.farm.bean.SellOrder_New;
import com.farm.bean.commembertab;
import com.farm.common.utils;
import com.farm.widget.CustomDialog_ListView;
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
public class Charge_Settlement extends Activity
{
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
    @ViewById
    TextView bz_fzrid;//包装负责人
    @ViewById
    TextView by_fzrid;//搬运负责人
    @ViewById
    EditText bz_nc_danjia;//包装单价
    @ViewById
    EditText by_nc_danjia;//搬运单价


    @ViewById
    Button charge_save;  //保存
    @ViewById
    Button charge_load; //修改

    SellOrder_New sellOrder_new;
    List<SellOrderDetail_New> listSellData = new ArrayList<SellOrderDetail_New>();
    String batchTime = "";
    String jsdId = "";
    String byId = "";
    String bzId = "";
    String zzsl = "";
    CustomDialog_ListView customDialog_listViews;
    List<Purchaser> listData_BY = new ArrayList<Purchaser>();
    List<Purchaser> listData_BZ = new ArrayList<Purchaser>();
    @ViewById
    ListView frame_listview_news;
    Charge_Settlement_Adapter charge_settlement_adapter;

    @ViewById
    View view1;
    @ViewById
    View view2;
    @ViewById
    LinearLayout nc_baozhuang;
    @ViewById
    LinearLayout ll_banyun;

    String isSave = "1";
    String jsdid = "";

    @Click
        //包装负责人
    void bz_fzrid()
    {
        List<String> listdata = new ArrayList<String>();
        List<String> listid = new ArrayList<String>();
        for (int i = 0; i < listData_BZ.size(); i++)
        {
            listdata.add(listData_BZ.get(i).getName());
            listid.add(listData_BZ.get(i).getId());
        }
        showDialog_bz(listdata, listid);
    }

    @Click
        //搬运负责人
    void by_fzrid()
    {
        List<String> listdata = new ArrayList<String>();
        List<String> listid = new ArrayList<String>();
        for (int i = 0; i < listData_BY.size(); i++)
        {
            listdata.add(listData_BY.get(i).getName());
            listid.add(listData_BY.get(i).getId());
        }
        showDialog_by(listdata, listid);
    }

    @AfterViews
    void afterview()
    {
        charge_save.setVisibility(View.VISIBLE);
        getsellOrderDetailBySaleId();

        view1.setVisibility(View.VISIBLE);
        view2.setVisibility(View.VISIBLE);
        nc_baozhuang.setVisibility(View.VISIBLE);
        ll_banyun.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        sellOrder_new = getIntent().getParcelableExtra("bean");
        IntentFilter intentfilter_update = new IntentFilter(AppContext.UPDATEMESSAGE_NEW_JSD);
        registerReceiver(receiver_update, intentfilter_update);
        getpurchaser();
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

    @Click
    void charge_save()
    {
        if (plateNumber.getText().toString().equals(""))
        {
            Toast.makeText(Charge_Settlement.this, "请填写车牌号", Toast.LENGTH_SHORT).show();
            return;
        }
        SellOrder_New sellOrder = new SellOrder_New();
        sellOrder.setInfoId(sellOrder_new.getUuid());
        sellOrder.setUid(sellOrder_new.getUid());
        sellOrder.setIsNeedAudit("2");
        sellOrder.setSettlestatus("2");

        sellOrder.setPlateNumber(plateNumber.getText().toString());//车车牌号
        sellOrder.setQualityWaterWeight(zp_ds_zhong.getText().toString());//正品带水重
        sellOrder.setQualityNetWeight(zp_bds_zhong.getText().toString());//正品净重
        sellOrder.setDefectWaterWeight(cp_ds_zhong.getText().toString());//次品带水重
        sellOrder.setDefectNetWeight(cp_jingzhong.getText().toString());//次品净重
        sellOrder.setPackPrice(bz_nc_danjia.getText().toString());//包装单价
        sellOrder.setCarryPrice(by_nc_danjia.getText().toString());//搬运单价
        sellOrder.setContractorId(bzId);
        sellOrder.setPickId(byId);
        StringBuilder builder = new StringBuilder();
        builder.append("{\"sellOrderSettlementlist\":[ ");
        builder.append(JSON.toJSONString(sellOrder));
        builder.append("]} ");
        addsellOrderSettlement(builder.toString());
    }

    //修改
    @Click
    void charge_load()
    {
        if (plateNumber.getText().toString().equals(""))
        {
            Toast.makeText(Charge_Settlement.this, "请填写车牌号", Toast.LENGTH_SHORT).show();
            return;
        }
        SellOrder_New sellOrder = new SellOrder_New();
        sellOrder.setInfoId(sellOrder_new.getUuid());
        sellOrder.setUid(sellOrder_new.getUid());
        sellOrder.setPlateNumber(plateNumber.getText().toString());//车车牌号
        sellOrder.setQualityWaterWeight(zp_ds_zhong.getText().toString());//正品带水重
        sellOrder.setQualityNetWeight(zp_bds_zhong.getText().toString());//正品净重
        sellOrder.setDefectWaterWeight(cp_ds_zhong.getText().toString());//次品带水重
        sellOrder.setDefectNetWeight(cp_jingzhong.getText().toString());//次品净重
        sellOrder.setPackPrice(bz_nc_danjia.getText().toString());//包装单价
        sellOrder.setCarryPrice(by_nc_danjia.getText().toString());//搬运单价
        sellOrder.setContractorId(bzId);
        sellOrder.setPickId(byId);
        updatesellOrderSettlement(sellOrder);
    }

    //添加结算单
    private void addsellOrderSettlement(String data)
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("action", "addsellOrderSettlement");
        params.setContentType("application/json");
        try
        {
            params.setBodyEntity(new StringEntity(data, "utf-8"));
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
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
                        jsdid = String.valueOf(result.getAffectedRows());
                        charge_save.setVisibility(View.GONE);
                        charge_load.setVisibility(View.VISIBLE);
                        jsdId = result.getAffectedRows() + "";
                        List<SellOrderDetail_New> listAddSellData = new ArrayList<SellOrderDetail_New>();
                        for (int i = 0; i < listSellData.size(); i++)
                        {
                            SellOrderDetail_New sellOrderDetail_new = new SellOrderDetail_New();
                            sellOrderDetail_new.setSettlementId(jsdId);
                            sellOrderDetail_new.setInfoId(listSellData.get(i).getUuid());
                            sellOrderDetail_new.setuid(listSellData.get(i).getuid());
                            sellOrderDetail_new.setBatchTime(listSellData.get(i).getBatchTime());
                            sellOrderDetail_new.setYear(listSellData.get(i).getYear());
                            sellOrderDetail_new.setparkid(listSellData.get(i).getparkid());
                            sellOrderDetail_new.setparkname(listSellData.get(i).getparkname());
                            sellOrderDetail_new.setareaid(listSellData.get(i).getareaid());
                            sellOrderDetail_new.setareaname(listSellData.get(i).getareaname());
                            sellOrderDetail_new.setcontractid(listSellData.get(i).getcontractid());
                            sellOrderDetail_new.setcontractname(listSellData.get(i).getcontractname());
                            listAddSellData.add(sellOrderDetail_new);
                        }
                        StringBuilder builder = new StringBuilder();
                        builder.append("{\"SellOrderDetailSeclist\": ");
                        builder.append(JSON.toJSONString(listAddSellData));
                        builder.append("} ");
                        addSellOrderDetailSec(builder.toString());



                  /*      Intent intent = new Intent();
                        intent.setAction(AppContext.UPDATEMESSAGE_PGDETAIL_UPDATE_DELETE);
                        sendBroadcast(intent);
                        Toast.makeText(PG_JSD_Detail.this,"保存成功",Toast.LENGTH_SHORT).show();
                        finish();*/
                    }

                } else
                {
                    AppContext.makeToast(Charge_Settlement.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(Charge_Settlement.this, "error_connectServer");
            }
        });
    }

    //获取销售明细表
    private void getsellOrderDetailBySaleId()
    {

        commembertab commembertab = AppContext.getUserInfo(Charge_Settlement.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("saleId", sellOrder_new.getUuid());
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
                        for (int i = 0; i < listNewData.size(); i++)
                        {
                            listNewData.get(i).setactualnumber("");
                        }
                        if (listNewData.size() > 0)
                        {
                            batchTime = listNewData.get(0).getBatchTime();
                        }

                        listSellData.addAll(listNewData);
                        charge_settlement_adapter = new Charge_Settlement_Adapter(Charge_Settlement.this, listNewData, sellOrder_new.getQualityNetWeight(), sellOrder_new.getDefectNetWeight(), isSave);
                        frame_listview_news.setAdapter(charge_settlement_adapter);
                        utils.setListViewHeight(frame_listview_news);

                    } else
                    {
                        listNewData = new ArrayList<SellOrderDetail_New>();
                    }

                } else
                {
                    AppContext.makeToast(Charge_Settlement.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(Charge_Settlement.this, "error_connectServer");
            }
        });
    }

    //根据订单计划的销售明细表生成销售明细子表的内容
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
                    AppContext.makeToast(Charge_Settlement.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(Charge_Settlement.this, "error_connectServer");
            }
        });
    }

    //获取销售明细子表
    public void getSellOrderDetailSec()
    {
        commembertab commembertab = AppContext.getUserInfo(Charge_Settlement.this);
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
                    if (result.getAffectedRows() != 0)
                    {

                        Toast.makeText(Charge_Settlement.this, "保存成功", Toast.LENGTH_SHORT).show();
                 /*       btn_upload.setVisibility(View.GONE);
                        ll_cbhlist.setVisibility(View.VISIBLE);
                        ll_jsd.setVisibility(View.VISIBLE);*/

                        isSave = "1";
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), SellOrderDetail_New.class);


                        charge_settlement_adapter = new Charge_Settlement_Adapter(Charge_Settlement.this, listNewData, sellOrder_new.getQualityNetWeight(), sellOrder_new.getDefectNetWeight(), isSave);
                        frame_listview_news.setAdapter(charge_settlement_adapter);
                        utils.setListViewHeight(frame_listview_news);
//                        shouData(listNewData);
                    } else
                    {
                        listNewData = new ArrayList<SellOrderDetail_New>();
                    }

                } else
                {
                    AppContext.makeToast(Charge_Settlement.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(Charge_Settlement.this, "error_connectServer");
            }
        });
    }

    //采购商，搬运工，包装工
    private void getpurchaser()
    {
        commembertab commembertab = AppContext.getUserInfo(Charge_Settlement.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("action", "getpurchaser");//jobGetList1
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<Purchaser> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        if (result.getAffectedRows() != 0)
                        {
                            listNewData = JSON.parseArray(result.getRows().toJSONString(), Purchaser.class);
                            for (int i = 0; i < listNewData.size(); i++)
                            {
                                if (listNewData.get(i).userType.equals("采购商"))
                                {
                                } else if (listNewData.get(i).userType.equals("包装工头"))
                                {
                                    listData_BZ.add(listNewData.get(i));
                                } else
                                {
                                    listData_BY.add(listNewData.get(i));
                                }
                            }

                        } else
                        {
                            listNewData = new ArrayList<Purchaser>();
                        }

                    } else
                    {
                        listNewData = new ArrayList<Purchaser>();
                    }

                } else
                {
                    AppContext.makeToast(Charge_Settlement.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(Charge_Settlement.this, "error_connectServer");
            }
        });
    }

    //包装工
    public void showDialog_bz(List<String> listdata, List<String> listid)
    {
        View dialog_layout = (LinearLayout) Charge_Settlement.this.getLayoutInflater().inflate(R.layout.customdialog_listview, null);
        customDialog_listViews = new CustomDialog_ListView(Charge_Settlement.this, R.style.MyDialog, dialog_layout, listdata, listid, new CustomDialog_ListView.CustomDialogListener()
        {
            @Override
            public void OnClick(Bundle bundle)
            {
                //id也是有的
                zzsl = bundle.getString("name");
                bz_fzrid.setText(zzsl);
                bzId = bundle.getString("id");

            }
        });
        customDialog_listViews.show();
    }

    //搬运工
    public void showDialog_by(List<String> listdata, List<String> listid)
    {
        View dialog_layout = (LinearLayout) Charge_Settlement.this.getLayoutInflater().inflate(R.layout.customdialog_listview, null);
        customDialog_listViews = new CustomDialog_ListView(Charge_Settlement.this, R.style.MyDialog, dialog_layout, listdata, listid, new CustomDialog_ListView.CustomDialogListener()
        {
            @Override
            public void OnClick(Bundle bundle)
            {
                //id也是有的
                zzsl = bundle.getString("name");
                by_fzrid.setText(zzsl);
                byId = bundle.getString("id");

            }
        });
        customDialog_listViews.show();
    }

    //修改
    private void updatesellOrderSettlement(SellOrder_New mSellOrder)
    {
  /*      sellOrder.setInfoId(sellOrder_new.getUuid());
        sellOrder.setUid(sellOrder_new.getUid());
        sellOrder.setPlateNumber(plateNumber.getText().toString());//车车牌号
        sellOrder.setQualityWaterWeight(zp_ds_zhong.getText().toString());//正品带水重
        sellOrder.setQualityNetWeight(zp_bds_zhong.getText().toString());//正品净重
        sellOrder.setDefectWaterWeight(cp_ds_zhong.getText().toString());//次品带水重
        sellOrder.setDefectNetWeight(cp_jingzhong.getText().toString());//次品净重
        sellOrder.setPackPrice(bz_nc_danjia.getText().toString());//包装单价
        sellOrder.setCarryPrice(by_nc_danjia.getText().toString());//搬运单价
        sellOrder.setContractorId(bzId);
        sellOrder.setPickId(byId);*/
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("id", jsdid);
//        params.addQueryStringParameter("settlestatus", "2");
//        params.addQueryStringParameter("isNeedAudit", "2");
        params.addQueryStringParameter("contractorId", bzId);
        params.addQueryStringParameter("pickId", byId);
        params.addQueryStringParameter("uid", mSellOrder.getUid());
        params.addQueryStringParameter("carryPrice", mSellOrder.getCarryPrice());
        params.addQueryStringParameter("packPrice", mSellOrder.getPackPrice());
        params.addQueryStringParameter("infoId", mSellOrder.getInfoId());
        params.addQueryStringParameter("plateNumber", mSellOrder.getPlateNumber());

        params.addQueryStringParameter("qualityWaterWeight", mSellOrder.getQualityWaterWeight());
        params.addQueryStringParameter("qualityNetWeight", mSellOrder.getQualityNetWeight());
        params.addQueryStringParameter("defectWaterWeight", mSellOrder.getDefectNetWeight());
        params.addQueryStringParameter("defectNetWeight", mSellOrder.getDefectNetWeight());
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


                    } else
                    {
                        listData = new ArrayList<SellOrder_New>();
                    }

                } else
                {
                    AppContext.makeToast(Charge_Settlement.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(Charge_Settlement.this, "error_connectServer");

            }
        });
    }
}

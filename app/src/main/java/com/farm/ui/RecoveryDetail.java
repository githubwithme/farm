package com.farm.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.AllType;
import com.farm.bean.Purchaser;
import com.farm.bean.Result;
import com.farm.bean.SellOrder_New;
import com.farm.bean.SellOrder_New_First;
import com.farm.bean.commembertab;
import com.farm.common.utils;
import com.farm.widget.CustomDialog_Bean;
import com.farm.widget.CustomDialog_CallTip;
import com.farm.widget.CustomDialog_ListView;
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
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${hmj} on 2016/6/27.
 */
@EActivity(R.layout.recoverydetail)
public class RecoveryDetail extends Activity
{

    CustomDialog_ListView customDialog_listView;
    MyDialog myDialog;
    CustomDialog_Bean customDialog_bean;
    String zzsl = "";
    @ViewById
    TextView bz_fzr;
    @ViewById
    TextView by_fzr;
    @ViewById
    TextView cl;


    @ViewById
    TextView tv_bz;
    @ViewById
    TextView tv_by;
    @ViewById
    TextView tv_cheliang;
    @ViewById
    EditText chepai;

    @ViewById
            TextView is_ready;
    Purchaser purchaser;
    String cgId = "";
    String byId = "";
    String bzId = "";
    String fzrId = "";
    String cpid = "";
    SellOrder_New sellOrder_new = new SellOrder_New();

    List<Purchaser> listData_CG = new ArrayList<Purchaser>();
    List<Purchaser> listData_BY = new ArrayList<Purchaser>();
    List<Purchaser> listData_BZ = new ArrayList<Purchaser>();
    List<AllType> listAlltype = new ArrayList<AllType>();

    @AfterViews
    void afterOncreate()
    {

        if (sellOrder_new.getIsReady().equals("True"))
        {
            is_ready.setText("未就绪");
        }else
        {
            is_ready.setText("就绪");
        }

        getpurchaser();
//        showdata();
    }

    @Click
//准备就绪
    void btn_save()
    {
        showDeleteTip();

    }
    private void showDeleteTip()
    {

        View dialog_layout = (LinearLayout)RecoveryDetail.this.getLayoutInflater().inflate(R.layout.customdialog_callback, null);
        myDialog = new MyDialog(RecoveryDetail.this, R.style.MyDialog, dialog_layout, "订单", "确定删除吗?", "删除", "取消", new MyDialog.CustomDialogListener()
        {
            @Override
            public void OnClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.btn_sure:
                        SellOrder_New sellOrders = new SellOrder_New();
                        sellOrders = sellOrder_new;
                        sellOrders.setIsReady("True");
                        StringBuilder builder = new StringBuilder();
                        SellOrder_New_First sellOrder_new_first = new SellOrder_New_First();
                        builder.append("{\"SellOrder_new\":[ ");
                        builder.append(JSON.toJSONString(sellOrders));
                        builder.append("], \"sellorderlistadd\": [");
                        builder.append(JSON.toJSONString(sellOrder_new_first));
                        builder.append("]} ");
                        newaddOrder(builder.toString());
                        break;
                    case R.id.btn_cancle:
                        myDialog.cancel();
                        break;
                }
            }
        });
        myDialog.show();
    }
    @Click
    void btn_add()//保存车牌号
    {


        SellOrder_New sellOrders = new SellOrder_New();
        sellOrders = sellOrder_new;
        sellOrders.setPlateNumber(chepai.getText().toString());
        StringBuilder builder = new StringBuilder();
        SellOrder_New_First sellOrder_new_first = new SellOrder_New_First();
        builder.append("{\"SellOrder_new\":[ ");
        builder.append(JSON.toJSONString(sellOrders));
        builder.append("], \"sellorderlistadd\": [");
        builder.append(JSON.toJSONString(sellOrder_new_first));
        builder.append("]} ");
        newaddOrder(builder.toString());
    }

    @ViewById
    LinearLayout btn_addby;

    @ViewById
    LinearLayout btn_addbz;
    @ViewById
    TextView tv_bzg;

    @Click
    void tv_cheliang()
    {
        JSONObject jsonObject = utils.parseJsonFile(RecoveryDetail.this, "dictionary.json");
        JSONArray jsonArray = null;
        try
        {
            jsonArray = JSONArray.parseArray(jsonObject.getString("number"));
        } catch (Exception e)
        {

        }

        List<String> list = new ArrayList<String>();
        for (int i = 0; i < jsonArray.size(); i++)
        {
            list.add(jsonArray.getString(i));
        }
        showDialog_workday(list, sellOrder_new);
    }

    public void showDialog_workday(List<String> list, final SellOrder_New sellOrder_new)
    {
        View dialog_layout = (RelativeLayout) RecoveryDetail.this.getLayoutInflater().inflate(R.layout.customdialog_listview, null);
        customDialog_listView = new CustomDialog_ListView(RecoveryDetail.this, R.style.MyDialog, dialog_layout, list, list, new CustomDialog_ListView.CustomDialogListener()
        {
            @Override
            public void OnClick(Bundle bundle)
            {
                zzsl = bundle.getString("name");
                SellOrder_New sellOrders = new SellOrder_New();
                sellOrders = sellOrder_new;
                sellOrders.setActualweight(zzsl);
                StringBuilder builder = new StringBuilder();
                SellOrder_New_First sellOrder_new_first = new SellOrder_New_First();
                builder.append("{\"SellOrder_new\":[ ");
                builder.append(JSON.toJSONString(sellOrders));
                builder.append("], \"sellorderlistadd\": [");
                builder.append(JSON.toJSONString(sellOrder_new_first));
                builder.append("]} ");
                newaddOrder(builder.toString());
            }
        });
        customDialog_listView.show();
    }

    @Click
    void btn_addbz()
    {

        showDialog_bz(listData_BZ);
    }

    @Click
    void btn_addby()
    {

        showDialog_by(listData_BY);
    }

    //搬运
    public void showDialog_by(List<Purchaser> listData_BZ)
    {
        View dialog_layout = (RelativeLayout) RecoveryDetail.this.getLayoutInflater().inflate(R.layout.customdialog_listview, null);
        customDialog_bean = new CustomDialog_Bean(RecoveryDetail.this, R.style.MyDialog, dialog_layout, listData_BZ, new CustomDialog_Bean.CustomDialogListener()
        {
            @Override
            public void OnClick(Bundle bundle)
            {
                tv_by.setText("搬运工：已落实");
                //id也是有的
                zzsl = bundle.getString("name");

                purchaser = bundle.getParcelable("bean");
                tv_bzg.setText(":" + zzsl + "  电话:" + purchaser.getTelephone());
                SellOrder_New sellOrders = new SellOrder_New();
                sellOrders = sellOrder_new;
                sellOrders.setCreatorid(purchaser.getId());
                SellOrder_New_First sellOrder_new_first = new SellOrder_New_First();
                StringBuilder builder = new StringBuilder();
                builder.append("{\"SellOrder_new\":[ ");
                builder.append(JSON.toJSONString(sellOrders));
                builder.append("], \"sellorderlistadd\": [");
                builder.append(JSON.toJSONString(sellOrder_new_first));
                builder.append("]} ");
                newaddOrder(builder.toString());
            }
        });
        customDialog_bean.show();
    }

    //包装工
    public void showDialog_bz(List<Purchaser> listData_BZ)
    {
        View dialog_layout = (RelativeLayout) RecoveryDetail.this.getLayoutInflater().inflate(R.layout.customdialog_listview, null);
        customDialog_bean = new CustomDialog_Bean(RecoveryDetail.this, R.style.MyDialog, dialog_layout, listData_BZ, new CustomDialog_Bean.CustomDialogListener()
        {
            @Override
            public void OnClick(Bundle bundle)
            {
                tv_bz.setText("包装工：已落实");
                //id也是有的
                zzsl = bundle.getString("name");

                purchaser = bundle.getParcelable("bean");
                bz_fzr.setText("工头:" + zzsl + "  电话:" + purchaser.getTelephone());
                SellOrder_New sellOrders = new SellOrder_New();
                sellOrders = sellOrder_new;
                sellOrders.setPactId(purchaser.getId());
                SellOrder_New_First sellOrder_new_first = new SellOrder_New_First();
                StringBuilder builder = new StringBuilder();
                builder.append("{\"SellOrder_new\":[ ");
                builder.append(JSON.toJSONString(sellOrders));
                builder.append("], \"sellorderlistadd\": [");
                builder.append(JSON.toJSONString(sellOrder_new_first));
                builder.append("]} ");
                newaddOrder(builder.toString());
            }
        });
        customDialog_bean.show();
    }

    private void showdata()
    {
        if (!sellOrder_new.getContractorId().equals(""))
        {
            bz_fzr.setText("包装工：已落实" + ";" + sellOrder_new.getContractorName());
        } else
        {
            bz_fzr.setText("包装工：未落实" + ";");
        }
        if (!sellOrder_new.getPickId().equals(""))
        {
            by_fzr.setText("搬运工：已落实" + ";" + sellOrder_new.getPickName());
        } else
        {
            by_fzr.setText("搬运工：未落实" + ";");

        }
        cl.setText("车辆：" + sellOrder_new.getPlateNumber());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        sellOrder_new = getIntent().getParcelableExtra("zbstudio");
    }

    private void newaddOrder(String data)
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("action", "editOrder");
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
//                        Toast.makeText(RecoveryDetail.this, "订单修改成功！", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent();
//                        intent.setAction(AppContext.BROADCAST_DD_REFASH);
                        intent.setAction(AppContext.BROADCAST_UPDATEAllORDER);
                        RecoveryDetail.this.sendBroadcast(intent);

                    }

                } else
                {
                    AppContext.makeToast(RecoveryDetail.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(RecoveryDetail.this, "error_connectServer");
            }
        });
    }

    private void getpurchaser()
    {
        commembertab commembertab = AppContext.getUserInfo(RecoveryDetail.this);
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
                                    listData_CG.add(listNewData.get(i));
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
                    AppContext.makeToast(RecoveryDetail.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(RecoveryDetail.this, "error_connectServer");
            }
        });
    }
}

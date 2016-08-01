package com.farm.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.RecoveryDetail_Adapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.AllType;
import com.farm.bean.Purchaser;
import com.farm.bean.Result;
import com.farm.bean.SellOrder_New;
import com.farm.bean.commembertab;
import com.farm.widget.CustomDialog_Bean;
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
@EActivity(R.layout.editrecovery)
public class EditRecovery extends Activity
{
    RecoveryDetail_Adapter recoveryDetail_adapter;
    CustomDialog_ListView customDialog_listView;
    MyDialog myDialog;
    CustomDialog_Bean customDialog_bean;
    String zzsl = "";

    @ViewById
    EditText CR_chanpin;
    @ViewById
    TextView tv_bz;
    @ViewById
    TextView tv_by;

    @ViewById
    EditText packPrice;
    @ViewById
    EditText carryPrice;

    @ViewById
    Button button_add;

    Purchaser purchaser;
    String byId = "";
    String bzId = "";
    //    SellOrder_New sellOrder_new = new SellOrder_New();
    String uuid;
    @ViewById
    Button btn_save;


    List<Purchaser> listData_CG = new ArrayList<Purchaser>();
    List<Purchaser> listData_BY = new ArrayList<Purchaser>();
    List<Purchaser> listData_BZ = new ArrayList<Purchaser>();
    List<AllType> listAlltype = new ArrayList<AllType>();

    @AfterViews
    void afterOncreate()
    {
        getpurchaser();
    }


    @Click
//准备就绪
    void btn_save()
    {
        showDeleteTip();

    }

    private void showDeleteTip()
    {

        View dialog_layout = EditRecovery.this.getLayoutInflater().inflate(R.layout.customdialog_callback, null);
        myDialog = new MyDialog(EditRecovery.this, R.style.MyDialog, dialog_layout, "就绪", "所有准备工作就绪?", "确定", "取消", new MyDialog.CustomDialogListener()
        {
            @Override
            public void OnClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.btn_sure:
                        updateSellOrderByuuid();
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
    void button_add()
    {
        if (CR_chanpin.getText().toString().equals(""))
        {
            Toast.makeText(EditRecovery.this, "请填写车牌号", Toast.LENGTH_SHORT);
            return;
        }
        commembertab commembertab = AppContext.getUserInfo(EditRecovery.this);
        SellOrder_New sellOrder = new SellOrder_New();
        sellOrder.setInfoId(uuid);//0
        sellOrder.setPlateNumber(CR_chanpin.getText().toString());
        sellOrder.setPickId(bzId);
        sellOrder.setContractorId(byId);
        sellOrder.setUid(commembertab.getuId());//0
        sellOrder.setCarryPrice(carryPrice.getText().toString());
        sellOrder.setPackPrice(packPrice.getText().toString());
        sellOrder.setIsNeedAudit("2");
        sellOrder.setIsReady("False");
        StringBuilder builder = new StringBuilder();
        builder.append("{\"sellOrderSettlementlist\":[ ");
        builder.append(JSON.toJSONString(sellOrder));
        builder.append("]} ");
        isnewaddOrder(builder.toString());

    }


    @Click
    void tv_bz()
    {

        showDialog_bz(listData_BZ);
    }

    @Click
    void tv_by()
    {
        showDialog_by(listData_BY);
    }

    //搬运
    public void showDialog_by(List<Purchaser> listData_BZ)
    {
        View dialog_layout = EditRecovery.this.getLayoutInflater().inflate(R.layout.customdialog_listview, null);
        customDialog_bean = new CustomDialog_Bean(EditRecovery.this, R.style.MyDialog, dialog_layout, listData_BZ, new CustomDialog_Bean.CustomDialogListener()
        {
            @Override
            public void OnClick(Bundle bundle)
            {
                tv_by.setText("搬运工：已落实");
                //id也是有的
                zzsl = bundle.getString("name");
                tv_by.setText(zzsl);
                purchaser = bundle.getParcelable("bean");
                byId = purchaser.getId();

            }
        });
        customDialog_bean.show();
    }

    //包装工
    public void showDialog_bz(List<Purchaser> listData_BZ)
    {
        View dialog_layout = EditRecovery.this.getLayoutInflater().inflate(R.layout.customdialog_listview, null);
        customDialog_bean = new CustomDialog_Bean(EditRecovery.this, R.style.MyDialog, dialog_layout, listData_BZ, new CustomDialog_Bean.CustomDialogListener()
        {
            @Override
            public void OnClick(Bundle bundle)
            {
                //id也是有的
                zzsl = bundle.getString("name");
                tv_bz.setText(zzsl);
                purchaser = bundle.getParcelable("bean");
                bzId = purchaser.getId();
            }
        });
        customDialog_bean.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        uuid = getIntent().getStringExtra("uuid");
    }


    private void getpurchaser()
    {
        commembertab commembertab = AppContext.getUserInfo(EditRecovery.this);
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
                    AppContext.makeToast(EditRecovery.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(EditRecovery.this, "error_connectServer");
            }
        });
    }

    private void isnewaddOrder(String data)
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
                        Toast.makeText(EditRecovery.this, "添加成功！", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent = new Intent();
                        intent.setAction(AppContext.UPDATEMESSAGE_PGDETAIL_UPDATE_DINGDAN);
                        sendBroadcast(intent);
                    }

                } else
                {
                    AppContext.makeToast(EditRecovery.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(EditRecovery.this, "error_connectServer");
            }
        });
    }


    private void updateSellOrderByuuid()
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uuid", uuid);
        params.addQueryStringParameter("isReady", "1");
        params.addQueryStringParameter("action", "updateSellOrderByuuid");
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
                    AppContext.makeToast(EditRecovery.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(EditRecovery.this, "error_connectServer");

            }
        });
    }
}

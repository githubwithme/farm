package com.farm.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.farm.common.utils;
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
@EActivity(R.layout.recoverydetail)
public class RecoveryDetail extends Activity
{
    RecoveryDetail_Adapter recoveryDetail_adapter;
    CustomDialog_ListView customDialog_listView;
    MyDialog myDialog;
    CustomDialog_Bean customDialog_bean;
    String zzsl = "";
    @ViewById
    Button button_add;
    @ViewById
    RelativeLayout rl_nodatatip;
    @ViewById
    TextView is_ready;
    @ViewById
    ListView hlistview_scroll_list;
    Purchaser purchaser;
    String byId = "";
    String bzId = "";
    SellOrder_New sellOrder_new = new SellOrder_New();
    String uuid;
    @ViewById
    Button btn_isready;

    DialogFragment_WaitTip dialog;

    public void showDialog_waitTip()
    {
        dialog = new DialogFragment_WaitTip_();
        Bundle bundle1 = new Bundle();
        dialog.setArguments(bundle1);
        dialog.show(getFragmentManager(), "TIP");
    }
//    dialog.loadingTip(getText(R.string.error_data).toString());
//    dialog.loadingTip(getText(R.string.error_network).toString());

    List<Purchaser> listData_CG = new ArrayList<Purchaser>();
    List<Purchaser> listData_BY = new ArrayList<Purchaser>();
    List<Purchaser> listData_BZ = new ArrayList<Purchaser>();
    List<AllType> listAlltype = new ArrayList<AllType>();

    @Click
    void btn_back()
    {
        finish();
    }
    @AfterViews
    void afterOncreate()
    {
 /*       if (sellOrder_new.getIsReady().equals("False")) {
            btn_isready.setVisibility(View.VISIBLE);
        }*/
        showDialog_waitTip();
        getDetailSecBysettleId();
    }


    @Click
//准备就绪
    void btn_isready()
    {
        showDeleteTip();
    }

    private void showDeleteTip()
    {

        View dialog_layout = RecoveryDetail.this.getLayoutInflater().inflate(R.layout.customdialog_callback, null);
        myDialog = new MyDialog(RecoveryDetail.this, R.style.MyDialog, dialog_layout, "就绪", "所有准备工作就绪?", "确定", "取消", new MyDialog.CustomDialogListener()
        {
            @Override
            public void OnClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.btn_sure:
                        updateSellOrderByuuid("");
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
        Intent intent = new Intent(RecoveryDetail.this, AddRecovery_.class);
        intent.putExtra("uuid", uuid);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        IntentFilter intentfilter_update = new IntentFilter(AppContext.UPDATEMESSAGE_PGDETAIL_UPDATE_DINGDAN);
        registerReceiver(receiver_update, intentfilter_update);
        uuid = getIntent().getStringExtra("uuid");
        sellOrder_new = getIntent().getParcelableExtra("bean");
    }

    BroadcastReceiver receiver_update = new BroadcastReceiver()// 从扩展页面返回信息
    {
        @SuppressWarnings("deprecation")
        @Override
        public void onReceive(Context context, Intent intent)
        {
            getDetailSecBysettleId();
        }
    };


    public void getDetailSecBysettleId()
    {
        rl_nodatatip.setVisibility(View.VISIBLE);
        commembertab commembertab = AppContext.getUserInfo(RecoveryDetail.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uuid", uuid);
        params.addQueryStringParameter("action", "getDetailSecBysettleId");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<SellOrder_New> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    listNewData = JSON.parseArray(result.getRows().toJSONString(), SellOrder_New.class);
                    recoveryDetail_adapter = new RecoveryDetail_Adapter(RecoveryDetail.this, listNewData);
                    hlistview_scroll_list.setAdapter(recoveryDetail_adapter);
                    utils.setListViewHeight(hlistview_scroll_list);

//                    updateSellOrderByuuid(String.valueOf(listNewData.size()));
                    Intent intent = new Intent();
                    intent.setAction(AppContext.UPDATEMESSAGE_CHE_LIANG);
                    intent.putExtra("num", listNewData.size() + "");
                    sendBroadcast(intent);


                    if (listNewData.size() > 0)
                    {
                        rl_nodatatip.setVisibility(View.GONE);
                    } else
                    {
                        rl_nodatatip.setVisibility(View.VISIBLE);
                    }

                } else
                {
                    AppContext.makeToast(RecoveryDetail.this, "error_connectDataBase");
                    dialog.loadingTip(getText(R.string.error_data).toString());
                    return;
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(RecoveryDetail.this, "error_connectServer");
                dialog.loadingTip(getText(R.string.error_network).toString());
            }
        });
    }

    private void updateSellOrderByuuid(String num)
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uuid", uuid);
        params.addQueryStringParameter("isReady", "1");
        params.addQueryStringParameter("plateNumber", num);
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

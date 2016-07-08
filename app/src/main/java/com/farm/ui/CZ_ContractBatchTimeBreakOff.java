package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.Adapter_ContractBatchtimeBreakOff;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.BatchTime;
import com.farm.bean.Result;
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
 * Created by ${hmj} on 2016/7/4.
 */
@EActivity(R.layout.ncz_contractbatchtimebreakoff)
public class CZ_ContractBatchTimeBreakOff extends Activity
{
    String contractid;
    String contractname;
    @ViewById
    ListView lv;
    @ViewById
    TextView tv_note;
    Adapter_ContractBatchtimeBreakOff adapter_contractBatchtimeBreakOff;

    @AfterViews
    void afterOncreate()
    {
        getActionBar().hide();
        contractid = getIntent().getStringExtra("contractid");
        contractname = getIntent().getStringExtra("contractname");
        tv_note.setText(contractname + "的断蕾情况");
        getSaleDataOfArea();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    private void getSaleDataOfArea()
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("contractid", contractid);
        params.addQueryStringParameter("year", utils.getYear());
        params.addQueryStringParameter("action", "CZ_getContractBreakoffData");//jobGetList1
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<BatchTime> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), BatchTime.class);
                        adapter_contractBatchtimeBreakOff = new Adapter_ContractBatchtimeBreakOff(CZ_ContractBatchTimeBreakOff.this, listNewData);
                        lv.setAdapter(adapter_contractBatchtimeBreakOff);
                    } else
                    {
                        listNewData = new ArrayList<BatchTime>();
                    }

                } else
                {
                    AppContext.makeToast(CZ_ContractBatchTimeBreakOff.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(CZ_ContractBatchTimeBreakOff.this, "error_connectServer");
            }
        });
    }

}

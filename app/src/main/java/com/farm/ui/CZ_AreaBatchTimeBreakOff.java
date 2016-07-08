package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.Adapter_AreaBatchtimeBreakOff;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.contractTab;
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
@EActivity(R.layout.cz_areabatchtimebreakoff)
public class CZ_AreaBatchTimeBreakOff extends Activity
{
    String batchtime;
    String areaid;
    String areaname;
    @ViewById
    ListView lv;
    @ViewById
    TextView tv_note;
    Adapter_AreaBatchtimeBreakOff adapter_areaBatchtimeBreakOff;

    @AfterViews
    void afterOncreate()
    {
        getActionBar().hide();
        batchtime = getIntent().getStringExtra("batchtime");
        areaid = getIntent().getStringExtra("areaid");
        areaname = getIntent().getStringExtra("areaname");
        tv_note.setText(areaname +"     "+batchtime+ "批次的断蕾情况");
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
        params.addQueryStringParameter("areaid", areaid);
        params.addQueryStringParameter("batchtime", batchtime);
        params.addQueryStringParameter("year", utils.getYear());
        params.addQueryStringParameter("action", "CZ_getAreaBatchtimeBreakOffData");//jobGetList1
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<contractTab> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), contractTab.class);
                        adapter_areaBatchtimeBreakOff = new Adapter_AreaBatchtimeBreakOff(CZ_AreaBatchTimeBreakOff.this, listNewData);
                        lv.setAdapter(adapter_areaBatchtimeBreakOff);
                    } else
                    {
                        listNewData = new ArrayList<contractTab>();
                    }

                } else
                {
                    AppContext.makeToast(CZ_AreaBatchTimeBreakOff.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(CZ_AreaBatchTimeBreakOff.this, "error_connectServer");
            }
        });
    }

}

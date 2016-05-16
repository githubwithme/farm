package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.GridViewAdapter_SellOrDetail_NCZ;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.BatchTime;
import com.farm.bean.Result;
import com.farm.bean.SellOrderDetail_New;
import com.farm.bean.commembertab;
import com.farm.common.FileHelper;
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

import java.util.List;

/**
 * Created by ${hmj} on 2016/5/1.
 */
@EActivity(R.layout.ncz_sellorderdetail)
public class NCZ_SellOrderDetail extends Activity
{
    BatchTime batchTime;
    GridViewAdapter_SellOrDetail_NCZ gridViewAdapter_sellOrDetail_ncz;
    @ViewById
    TextView tv_title;
    @ViewById
    GridView gv;

    @Click
    void btn_sale()
    {
        int count = gv.getChildCount();
        for (int i = 0; i < count; i++)
        {
            LinearLayout linearlayout = (LinearLayout) gv.getChildAt(i);
        }
    }

    @AfterViews
    void afterOncreate()
    {
        tv_title.setText(batchTime.getBatchTime());
        getSellOrderDetailByBatchtime_temp();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        batchTime = getIntent().getParcelableExtra("bean");
    }
    private void getSellOrderDetailByBatchtime_temp()
    {
        List<SellOrderDetail_New> listNewData = FileHelper.getAssetsData(NCZ_SellOrderDetail.this, "getSellOrderDetailByBatchtime", SellOrderDetail_New.class);
        gridViewAdapter_sellOrDetail_ncz = new GridViewAdapter_SellOrDetail_NCZ(NCZ_SellOrderDetail.this, listNewData);
        gv.setAdapter(gridViewAdapter_sellOrDetail_ncz);
    }

    private void getSellOrderDetailByBatchtime()
    {
        commembertab commembertab = AppContext.getUserInfo(NCZ_SellOrderDetail.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("parkid", commembertab.getparkId());
        params.addQueryStringParameter("year", batchTime.getYear());
        params.addQueryStringParameter("batchTime", batchTime.getBatchTime());
        params.addQueryStringParameter("action", "getSellOrderDetailByBatchtime");
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
                        gridViewAdapter_sellOrDetail_ncz = new GridViewAdapter_SellOrDetail_NCZ(NCZ_SellOrderDetail.this, listNewData);
                        gv.setAdapter(gridViewAdapter_sellOrDetail_ncz);
                    } else
                    {
                    }

                } else
                {
                    AppContext.makeToast(NCZ_SellOrderDetail.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_SellOrderDetail.this, "error_connectServer");

            }
        });
    }
}

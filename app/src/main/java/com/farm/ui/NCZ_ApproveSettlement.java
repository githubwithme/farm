package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.Adapter_NCZApproveSettlement;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.SellOrder_New;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Iterator;
import java.util.List;

/**
 * Created by shuwenouwan on 2016/8/6.
 */
@EActivity(R.layout.ncz_approvesettlement)
public class NCZ_ApproveSettlement extends Activity
{
    Adapter_NCZApproveSettlement adapter_nczApproveSettlement;
    List<SellOrder_New> listNewData = null;
    SellOrder_New sellOrder_new;
    @ViewById
    ListView lv;

    @AfterViews
    void afterview()
    {
        getDetailSecBysettleId();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        sellOrder_new = getIntent().getParcelableExtra("zbstudio");
        getActionBar().hide();
    }

    public void getDetailSecBysettleId()
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uuid", sellOrder_new.getUuid());
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

                    Iterator<SellOrder_New> it = listNewData.iterator();
                    while (it.hasNext())
                    {
                        SellOrder_New sellordersd = it.next();
                        if (sellordersd.getIsNeedAudit().equals("0")||sellordersd.getSettlestatus().equals("0"))
                        {

                        }else
                        {
                            it.remove(); }
                    }
                    adapter_nczApproveSettlement = new Adapter_NCZApproveSettlement(NCZ_ApproveSettlement.this, listNewData);
                    lv.setAdapter(adapter_nczApproveSettlement);
                } else
                {
                    AppContext.makeToast(NCZ_ApproveSettlement.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_ApproveSettlement.this, "error_connectServer");
            }
        });
    }
}

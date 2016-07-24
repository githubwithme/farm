package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.Adapter_PGCommandProgress;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.ContractCommandProgressBean;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.common.FileHelper;
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
 * Created by ${hmj} on 2016/7/12.
 */
@EActivity(R.layout.pg_commandprogress)
public class PG_CommandProgress extends Activity
{
    List<ContractCommandProgressBean> listNewData = null;
    Adapter_PGCommandProgress adapter_pgCommandProgress;
    @ViewById
    ListView lv;
    
    @AfterViews
    void AfterOncreate()
    {
        getNewSaleList_test();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
    }


    private void getNewSaleList_test()
    {
        listNewData = FileHelper.getAssetsData(PG_CommandProgress.this, "getContractProgress", ContractCommandProgressBean.class);
        if (listNewData != null)
        {
            adapter_pgCommandProgress = new Adapter_PGCommandProgress(PG_CommandProgress.this, listNewData);
            lv.setAdapter(adapter_pgCommandProgress);
        }

    }
    private void getContractList()
    {
        commembertab commembertab = AppContext.getUserInfo(PG_CommandProgress.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("workuserid", commembertab.getId());
        params.addQueryStringParameter("userid", commembertab.getId());
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("username", commembertab.getuserName());
        params.addQueryStringParameter("orderby", "regDate desc");
        params.addQueryStringParameter("strWhere", "zt:1");
        params.addQueryStringParameter("action", "commandGetList");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onFailure(HttpException e, String s)
            {
                AppContext.makeToast(PG_CommandProgress.this, "error_connectServer");
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), ContractCommandProgressBean.class);
                        adapter_pgCommandProgress = new Adapter_PGCommandProgress(PG_CommandProgress.this, listNewData);
                        lv.setAdapter(adapter_pgCommandProgress);
                    } else
                    {
                        listNewData = new ArrayList<ContractCommandProgressBean>();
                    }
                } else
                {
                    AppContext.makeToast(PG_CommandProgress.this, "error_connectDataBase");
                    return;
                }

            }
        });
    }
}

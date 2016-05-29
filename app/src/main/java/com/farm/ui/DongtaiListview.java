package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.DongtaiListviewAdapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.DynamicEntity;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
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
import java.util.Iterator;
import java.util.List;

/**
 * Created by hasee on 2016/5/29.
 */
@EActivity(R.layout.dongtailistview)
public class DongtaiListview extends Activity {
    DongtaiListviewAdapter dongtaiListviewAdapter;
    @ViewById
    ListView listviewss;

    @AfterViews
    void aftetcreate()
    {
        getEventList();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private void getEventList()
    {

        commembertab commembertab = AppContext.getUserInfo(DongtaiListview.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("userid", commembertab.getId());
        params.addQueryStringParameter("action", "GetDynamicData");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String a = responseInfo.result;
                List<DynamicEntity> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() > 0) {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), DynamicEntity.class);
                        dongtaiListviewAdapter=new DongtaiListviewAdapter(DongtaiListview.this,listNewData);
                        listviewss.setAdapter(dongtaiListviewAdapter);


                    }
                } else {
                    AppContext.makeToast(DongtaiListview.this, "error_connectDataBase");

                    return;
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                String a = error.getMessage();
                AppContext.makeToast(DongtaiListview.this, "error_connectServer");

            }
        });
    }
}

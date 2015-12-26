package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.addgcd)
public class AddGcd extends Activity
{
    @ViewById
    Button imgbtn_back;
    @ViewById
    EditText et_plantName;
    @ViewById
    EditText et_plantNote;

    @Click
    void imgbtn_back()
    {
        finish();
    }


    @Click
    void btn_upload()
    {
        AddData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
    }

    private void AddData()
    {
        commembertab commembertab = AppContext.getUserInfo(this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("userid", commembertab.getId());
        params.addQueryStringParameter("userName", commembertab.getrealName());
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("parkId", commembertab.getparkId());
        params.addQueryStringParameter("parkName", commembertab.getparkName());
        params.addQueryStringParameter("areaId", commembertab.getareaId());
        params.addQueryStringParameter("areaName", commembertab.getareaName());
        params.addQueryStringParameter("action", "plantGCDAdd");

        params.addQueryStringParameter("gcdName", et_plantName.getText().toString());
        params.addQueryStringParameter("gcdNote", et_plantNote.getText().toString());

        params.addQueryStringParameter("x", "112.25482264");
        params.addQueryStringParameter("y", "23.548768");
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
                        Toast.makeText(AddGcd.this, "保存成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    } else
                    {
                        AppContext.makeToast(AddGcd.this, "error_connectDataBase");
                    }
                } else
                {
                    AppContext.makeToast(AddGcd.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException arg0, String arg1)
            {
                AppContext.makeToast(AddGcd.this, "error_connectServer");
                AppContext.makeToast(AddGcd.this, "error_connectServer");
            }
        });
    }

}

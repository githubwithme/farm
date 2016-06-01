package com.farm.ui;

import android.app.Activity;
import android.content.Intent;
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
import com.media.HomeFragmentActivity;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.addplant)
public class AddPlant extends Activity
{
    @ViewById
    Button btn_upload;
    @ViewById
    EditText et_plantName;
    @ViewById
    EditText et_plantNote;
    String areaid;
    String gcdid;
    String gcdName;


    @Click
    void imgbtn_back()
    {
        finish();
    }
    @Click
    void imgbtn_addpicture()
    {
        Intent intent = new Intent(AddPlant.this, HomeFragmentActivity.class);
        intent.putExtra("type", "picture");
        intent.putExtra("From", "plant");
        startActivity(intent);
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
        gcdid = getIntent().getStringExtra("gcdid");
        gcdName = getIntent().getStringExtra("gcdName");
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
        params.addQueryStringParameter("gcdId", gcdid);
        params.addQueryStringParameter("gcdName", gcdName);
        params.addQueryStringParameter("areaName", commembertab.getareaName());
        params.addQueryStringParameter("action", "plantAdd");

        params.addQueryStringParameter("plantName", et_plantName.getText().toString());
        params.addQueryStringParameter("plantNote", et_plantNote.getText().toString());
        params.addQueryStringParameter("plantType", "0");

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
                        Toast.makeText(AddPlant.this, "保存成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    } else
                    {
                        AppContext.makeToast(AddPlant.this, "error_connectDataBase");
                    }
                } else
                {
                    AppContext.makeToast(AddPlant.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException arg0, String arg1)
            {
                AppContext.makeToast(AddPlant.this, "error_connectServer");
                AppContext.makeToast(AddPlant.this, "error_connectServer");
            }
        });
    }

}

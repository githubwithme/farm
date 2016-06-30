package com.farm.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Purchaser;
import com.farm.bean.Result;
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

/**
 * Created by hasee on 2016/6/28.
 */
@EActivity(R.layout.add_workpeopel)
public class Add_workPeopel extends Activity
{

    String type;
    Purchaser purchaser;
    @ViewById
    TextView types;
    @ViewById
    EditText add_name;
    @ViewById
    EditText add_phone;
    @ViewById
    EditText add_email;
    @ViewById
    EditText add_address;

    @Click
    void add_save()
    {
        if (types.getText().toString().equals(""))
        {
            Toast.makeText(Add_workPeopel.this, "请先填写人员类别", Toast.LENGTH_SHORT).show();
            return;
        }
        if (add_name.getText().toString().equals(""))
        {
            Toast.makeText(Add_workPeopel.this, "请填写人员姓名", Toast.LENGTH_SHORT).show();
            return;
        }
        if (add_phone.getText().toString().equals(""))
        {
            Toast.makeText(Add_workPeopel.this, "请填写人员电话", Toast.LENGTH_SHORT).show();
            return;
        }
        purchaser=new Purchaser();
        purchaser.setUserType(types.getText().toString());
        purchaser.setName(add_name.getText().toString());
        purchaser.setTelephone(add_phone.getText().toString());
        purchaser.setMailbox(add_email.getText().toString());
        purchaser.setAddress(add_address.getText().toString());
        StringBuilder builder = new StringBuilder();
        builder.append("{\"purchaser\": [");
        builder.append(JSON.toJSONString(purchaser));
        builder.append("]} ");
        addOrder( builder.toString());
    }
    @AfterViews
    void afterview()
    {
        types.setText(type);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        type = getIntent().getStringExtra("type");
    }

    private void addOrder( String data)
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("action", "addpurchaser");
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
                        Toast.makeText(Add_workPeopel.this, "添加成功！", Toast.LENGTH_SHORT).show();

                        finish();
                    }

                } else
                {
                    AppContext.makeToast(Add_workPeopel.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(Add_workPeopel.this, "error_connectServer");
            }
        });
    }
}

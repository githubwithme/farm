package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
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

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by hasee on 2016/6/13.
 */
@EActivity(R.layout.pg_workorder)
public class PG_WorkOrder extends Activity
{

    @ViewById
    EditText fuzeren;
    @ViewById
    EditText gd_num;
    @ViewById
    EditText gd_xm;
    @ViewById
    EditText gd_yq;
    @ViewById
    EditText gd_money;
    @ViewById
    EditText gd_note;
    @ViewById
    CheckBox gd_df;
    commembertab commembertab;

    @AfterViews
    void afterview()
    {

    }

    @Click
    void btn_upload()
    {
        saveData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        getActionBar().hide();
        commembertab = AppContext.getUserInfo(this);
    }

    private void saveData()
    {
        String check = "";
        if (gd_df.isChecked())
        {
            check = gd_df.getText().toString();
        } else
        {
            check = "无";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        RequestParams params = new RequestParams();
//        params.addQueryStringParameter("eventId", commembertab.getId());
        params.addQueryStringParameter("reportorId", commembertab.getId());
        params.addQueryStringParameter("reportor", commembertab.getrealName());
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("action", "eventRecordAdd");
        params.addQueryStringParameter("fuzeren", fuzeren.getText().toString());
        params.addQueryStringParameter("gd_num", gd_num.getText().toString());
        params.addQueryStringParameter("gd_xm", gd_xm.getText().toString());
        params.addQueryStringParameter("gd_yq", gd_yq.getText().toString());
        params.addQueryStringParameter("gd_money", gd_money.getText().toString());
        params.addQueryStringParameter("gd_note", gd_note.getText().toString());
        params.addQueryStringParameter("gd_money", check);

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
//                List<ReportedBean> listData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
//                    listData = JSON.parseArray(result.getRows().toJSONString(), ReportedBean.class);


                }
            }

            @Override
            public void onFailure(HttpException error, String arg1)
            {
                String a = error.getMessage();
                AppContext.makeToast(PG_WorkOrder.this, "error_connectServer");
            }
        });
    }
}

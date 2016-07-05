package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.SellOrderDetail_New;
import com.farm.bean.commembertab;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.androidannotations.annotations.EActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hasee on 2016/6/24.
 */
//@EActivity(R.layout.pg_jsd_cbh)
//@EActivity(R.layout.pg_jsd_add)
@EActivity(R.layout.pg_jsd_adapter)
public class PG_JSD_CBH extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
    }


}

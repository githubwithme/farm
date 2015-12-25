package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.bean.PlantGcjl;
import com.farm.bean.Result;
import com.farm.bean.plantgrowthtab;
import com.farm.common.utils;
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

@EActivity(R.layout.activity_observation_record)
public class ObservationRecordActivity extends Activity
{
    List<PlantGcjl> lsitNewData = null;
    String gcid;
    @ViewById
    RelativeLayout rl_pb;
    @ViewById
    LinearLayout ll_tip;
    @ViewById
    ProgressBar pb;
    @ViewById
    TextView tv_tip;
    @ViewById
    TextView tv_szq;
    @ViewById
    ImageButton imgbtn_back;

    @Click
    void imgbtn_back()
    {
        finish();
    }

    @AfterViews
    void afterOncreate()
    {
        getCommandlist();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        gcid = getIntent().getStringExtra("gcid");
    }

    private void getCommandlist()
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("gcjlid", gcid);
        params.addQueryStringParameter("action", "getGCJLByID");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)
                {
                    JSONObject jsonObject = utils.parseJsonFile(ObservationRecordActivity.this, "dictionary.json");
                    String aa = jsonObject.getString("gcjl");
                    lsitNewData = JSON.parseArray(JSON.parseObject(jsonObject.getString("gcjl"), Result.class).getRows().toJSONString(), PlantGcjl.class);
                    if (lsitNewData != null)
                    {
                        showData();
                    }
//                    if (result.getAffectedRows() != 0)
//                    {
//                        lsitNewData = JSON.parseArray(result.getRows().toJSONString(), PlantGcjl.class);
//                        if (lsitNewData != null)
//                        {
//                            showData();
//                        }
//
//                    } else
//                    {
//                        ll_tip.setVisibility(View.VISIBLE);
//                        tv_tip.setText("暂无数据！");
//                        pb.setVisibility(View.GONE);
//                    }
                } else
                {
                    ll_tip.setVisibility(View.VISIBLE);
                    tv_tip.setText("数据加载异常！");
                    pb.setVisibility(View.GONE);
                    return;
                }
            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                ll_tip.setVisibility(View.VISIBLE);
                tv_tip.setText("网络连接异常！");
                pb.setVisibility(View.GONE);
            }
        });
    }

    private void showData()
    {
        plantgrowthtab plantgrowthtab = lsitNewData.get(0).getPlantGrowth().get(1);
        tv_szq.setText(plantgrowthtab.getyNum());
    }
}

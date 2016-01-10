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
import com.farm.R;
import com.farm.adapter.AddPlantObservation_stepFive_bx_Adapter;
import com.farm.adapter.AddPlantObservation_stepFive_zz_Adapter;
import com.farm.app.AppConfig;
import com.farm.bean.Dictionary;
import com.farm.bean.FJ_SCFJ;
import com.farm.bean.PlantGcjl;
import com.farm.bean.Result;
import com.farm.bean.plantgrowthtab;
import com.farm.common.utils;
import com.farm.widget.CustomExpandableListView;
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

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_observation_record)
public class ObservationRecordActivity extends Activity
{
    List<FJ_SCFJ> list_fj_scfj = new ArrayList<>();
    AddPlantObservation_stepFive_bx_Adapter addPlantObservation_stepFive_bx_adapter;
    AddPlantObservation_stepFive_zz_Adapter addPlantObservation_stepFive_zz_adapter;
    Dictionary dic;
    List<plantgrowthtab> list_plantgrowthtab = new ArrayList<>();
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
    TextView tv_gcsj;
    @ViewById
    TextView tv_gcq;
    @ViewById
    ImageButton imgbtn_back;
    @ViewById
    CustomExpandableListView expanded_bx;
    @ViewById
    CustomExpandableListView expanded_zz;

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
                    if (result.getAffectedRows() != 0)
                    {
                        lsitNewData = JSON.parseArray(result.getRows().toJSONString(), PlantGcjl.class);
                        if (lsitNewData != null)
                        {
                            showData(lsitNewData.get(0));
                        }

                    } else
                    {
                        ll_tip.setVisibility(View.VISIBLE);
                        tv_tip.setText("暂无数据！");
                        pb.setVisibility(View.GONE);
                    }
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

    private void showData(PlantGcjl plantGcjl)
    {
        tv_gcsj.setText(lsitNewData.get(0).getRegDate());
        tv_gcq.setText(lsitNewData.get(0).getGcq());

//        dic = dictionary;
        addPlantObservation_stepFive_bx_adapter = new AddPlantObservation_stepFive_bx_Adapter(ObservationRecordActivity.this, dic, expanded_bx);
        expanded_bx.setAdapter(addPlantObservation_stepFive_bx_adapter);
        utils.setListViewHeight(expanded_bx);

        addPlantObservation_stepFive_zz_adapter = new AddPlantObservation_stepFive_zz_Adapter(ObservationRecordActivity.this, expanded_zz, list_plantgrowthtab, list_fj_scfj);
        expanded_zz.setAdapter(addPlantObservation_stepFive_zz_adapter);
        utils.setListViewHeight(expanded_zz);

//        PlantObservationAdapter addPlantObservationAdapter = new PlantObservationAdapter(ObservationRecordActivity.this, lsitNewData.get(0).getPlantGrowth());
//        lv.setAdapter(addPlantObservationAdapter);
    }
}

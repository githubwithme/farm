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
import com.farm.adapter.PlantObservationAdapter;
import com.farm.app.AppConfig;
import com.farm.bean.PlantGcjl;
import com.farm.bean.Result;
import com.farm.widget.CustomListView;
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
    TextView tv_jj;
    @ViewById
    TextView tv_y;
    @ViewById
    TextView tv_gx;
    @ViewById
    TextView tv_gcq;
    @ViewById
    ImageButton imgbtn_back;
    @ViewById
    CustomListView lv;

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
                            showData();
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

    private void showData()
    {
        tv_gcq.setText(lsitNewData.get(0).getGcq());
        tv_jj.setText(lsitNewData.get(0).getJjbx());
        tv_y.setText(lsitNewData.get(0).getYbx());
        tv_gx.setText(lsitNewData.get(0).getGxbx());

        PlantObservationAdapter addPlantObservationAdapter = new PlantObservationAdapter(ObservationRecordActivity.this, lsitNewData.get(0).getPlantGrowth());
        lv.setAdapter(addPlantObservationAdapter);
    }
}

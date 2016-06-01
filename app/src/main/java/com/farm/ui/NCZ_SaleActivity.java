package com.farm.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.ChartEntity;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.common.SortComparator;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ${hmj} on 2016/5/25.
 */
@EActivity(R.layout.ncz_saleactivity)
public class NCZ_SaleActivity extends Activity
{
    @ViewById
    BarChart chart_bar;
    List<ChartEntity> list_leftnumber = new ArrayList<>();

    @Click
    void btn_back()
    {
        finish();
    }

    @AfterViews
    void afterview()
    {
        chart_bar.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart_bar.setData(generateDataBar());
        getListData_left();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
    }

    public void init()
    {

    }

    private ArrayList<String> generateItem(List<ChartEntity> list)
    {
        ArrayList<String> m = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++)
        {
            m.add(list.get(i).getItem());
        }
        return m;
    }

    private BarData generateDataBar()
    {
        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        for (int i = 0; i < list_leftnumber.size(); i++)
        {
            entries.add(new BarEntry(Float.valueOf(list_leftnumber.get(i).getNumber()), i));
        }
        BarDataSet d1 = new BarDataSet(entries, "物资剩余量/kg");
        d1.setBarSpacePercent(20f);
        d1.setColor(Color.rgb(255, 0, 255));
        d1.setHighLightAlpha(255);
        d1.setStackLabels(new String[1]);
        d1.setValueTextSize(12);


        ArrayList<BarDataSet> sets = new ArrayList<BarDataSet>();
        sets.add(d1);
        BarData cd = new BarData(generateItem(list_leftnumber), sets);
        return cd;
    }


    private void getListData_left()
    {
        commembertab commembertab = AppContext.getUserInfo(NCZ_SaleActivity.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("action", "getStorehouseByUid");
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
                        list_leftnumber = JSON.parseArray(result.getRows().toJSONString(), ChartEntity.class);
                        Comparator comp = new SortComparator();//排序
                        Collections.sort(list_leftnumber, comp);
                        XAxis xAxis = chart_bar.getXAxis();
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        chart_bar.setData(generateDataBar());
                        chart_bar.invalidate();
                    } else
                    {
                        list_leftnumber = new ArrayList<ChartEntity>();
                    }
                } else
                {
                    AppContext.makeToast(NCZ_SaleActivity.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_SaleActivity.this, "error_connectServer");
            }
        });
    }


}

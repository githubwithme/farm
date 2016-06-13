package com.farm.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.ChartEntity;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.bean.parktab;
import com.farm.chart.ChartItem;
import com.farm.common.utils;
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
import java.util.List;

/**
 * Created by ${hmj} on 2016/5/25.
 */
@EActivity(R.layout.ncz_salechart)
public class NCZ_SaleChart extends Activity
{
    List<parktab> listNewData = new ArrayList<>();
    @ViewById
    BarChart chart_bar;

    @Click
    void btn_detail()
    {
        Intent intent = new Intent(NCZ_SaleChart.this, NCZ_FarmSale_.class);
        startActivity(intent);
    }

    @Click
    void btn_createorders()
    {
        Intent intent = new Intent(NCZ_SaleChart.this, NCZ_CreateOrder_SelectBatchTime_.class);
        startActivity(intent);
    }


    @Click
    void btn_orders()
    {
        Intent intent = new Intent(NCZ_SaleChart.this, NCZ_OrderManager_.class);
        startActivity(intent);
    }

    @Click
    void btn_back()
    {
        finish();
    }

    @AfterViews
    void afterview()
    {
        chart_bar.setDescription("");
        chart_bar.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart_bar.setData(generateDataBar());
        chart_bar.setDrawValueAboveBar(false);
        getBatchTimeByUid();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
    }

    public void init()
    {
//        ArrayList<ChartItem> list = new ArrayList<ChartItem>();
//        list.add(new BarChartItem(R.layout.goodsanalysis, generateDataBar(), GoodsAnalysisActivity.this.getApplicationContext()));
////        list.add(new LineChartItem(R.layout.saleanalysis_linechart, generateDataLine(), GoodsAnalysis.this.getApplicationContext()));
//        ChartDataAdapter cda = new ChartDataAdapter(GoodsAnalysisActivity.this.getApplicationContext(), list);
//        listView1.setAdapter(cda);
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
//        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
//        for (int i = 0; i < listNewData.size(); i++)
//        {
//            entries.add(new BarEntry(Float.valueOf(listNewData.get(i).getNumber()), i));
//        }
//        BarDataSet d1 = new BarDataSet(entries, "物资剩余量/kg");
//        d1.setBarSpacePercent(20f);
//        d1.setColor(Color.rgb(255, 0, 255));
//        d1.setHighLightAlpha(255);
//        d1.setStackLabels(new String[1]);
//        d1.setValueTextSize(12);

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();


        for (int i = 0; i < listNewData.size(); i++)
        {
            parktab parktab = listNewData.get(i);
            yVals1.add(new BarEntry(new float[]{Float.valueOf(parktab.getAllsaleout()), Float.valueOf(parktab.getAllsalein()), Float.valueOf(parktab.getAllsalefor())}, i));
        }
        ArrayList<String> listitem = new ArrayList<String>();
        for (int i = 0; i < listNewData.size(); i++)
        {
            listitem.add(listNewData.get(i).getparkName());
        }

        BarDataSet set1 = new BarDataSet(yVals1, "单位:株");
        set1.setColors(getColors());
        set1.setStackLabels(new String[]{"已售", "售中", "待售"});

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);

        BarData cd = new BarData(listitem, set1);
        cd.setValueTextSize(16);
        cd.setValueTextColor(getResources().getColor(R.color.white));
//        data.setValueFormatter(new MyValueFormatter());


//        ArrayList<BarDataSet> sets = new ArrayList<BarDataSet>();
//        sets.add(d1);
//        BarData cd = new BarData(generateItem(list_leftnumber), sets);
        return cd;
    }


    private class ChartDataAdapter extends ArrayAdapter<ChartItem>
    {

        public ChartDataAdapter(Context context, List<ChartItem> objects)
        {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            return getItem(position).getView(position, convertView, getContext());
        }

        @Override
        public int getItemViewType(int position)
        {
            // return the views type
            return getItem(position).getItemType();
        }

        @Override
        public int getViewTypeCount()
        {
            return 3; // we have 3 different item-types
        }
    }


    private int[] getColors()
    {
//        int stacksize = 3;
//        int[] colors = new int[stacksize];
//        for (int i = 0; i < stacksize; i++)
//        {
//            colors[i] = ColorTemplate.VORDIPLOM_COLORS[i];
//        }
        int[] colors = new int[]{Color.rgb(255, 0, 0), Color.rgb(0, 0, 255), Color.rgb(0, 255, 0)};
        return colors;
    }

    private void getBatchTimeByUid()
    {
        commembertab commembertab = AppContext.getUserInfo(NCZ_SaleChart.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("year", utils.getYear());
        params.addQueryStringParameter("action", "getBatchTimeByUid");//jobGetList1
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), parktab.class);
                        chart_bar.setData(generateDataBar());
                        chart_bar.invalidate();
                    } else
                    {
                        listNewData = new ArrayList<parktab>();
                    }

                } else
                {
                    AppContext.makeToast(NCZ_SaleChart.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_SaleChart.this, "error_connectServer");
            }
        });
    }

    private void cancleOrder()
    {
        commembertab commembertab = AppContext.getUserInfo(NCZ_SaleChart.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("year", utils.getYear());
        params.addQueryStringParameter("action", "cancleOrder");//jobGetList1
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), parktab.class);
                        chart_bar.setData(generateDataBar());
                        chart_bar.invalidate();
                    } else
                    {
                        listNewData = new ArrayList<parktab>();
                    }

                } else
                {
                    AppContext.makeToast(NCZ_SaleChart.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_SaleChart.this, "error_connectServer");
            }
        });
    }
}

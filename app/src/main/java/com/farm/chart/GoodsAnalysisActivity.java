package com.farm.chart;

import android.app.Activity;
import android.content.Context;
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
import com.farm.common.SortComparator;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
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
@EActivity(R.layout.goodsanalysisactivity_new)
public class GoodsAnalysisActivity extends Activity
{
    @ViewById
    BarChart chart_bar;
    @ViewById
    LineChart chart_line;
    List<ChartEntity> list_leftnumber = null;
    List<ChartEntity> list_outnumber = null;

    @Click
    void btn_back()
    {
        finish();
    }

    @AfterViews
    void afterview()
    {
        getListData_left();
        getListData_out();
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

//        BarDataSet d2 = new BarDataSet(entries, "待处理事件");
//        d2.setBarSpacePercent(20f);
//        d2.setColor(Color.rgb(0, 255, 255));
//        d2.setHighLightAlpha(255);
//        d2.setStackLabels(new String[1]);
//        d2.setValueTextSize(12);
//
//        BarDataSet d3 = new BarDataSet(entries, "处理中事件");
//        d3.setBarSpacePercent(20f);
//        d3.setColor(Color.rgb(255, 255, 0));
//        d3.setHighLightAlpha(255);
//        d3.setStackLabels(new String[1]);
//        d3.setValueTextSize(12);


        ArrayList<BarDataSet> sets = new ArrayList<BarDataSet>();
        sets.add(d1);
//        sets.add(d2);
//        sets.add(d3);
        BarData cd = new BarData(generateItem(list_leftnumber), sets);
        return cd;
    }

    private LineData generateDataLine_used()
    {
        ArrayList<Entry> e1 = new ArrayList<Entry>();
        for (int i = 0; i < list_outnumber.size(); i++)
        {
            e1.add(new Entry(Float.valueOf(list_outnumber.get(i).getNumber()), i));
        }
        LineDataSet d1 = new LineDataSet(e1, "销售");
        d1.setLineWidth(1.8f);
        d1.setCircleSize(3.6f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setDrawValues(false);
        d1.setValueTextSize(20f);

//        ArrayList<Entry> e2 = new ArrayList<Entry>();
//        for (int i = 0; i < listNewData.size(); i++)
//        {
//            e2.add(new Entry(Float.valueOf(listNewData.get(i).getNumber()), i));
//        }
//        LineDataSet d2 = new LineDataSet(e2, "异常植株");
//        d2.setLineWidth(5f);
//        d2.setCircleSize(3.5f);
//        d2.setHighLightColor(Color.rgb(244, 117, 117));
//        d2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
//        d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
//        d2.setDrawValues(false);
//        d2.setValueTextSize(20f);


        ArrayList<LineDataSet> sets = new ArrayList<LineDataSet>();
        sets.add(d1);
//        sets.add(d2);

        LineData cd = new LineData(generateItem(list_outnumber), sets);
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

    private void getListData_left()
    {
        commembertab commembertab = AppContext.getUserInfo(GoodsAnalysisActivity.this);
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
                    } else
                    {
                        list_leftnumber = new ArrayList<ChartEntity>();
                    }
                } else
                {
                    AppContext.makeToast(GoodsAnalysisActivity.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(GoodsAnalysisActivity.this, "error_connectServer");
            }
        });
    }

    private void getListData_out()
    {
        commembertab commembertab = AppContext.getUserInfo(GoodsAnalysisActivity.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("action", "getgoodsOutSumByUid");
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
                        list_outnumber = JSON.parseArray(result.getRows().toJSONString(), ChartEntity.class);
                        Comparator comp = new SortComparator();//排序
                        Collections.sort(list_outnumber, comp);
                        XAxis xAxis = chart_line.getXAxis();
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        chart_line.setData(generateDataLine_used());

                    } else
                    {
                        list_outnumber = new ArrayList<ChartEntity>();
                    }
                } else
                {
                    AppContext.makeToast(GoodsAnalysisActivity.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(GoodsAnalysisActivity.this, "error_connectServer");
            }
        });
    }
}

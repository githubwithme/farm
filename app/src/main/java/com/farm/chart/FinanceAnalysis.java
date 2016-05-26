package com.farm.chart;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.ChartEntity;
import com.farm.common.FileHelper;
import com.farm.common.SortComparator;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

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
@EActivity(R.layout.financeanalysisactivity)
public class FinanceAnalysis extends Activity
{
    @ViewById
    ListView listView1;
    List<ChartEntity> listNewData = null;
    @ViewById
    TextView tv_title;

    @Click
    void btn_back()
    {
        finish();
    }

    @AfterViews
    void afterview()
    {
        listNewData = FileHelper.getAssetsData(FinanceAnalysis.this, "getAnalysisData", ChartEntity.class);
        Comparator comp = new SortComparator();//排序
        Collections.sort(listNewData, comp);
        init();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
    }

    public void init()
    {
        ArrayList<ChartItem> list = new ArrayList<ChartItem>();
        list.add(new BarChartItem(R.layout.goodsanalysis, generateDataBar(), FinanceAnalysis.this.getApplicationContext()));
        ChartDataAdapter cda = new ChartDataAdapter(FinanceAnalysis.this.getApplicationContext(), list);
        listView1.setAdapter(cda);

    }

    private ArrayList<String> generateItem()
    {
        ArrayList<String> m = new ArrayList<String>();
        for (int i = 0; i < listNewData.size(); i++)
        {
            m.add(listNewData.get(i).getItem());
        }
        return m;
    }

    private BarData generateDataBar()
    {
        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        for (int i = 0; i < listNewData.size(); i++)
        {
            entries.add(new BarEntry(Float.valueOf(listNewData.get(i).getNumber()), i));
        }
        BarDataSet d1 = new BarDataSet(entries, "物资 ");
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
        BarData cd = new BarData(generateItem(), sets);
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
}

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
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

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
@EActivity(R.layout.saleanalysisactivity)
public class SaleAnalysis extends Activity
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
        listNewData = FileHelper.getAssetsData(SaleAnalysis.this, "getAnalysisData", ChartEntity.class);
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
        list.add(new LineChartItem(R.layout.saleanalysis_linechart, generateDataLine(), SaleAnalysis.this.getApplicationContext()));
        ChartDataAdapter cda = new ChartDataAdapter(SaleAnalysis.this.getApplicationContext(), list);
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

    private LineData generateDataLine()
    {
        ArrayList<Entry> e1 = new ArrayList<Entry>();
        for (int i = 0; i < listNewData.size(); i++)
        {
            e1.add(new Entry(Float.valueOf(listNewData.get(i).getNumber()), i));
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

        LineData cd = new LineData(generateItem(), sets);
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

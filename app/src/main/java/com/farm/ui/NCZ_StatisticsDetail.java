package com.farm.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.parkweathertab;
import com.farm.chart.BarChartActivity;
import com.farm.chart.BarChartItem;
import com.farm.chart.ChartItem;
import com.farm.chart.LineChartItem;
import com.farm.chart.PieChartItem;
import com.farm.common.FileHelper;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/5/19.
 */
@EActivity(R.layout.ncz_statisticsdetail)
public class NCZ_StatisticsDetail extends Activity {

    @ViewById
    ListView listView1;
    List<parkweathertab> listNewData = null;
    @ViewById
    TextView tv_title;
    String aa;
    String zhuangtai;
    @Click
    void btn_back() {
        finish();
    }

    @AfterViews
    void afterview() {

        listNewData= FileHelper.getAssetsData(NCZ_StatisticsDetail.this, "getDayWeatherAllHour", parkweathertab.class);
//        NCZ_StatisticsDetail.this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        tv_title.setText(aa);
        String x=aa;
        String y=zhuangtai;
        init();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        aa = getIntent().getStringExtra("statistics");
        zhuangtai = getIntent().getStringExtra("static");//0

    }
    public void init()
    {

        ArrayList<ChartItem> list = new ArrayList<ChartItem>();
        if (aa.equals("植株统计")) {
            list.add(new LineChartItem(R.layout.list_item_linechart_plant, generateDataLine_plant(1), NCZ_StatisticsDetail.this.getApplicationContext()));
        }else if (aa.equals("事件统计"))
        {
            list.add(new BarChartItem(R.layout.list_item_barchart_event,generateDataBar_event(1), NCZ_StatisticsDetail.this.getApplicationContext()));
        }else{
            list.add(new PieChartItem(generateDataPie(1), NCZ_StatisticsDetail.this.getApplicationContext()));
        }

        ChartDataAdapter cda = new ChartDataAdapter(NCZ_StatisticsDetail.this.getApplicationContext(), list);
        listView1.setAdapter(cda);

    }
    private PieData generateDataPie(int cnt)
    {

        ArrayList<Entry> entries = new ArrayList<Entry>();

        for (int i = 0; i < 4; i++)
        {
            entries.add(new Entry(Float.valueOf(listNewData.get(i).gettempM()), i));
        }

        PieDataSet d = new PieDataSet(entries, "库存");

        // space between slices
        d.setSliceSpace(2f);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);

        PieData cd = new PieData(getQuarters(), d);
        return cd;
    }
    private ArrayList<String> getQuarters()
    {

        ArrayList<String> q = new ArrayList<String>();
        q.add("使用出库");
        q.add("物资采购");
        q.add("物资转库");
        q.add("物资报废");

        return q;
    }
    private LineData generateDataLine_plant(int cnt)
    {
        ArrayList<Entry> e1 = new ArrayList<Entry>();
        for (int i = 0; i < listNewData.size(); i++)
        {
            e1.add(new Entry(Float.valueOf(listNewData.get(i).gettempM()), i));
        }
        LineDataSet d1 = new LineDataSet(e1, "正常植株" );
        d1.setLineWidth(5f);
        d1.setCircleSize(3.5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setDrawValues(false);
        d1.setValueTextSize(20f);

        ArrayList<Entry> e2 = new ArrayList<Entry>();
        for (int i = 0; i < listNewData.size(); i++)
        {
            e2.add(new Entry(Float.valueOf(listNewData.get(i).gettempH()), i));
        }
        LineDataSet d2 = new LineDataSet(e2, "异常植株" );
        d2.setLineWidth(5f);
        d2.setCircleSize(3.5f);
        d2.setHighLightColor(Color.rgb(244, 117, 117));
        d2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setDrawValues(false);
        d2.setValueTextSize(20f);



        ArrayList<LineDataSet> sets = new ArrayList<LineDataSet>();
        sets.add(d1);
        sets.add(d2);

        LineData cd = new LineData(getMonths(), sets);
        return cd;
    }

    private BarData generateDataBar_event(int cnt)
    {

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

        for (int i = 0; i < listNewData.size(); i++)
        {
            entries.add(new BarEntry(Float.valueOf(listNewData.get(i).gettempM()), i));
        }

        BarDataSet d1 = new BarDataSet(entries, "已处理事件 " );
        d1.setBarSpacePercent(20f);
        d1.setColor(Color.rgb(255, 0, 255));
        d1.setHighLightAlpha(255);
        d1.setStackLabels(new String[1]);
        d1.setValueTextSize(12);

        BarDataSet d2 = new BarDataSet(entries, "待处理事件" );
        d2.setBarSpacePercent(20f);
        d2.setColor(Color.rgb(0, 255, 255));
        d2.setHighLightAlpha(255);
        d2.setStackLabels(new String[1]);
        d2.setValueTextSize(12);

        BarDataSet d3 = new BarDataSet(entries, "处理中事件" );
        d3.setBarSpacePercent(20f);
        d3.setColor(Color.rgb(255, 255, 0));
        d3.setHighLightAlpha(255);
        d3.setStackLabels(new String[1]);
        d3.setValueTextSize(12);


        ArrayList<BarDataSet> sets = new ArrayList<BarDataSet>();
        sets.add(d1);
        sets.add(d2);
        sets.add(d3);
        BarData cd = new BarData(getMonths(), sets);
        return cd;
    }

    private ArrayList<String> getMonths()
    {

        ArrayList<String> m = new ArrayList<String>();
        m.add("1");
        m.add("2");
        m.add("3");
        m.add("4");
        m.add("5");
        m.add("6");
        m.add("7");
        m.add("8");
        m.add("9");
        m.add("10");
        m.add("11");
        m.add("12");
//		m.add("13");
//		m.add("14");
//		m.add("15");
//		m.add("16");
//		m.add("17");
//		m.add("18");
//		m.add("19");
//		m.add("20");
//		m.add("21");
//		m.add("22");
//		m.add("23");
//		m.add("24");
//		m.add("25");
//		m.add("26");
//		m.add("27");
//		m.add("28");
//		m.add("29");
//		m.add("30");

        return m;
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

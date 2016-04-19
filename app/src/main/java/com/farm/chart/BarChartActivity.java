package com.farm.chart;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.farm.R;
import com.farm.bean.parkweathertab;
import com.farm.common.FileHelper;
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
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.mutilchartfragment)
public class BarChartActivity extends Activity
{
	List<parkweathertab> listNewData = null;
	protected String[] mMonths = new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec" };

	protected String[] mParties = new String[] { "Party A", "Party B", "Party C", "Party D", "Party E", "Party F", "Party G", "Party H", "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P", "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X", "Party Y", "Party Z" };

	// @Override
	// public void onBackPressed()
	// {
	// super.onBackPressed();
	// overridePendingTransition(R.anim.move_left_in_activity,
	// R.anim.move_right_out_activity);
	// }
	@ViewById
	ListView listView1;
	@AfterViews
	void afterOncreate()
	{
		listNewData= FileHelper.getAssetsData(BarChartActivity.this, "getDayWeatherAllHour", parkweathertab.class);
		BarChartActivity.this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		ArrayList<ChartItem> list = new ArrayList<ChartItem>();
		// 30 items
//		for (int i = 0; i < 30; i++)
//		{
//			if (i % 3 == 0)
//			{
//				list.add(new LineChartItem(generateDataLine(i + 1), BarChartActivity.this.getApplicationContext()));
//			}
//			else if (i % 3 == 1)
//			{
//				list.add(new BarChartItem(generateDataBar(i + 1), BarChartActivity.this.getApplicationContext()));
//			}
//			else if (i % 3 == 2)
//			{
//				list.add(new PieChartItem(generateDataPie(i + 1), BarChartActivity.this.getApplicationContext()));
//			}
//		}

			list.add(new LineChartItem(R.layout.list_item_linechart_plant,generateDataLine_plant(1), BarChartActivity.this.getApplicationContext()));
			list.add(new BarChartItem(R.layout.list_item_barchart_event,generateDataBar_event(1), BarChartActivity.this.getApplicationContext()));
			list.add(new BarChartItem(R.layout.list_item_barchart_job,generateDataBar_job(2), BarChartActivity.this.getApplicationContext()));
			list.add(new LineChartItem(R.layout.list_item_linechart_sale,generateDataLine_sale(2), BarChartActivity.this.getApplicationContext()));
		    list.add(new PieChartItem(generateDataPie(1), BarChartActivity.this.getApplicationContext()));
		    list.add(new LineChartItem(R.layout.list_item_linechart_goods,generateDataLine_goods(3), BarChartActivity.this.getApplicationContext()));
			list.add(new BarChartItem(R.layout.list_item_barchart_command,generateDataBar_command(3), BarChartActivity.this.getApplicationContext()));
			list.add(new PieChartItem(generateDataPie(1), BarChartActivity.this.getApplicationContext()));
		ChartDataAdapter cda = new ChartDataAdapter(BarChartActivity.this.getApplicationContext(), list);
		listView1.setAdapter(cda);
	}



	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getActionBar().hide();
	}

	/** adapter that supports 3 different item types */
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

	/**
	 * generates a random ChartData object with just one DataSet
	 * 
	 * @return
	 */
	private LineData generateDataLine_goods(int cnt)
	{
		ArrayList<Entry> e1 = new ArrayList<Entry>();
		for (int i = 0; i < listNewData.size(); i++)
		{
			e1.add(new Entry(Float.valueOf(listNewData.get(i).gettempM()), i));
		}
		LineDataSet d1 = new LineDataSet(e1, "使用出库" );
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
		LineDataSet d2 = new LineDataSet(e2, "物资采购" );
		d2.setLineWidth(5f);
		d2.setCircleSize(3.5f);
		d2.setHighLightColor(Color.rgb(244, 117, 117));
		d2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
		d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
		d2.setDrawValues(false);
		d2.setValueTextSize(20f);

		ArrayList<Entry> e3 = new ArrayList<Entry>();
		for (int i = 0; i < listNewData.size(); i++)
		{
			e3.add(new Entry(Float.valueOf(listNewData.get(i).gettempL()), i));
		}
		LineDataSet d3 = new LineDataSet(e3, "物资转库" );
		d3.setLineWidth(2.5f);
		d3.setCircleSize(3.5f);
		d3.setHighLightColor(Color.rgb(244, 117, 117));
		d3.setColor(ColorTemplate.VORDIPLOM_COLORS[2]);
		d3.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[2]);
		d3.setDrawValues(false);
		d3.setValueTextSize(20f);

		ArrayList<LineDataSet> sets = new ArrayList<LineDataSet>();
		sets.add(d1);
		sets.add(d2);
		sets.add(d3);

		LineData cd = new LineData(getMonths(), sets);
		return cd;
	}
	private LineData generateDataLine_sale(int cnt)
	{
		ArrayList<Entry> e1 = new ArrayList<Entry>();
		for (int i = 0; i < listNewData.size(); i++)
		{
			e1.add(new Entry(Float.valueOf(listNewData.get(i).gettempM()), i));
		}
		LineDataSet d1 = new LineDataSet(e1, "已售" );
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
		LineDataSet d2 = new LineDataSet(e2, "售中" );
		d2.setLineWidth(5f);
		d2.setCircleSize(3.5f);
		d2.setHighLightColor(Color.rgb(244, 117, 117));
		d2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
		d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
		d2.setDrawValues(false);
		d2.setValueTextSize(20f);

		ArrayList<Entry> e3 = new ArrayList<Entry>();
		for (int i = 0; i < listNewData.size(); i++)
		{
			e3.add(new Entry(Float.valueOf(listNewData.get(i).gettempL()), i));
		}
		LineDataSet d3 = new LineDataSet(e3, "待售" );
		d3.setLineWidth(2.5f);
		d3.setCircleSize(3.5f);
		d3.setHighLightColor(Color.rgb(244, 117, 117));
		d3.setColor(ColorTemplate.VORDIPLOM_COLORS[2]);
		d3.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[2]);
		d3.setDrawValues(false);
		d3.setValueTextSize(20f);

		ArrayList<LineDataSet> sets = new ArrayList<LineDataSet>();
		sets.add(d1);
		sets.add(d2);
		sets.add(d3);

		LineData cd = new LineData(getMonths(), sets);
		return cd;
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

	/**
	 * generates a random ChartData object with just one DataSet
	 * 
	 * @return
	 */
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
	private BarData generateDataBar_job(int cnt)
	{

		ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

		for (int i = 0; i < listNewData.size(); i++)
		{
			entries.add(new BarEntry(Float.valueOf(listNewData.get(i).gettempM()), i));
		}

		BarDataSet d1 = new BarDataSet(entries, "优良工作" );
		d1.setBarSpacePercent(20f);
		d1.setColor(Color.rgb(255, 0, 255));
		d1.setHighLightAlpha(255);
		d1.setStackLabels(new String[1]);
		d1.setValueTextSize(12);

		BarDataSet d2 = new BarDataSet(entries, "合格工作" );
		d2.setBarSpacePercent(20f);
		d2.setColor(Color.rgb(0, 255, 255));
		d2.setHighLightAlpha(255);
		d2.setStackLabels(new String[1]);
		d2.setValueTextSize(12);

		BarDataSet d3 = new BarDataSet(entries, "不合格工作" );
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
	private BarData generateDataBar_command(int cnt)
	{

		ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

		for (int i = 0; i < listNewData.size(); i++)
		{
			entries.add(new BarEntry(Float.valueOf(listNewData.get(i).gettempM()), i));
		}

		BarDataSet d1 = new BarDataSet(entries, "下发指令" );
		d1.setBarSpacePercent(20f);
		d1.setColor(Color.rgb(255, 0, 255));
		d1.setHighLightAlpha(255);
		d1.setStackLabels(new String[1]);
		d1.setValueTextSize(12);

		BarDataSet d2 = new BarDataSet(entries, "自发指令" );
		d2.setBarSpacePercent(20f);
		d2.setColor(Color.rgb(0, 255, 255));
		d2.setHighLightAlpha(255);
		d2.setStackLabels(new String[1]);
		d2.setValueTextSize(12);



		ArrayList<BarDataSet> sets = new ArrayList<BarDataSet>();
		sets.add(d1);
		sets.add(d2);
		BarData cd = new BarData(getMonths(), sets);
		return cd;
	}

	/**
	 * generates a random ChartData object with just one DataSet
	 * 
	 * @return
	 */
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
}

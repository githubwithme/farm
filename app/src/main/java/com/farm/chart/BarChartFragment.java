package com.farm.chart;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.farm.R;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class BarChartFragment extends Fragment
{
	protected String[] mMonths = new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec" };

	protected String[] mParties = new String[] { "Party A", "Party B", "Party C", "Party D", "Party E", "Party F", "Party G", "Party H", "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P", "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X", "Party Y", "Party Z" };

	// @Override
	// public void onBackPressed()
	// {
	// super.onBackPressed();
	// overridePendingTransition(R.anim.move_left_in_activity,
	// R.anim.move_right_out_activity);
	// }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{

		View rootView = inflater.inflate(R.layout.mutilchartfragment, container, false);

		getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		ListView lv = (ListView) rootView.findViewById(R.id.listView1);

		ArrayList<ChartItem> list = new ArrayList<ChartItem>();

		// 30 items
		for (int i = 0; i < 3; i++)
		{

			if (i % 3 == 0)
			{
				list.add(new LineChartItem(generateDataLine(i + 1), getActivity().getApplicationContext()));
			} 
//			else if (i % 3 == 1)
//			{
//				list.add(new BarChartItem(generateDataBar(i + 1), getActivity().getApplicationContext()));
//			} 
//			else if (i % 3 == 2)
//			{
//				list.add(new PieChartItem(generateDataPie(i + 1), getActivity().getApplicationContext()));
//			}
		}

		ChartDataAdapter cda = new ChartDataAdapter(getActivity().getApplicationContext(), list);
		lv.setAdapter(cda);
		return rootView;
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
	private LineData generateDataLine(int cnt)
	{

		ArrayList<Entry> e1 = new ArrayList<Entry>();

		for (int i = 0; i < 12; i++)
		{
			e1.add(new Entry((int) (Math.random() * 65) + 40, i));
		}

		LineDataSet d1 = new LineDataSet(e1, "New DataSet " + cnt + ", (1)");
		d1.setLineWidth(2.5f);
		d1.setCircleSize(4.5f);
		d1.setHighLightColor(Color.rgb(244, 117, 117));
		d1.setDrawValues(false);

		ArrayList<Entry> e2 = new ArrayList<Entry>();

		for (int i = 0; i < 12; i++)
		{
			e2.add(new Entry(e1.get(i).getVal() - 30, i));
		}

		LineDataSet d2 = new LineDataSet(e2, "New DataSet " + cnt + ", (2)");
		d2.setLineWidth(2.5f);
		d2.setCircleSize(4.5f);
		d2.setHighLightColor(Color.rgb(244, 117, 117));
		d2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
		d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
		d2.setDrawValues(false);

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
	private BarData generateDataBar(int cnt)
	{

		ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

		for (int i = 0; i < 12; i++)
		{
			entries.add(new BarEntry((int) (Math.random() * 70) + 30, i));
		}

		BarDataSet d = new BarDataSet(entries, "New DataSet " + cnt);
		d.setBarSpacePercent(20f);
		d.setColors(ColorTemplate.VORDIPLOM_COLORS);
		d.setHighLightAlpha(255);

		BarData cd = new BarData(getMonths(), d);
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
			entries.add(new Entry((int) (Math.random() * 70) + 30, i));
		}

		PieDataSet d = new PieDataSet(entries, "");

		// space between slices
		d.setSliceSpace(2f);
		d.setColors(ColorTemplate.VORDIPLOM_COLORS);

		PieData cd = new PieData(getQuarters(), d);
		return cd;
	}

	private ArrayList<String> getQuarters()
	{

		ArrayList<String> q = new ArrayList<String>();
		q.add("1st Quarter");
		q.add("2nd Quarter");
		q.add("3rd Quarter");
		q.add("4th Quarter");

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
		m.add("13");
		m.add("14");
		m.add("15");
		m.add("16");
		m.add("17");
		m.add("18");
		m.add("19");
		m.add("20");
		m.add("21");
		m.add("22");
		m.add("23");
		m.add("24");
		m.add("25");
		m.add("26");
		m.add("27");
		m.add("28");
		m.add("29");
		m.add("30");

		return m;
	}
}

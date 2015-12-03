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
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class LineChartFragment extends Fragment
{

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

		for (int i = 0; i < 30; i++)
		{
			e1.add(new Entry((int) (Math.random() * 2) + 3, i));
		}

		LineDataSet d1 = new LineDataSet(e1, "New DataSet " + cnt + ", (1)");
		d1.setLineWidth(2.5f);
		d1.setCircleSize(4.5f);
		d1.setHighLightColor(Color.rgb(244, 117, 117));
		d1.setDrawValues(false);

		ArrayList<Entry> e2 = new ArrayList<Entry>();

		for (int i = 0; i < 30; i++)
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

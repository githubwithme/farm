package com.farm.chart;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;

import com.farm.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.ChartData;

public class BarChartItem extends ChartItem
{

	int res;
	private Typeface mTf;

	public BarChartItem(int res,ChartData<?> cd, Context c)
	{
		super(cd);
		this.res=res;
		mTf = Typeface.createFromAsset(c.getAssets(), "OpenSans-Regular.ttf");
	}

	@Override
	public int getItemType()
	{
		return TYPE_BARCHART;
	}

	@Override
	public View getView(int position, View convertView, Context c)
	{

		ViewHolder holder = null;

		if (convertView == null)
		{

			holder = new ViewHolder();

			convertView = LayoutInflater.from(c).inflate(res, null);
			holder.chart = (BarChart) convertView.findViewById(R.id.chart);

			convertView.setTag(holder);

		} else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		// apply styling
		holder.chart.setDescription("");
		holder.chart.setDrawGridBackground(false);
		holder.chart.setDrawBarShadow(false);

		XAxis xAxis = holder.chart.getXAxis();
		xAxis.setPosition(XAxisPosition.BOTTOM);
		xAxis.setTypeface(mTf);
		xAxis.setDrawGridLines(false);
		xAxis.setDrawAxisLine(true);

		YAxis leftAxis = holder.chart.getAxisLeft();
		leftAxis.setTypeface(mTf);
		leftAxis.setLabelCount(5);
		leftAxis.setSpaceTop(20f);

		YAxis rightAxis = holder.chart.getAxisRight();
		rightAxis.setTypeface(mTf);
		rightAxis.setLabelCount(5);
		rightAxis.setSpaceTop(20f);

		mChartData.setValueTypeface(mTf);

		// set data
		holder.chart.setData((BarData) mChartData);

		// do not forget to refresh the chart
		// holder.chart.invalidate();
		holder.chart.animateY(700);

		return convertView;
	}

	private static class ViewHolder
	{
		BarChart chart;
	}
}

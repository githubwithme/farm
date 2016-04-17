package com.farm.chart;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;

import com.farm.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.LineData;

public class LineChartItem extends ChartItem
{

	int res;
	private Typeface mTf;

	public LineChartItem(int res,ChartData<?> cd, Context c)
	{
		super(cd);
this.res=res;
		mTf = Typeface.createFromAsset(c.getAssets(), "OpenSans-Regular.ttf");
	}

	@Override
	public int getItemType()
	{
		return TYPE_LINECHART;
	}

	@Override
	public View getView(int position, View convertView, Context c)
	{

		ViewHolder holder = null;

		if (convertView == null)
		{

			holder = new ViewHolder();

			convertView = LayoutInflater.from(c).inflate(res, null);
			holder.chart = (LineChart) convertView.findViewById(R.id.chart);

			convertView.setTag(holder);

		} else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		// apply styling
		// holder.chart.setValueTypeface(mTf);
		holder.chart.setDescription("");
		holder.chart.setDrawGridBackground(false);

		XAxis xAxis = holder.chart.getXAxis();
		xAxis.setPosition(XAxisPosition.BOTTOM);
		xAxis.setTypeface(mTf);
		xAxis.setDrawGridLines(false);
		xAxis.setDrawAxisLine(true);

		YAxis leftAxis = holder.chart.getAxisLeft();
		leftAxis.setTypeface(mTf);
		leftAxis.setLabelCount(5);

		YAxis rightAxis = holder.chart.getAxisRight();
		rightAxis.setTypeface(mTf);
		rightAxis.setLabelCount(5);
		rightAxis.setDrawGridLines(false);

		// set data
		holder.chart.setData((LineData) mChartData);

		// do not forget to refresh the chart
		// holder.chart.invalidate();
		holder.chart.animateX(750);

		return convertView;
	}

	private static class ViewHolder
	{
		LineChart chart;
	}
}

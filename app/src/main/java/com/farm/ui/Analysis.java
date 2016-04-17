package com.farm.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.farm.R;
import com.farm.chart.BarChartFragment;
import com.farm.chart.PieChartFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@SuppressLint("NewApi")
@EActivity(R.layout.analysis)
public class Analysis extends Activity
{
	Fragment_SaleAnalysis fragment_saleAnalysis;
//	Fragment_EventAnalysis fragment_eventAnalysis;
	BarChartFragment barChartFragment;
//	HistoryWeatherFragment historyWeatherFragment;
	PieChartFragment pieChartFragment;
	Fragment mContent_hours = new Fragment();
	Fragment mContent_week = new Fragment();
	Fragment mContent_history = new Fragment();
	@ViewById
	View line;
	@ViewById
	TextView tv_title;
	PopupWindow pw_tab;
	View pv_tab;
	// parkweathertab parkweathertab;
	String parkid;
	@ViewById
	ImageButton btn_back;

	@Click
	void btn_back()
	{
		finish();
	}



	@Override
	protected void onResume()
	{
		super.onResume();
		pieChartFragment = new PieChartFragment();
		Bundle bundle = new Bundle();
		bundle.putString("parkid", parkid);
		pieChartFragment.setArguments(bundle);
		switchContent_history(mContent_history, pieChartFragment);
	}

	@AfterViews
	void afterOncreate()
	{
		 switchContent_day(mContent_hours, fragment_saleAnalysis);
		 switchContent_week(mContent_week, barChartFragment);
		 switchContent_history(mContent_history, pieChartFragment);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		parkid = getIntent().getStringExtra("parkid");
		Bundle bundle = new Bundle();
		bundle.putString("parkid", parkid);
		fragment_saleAnalysis = new Fragment_SaleAnalysis();
		fragment_saleAnalysis.setArguments(bundle);
		barChartFragment = new BarChartFragment();
		barChartFragment.setArguments(bundle);
		pieChartFragment = new PieChartFragment();
		pieChartFragment.setArguments(bundle);
		pieChartFragment = new PieChartFragment();
		pieChartFragment.setArguments(bundle);
	}





	public void switchContent_day(Fragment from, Fragment to)
	{
		if (mContent_hours != to)
		{
			mContent_hours = to;
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			if (!to.isAdded())
			{ // 先判断是否被add过
				transaction.hide(from).add(R.id.chart_day, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
			} else
			{
				transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
			}
		}
	}

	public void switchContent_week(Fragment from, Fragment to)
	{
		if (mContent_week != to)
		{
			mContent_week = to;
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			if (!to.isAdded())
			{ // 先判断是否被add过
				transaction.hide(from).add(R.id.chart_week, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
			} else
			{
				transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
			}
		}
	}

	public void switchContent_history(Fragment from, Fragment to)
	{
		if (mContent_history != to)
		{
			mContent_history = to;
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			if (!to.isAdded())
			{ // 先判断是否被add过
				transaction.hide(from).add(R.id.chart_history, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
			} else
			{
				transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
			}
		}
	}
}

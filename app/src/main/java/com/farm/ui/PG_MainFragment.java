package com.farm.ui;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.farm.R;
import com.farm.adapter.Common_TodayJobAdapter;
import com.farm.app.AppContext;
import com.farm.bean.commembertab;
import com.farm.bean.jobtab;
import com.farm.common.utils;
import com.farm.widget.PullToRefreshListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :hc-sima
 * @version :1.0
 * @createTime：2015-8-14 下午2:19:43
 * @description :主界面
 */
@EFragment
public class PG_MainFragment extends Fragment
{
	private List<jobtab> listData = new ArrayList<jobtab>();
	Common_TodayJobAdapter adapter;
	TimeThread timethread;
	List<jobtab> list;
	Fragment mContent = new Fragment();
	Fragment mContent_todayjob = new Fragment();
	Common_TodayJob common_TodayJob;
	DayWeatherFragment dayWeatherFragment;
	@ViewById
	PullToRefreshListView frame_listview_news;
	@ViewById
	TextView tv_day;
	@ViewById
	TextView tv_title;

	@Click
	void btn_nature()
	{
		Intent intent = new Intent(getActivity(), Common_AddSpontaneityWork_.class);
		startActivity(intent);
	}

	@Click
	void btn_cmd()
	{
		Intent intent = new Intent(getActivity(), SelectorCommand_.class);
		intent.putParcelableArrayListExtra("jobtablist", (ArrayList<? extends Parcelable>) listData);
		startActivity(intent);
	}

	@Click
	void tv_more()
	{
		Intent intent = new Intent(getActivity(), Common_MoreJob_.class);
		startActivity(intent);
	}

	@AfterViews
	void afterOncreate()
	{
		tv_day.setText(utils.getTodayAndwWeek());
		tv_title.setText("首页");
		switchContent(mContent, dayWeatherFragment);
		switchContent_todayjob(mContent_todayjob, common_TodayJob);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.pg_fragment_main, container, false);
		commembertab commembertab = AppContext.getUserInfo(getActivity());
		String aa=commembertab.getId();
		dayWeatherFragment = new DayWeatherFragment_();
		common_TodayJob = new Common_TodayJob_();
		Bundle bundle = new Bundle();
		bundle.putString("workuserid", commembertab.getId());
		common_TodayJob.setArguments(bundle);
		timethread = new TimeThread();
		timethread.setStop(false);
		timethread.setSleep(false);
		timethread.start();
		return rootView;
	}

	public void switchContent(Fragment from, Fragment to)
	{
		if (mContent != to)
		{
			mContent = to;
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			if (!to.isAdded())
			{ // 先判断是否被add过
				transaction.hide(from).add(R.id.container_weather, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
			} else
			{
				transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
			}
		}
	}

	public void switchContent_todayjob(Fragment from, Fragment to)
	{
		if (mContent_todayjob != to)
		{
			mContent_todayjob = to;
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			if (!to.isAdded())
			{ // 先判断是否被add过
				transaction.hide(from).add(R.id.container_todayjob, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
			} else
			{
				transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
			}
		}
	}

	class TimeThread extends Thread
	{
		private boolean isSleep = true;
		private boolean stop = false;

		public void run()
		{
			Long starttime = 0l;
			while (!stop)
			{
				if (isSleep)
				{
				} else
				{
					try
					{
						Thread.sleep(AppContext.TIME_REFRESH);
						starttime = starttime + 1000;
						common_TodayJob.refreshData();
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		}

		public void setSleep(boolean sleep)
		{
			isSleep = sleep;
		}

		public void setStop(boolean stop)
		{
			this.stop = stop;
		}
	}

	@Override
	public void onDestroyView()
	{
		super.onDestroyView();
		timethread.setStop(true);
		timethread.interrupt();
		timethread = null;
	}
}

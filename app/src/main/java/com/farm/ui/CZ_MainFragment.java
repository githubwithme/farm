package com.farm.ui;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.HaveReadNumber;
import com.farm.bean.Result;
import com.farm.bean.areatab;
import com.farm.bean.commembertab;
import com.farm.common.SqliteDb;
import com.farm.common.utils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

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
public class CZ_MainFragment extends Fragment
{
	TimeThread timethread;
	commembertab commembertab;
	Fragment mContent = new Fragment();
	Fragment mContent_todayjob = new Fragment();
	Common_TodayJob common_TodayJob;
	DayWeatherFragment dayWeatherFragment;
	@ViewById
	TextView tv_day;
	@ViewById
	TextView tv_title;
	@ViewById
	TextView tv_plantnumber;
	@ViewById
	TextView tv_worknumber;
	@ViewById
	TextView tv_cmdnumber;
	@ViewById
	TextView tv_plantnumber_new;
	@ViewById
	TextView tv_cmdnumber_new;
	@ViewById
	TextView tv_worknumber_new;
	@ViewById
	FrameLayout fl_worknumber_new;
	@ViewById
	FrameLayout fl_plantnumber_new;
	@ViewById
	FrameLayout fl_cmdnumber_new;
	areatab areatab;
	int allCount_plant;
	int allCount_job;
	int allCount_cmd;

	@Click
	void tl_jrqp()
	{
		fl_plantnumber_new.setVisibility(View.GONE);
		fl_cmdnumber_new.setVisibility(View.GONE);
		fl_worknumber_new.setVisibility(View.GONE);
		HaveReadNumber haveReadNumber = getHaveReadData(commembertab.getId() + areatab.getWorkuserid());
		if (haveReadNumber != null)
		{
			updateHaveReadData(areatab.getWorkuserid(), "pq_plantnum", String.valueOf(allCount_plant));
			updateHaveReadData(areatab.getWorkuserid(), "pq_jobnum", String.valueOf(allCount_job));
			updateHaveReadData(areatab.getWorkuserid(), "pq_cmdnum", String.valueOf(allCount_cmd));
		} else
		{
			saveHaveReadData(areatab.getWorkuserid(),String.valueOf(allCount_plant),  String.valueOf(allCount_job), String.valueOf(allCount_cmd));
		}
		Intent intent = new Intent(getActivity(), CZ_ToDayPQ_.class);
		intent.putExtra("parkid", commembertab.getparkId());
		startActivity(intent);
	}

	@AfterViews
	void afterOncreate()
	{
		tv_day.setText(utils.getTodayAndwWeek());
		tv_title.setText("首页");
		getListData();
		switchContent(mContent, dayWeatherFragment);
		switchContent_todayjob(mContent_todayjob, common_TodayJob);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.cz_fragment_main1, container, false);
		dayWeatherFragment = new DayWeatherFragment_();
		commembertab = AppContext.getUserInfo(getActivity());
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

	private void getListData()
	{
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("userid", commembertab.getId());
		params.addQueryStringParameter("parkID", commembertab.getparkId());
		params.addQueryStringParameter("uid", commembertab.getuId());
		params.addQueryStringParameter("username", commembertab.getuserName());
		params.addQueryStringParameter("action", "parkGetbyParkID");
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
		{
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				String a = responseInfo.result;
				List<areatab> listNewData = null;
				Result result = JSON.parseObject(responseInfo.result, Result.class);
				if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
				{
					if (result.getAffectedRows() != 0)
					{
						listNewData = JSON.parseArray(result.getRows().toJSONString(), areatab.class);
						if (listNewData != null)
						{
							areatab = listNewData.get(0);
							HaveReadNumber haveReadNumber = getHaveReadData(commembertab.getId() + areatab.getWorkuserid());
							if (haveReadNumber != null)
							{
								String num_job = haveReadNumber.getPq_jobnum();
								String num_plant = haveReadNumber.getPq_plantnum();
								String num_cmd = haveReadNumber.getPq_cmdnum();
								allCount_plant = Integer.valueOf(areatab.getPlantGrowCount()) + Integer.valueOf(areatab.getPlantGrowVideoCount());
								allCount_job = Integer.valueOf(areatab.getJobCount()) + Integer.valueOf(areatab.getJobVideoCount());
								allCount_cmd = Integer.valueOf(areatab.getCommandCount()) + Integer.valueOf(areatab.getCommandVideoCount());
								if (num_plant != null && !num_plant.equals("") && (Integer.valueOf(num_plant) < allCount_plant))
								{
									int num = allCount_plant - Integer.valueOf(num_plant);
									fl_plantnumber_new.setVisibility(View.VISIBLE);
									tv_plantnumber_new.setText(String.valueOf(num));
								}
								if (num_job != null && !num_job.equals("") && (Integer.valueOf(num_job) < allCount_job))
								{
									int num = allCount_job - Integer.valueOf(num_job);
									fl_worknumber_new.setVisibility(View.VISIBLE);
									tv_worknumber_new.setText(String.valueOf(num));
								}
								if (num_cmd != null && !num_cmd.equals("") && (Integer.valueOf(num_cmd) < allCount_cmd))
								{
									int num = allCount_cmd - Integer.valueOf(num_cmd);
									fl_cmdnumber_new.setVisibility(View.VISIBLE);
									tv_cmdnumber_new.setText(String.valueOf(num));
								}
							} else
							{
								saveHaveReadData(areatab.getWorkuserid(),String.valueOf(allCount_plant),  String.valueOf(allCount_job), String.valueOf(allCount_cmd));
							}
							tv_worknumber.setText(areatab.getJobCount());
							tv_plantnumber.setText(areatab.getPlantGrowCount());
							tv_cmdnumber.setText(areatab.getCommandCount());
						}
					} else
					{
						listNewData = new ArrayList<areatab>();
					}
				} else
				{
					AppContext.makeToast(getActivity(), "error_connectDataBase");
					return;
				}
			}

			@Override
			public void onFailure(HttpException error, String msg)
			{
				AppContext.makeToast(getActivity(), "error_connectServer");
			}
		});
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

	private void saveHaveReadData(String id, String plantnum, String jobnum, String cmdnum)
	{
		HaveReadNumber haveReadNumber = new HaveReadNumber();
		haveReadNumber.setId(commembertab.getId() + id);
		haveReadNumber.setPlantnum("");
		haveReadNumber.setAsknum("");
		haveReadNumber.setJobnum("");
		haveReadNumber.setPq_jobnum(jobnum);
		haveReadNumber.setPq_plantnum(plantnum);
		haveReadNumber.setPq_cmdnum(cmdnum);
		SqliteDb.save(getActivity(), haveReadNumber);
	}

	private HaveReadNumber getHaveReadData(String id)
	{
		HaveReadNumber haveReadNumber = (HaveReadNumber) SqliteDb.getHaveReadData(getActivity(), HaveReadNumber.class, id);
		return haveReadNumber;
	}

	private void updateHaveReadData(String id, String columnname, String columvalues)
	{
		HaveReadNumber haveReadNumber = new HaveReadNumber();
		haveReadNumber.setId(commembertab.getId() + id);
		if (columnname.equals("plantnum"))
		{
			haveReadNumber.setPlantnum(columvalues);
		} else if (columnname.equals("jobnum"))
		{
			haveReadNumber.setJobnum(columvalues);
		} else if (columnname.equals("asknum"))
		{
			haveReadNumber.setAsknum(columvalues);
		} else if (columnname.equals("pq_plantnum"))
		{
			haveReadNumber.setPq_plantnum(columvalues);
		} else if (columnname.equals("pq_jobnum"))
		{
			haveReadNumber.setPq_jobnum(columvalues);
		} else if (columnname.equals("pq_cmdnum"))
		{
			haveReadNumber.setPq_cmdnum(columvalues);
		}
		SqliteDb.updateHaveReadData(getActivity(), haveReadNumber, id, columnname);
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
						getListData();
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

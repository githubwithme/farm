package com.farm.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.bean.parkweathertab;
import com.farm.common.utils;
import com.farm.widget.CalendarView;
import com.farm.widget.CalendarView.OnItemClickListener;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author :hc-sima
 * @version :1.0
 * @createTime：2015-8-14 下午2:19:43
 * @description :主界面
 */
@EFragment
public class HistoryWeatherFragment extends Fragment
{
	String parkid;
	@ViewById
	CalendarView calendar;
	@ViewById
	ImageButton calendarLeft;
	@ViewById
	TextView calendarCenter;
	@ViewById
	TextView tv_day;
	@ViewById
	TextView tv_wd;
	@ViewById
	ImageButton calendarRight;
	SimpleDateFormat format;
	@ViewById
	RelativeLayout layout_calendar;

	@AfterViews
	void afterOncreate()
	{
		getToDayWeather(utils.getToday());
		tv_day.setText(utils.getToday());
		format = new SimpleDateFormat("yyyy-MM-dd");
		calendar.setSelectMore(false); // 单选
		try
		{
			// 设置日历日期
			Date date = format.parse("2015-01-01");
			calendar.setCalendarData(date);
		} catch (ParseException e)
		{
			e.printStackTrace();
		}

		// 获取日历中年月 ya[0]为年，ya[1]为月（格式大家可以自行在日历控件中改）
		String[] ya = calendar.getYearAndmonth().split("-");
		calendarCenter.setText(ya[0] + "年" + ya[1] + "月");
		calendarLeft.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// 点击上一月 同样返回年月
				String leftYearAndmonth = calendar.clickLeftMonth();
				String[] ya = leftYearAndmonth.split("-");
				calendarCenter.setText(ya[0] + "年" + ya[1] + "月");
			}
		});

		calendarRight.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// 点击下一月
				String rightYearAndmonth = calendar.clickRightMonth();
				String[] ya = rightYearAndmonth.split("-");
				calendarCenter.setText(ya[0] + "年" + ya[1] + "月");
			}
		});

		// 设置控件监听，可以监听到点击的每一天（大家也可以在控件中根据需求设定）
		calendar.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void OnItemClick(Date selectedStartDate, Date selectedEndDate, Date downDate)
			{
				if (calendar.isSelectMore())
				{
					Toast.makeText(getActivity(), format.format(selectedStartDate) + "到" + format.format(selectedEndDate), Toast.LENGTH_SHORT).show();
					tv_day.setText(format.format(downDate));
					tv_wd.setText("27℃");
				} else
				{
					// Toast.makeText(getActivity(), format.format(downDate),
					// Toast.LENGTH_SHORT).show();
					getToDayWeather(format.format(downDate));
					tv_day.setText(format.format(downDate));

				}
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		parkid = getArguments().getString("parkid");
		View rootView = inflater.inflate(R.layout.historyweatherfragment, container, false);
		return rootView;
	}

	private void getToDayWeather(String day)
	{
		commembertab commembertab = AppContext.getUserInfo(getActivity());
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("uid", commembertab.getuId());
		params.addQueryStringParameter("day", day);
		params.addQueryStringParameter("parkID", parkid);
		params.addQueryStringParameter("action", "getDayWeather");
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
		{
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				String a = responseInfo.result;
				List<parkweathertab> listNewData = null;
				Result result = JSON.parseObject(responseInfo.result, Result.class);
				if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
				{
					if (result.getAffectedRows() != 0)
					{
						listNewData = JSON.parseArray(result.getRows().toJSONString(), parkweathertab.class);
						parkweathertab parkweathertab = listNewData.get(0);
						tv_wd.setText("温度：" + parkweathertab.gettempL() + "℃" + "-" + parkweathertab.gettempH() + "℃" + "     平均温度" + parkweathertab.gettempM() + "℃");
					} else
					{
						listNewData = new ArrayList<parkweathertab>();
						tv_wd.setText("温度:暂无" + "     平均温度:暂无");
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
}

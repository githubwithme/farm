package com.farm.ui;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.bean.parkweathertab;
import com.farm.common.BitmapHelper;
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
public class DayWeatherFragment extends Fragment
{
	@ViewById
	TextView tv_tempH;
	@ViewById
	TextView tv_tempL;
	@ViewById
	TextView tv_from;
	@ViewById
	TextView tv_tempM;
	@ViewById
	TextView tv_weather;

	@ViewById
	TextView tv_tip_tempH;
	@ViewById
	TextView tv_tip_tempL;
	@ViewById
	TextView tv_tip_from;
	@ViewById
	TextView tv_tip_tempM;
	@ViewById
	TextView tv_tip_weather;
	@ViewById
	ImageView img_weather;
	@ViewById
	ProgressBar pb_getweather;
	@ViewById
	RelativeLayout rl_weather;
	@ViewById
	TextView tv_tip_getweather;
	@ViewById
	LinearLayout ll_tip;
	parkweathertab parkweathertab;

	@Click
	void rl_weather()
	{
		Intent intent = new Intent(getActivity(), WeatherActivity_.class);
		intent.putExtra("parkid", parkweathertab.getparkId());
		getActivity().startActivity(intent);
	}

	@AfterViews
	void afterOncreate()
	{
		// Typeface typeface=AppContext.getTypeface(getActivity(), "kaiti.ttf");
		// tv_tempH.setTypeface(typeface);
		// tv_tempL.setTypeface(typeface);
		// tv_weather.setTypeface(typeface);
		// tv_tempM.setTypeface(typeface);
		// tv_from.setTypeface(typeface);
		// tv_tip_tempH.setTypeface(typeface);
		// tv_tip_tempL.setTypeface(typeface);
		// tv_tip_weather.setTypeface(typeface);
		// tv_tip_tempM.setTypeface(typeface);
		// tv_tip_from.setTypeface(typeface);
		getToDayWeather();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		if (parkweathertab != null)
		{
			getToDayWeather();
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.dayweatherfragment, container, false);
		return rootView;
	}

	private void getToDayWeather()
	{
		commembertab commembertab = AppContext.getUserInfo(getActivity());
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("uid", commembertab.getuId());
		params.addQueryStringParameter("day", utils.getToday());
		params.addQueryStringParameter("parkID", commembertab.getparkId());
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
						ll_tip.setVisibility(View.GONE);
						rl_weather.setVisibility(View.VISIBLE);
						parkweathertab = listNewData.get(0);
						showWeather(parkweathertab);
					} else
					{
						listNewData = new ArrayList<parkweathertab>();
						pb_getweather.setVisibility(View.GONE);
						tv_tip_getweather.setText("获取天气失败");
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
				String a = error.getMessage();
				AppContext.makeToast(getActivity(), "error_connectServer");
			}
		});
	}

	private void showWeather(parkweathertab parkweathertab)
	{
		if (parkweathertab.getbManMod().equals("0"))
		{
			tv_from.setText("中国天气网");
		} else
		{
			tv_from.setText(parkweathertab.getcjUserName());
		}
		tv_tempH.setText(parkweathertab.gettempH() + "℃");
		tv_tempL.setText(parkweathertab.gettempL() + "℃");
		tv_weather.setText(parkweathertab.getweather());
		tv_tempM.setText(parkweathertab.gettempM() + "℃");
		if (!parkweathertab.getdimg().equals(""))
		{
			BitmapHelper.setImageViewBackground(getActivity(), img_weather, AppConfig.baseurl + parkweathertab.getdimg());
		}
		if (!parkweathertab.getnimg().equals(""))
		{
			BitmapHelper.setImageViewBackground(getActivity(), img_weather, AppConfig.baseurl + parkweathertab.getnimg());
		}
	}
}

package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.bean.parkweathertab;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.pg_changeweather)
public class CZ_ChangeWeather extends Activity
{
	@ViewById
	Button btn_upload;
	@ViewById
	EditText et_tempL;
	@ViewById
	EditText et_tempH;
	@ViewById
	EditText et_tempM;
	@ViewById
	EditText et_weather;
	parkweathertab parkweathertab;

	@Click
	void btn_upload()
	{
		AddData();
	}

	@Click
	void imgbtn_back()
	{
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		parkweathertab = getIntent().getParcelableExtra("bean");
		parkweathertab = getIntent().getParcelableExtra("bean");
	}

	private void AddData()
	{
		commembertab commembertab = AppContext.getUserInfo(this);
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("userid", commembertab.getId());
		params.addQueryStringParameter("username", commembertab.getuserName());
		params.addQueryStringParameter("action", "WeatherUpdate");
		params.addQueryStringParameter("weatherId", parkweathertab.getid());
		params.addQueryStringParameter("tempL", et_tempL.getText().toString());
		params.addQueryStringParameter("tempH", et_tempH.getText().toString());
		params.addQueryStringParameter("tempM", et_tempM.getText().toString());
		params.addQueryStringParameter("weather", et_weather.getText().toString());
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
		{
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				Result result = JSON.parseObject(responseInfo.result, Result.class);
				if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
				{
					if (result.getAffectedRows() != 0)
					{
						finish();
					} else
					{
						AppContext.makeToast(CZ_ChangeWeather.this, "error_connectDataBase");
					}
				} else
				{
					AppContext.makeToast(CZ_ChangeWeather.this, "error_connectDataBase");
					return;
				}
			}

			@Override
			public void onFailure(HttpException arg0, String arg1)
			{
				AppContext.makeToast(CZ_ChangeWeather.this, "error_connectServer");
			}
		});
	}

}

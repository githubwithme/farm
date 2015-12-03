package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.bean.Result;
import com.farm.common.ConnectionHelper;
import com.farm.common.utils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.entity.FileUploadEntity;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

@EActivity(R.layout.addassess)
public class AddAssess extends Activity
{
	CountDownLatch latch;

	@Click
	void btn_save()
	{
		upload();
		uploadAudio("");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getActionBar().hide();
	}

	private void upload()
	{
		String uuid = java.util.UUID.randomUUID().toString();
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("zwid", uuid);

		String params = ConnectionHelper.setParams("APP.AddPlant", "0", hashMap);
		new HttpUtils().send(HttpRequest.HttpMethod.POST, AppConfig.testurl, ConnectionHelper.getParas(params), new RequestCallBack<String>()
		{
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				Result result = JSON.parseObject(responseInfo.result, Result.class);
				if (result.getResultCode() == 200)// 连接数据库成功
				{
					showProgress();
				}

			}

			@Override
			public void onFailure(HttpException error, String msg)
			{
				Toast.makeText(AddAssess.this, "上传失败", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void uploadAudio(String path)
	{
		File file = new File(path);
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("param", "commandauto/" + utils.getToday());
		params.addQueryStringParameter("first", "true");
		params.addQueryStringParameter("last", "true");
		params.addQueryStringParameter("offset", "0");
		params.addQueryStringParameter("file", file.getName());
		params.setBodyEntity(new FileUploadEntity(file, "text/html"));
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, AppConfig.uploadurl, params, new RequestCallBack<String>()
		{
			@Override
			public void onStart()
			{
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading)
			{
				if (isUploading)
				{
				}
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				showProgress();
			}

			@Override
			public void onFailure(HttpException error, String msg)
			{
			}
		});
	}

	private void showProgress()
	{
		latch.countDown();
		Long l = latch.getCount();
		if (l.intValue() == 0) // 全部线程是否已经结束
		{
			Toast.makeText(AddAssess.this, "上传成功！", Toast.LENGTH_SHORT).show();
			finish();
		}
	}
}

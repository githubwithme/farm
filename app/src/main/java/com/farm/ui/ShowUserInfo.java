package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.common.BitmapHelper;
import com.farm.widget.CircleImageView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.showuserinfo)
public class ShowUserInfo extends Activity
{

	@ViewById
	CircleImageView circle_img;
	String userid;

	@AfterViews
	void afterOncreate()
	{
//		getShowUserInfo(userid);
		BitmapHelper.setImageViewBackground(ShowUserInfo.this, circle_img, "http://img.popoho.com/UploadPic/2010-12/201012297441441.png");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		userid = getIntent().getStringExtra("userid");
	}

	private void getShowUserInfo(String userid)
	{
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("userid", userid);
		params.addQueryStringParameter("action", "getUserInfo");
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
		{
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				String a = responseInfo.result;
				List<commembertab> listData = null;
				Result result = JSON.parseObject(responseInfo.result, Result.class);
				if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
				{
					if (result.getAffectedRows() != 0)
					{
						listData = JSON.parseArray(result.getRows().toJSONString(), commembertab.class);
						if (listData == null)
						{
							Toast.makeText(ShowUserInfo.this, "获取数据异常!", Toast.LENGTH_SHORT).show();
						} else
						{
							showData(listData.get(0));
						}
					} else
					{
						Toast.makeText(ShowUserInfo.this, "用户名或密码错误!", Toast.LENGTH_SHORT).show();
					}
				} else
				{
					Toast.makeText(ShowUserInfo.this, "连接数据服务器出错了!", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(HttpException error, String msg)
			{
				Toast.makeText(ShowUserInfo.this, "连接服务器出错了!", Toast.LENGTH_SHORT).show();
			}
		});

	}

	private void showData(commembertab commembertab)
	{
		// BitmapHelper.setImageViewBackground(ShowUserInfo.this, circle_img,
		// "http://img.popoho.com/UploadPic/2010-12/201012297441441.png");
	}

}

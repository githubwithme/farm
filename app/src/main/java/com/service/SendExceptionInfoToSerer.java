package com.service;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.common.GetMobilePhoneInfo;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class SendExceptionInfoToSerer extends Service
{
	AlertDialog dialog;
	private static final String TAG = "NotificationService";

	public static final String ACTION_NOTIFICATION_CONTROL = "action_notification_control";
	public static final String KEY_COMMAND_SHOW = "show_notification";
	public static final String KEY_COMMAND_UPDATE = "update_notification";
	public static final String KEY_COMMAND_REMOVE = "remove_notification";

	public static final String TIME_KEY = "time_key";
	public static final int NOTIFICATIN_ID = 100;
	String action;
	private Notification mNotification;
	private NotificationManager manager;
	private PendingIntent pendingIntent;
	private Intent intent;

	@Override
	public IBinder onBind(Intent intent)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		action = intent.getAction();
		String info = intent.getStringExtra("info");
		sendExceptionInfoToServer(info);
		return super.onStartCommand(intent, flags, startId);
	}



	private void sendExceptionInfoToServer(String info)
	{
		commembertab commembertab = AppContext.getUserInfo(SendExceptionInfoToSerer.this);
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("UUID", GetMobilePhoneInfo.getDeviceUuid(SendExceptionInfoToSerer.this).toString());
		params.addQueryStringParameter("exceptionInfo", info);
		params.addQueryStringParameter("userid", commembertab.getId());
		params.addQueryStringParameter("username", commembertab.getrealName());
		params.addQueryStringParameter("action", "saveAppException");
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
		{
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				String a = responseInfo.result;
				Result result = JSON.parseObject(responseInfo.result, Result.class);
				if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
				{
					if (result.getAffectedRows() != 0)
					{
						Toast.makeText(SendExceptionInfoToSerer.this, "已处理异常信息！", Toast.LENGTH_SHORT).show();
					} else
					{
						AppContext.makeToast(SendExceptionInfoToSerer.this, "error_connectDataBase");
					}
				} else
				{
					AppContext.makeToast(SendExceptionInfoToSerer.this, "error_connectDataBase");
					return;
				}

			}

			@Override
			public void onFailure(HttpException arg0, String arg1)
			{
				AppContext.makeToast(SendExceptionInfoToSerer.this, "error_connectServer");
			}
		});
	}





}

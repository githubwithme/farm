package com.farm.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.commandtab;
import com.farm.bean.commembertab;
import com.farm.common.utils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.entity.FileUploadEntity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@EActivity(R.layout.pg_commandfeedback)
public class Common_CommandFeedBack extends Activity
{
	CountDownLatch latch;
	@ViewById
	EditText et_jobnote;
	@ViewById
	Button btn_save;
	commandtab commandtab;
	AudioFragment audioFragment;
	commembertab commembertab;

	@Click
	void btn_save()
	{
		commandSetStatus(commandtab);
	}

	@Click
	void tv_area()
	{
	}

	@Click
	void tv_selectcmd()
	{
	}

	@Click
	void tv_workday()
	{
	}

	@Click
	void tv_timelimit()
	{

	}

	@AfterViews
	void afterOncreate()
	{
		switchContent(mContent, audioFragment);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		commembertab = AppContext.getUserInfo(this);
		commandtab = getIntent().getParcelableExtra("bean");
		audioFragment = new AudioFragment_();
	}

	private void commandSetStatus(commandtab commandtab)
	{
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("userid", commembertab.getId());
		params.addQueryStringParameter("userName", commembertab.getrealName());
		params.addQueryStringParameter("uid", commembertab.getuId());
		params.addQueryStringParameter("action", "commandSetStatus");

		String a = commandtab.getStatusid();
		params.addQueryStringParameter("statusid", commandtab.getStatusid());
		params.addQueryStringParameter("commStatus", "2");
		params.addQueryStringParameter("feedbackNote", et_jobnote.getText().toString());
		params.addQueryStringParameter("feedbackDate", utils.getToday());
		params.addQueryStringParameter("confirmDate", "");
		params.addQueryStringParameter("finishDate", "");
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
		{
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				String a = responseInfo.result;
				List<commandtab> listData = null;
				Result result = JSON.parseObject(responseInfo.result, Result.class);
				if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
				{
					listData = JSON.parseArray(result.getRows().toJSONString(), commandtab.class);
					if (listData == null)
					{
						AppContext.makeToast(Common_CommandFeedBack.this, "error_connectDataBase");
					} else
					{
						if (audioFragment.getAudiopath() != null)
						{
							uploadAudio(listData.get(0).getId(), audioFragment.getAudiopath());
						} else
						{
							Toast.makeText(Common_CommandFeedBack.this, "保存成功！", Toast.LENGTH_SHORT).show();
							finish();
						}
					}

				} else
				{
					AppContext.makeToast(Common_CommandFeedBack.this, "error_connectDataBase");
					return;
				}
			}

			@Override
			public void onFailure(HttpException error, String arg1)
			{
				AppContext.makeToast(Common_CommandFeedBack.this, "error_connectServer");
			}
		});
	}

	private void uploadAudio(String id, File audioFile)
	{
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("action", "UpLoadFilefeedbackAudio");
		params.addQueryStringParameter("statusid", id);
		params.addQueryStringParameter("workuserid", commembertab.getId());
		params.addQueryStringParameter("workusername", commembertab.getrealName());
		params.addQueryStringParameter("file", audioFile.getName());
		params.setBodyEntity(new FileUploadEntity(audioFile, "text/html"));
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, AppConfig.uploadurl, params, new RequestCallBack<String>()
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
						Toast.makeText(Common_CommandFeedBack.this, "反馈成功！", Toast.LENGTH_SHORT).show();
						finish();
					} else
					{
						AppContext.makeToast(Common_CommandFeedBack.this, "error_connectDataBase");
					}
				} else
				{
					AppContext.makeToast(Common_CommandFeedBack.this, "error_connectDataBase");
					return;
				}

			}

			@Override
			public void onFailure(HttpException error, String msg)
			{
				String a = error.getMessage();
				AppContext.makeToast(Common_CommandFeedBack.this, "error_connectServer");
			}
		});
	}

	Fragment mContent = new Fragment();

	public void switchContent(Fragment from, Fragment to)
	{
		if (mContent != to)
		{
			mContent = to;
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			if (!to.isAdded())
			{ // 先判断是否被add过
				transaction.hide(from).add(R.id.container, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
			} else
			{
				transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
			}
		}
	}
}

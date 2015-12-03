package com.farm.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.bean.jobtab;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.cz_pg_assess)
public class CZ_PG_Assess extends Activity
{
	jobtab jobtab;
	// AudioFragment audioFragment;
	String scoreselected;
	@ViewById
	ImageButton imgbtn_home;
	@ViewById
	Button btn_sure;
	@ViewById
	RadioGroup rg_score;
	@ViewById
	EditText et_note;

	@Click
	void btn_sure()
	{
		addScore();
	}

	@AfterViews
	void afterOncreate()
	{
		// switchContent(mContent, audioFragment);
		rg_score.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1)
			{
				RadioButton radioButton = (RadioButton) findViewById(arg0.getCheckedRadioButtonId());
				scoreselected = (String) radioButton.getText();
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		jobtab = getIntent().getParcelableExtra("bean");
		// audioFragment = new AudioFragment_();
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

	private void addScore()
	{
		commembertab commembertab = AppContext.getUserInfo(this);
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("userid", commembertab.getId());
		params.addQueryStringParameter("username", commembertab.getrealName());
		params.addQueryStringParameter("uid", commembertab.getuId());
		params.addQueryStringParameter("action", "jobTabAssessByID");
		params.addQueryStringParameter("jobID", jobtab.getId());
		params.addQueryStringParameter("assessScore", String.valueOf(getScore(scoreselected)));
		params.addQueryStringParameter("assessNote", et_note.getText().toString());
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
		{
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				String a = responseInfo.result;
				List<jobtab> listData = null;
				Result result = JSON.parseObject(responseInfo.result, Result.class);
				if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
				{
					if (result.getAffectedRows() != 0)
					{
						listData = JSON.parseArray(result.getRows().toJSONString(), jobtab.class);
						// if (audioFragment.getAudiopath() != null)
						// {
						// uploadAudio(listData.get(0).getId(),
						// audioFragment.getAudiopath());
						// } else
						// {
						Toast.makeText(CZ_PG_Assess.this, "保存成功！", Toast.LENGTH_SHORT).show();
						finish();
						// }
					} else
					{
						AppContext.makeToast(CZ_PG_Assess.this, "error_connectDataBase");
					}
				} else
				{
					AppContext.makeToast(CZ_PG_Assess.this, "error_connectDataBase");
					return;
				}
			}

			@Override
			public void onFailure(HttpException arg0, String arg1)
			{
				AppContext.makeToast(CZ_PG_Assess.this, "error_connectServer");
			}
		});
	}

	// private void uploadAudio(String id, File audioFile)
	// {
	// RequestParams params = new RequestParams();
	// params.addQueryStringParameter("action", "UpLoadFileJobAssessAudio");
	// params.addQueryStringParameter("jobID", id);
	// params.addQueryStringParameter("file", audioFile.getName());
	// params.setBodyEntity(new FileUploadEntity(audioFile, "text/html"));
	// HttpUtils http = new HttpUtils();
	// http.send(HttpRequest.HttpMethod.POST, AppConfig.uploadurl, params, new
	// RequestCallBack<String>()
	// {
	//
	// @Override
	// public void onSuccess(ResponseInfo<String> responseInfo)
	// {
	// String a = responseInfo.result;
	// Result result = JSON.parseObject(responseInfo.result, Result.class);
	// if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
	// {
	// if (result.getAffectedRows() != 0)
	// {
	// Toast.makeText(CZ_PG_Assess.this, "保存成功！", Toast.LENGTH_SHORT).show();
	// finish();
	// } else
	// {
	// AppContext.makeToast(CZ_PG_Assess.this, "error_connectDataBase");
	// }
	// } else
	// {
	// AppContext.makeToast(CZ_PG_Assess.this, "error_connectDataBase");
	// return;
	// }
	//
	// }
	//
	// @Override
	// public void onFailure(HttpException error, String msg)
	// {
	// AppContext.makeToast(CZ_PG_Assess.this, "error_connectServer");
	// }
	// });
	// }

	private int getScore(String score)
	{

		if (score.equals("优"))
		{
			return 10;
		} else if (score.equals("合格"))
		{
			return 8;
		} else if (score.equals("不合格"))
		{
			return 0;
		}
		return 0;
	}
}

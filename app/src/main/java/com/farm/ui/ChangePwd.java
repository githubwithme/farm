package com.farm.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.common.SqliteDb;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.changepwd)
public class ChangePwd extends Activity
{
	@ViewById
	Button btn_sure;
	@ViewById
	EditText et_confirmnewpwd;
	@ViewById
	EditText et_newpwd;
	@ViewById
	EditText et_oldpwd;

	@Click
	void btn_sure()
	{
		if (!et_oldpwd.getText().toString().equals("") && !et_newpwd.getText().toString().equals("") && !et_confirmnewpwd.getText().equals(""))
		{
			if (et_newpwd.getText().toString().equals(et_confirmnewpwd.getText().toString()))
			{
				changePassword();
			} else
			{
				Toast.makeText(ChangePwd.this, "两次密码不一致!请重新输入", Toast.LENGTH_SHORT).show();
			}
		} else
		{
			Toast.makeText(ChangePwd.this, "请输入密码!", Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getActionBar().hide();
	}

	private void changePassword()
	{
		commembertab commembertab = AppContext.getUserInfo(this);
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("workuserid", commembertab.getId());
		params.addQueryStringParameter("userPwdold", et_oldpwd.getText().toString());
		params.addQueryStringParameter("userPwd1", et_newpwd.getText().toString());
		params.addQueryStringParameter("userPwd2", et_confirmnewpwd.getText().toString());
		params.addQueryStringParameter("action", "editPwd");
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
					Toast.makeText(ChangePwd.this, "修改成功!", Toast.LENGTH_SHORT).show();
					CleanLoginInfo();
					Intent intent = new Intent(ChangePwd.this, Login_.class);
					startActivity(intent);
					finish();

				} else
				{
					Toast.makeText(ChangePwd.this, result.getException().toString(), Toast.LENGTH_SHORT).show();
					return;
				}

			}

			@Override
			public void onFailure(HttpException arg0, String arg1)
			{
				AppContext.makeToast(ChangePwd.this, "error_connectServer");
			}
		});
	}

	private void CleanLoginInfo()
	{
		SharedPreferences sp = ChangePwd.this.getSharedPreferences("userInfo", ChangePwd.this.MODE_PRIVATE);
		String userName = sp.getString("userName", "");
		commembertab commembertab = (commembertab) SqliteDb.getCurrentUser(ChangePwd.this, commembertab.class, userName);
		commembertab.setuserPwd("");
		commembertab.setAutoLogin("0");
		SqliteDb.existSystem(ChangePwd.this, commembertab);
	}
}

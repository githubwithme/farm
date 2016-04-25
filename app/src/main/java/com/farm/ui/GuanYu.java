package com.farm.ui;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.farm.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.guanyu)
public class GuanYu extends Activity
{
	PackageInfo packageInfo = null;
	@ViewById
	TextView tv_gy;
	@Click
	void btn_back()
	{
		finish();
	}
@AfterViews
void Afteroncreate()
{
	String localVersion = packageInfo.versionName;
	tv_gy.setText("当前版本为："+localVersion);
}
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getActionBar().hide();
//		packageInfo = getActivity().getApplicationContext().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
//		packageInfo = GuanYu.this.getPackageManager().getPackageInfo(GuanYu.this.getPackageName(),0);;
		try {
			packageInfo=getApplicationContext().getPackageManager().getPackageInfo(getPackageName(),0);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
	}
	@Override
	protected void onChildTitleChanged(Activity childActivity, CharSequence title)
	{
		super.onChildTitleChanged(childActivity, title);
	}
}

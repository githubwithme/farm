package com.farm.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.farm.R;
import com.farm.app.AppContext;
import com.farm.app.AppManager;
import com.farm.bean.commembertab;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.common_pqyqcommandlist)
public class Common_PQYQCommandList extends Activity
{
	Fragment mContent = new Fragment();
	Common_PQYQCommandFragment common_PQYQCommandFragment;
	commembertab commembertab;

	String workuserid;
	@AfterViews
	void afterOncreate()
	{
		switchContent(mContent, common_PQYQCommandFragment);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		commembertab = AppContext.getUserInfo(this);
		workuserid=getIntent().getStringExtra("workuserid");
		common_PQYQCommandFragment = new Common_PQYQCommandFragment_();
		Bundle bundle = new Bundle();
		bundle.putString("workuserid", workuserid);
		common_PQYQCommandFragment.setArguments(bundle);
		AppManager.getAppManager().addActivity(this);
	}

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

package com.farm.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.planttab;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.growthtree)
public class GrowthTree extends Activity
{
	planttab planttab;
	Fragment mContent = new Fragment();
	TreeFragment treeFragment;

	@ViewById
	TextView tv_title;
	@ViewById
	TextView tv_yq;
	@ViewById
	TextView tv_pq;
	@ViewById
	TextView tv_plantid;

	@AfterViews
	void afterOncreate()
	{
		tv_title.setText(planttab.getplantName());
		tv_plantid.setText("植株ID:" + planttab.getId());
		tv_yq.setText(planttab.getparkName());
		tv_pq.setText(planttab.getareaName());
		switchContent(mContent, treeFragment);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		planttab = getIntent().getParcelableExtra("bean");
		treeFragment = new TreeFragment_();
		Bundle bundle = new Bundle();
		bundle.putParcelable("bean", planttab);
		treeFragment.setArguments(bundle);
	}

	public void setTitle(String str)
	{
		tv_title.setText(str);
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

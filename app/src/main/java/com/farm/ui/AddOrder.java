package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;

import com.farm.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;


/**
 * 1、填写订单信息：订单信息、销售明细
 * 2、确定订单后更新产品批次表（productbatch）
 * 3、刷新产品页面
 */
@EActivity(R.layout.addorder)
public class AddOrder extends Activity
{

//	@ViewById
//	FrameLayout fl_new;
//
//
//	@Click
//	void tl_home()
//	{
//
//	}



	@AfterViews
	void afterOncreate()
	{

	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getActionBar().hide();

	}





}

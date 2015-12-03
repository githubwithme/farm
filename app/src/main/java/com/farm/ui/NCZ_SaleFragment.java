package com.farm.ui;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.farm.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * @author :hc-sima
 * @version :1.0
 * @createTime：2015-8-14 下午2:19:43
 * @description :主界面
 */
@EFragment
public class NCZ_SaleFragment extends Fragment
{
	Fragment mContent = new Fragment();
	@ViewById
	FrameLayout container_weather;
	@ViewById
	FrameLayout container_work;



	@Click
	void btn_ckc()
	{
		Intent intent = new Intent(getActivity(), GoodList_.class);
		getActivity().startActivity(intent);
	}


	@Click
	void btn_product()
	{
		Intent intent = new Intent(getActivity(), ProductBatchList_.class);
		getActivity().startActivity(intent);
	}


	@Click
	void btn_xs()
	{
		Intent intent = new Intent(getActivity(), SaleList_.class);
		getActivity().startActivity(intent);
	}


	@AfterViews
	void afterOncreate()
	{
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.ncz_salefragment, container, false);
		return rootView;
	}

}

package com.farm.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.bean.plantgrowthtab;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :hc-sima
 * @version :1.0
 * @createTime：2015-8-14 下午6:33:24
 * @description :展示动物类
 */
@SuppressLint("NewApi")
@EActivity(R.layout.showplantgrowth)
public class ShowPlantGrowth extends Activity
{
	LinearLayout ll_video;
	Fragment mContent_container = new Fragment();
	Fragment mContent_container_more = new Fragment();
	plantgrowthtab plantgrowthtab;
	TextView tv_wNum;
	TextView tv_yNum;
	TextView tv_hNum;
	TextView tv_yColor;
	TextView tv_xNum;
	TextView tv_cDate;
	TextView tv_zDate;
	OtherFragment otherFragment;
	FoundationFragment foundationFragment;
	@ViewById
	Button btn_foundation;
	@ViewById
	TextView tv_title;
	@ViewById
	ImageButton imgbtn_back;
	@ViewById
	Button btn_other;
	@ViewById
	FrameLayout fl_contain;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		plantgrowthtab = (plantgrowthtab) getIntent().getParcelableExtra("bean");
		otherFragment = new OtherFragment();
		foundationFragment = new FoundationFragment();
	}

	@AfterViews
	void afterOncreate()
	{
		btn_foundation.setSelected(true);
		btn_foundation.setTextColor(getResources().getColor(R.color.black));
		switchcontainer(R.id.fl_contain, mContent_container, foundationFragment);
		show(plantgrowthtab);
		setImage(plantgrowthtab.getImgUrl());
	}

	@Click
	void imgbtn_back()
	{
		finish();
	}

	@Click
	void btn_foundation()
	{
		switchcontainer(R.id.fl_contain, mContent_container, foundationFragment);
		btn_other.setSelected(false);
		btn_foundation.setSelected(true);
		btn_foundation.setTextColor(getResources().getColor(R.color.red));
		btn_other.setTextColor(getResources().getColor(R.color.black));
	}

	private void show(plantgrowthtab plantgrowthtab)
	{
		tv_title.setText(plantgrowthtab.getplantName());
		btn_foundation.setText(plantgrowthtab.getregDate().substring(0, plantgrowthtab.getregDate().lastIndexOf(" ")) + "观察情况");
		btn_foundation.setTextColor(getResources().getColor(R.color.red));
	}

	@Click
	void btn_other()
	{
		switchcontainer(R.id.fl_contain, mContent_container, otherFragment);
		btn_foundation.setSelected(false);
		btn_other.setSelected(true);
		btn_other.setTextColor(getResources().getColor(R.color.red));
		btn_foundation.setTextColor(getResources().getColor(R.color.black));
	}

	class FoundationFragment extends Fragment
	{
		@Override
		public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
		{
			View rootView = inflater.inflate(R.layout.foundationfragment_plantgrowth, container, false);
			tv_hNum = (TextView) rootView.findViewById(R.id.tv_hNum);
			tv_wNum = (TextView) rootView.findViewById(R.id.tv_wNum);
			tv_yNum = (TextView) rootView.findViewById(R.id.tv_yNum);
			tv_yColor = (TextView) rootView.findViewById(R.id.tv_yColor);
			tv_xNum = (TextView) rootView.findViewById(R.id.tv_xNum);
			tv_cDate = (TextView) rootView.findViewById(R.id.tv_cDate);
			tv_zDate = (TextView) rootView.findViewById(R.id.tv_zDate);

			tv_hNum.setText(plantgrowthtab.gethNum());
			tv_wNum.setText(plantgrowthtab.getwNum());
			tv_yNum.setText(plantgrowthtab.getyNum());
			tv_yColor.setText(plantgrowthtab.getyColor());
			tv_xNum.setText(plantgrowthtab.getxNum());
			if (!plantgrowthtab.getcDate().equals(""))
			{
				tv_cDate.setText(plantgrowthtab.getcDate().substring(5, 10));
			}
			if (!plantgrowthtab.getzDate().equals(""))
			{
				tv_zDate.setText(plantgrowthtab.getzDate().substring(5, 10));
			}
			return rootView;
		}
	}

	class OtherFragment extends Fragment
	{
		@Override
		public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
		{
			View rootView = inflater.inflate(R.layout.otherinfomationfragment_animal, container, false);
			TextView tv_cjr = (TextView) rootView.findViewById(R.id.tv_cjr);
			TextView tv_cjsj = (TextView) rootView.findViewById(R.id.tv_cjsj);
			TextView tv_xgr = (TextView) rootView.findViewById(R.id.tv_xgr);
			TextView tv_xgsj = (TextView) rootView.findViewById(R.id.tv_xgsj);

			tv_xgsj.setVisibility(View.GONE);
			tv_cjr.setText(plantgrowthtab.getcjUserName());
			tv_cjsj.setText(plantgrowthtab.getcjDate());
			tv_xgr.setText(plantgrowthtab.getcjUserName());
			return rootView;
		}
	}

	public void switchcontainer(int resource, Fragment from, Fragment to)
	{
		if (mContent_container != to)
		{
			mContent_container = to;
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			if (!to.isAdded())
			{ // 先判断是否被add过
				transaction.hide(from).add(resource, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
			} else
			{
				transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
			}
		}
	}

	public void switchcontainermore(int resource, Fragment from, Fragment to)
	{
		if (mContent_container_more != to)
		{
			mContent_container_more = to;
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			if (!to.isAdded())
			{ // 先判断是否被add过
				transaction.hide(from).add(resource, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
			} else
			{
				transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
			}
		}
	}

	private void setImage(List<String> imglist)
	{
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < imglist.size(); i++)
		{
			list.add(AppConfig.baseurl + imglist.get(i));
		}
		PictureScrollFragment pictureScrollFragment = new PictureScrollFragment();
		Bundle bundle = new Bundle();
		bundle.putStringArrayList("imgurl", (ArrayList<String>) list);
		pictureScrollFragment.setArguments(bundle);
		getFragmentManager().beginTransaction().replace(R.id.img_container, pictureScrollFragment).commit();
	}
}

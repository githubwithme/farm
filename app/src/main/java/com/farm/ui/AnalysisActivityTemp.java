package com.farm.ui;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.farm.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@SuppressLint("NewApi")
@EActivity(R.layout.analysisactivitytemp)
public class AnalysisActivityTemp extends FragmentActivity
{
	private DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
	private ViewPager mViewPager;
	ActionBar actionBar;
	private String actionBarTab;
	ActionBar.TabListener tabListener;

	@Click
	void btn_back()
	{
		finish();
	}

	@AfterViews
	void afterOncreate()
	{
		init();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	private void init()
	{
		String[] str = new String[] { "销售", "生产", "库存" };
		mViewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		tabListener = new ActionBar.TabListener()
		{
			@Override
			public void onTabSelected(Tab tab, android.app.FragmentTransaction ft)
			{
				mViewPager.setCurrentItem(tab.getPosition());// 点击（不是滑动）tab标签时切换到相应的内容页
			}

			@Override
			public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft)
			{
			}

			@Override
			public void onTabReselected(Tab tab, android.app.FragmentTransaction ft)
			{
			}
		};
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
		{
			@Override
			public void onPageSelected(int position)
			{
				actionBar.setSelectedNavigationItem(position);// 滑动中间的内容页，让操作栏的标签页高亮（不是切换页面），同时该方法会触发下面的onTabSelected方法
			}
		});
		mDemoCollectionPagerAdapter = new DemoCollectionPagerAdapter(getSupportFragmentManager(), str);
		mViewPager.setAdapter(mDemoCollectionPagerAdapter);
	}

	/**
	 * 循环加载pager页面
	 * 
	 * @author Administrator
	 * 
	 */
	public class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter
	{
		String[] str;

		public DemoCollectionPagerAdapter(FragmentManager fm)
		{
			super(fm);
		}

		public DemoCollectionPagerAdapter(FragmentManager fm, String[] str)
		{
			super(fm);
			this.str = str;
		}

		@Override
		public Fragment getItem(int i)
		{
			Fragment fragment = null;
			if (str[i].equals("生产"))
			{
				fragment = new ProductAnalysisFragment();
			} else if (str[i].equals("销售"))
			{
				fragment = new SaleAnalysisFragment();
			} else if (str[i].equals("库存"))
			{
				fragment = new GoodAnalysisFragment();
			}
			return fragment;
		}

		@Override
		public int getCount()
		{
			return str.length;
		}

		@Override
		public CharSequence getPageTitle(int position)
		{
			return "OBJECT " + (position + 1);
		}
	}

	class ProductAnalysisFragment extends Fragment
	{
		Bundle bundle;
		View view;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
			View rootView = inflater.inflate(R.layout.ifragment, container, false);
			return rootView;

		}
	}

	class SaleAnalysisFragment extends Fragment
	{
		Bundle bundle;
		View view;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{

			View rootView = inflater.inflate(R.layout.costlist, container, false);
			return rootView;

		}
	}

	class GoodAnalysisFragment extends Fragment
	{
		Bundle bundle;
		View view;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
			View rootView = inflater.inflate(R.layout.commandlist, container, false);
			return rootView;

		}
	}
}

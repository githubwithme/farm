package com.farm.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.Model;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_scrollgrid)
public class GoodList extends FragmentActivity
{
	SelectorFragment selectorUi;
	Fragment mContent = new Fragment();
	private String[] list;
	private TextView[] tvList;
	private View[] views;
	private LayoutInflater inflater;
	private int currentItem = 0;
	private ShopAdapter shopAdapter;

	@ViewById
	ImageView iv_dowm_tab;
	@ViewById
	ScrollView tools_scrlllview;
	@ViewById
	LinearLayout tools;
	@ViewById
	ViewPager goods_pager;

	@AfterViews
	void afterOncreate()
	{
		shopAdapter = new ShopAdapter(getSupportFragmentManager());
		inflater = LayoutInflater.from(this);
		showToolsView();
		initPager();
		// switchContent(mContent, selectorUi);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getActionBar().hide();
	}

	public void switchContent(Fragment from, Fragment to)
	{
		if (mContent != to)
		{
			mContent = to;
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			if (!to.isAdded())
			{ // 先判断是否被add过
				transaction.hide(from).add(R.id.top_container, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
			} else
			{
				transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
			}
		}
	}

	/**
	 * 动态生成显示items中的textview
	 */
	private void showToolsView()
	{
		list = Model.toolsList;
		tvList = new TextView[list.length];
		views = new View[list.length];

		for (int i = 0; i < list.length; i++)
		{
			View view = inflater.inflate(R.layout.item_list_layout, null);
			view.setId(i);
			view.setOnClickListener(toolsItemListener);
			TextView textView = (TextView) view.findViewById(R.id.text);
			textView.setText(list[i]);
			tools.addView(view);
			tvList[i] = textView;
			views[i] = view;
		}
		changeTextColor(0);
	}

	private View.OnClickListener toolsItemListener = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			goods_pager.setCurrentItem(v.getId());
		}
	};

	/**
	 * initPager<br/>
	 * 初始化ViewPager控件相关内容
	 */
	private void initPager()
	{
		goods_pager.setAdapter(shopAdapter);
		goods_pager.setOnPageChangeListener(onPageChangeListener);
	}

	/**
	 * OnPageChangeListener<br/>
	 * 监听ViewPager选项卡变化事的事件
	 */
	private OnPageChangeListener onPageChangeListener = new OnPageChangeListener()
	{
		@Override
		public void onPageSelected(int arg0)
		{
			if (goods_pager.getCurrentItem() != arg0)
				goods_pager.setCurrentItem(arg0);
			if (currentItem != arg0)
			{
				changeTextColor(arg0);
				changeTextLocation(arg0);
			}
			currentItem = arg0;
		}

		@Override
		public void onPageScrollStateChanged(int arg0)
		{
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2)
		{
		}
	};

	/**
	 * ViewPager 加载选项卡
	 * 
	 * @author Administrator
	 * 
	 */
	private class ShopAdapter extends FragmentPagerAdapter
	{
		public ShopAdapter(FragmentManager fm)
		{
			super(fm);
		}

		@Override
		public Fragment getItem(int index)
		{
			Fragment fragment = new ProTypeFragment();
			Bundle bundle = new Bundle();
			bundle.putInt("index", index);
			fragment.setArguments(bundle);
			return fragment;
		}

		@Override
		public int getCount()
		{
			return list.length;
		}
	}

	/**
	 * 改变textView的颜色
	 * 
	 * @param id
	 */
	private void changeTextColor(int id)
	{
		for (int i = 0; i < tvList.length; i++)
		{
			if (i != id)
			{
				tvList[i].setBackgroundColor(0x00000000);
				tvList[i].setTextColor(0xFF000000);
			}
		}
		tvList[id].setBackgroundColor(0xFFFFFFFF);
		tvList[id].setTextColor(0xFFFF5D5E);
	}

	/**
	 * 改变栏目位置
	 * 
	 * @param clickPosition
	 */
	private void changeTextLocation(int clickPosition)
	{
		int x = (views[clickPosition].getTop());
		tools_scrlllview.smoothScrollTo(0, x);
	}

}
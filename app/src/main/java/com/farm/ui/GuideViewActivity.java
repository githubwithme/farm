package com.farm.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.farm.R;
import com.farm.adapter.ViewPagerAdapter;

import java.util.ArrayList;

/**
 * @author HMJ
 * @version 5 功能描述： 1显示通知栏图标，实现引导界面，记录第一次使用和提交手机uuid到服务器 2其后每次使用直接进入开屏界面
 */
public class GuideViewActivity extends Activity
{
	// 定义ViewPager对象
	private ViewPager viewPager;
	// 定义ViewPager适配器
	private ViewPagerAdapter vpAdapter;
	// 定义一个ArrayList来存放View
	private ArrayList<View> views;
	// 定义各个界面View对象
	private View view1, view2, view3, view4, view5, view6;
	// 定义底部小点图片
	private ImageView pointImage1, pointImage2, pointImage3, pointImage4, pointImage5;
	// 定义开始按钮对象
	private Button btn_Start;
	private String firstUse;
	SharedPreferences sp;
	// 当前的位置索引值
	private int currIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		TrayNotification.addNotification(GuideViewActivity.this, R.drawable.logo, "欢迎使用农场易");// 让程序图标显示在托盘通知栏
		sp = this.getSharedPreferences("MY_PRE", MODE_PRIVATE);
		firstUse = sp.getString("firstUse", "");
		if (firstUse.equals("true"))
		{
			Intent intent1 = new Intent(GuideViewActivity.this, GuideViewDoor.class);
			startActivity(intent1);
			GuideViewActivity.this.finish();
		} else
		{
			setContentView(R.layout.guidemain);
			initView();
			Editor editor = sp.edit();
			editor.putString("firstUse", "true");
			editor.commit();
		}
	}

	/**
	 * 初始化组件
	 */
	private void initView()
	{
		viewPager = (ViewPager) this.findViewById(R.id.viewpager);
		pointImage1 = (ImageView) findViewById(R.id.page1);
		pointImage2 = (ImageView) findViewById(R.id.page2);
		pointImage3 = (ImageView) findViewById(R.id.page3);
		pointImage4 = (ImageView) findViewById(R.id.page4);
		LayoutInflater inflater = LayoutInflater.from(this);
		view1 = inflater.inflate(R.layout.guide_view01, null);
		view2 = inflater.inflate(R.layout.guide_view02, null);
		view3 = inflater.inflate(R.layout.guide_view03, null);
		view4 = inflater.inflate(R.layout.guide_view04, null);
		btn_Start = (Button) view4.findViewById(R.id.btn_Start);
		views = new ArrayList<View>();
		views.add(view1);
		views.add(view2);
		views.add(view3);
		views.add(view4);
		vpAdapter = new ViewPagerAdapter(views);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		viewPager.setAdapter(vpAdapter);
		vpAdapter.notifyDataSetChanged();
		btn_Start.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				Intent intent = new Intent(GuideViewActivity.this, GuideViewDoor.class);
				startActivity(intent);
				GuideViewActivity.this.finish();
			}
		});
	}

	public class MyOnPageChangeListener implements OnPageChangeListener
	{
		@Override
		public void onPageSelected(int position)
		{
			switch (position)
			{
			case 0:
				pointImage1.setImageDrawable(getResources().getDrawable(R.drawable.page_indicator_focused));
				pointImage2.setImageDrawable(getResources().getDrawable(R.drawable.page_indicator_unfocused));
				break;
			case 1:
				pointImage2.setImageDrawable(getResources().getDrawable(R.drawable.page_indicator_focused));
				pointImage1.setImageDrawable(getResources().getDrawable(R.drawable.page_indicator_unfocused));
				pointImage3.setImageDrawable(getResources().getDrawable(R.drawable.page_indicator_unfocused));
				break;
			case 2:
				pointImage3.setImageDrawable(getResources().getDrawable(R.drawable.page_indicator_focused));
				pointImage4.setImageDrawable(getResources().getDrawable(R.drawable.page_indicator_unfocused));
				pointImage2.setImageDrawable(getResources().getDrawable(R.drawable.page_indicator_unfocused));
				break;
			case 3:
				pointImage4.setImageDrawable(getResources().getDrawable(R.drawable.page_indicator_focused));
				pointImage3.setImageDrawable(getResources().getDrawable(R.drawable.page_indicator_unfocused));
				break;
			}
			currIndex = position;
		}

		@Override
		public void onPageScrollStateChanged(int arg0)
		{
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2)
		{
		}
	}

}

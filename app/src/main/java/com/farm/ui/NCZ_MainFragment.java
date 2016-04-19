package com.farm.ui;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.farm.R;
import com.farm.common.utils;
import com.farm.widget.PullToRefreshListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :hc-sima
 * @version :1.0
 * @createTime：2015-8-14 下午2:19:43
 * @description :主界面
 */
@EFragment
public class NCZ_MainFragment extends Fragment
{
	Fragment mContent = new Fragment();
	Fragment mContent_todayjob = new Fragment();
	NCZ_YQPQ ncz_WorkList;
	@ViewById
	PullToRefreshListView frame_listview_news;
	@ViewById
	ScrollView sl;
	@ViewById
	Button btn_zg;
	@ViewById
	TextView tv_day;
	@ViewById
	TextView tv_title;
	@Override
	public void onHiddenChanged(boolean hidden)
	{
		super.onHiddenChanged(hidden);
		ncz_WorkList.setThreadStatus(hidden);
	}

	@AfterViews
	void afterOncreate()
	{
		// tv_title.setTypeface(FontManager.getTypefaceByFontName(getActivity(),
		// "wsyh.ttf"));
		ncz_WorkList = new NCZ_YQPQ_();
		tv_day.setText(utils.getTodayAndwWeek());
		tv_title.setText("首页");
		switchContent_todayjob(mContent_todayjob, ncz_WorkList);
		setImage(initURL());
		setMenu();
	}

	@Click
	void btn_zg()
	{
		Intent intent = new Intent(getActivity(), DaoGangList_.class);
		getActivity().startActivity(intent);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.ncz_fragment_main, container, false);
		return rootView;
	}

	public void switchContent_todayjob(Fragment from, Fragment to)
	{
		if (mContent_todayjob != to)
		{
			mContent_todayjob = to;
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			if (!to.isAdded())
			{ // 先判断是否被add过
				transaction.hide(from).add(R.id.container_todayjob, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
			} else
			{
				transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
			}
		}
	}

	private List<String> initURL()
	{
		List<String> list = new ArrayList<String>();
		list.add("http://pic4.nipic.com/20090827/3095621_083213047918_2.jpg");
		list.add("http://pica.nipic.com/2008-07-22/2008722162232801_2.jpg");
		list.add("http://pic1a.nipic.com/2008-10-15/2008101517835380_2.jpg");
		return list;
	}

	private void setMenu()
	{
		MenuScrollFragment menuScrollFragment = new MenuScrollFragment();
		getFragmentManager().beginTransaction().replace(R.id.menu_container, menuScrollFragment).commit();
	}

	private void setImage(List<String> imglist)
	{
		PictureScrollFragment pictureScrollFragment = new PictureScrollFragment();
		Bundle bundle = new Bundle();
		bundle.putStringArrayList("imgurl", (ArrayList<String>) imglist);
		pictureScrollFragment.setArguments(bundle);
		getFragmentManager().beginTransaction().replace(R.id.img_container, pictureScrollFragment).commit();
	}
}

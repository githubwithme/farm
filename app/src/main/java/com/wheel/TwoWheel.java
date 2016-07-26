package com.wheel;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.farm.R;

import java.util.ArrayList;
import java.util.HashMap;

public class TwoWheel implements OnWheelChangedListener, WheelInterface
{
	static ArrayWheelAdapter firstWheelAdapter;
	static ArrayWheelAdapter secondWheelAdapter;
	private static PopupWindow popupWindow;
	private static View popupWindowView;
	private static WheelView firstItemView;
	private static WheelView secondItemView;
	static TextView tv_showfirstItemselected;
	static TwoWheel twoWheel;
	protected static String[] firstItemData;
	protected static HashMap<String, String[]> secondItemData = new HashMap<String, String[]>();

	protected static String firstItemSelected;
	protected static String secondItemSelected;
	static int firstPostion;
	static int secondPostion;
	static Context context;

	public TwoWheel(Context context)
	{
		TwoWheel.context = context;
	}

	public void buildWheel(WheelView firstItemView, WheelView secondItemView, String[] firstItemData, HashMap<String, String[]> secondItemData)
	{
		TwoWheel.firstItemView = firstItemView;
		TwoWheel.secondItemView = secondItemView;
		TwoWheel.firstItemData = firstItemData;
		TwoWheel.secondItemData = secondItemData;
		setUpListener();
		setUpData();
	}

	private void setUpListener()
	{
		// 添加change事件
		firstItemView.addChangingListener(this);
		// 添加change事件
		secondItemView.addChangingListener(this);
	}

	private void setUpData()
	{
		firstWheelAdapter = new ArrayWheelAdapter<String>(context, firstItemData);
		firstItemView.setViewAdapter(firstWheelAdapter);// adapter中可以设置字体颜色大小
		// 设置可见条目数量
		firstItemView.setVisibleItems(0);
		secondItemView.setVisibleItems(0);
		firstItemView.setCurrentItem(0);
		setTextviewSize(firstItemData[0], firstWheelAdapter);
		updateCities();
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue)
	{
		// TODO Auto-generated method stub
		if (wheel == firstItemView)
		{
			updateCities();
		} else if (wheel == secondItemView)
		{
			firstPostion = secondItemView.getCurrentItem();
			secondItemSelected = secondItemData.get(firstItemSelected)[firstPostion];
		}
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private static void updateCities()
	{
		secondPostion = firstItemView.getCurrentItem();
		firstItemSelected = firstItemData[secondPostion];
		String[] cities = secondItemData.get(firstItemSelected);
		if (cities == null)
		{
			cities = new String[] { "" };
		}
		secondWheelAdapter = new ArrayWheelAdapter<String>(context, cities);
		secondItemView.setViewAdapter(secondWheelAdapter);
		secondItemView.setCurrentItem(0);
		secondItemSelected = secondItemData.get(firstItemSelected)[0];
		setTextviewSize(twoWheel.getSecondItemSelected(), secondWheelAdapter);
	}

	public String getFirstItemSelected()
	{
		return firstItemSelected;
	}

	public String getSecondItemSelected()
	{
		return secondItemSelected;
	}

	/**
	 * 设置字体大小
	 * 
	 * @param curriteItemText
	 * @param arrayWheelAdapter
	 */
	public static void setTextviewSize(String curriteItemText, ArrayWheelAdapter arrayWheelAdapter)
	{
		ArrayList<View> arrayList = arrayWheelAdapter.getTestViews();
		int size = arrayList.size();
		String currentText;
		for (int i = 0; i < size; i++)
		{
			TextView textvew = (TextView) arrayList.get(i);
			currentText = textvew.getText().toString();
			if (curriteItemText.equals(currentText))
			{
				textvew.setTextSize(26);
				textvew.setTextColor(context.getResources().getColor(R.color.bg_yellow));
			} else
			{
				textvew.setTextSize(20);
				textvew.setTextColor(context.getResources().getColor(R.color.black));
			}
		}
	}

	public static void showTwoWheel(Context context, TextView textView, final String[] firstItemid, final String[] firstItemData, final HashMap<String, String[]> secondItemid, final HashMap<String, String[]> secondItemData)
	{
		if (popupWindow != null && popupWindow.isShowing())
		{
			Toast.makeText(context, "请勿重复点击！", Toast.LENGTH_SHORT).show();
		} else
		{
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			popupWindowView = inflater.inflate(R.layout.twowheel_in_bottom, null);
			popupWindow = new PopupWindow(popupWindowView, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, true);
			firstItemView = (WheelView) popupWindowView.findViewById(R.id.firstItem);
			secondItemView = (WheelView) popupWindowView.findViewById(R.id.secondItem);
			twoWheel = new TwoWheel(context);
			twoWheel.buildWheel(firstItemView, secondItemView, firstItemData, secondItemData);
			popupWindow.setAnimationStyle(R.style.bottominbottomout);// 设置PopupWindow的弹出和消失效果
			Button confirmButton = (Button) popupWindowView.findViewById(R.id.confirmButton);
			popupWindow.showAtLocation(confirmButton, Gravity.CENTER, 0, 0);
			tv_showfirstItemselected = textView;
			confirmButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View arg0)
				{
					popupWindow.dismiss();
					Bundle bundle = new Bundle();
					bundle.putString("FN", twoWheel.getFirstItemSelected());
					bundle.putString("SN", twoWheel.getSecondItemSelected());
					bundle.putString("FI", firstItemid[firstItemView.getCurrentItem()]);
					bundle.putString("SI", secondItemid.get(firstItemid[firstItemView.getCurrentItem()])[secondItemView.getCurrentItem()]);
					tv_showfirstItemselected.setTag(bundle);
					tv_showfirstItemselected.setText(twoWheel.getFirstItemSelected() + twoWheel.getSecondItemSelected());
				}
			});
			Button cancleButton = (Button) popupWindowView.findViewById(R.id.cancleButton);
			cancleButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View arg0)
				{
					popupWindow.dismiss();
				}
			});
		}

		firstItemView.addChangingListener(new OnWheelChangedListener()
		{

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue)
			{
				setTextviewSize(twoWheel.getFirstItemSelected(), firstWheelAdapter);
				updateCities();
			}
		});

		firstItemView.addScrollingListener(new OnWheelScrollListener()
		{

			@Override
			public void onScrollingStarted(WheelView wheel)
			{

			}

			@Override
			public void onScrollingFinished(WheelView wheel)
			{
				setTextviewSize(twoWheel.getFirstItemSelected(), firstWheelAdapter);
				updateCities();
			}
		});

		secondItemView.addChangingListener(new OnWheelChangedListener()
		{

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue)
			{
				setTextviewSize(twoWheel.getSecondItemSelected(), secondWheelAdapter);
			}
		});

		secondItemView.addScrollingListener(new OnWheelScrollListener()
		{

			@Override
			public void onScrollingStarted(WheelView wheel)
			{

			}

			@Override
			public void onScrollingFinished(WheelView wheel)
			{
				setTextviewSize(twoWheel.getSecondItemSelected(), secondWheelAdapter);
			}
		});
	}
}

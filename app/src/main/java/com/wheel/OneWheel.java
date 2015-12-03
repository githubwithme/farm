package com.wheel;

import android.annotation.SuppressLint;
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

@SuppressLint("NewApi")
public class OneWheel implements OnWheelChangedListener, WheelInterface, OnWheelClickedListener
{
	static ArrayWheelAdapter arrayWheelAdapter;
	private static View popupWindowView;
	private static PopupWindow popupWindow;
	private static WheelView firstItemView;
	protected static String[] firstItemData;
	protected static String[] firstItemId;
	static String firstItemSelected;
	protected static int pCurrent;
	static Context context;
	public static String ID;
	static TextView tv_showfirstItemselected;
	static OneWheel onewheel;

	public OneWheel(Context context)
	{
		this.context = context;
	}

	public void setID(String iD)
	{
		ID = iD;
	}

	public static String getID()
	{
		return ID;
	}

	public void buildWheel(WheelView firstItemView, String[] firstItemData)
	{
		this.firstItemView = firstItemView;
		this.firstItemData = firstItemData;
		setUpListener();
		setUpData();
	}

	private void setUpListener()
	{
		// 添加change事件
		firstItemView.addChangingListener(this);
		firstItemView.addClickingListener(this);
	}

	private void setUpData()
	{
		arrayWheelAdapter = new ArrayWheelAdapter<String>(context, firstItemData);
		firstItemView.setViewAdapter(arrayWheelAdapter);
		// 设置可见条目数量
		firstItemView.setVisibleItems(12);
		firstItemView.setCurrentItem(0);
		firstItemSelected = firstItemData[0];
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue)
	{
		if (wheel == firstItemView)
		{
			pCurrent = firstItemView.getCurrentItem();
			firstItemSelected = firstItemData[pCurrent];
		}
	}

	public static String getFirstItemSelected()
	{
		return firstItemSelected;
	}

	public static void showWheel(Context context, String[] firstId, String[] firstData, TextView textView)
	{
		tv_showfirstItemselected = textView;
		firstItemId = firstId;
		firstItemData = firstData;
		if (popupWindow != null && popupWindow.isShowing())
		{
			Toast.makeText(context, "请勿重复点击！", Toast.LENGTH_SHORT).show();
		} else
		{
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			popupWindowView = inflater.inflate(R.layout.onewheel_in_bottom, null);
			popupWindow = new PopupWindow(popupWindowView, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, true);
			firstItemView = (WheelView) popupWindowView.findViewById(R.id.firstItem);
			onewheel = new OneWheel(context);
			onewheel.buildWheel(firstItemView, firstItemData);
			popupWindow.setAnimationStyle(R.style.bottominbottomout);// 设置PopupWindow的弹出和消失效果
			Button confirmButton = (Button) popupWindowView.findViewById(R.id.confirmButton);
			popupWindow.showAtLocation(confirmButton, Gravity.CENTER, 0, 0);
			confirmButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View arg0)
				{
					popupWindow.dismiss();
					tv_showfirstItemselected.setText(onewheel.getFirstItemSelected());
					Bundle bundle = new Bundle();
					bundle.putString("FN", onewheel.getFirstItemSelected());
					bundle.putString("FI", firstItemId[firstItemView.getCurrentItem()]);
					tv_showfirstItemselected.setTag(bundle);
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
			firstItemView.addChangingListener(new OnWheelChangedListener()
			{
				@Override
				public void onChanged(WheelView wheel, int oldValue, int newValue)
				{
					// TODO Auto-generated method stub
					// String currentText = wheel.getFirstItemSelected();
					// strProvince = currentText;
					setTextviewSize(onewheel.getFirstItemSelected(), arrayWheelAdapter);
					// String[] citys = mCitisDatasMap.get(currentText);
					// initCitys(citys);
					// cityAdapter = new AddressTextAdapter(context, arrCitys,
					// 0, maxsize, minsize);
					// wvCitys.setVisibleItems(5);
					// wvCitys.setViewAdapter(cityAdapter);
					// wvCitys.setCurrentItem(0);
				}
			});
		}
		firstItemView.addScrollingListener(new OnWheelScrollListener()
		{

			@Override
			public void onScrollingStarted(WheelView wheel)
			{

			}

			@Override
			public void onScrollingFinished(WheelView wheel)
			{
				setTextviewSize(onewheel.getFirstItemSelected(), arrayWheelAdapter);
			}
		});
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
				textvew.setTextSize(24);
				textvew.setTextColor(context.getResources().getColor(R.color.bg_yellow));
			} else
			{
				textvew.setTextSize(22);
				textvew.setTextColor(context.getResources().getColor(R.color.black));
			}
		}
	}

	@Override
	public void onItemClicked(WheelView wheel, int itemIndex)
	{
		if (wheel == firstItemView)
		{
			int pCurrent = firstItemView.getCurrentItem();
			firstItemSelected = firstItemData[pCurrent];
			Toast.makeText(context, firstItemSelected, Toast.LENGTH_SHORT).show();
		}
	}
}

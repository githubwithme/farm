package com.farm.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.farm.R;
import com.farm.widget.CalendarView;
import com.farm.widget.CalendarView.OnItemClickListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author :hc-sima
 * @version :1.0
 * @createTime：2015-8-14 下午2:19:43
 * @description :主界面
 */
@EFragment
public class TimeSeLectorFragment extends Fragment
{
	@ViewById
	CalendarView calendar;
	@ViewById
	ImageButton calendarLeft;
	@ViewById
	TextView calendarCenter;
	@ViewById
	ImageButton calendarRight;
	SimpleDateFormat format;
	@ViewById
	ImageView iv_dowm_tab;
	@ViewById
	LinearLayout layout_calendar;
	@ViewById
	RelativeLayout rl_time;

	@Click
	void rl_time()
	{

		if (layout_calendar.isShown())
		{
			layout_calendar.setVisibility(View.GONE);
		} else
		{
			layout_calendar.setVisibility(View.VISIBLE);
		}

	}

	@AfterViews
	void afterOncreate()
	{
		format = new SimpleDateFormat("yyyy-MM-dd");
		calendar.setSelectMore(false); // 单选
		try
		{
			// 设置日历日期
			Date date = format.parse("2015-01-01");
			calendar.setCalendarData(date);
		} catch (ParseException e)
		{
			e.printStackTrace();
		}

		// 获取日历中年月 ya[0]为年，ya[1]为月（格式大家可以自行在日历控件中改）
		String[] ya = calendar.getYearAndmonth().split("-");
		calendarCenter.setText(ya[0] + "年" + ya[1] + "月");
		calendarLeft.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// 点击上一月 同样返回年月
				String leftYearAndmonth = calendar.clickLeftMonth();
				String[] ya = leftYearAndmonth.split("-");
				calendarCenter.setText(ya[0] + "年" + ya[1] + "月");
			}
		});

		calendarRight.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// 点击下一月
				String rightYearAndmonth = calendar.clickRightMonth();
				String[] ya = rightYearAndmonth.split("-");
				calendarCenter.setText(ya[0] + "年" + ya[1] + "月");
			}
		});

		// 设置控件监听，可以监听到点击的每一天（大家也可以在控件中根据需求设定）
		calendar.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void OnItemClick(Date selectedStartDate, Date selectedEndDate, Date downDate)
			{
				if (calendar.isSelectMore())
				{
					Toast.makeText(getActivity(), format.format(selectedStartDate) + "到" + format.format(selectedEndDate), Toast.LENGTH_SHORT).show();
					layout_calendar.setVisibility(View.GONE);
				} else
				{
					Toast.makeText(getActivity(), format.format(downDate), Toast.LENGTH_SHORT).show();
					layout_calendar.setVisibility(View.GONE);
				}
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.timeselectorfragment, container, false);
		return rootView;
	}

}

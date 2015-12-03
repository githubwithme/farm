package com.farm.widget;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class MyTimepicker
{

	private TextView mDateDisplay;
	private Button mPickDate;

	private int mHour;
	private int mMinute;

	Context context;

	public MyTimepicker(Context context)
	{
		this.context = context;
	}

	public Dialog getDialog()
	{
		// ��õ�ǰʱ��
		final Calendar c = Calendar.getInstance();
		mHour = c.get(Calendar.HOUR);
		mMinute = c.get(Calendar.MINUTE);
		return new TimePickerDialog(context, mTimeSetListener, mHour, mMinute, true);
	}

	public void updateDisplay(TextView textView)
	{
		textView.setText(new StringBuilder().append(mHour).append("-").append(mMinute + 1));
	}

	public void updateDisplay(EditText editText)
	{
		editText.setText(new StringBuilder().append(mHour).append("-").append(mMinute + 1));
	}

	public OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener()
	{
		@Override
		public void onTimeSet(TimePicker arg0, int hour, int minute)
		{
			mHour = hour;
			mMinute = minute;
		}
	};

}
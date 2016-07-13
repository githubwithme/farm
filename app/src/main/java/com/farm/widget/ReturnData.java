package com.farm.widget;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by hasee on 2016/7/13.
 */
public class ReturnData
{
    private TextView mDateDisplay;
    private Button mPickDate;

    private int mYear;
    private int mMonth;
    private int mDay;


    TextView textView = null;
    EditText editText = null;

    static final int DATE_DIALOG_ID = 0;
    Context context;

    public ReturnData(Context context, TextView textView)
    {
        this.context = context;
        this.textView = textView;
    }

    public ReturnData(Context context, EditText editText)
    {
        this.context = context;
        this.editText = editText;
    }

    public Dialog getDialog()
    {
        // ��õ�ǰʱ��
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        return new DatePickerDialog(context, mDateSetListener, mYear, mMonth, mDay);
    }

    // updates the date we display in the TextView
    public void updateDisplay(TextView textView)
    {
        textView.setText(new StringBuilder().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay).append(" "));

    }

    public void updateDisplay(EditText editText)
    {
        editText.setText(new StringBuilder().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay).append(" "));
    }

    public DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener()
    {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
        {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            if (textView == null)
            {
                updateDisplay(editText);
            } else
            {
                updateDisplay(textView);
            }

        }
    };
}

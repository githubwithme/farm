package com.farm.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.farm.R;

/**
 * Created by ${hmj} on 2016/6/27.
 */
public class CustomArrayAdapter extends ArrayAdapter<String>
{
    private Context mContext;
    private String[] mStringArray;

    public CustomArrayAdapter(Context context, String[] stringArray)
    {
        super(context, R.layout.spinner_item, stringArray);
        mContext = context;
        mStringArray = stringArray;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        //修改Spinner展开后的字体颜色
        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.spinner_dropdown_item, parent, false);
        }

        //此处text1是Spinner默认的用来显示文字的TextView
        TextView tv = (TextView) convertView.findViewById(R.id.tv_dropdown);
        tv.setText(mStringArray[position]);
        return convertView;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // 修改Spinner选择后结果的字体颜色
        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.spinner_item, parent, false);
        }

        //此处text1是Spinner默认的用来显示文字的TextView
        TextView tv = (TextView) convertView.findViewById(R.id.tv_spinner);
        tv.setText(mStringArray[position]);
        return convertView;
    }

}
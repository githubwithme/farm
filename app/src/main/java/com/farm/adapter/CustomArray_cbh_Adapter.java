package com.farm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.DepartmentBean;
import com.farm.bean.contractTab;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hasee on 2016/6/27.
 */
public class CustomArray_cbh_Adapter extends BaseAdapter
{
    private Context context;
    List<contractTab> listdata = new ArrayList<contractTab>();
    contractTab contractTab;
    private LayoutInflater listContainer;// 视图容器

    public CustomArray_cbh_Adapter(Context context, List<contractTab> listdata)
    {
        this.context = context;
        this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.listdata = listdata;
    }

    public int getCount()
    {
        return listdata.size();
    }

    public Object getItem(int arg0)
    {
        return null;
    }

    public long getItemId(int arg0)
    {
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        contractTab=listdata.get(position);
        // 修改Spinner选择后结果的字体颜色
        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.spinner_item, parent, false);
        }

        //此处text1是Spinner默认的用来显示文字的TextView
        TextView tv = (TextView) convertView.findViewById(R.id.tv_spinner);
        tv.setText(contractTab.getareaName()+"\n"+contractTab.getContractNum());
        return convertView;
    }
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        //修改Spinner展开后的字体颜色
        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.spinner_dropdown_item, parent, false);
        }

        //此处text1是Spinner默认的用来显示文字的TextView
        TextView tv = (TextView) convertView.findViewById(R.id.tv_dropdown);
        tv.setText(contractTab.getareaName()+"\n"+contractTab.getContractNum());
        return convertView;

    }
}

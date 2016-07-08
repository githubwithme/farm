package com.farm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.contractTab;

import java.util.HashMap;
import java.util.List;

public class Adapter_AreaBatchtimeBreakOff extends BaseAdapter
{
    private Context context;// 运行上下文
    private List<contractTab> listItems;// 数据集合
    private LayoutInflater listContainer;// 视图容器
    ListItemView listItemView = null;

    static class ListItemView
    {
        public TextView tv_allnumber;
        public TextView tv_contractname;
    }

    public Adapter_AreaBatchtimeBreakOff(Context context, List<contractTab> data)
    {
        this.context = context;
        this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.listItems = data;
    }

    public int getCount()
    {
        return listItems.size();
    }

    public Object getItem(int arg0)
    {
        return null;
    }

    public long getItemId(int arg0)
    {
        return 0;
    }

    HashMap<Integer, View> lmap = new HashMap<Integer, View>();

    public View getView(int position, View convertView, ViewGroup parent)
    {
        contractTab contractTab = listItems.get(position);
        // 自定义视图
        if (lmap.get(position) == null)
        {
            // 获取list_item布局文件的视图
            if (position == 0)
            {
                convertView = listContainer.inflate(R.layout.adapter_areabatchtimebreakofftop, null);
            } else
            {
                convertView = listContainer.inflate(R.layout.adapter_areabatchtimebreakoff, null);
            }

            listItemView = new ListItemView();
            // 获取控件对象
            listItemView.tv_contractname = (TextView) convertView.findViewById(R.id.tv_contractname);
            listItemView.tv_allnumber = (TextView) convertView.findViewById(R.id.tv_allnumber);

            // 设置控件集到convertView
            lmap.put(position, convertView);
            convertView.setTag(listItemView);
        } else
        {
            convertView = lmap.get(position);
            listItemView = (ListItemView) convertView.getTag();
        }
        // 设置文字和图片

        listItemView.tv_contractname.setText(contractTab.getContractname());
        listItemView.tv_allnumber.setText(contractTab.getAllnumber());
        return convertView;
    }


}
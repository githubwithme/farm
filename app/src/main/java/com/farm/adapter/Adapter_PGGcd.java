package com.farm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.PlantGcd;

import java.util.HashMap;
import java.util.List;

public class Adapter_PGGcd extends BaseAdapter
{
    private Context context;// 运行上下文
    private List<PlantGcd> listItems;// 数据集合
    private LayoutInflater listContainer;// 视图容器
    String String;
    ListItemView listItemView = null;
    PlantGcd plantGcd;

    static class ListItemView
    {
        public TextView tv_name;
    }

    public Adapter_PGGcd(Context context, List<PlantGcd> data)
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
        plantGcd = listItems.get(position);
        // 自定义视图

        if (lmap.get(position) == null)
        {
            // 获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.commember_item, null);
            listItemView = new ListItemView();
            // 获取控件对象
            listItemView.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            // 设置控件集到convertView
            lmap.put(position, convertView);
            convertView.setTag(listItemView);
        } else
        {
            convertView = lmap.get(position);
            listItemView = (ListItemView) convertView.getTag();
        }
        // 设置文字和图片
        listItemView.tv_name.setText(listItems.get(position).getPlantgcdName());
        return convertView;
    }
}
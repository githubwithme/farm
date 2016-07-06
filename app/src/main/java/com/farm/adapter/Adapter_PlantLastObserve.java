package com.farm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.plantgrowthtab;

import java.util.HashMap;
import java.util.List;

public class Adapter_PlantLastObserve extends BaseAdapter
{
    private Context context;// 运行上下文
    private List<plantgrowthtab> listItems;// 数据集合
    private LayoutInflater listContainer;// 视图容器
    ListItemView listItemView = null;

    static class ListItemView
    {
        public TextView tv_plantname;
        public TextView tv_zg;
        public TextView tv_wj;
        public TextView tv_ys;
        public TextView tv_lys;
    }

    public Adapter_PlantLastObserve(Context context, List<plantgrowthtab> data)
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
        plantgrowthtab plantgrowthtab = listItems.get(position);
        // 自定义视图
        if (lmap.get(position) == null)
        {
            // 获取list_item布局文件的视图
            if (position == 0)
            {
                convertView = listContainer.inflate(R.layout.adapter_plantlastobservetop, null);
            } else
            {
                convertView = listContainer.inflate(R.layout.adapter_plantlastobserve, null);
            }

            listItemView = new ListItemView();
            // 获取控件对象
            listItemView.tv_plantname = (TextView) convertView.findViewById(R.id.tv_plantname);
            listItemView.tv_zg = (TextView) convertView.findViewById(R.id.tv_zg);
            listItemView.tv_wj = (TextView) convertView.findViewById(R.id.tv_wj);
            listItemView.tv_ys = (TextView) convertView.findViewById(R.id.tv_ys);
            listItemView.tv_lys = (TextView) convertView.findViewById(R.id.tv_lys);

            // 设置控件集到convertView
            lmap.put(position, convertView);
            convertView.setTag(listItemView);
        } else
        {
            convertView = lmap.get(position);
            listItemView = (ListItemView) convertView.getTag();
        }
        // 设置文字和图片

        listItemView.tv_plantname.setText(plantgrowthtab.getplantName());
        listItemView.tv_zg.setText(plantgrowthtab.gethNum());
        listItemView.tv_wj.setText(plantgrowthtab.getwNum());
        listItemView.tv_ys.setText(plantgrowthtab.getyNum());
        listItemView.tv_lys.setText(plantgrowthtab.getxNum());//有问题
        return convertView;
    }


}
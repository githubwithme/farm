package com.farm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.goodslisttab;

import java.util.HashMap;
import java.util.List;

public class GoodsSelected_Adapter extends BaseAdapter
{
    List<goodslisttab> list;
    private Context context;
    private LayoutInflater listContainer;// 视图容器

    public GoodsSelected_Adapter(Context context, List<goodslisttab> list)
    {
        this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.context = context;
        this.list = list;
    }

    public int getCount()
    {
        return list.size();
    }

    public Object getItem(int position)
    {
        return list.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }

    HashMap<Integer, View> lmap = new HashMap<Integer, View>();

    public View getView(int arg0, View convertView, ViewGroup viewGroup)
    {
        ListItemView listItemView = null;
        if (lmap.get(arg0) == null)
        {
            convertView = listContainer.inflate(R.layout.goodselectedadapter, null);
            listItemView = new ListItemView();
            listItemView.tv_fl = (TextView) convertView.findViewById(R.id.tv_fl);
            listItemView.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
            listItemView.tv_gg = (TextView) convertView.findViewById(R.id.tv_gg);

            listItemView.tv_fl.setText(list.get(arg0).getgoodsName());
            listItemView.tv_number.setText(list.get(arg0).getGoodsSum()+"  "+list.get(arg0).getgoodsunit());
            listItemView.tv_gg.setText(list.get(arg0).getgoodsSpec());

            lmap.put(arg0, convertView);
            convertView.setTag(listItemView);
        } else
        {
            convertView = lmap.get(arg0);
            listItemView = (ListItemView) convertView.getTag();
        }
        return convertView;
    }


    static class ListItemView
    { // 自定义控件集合
        TextView tv_fl;
        TextView tv_number;
        TextView tv_gg;
    }


}

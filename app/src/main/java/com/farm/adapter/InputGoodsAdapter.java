package com.farm.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.goodslisttab;

import java.util.HashMap;
import java.util.List;

public class InputGoodsAdapter extends BaseAdapter
{
    List<goodslisttab> list;
    private Context context;
    String parkid = "";
    String parkName = "";
    String areaid = "";
    String areaName = "";
    private LayoutInflater listContainer;// 视图容器

    public InputGoodsAdapter(Context context, List<goodslisttab> list, String parkid, String parkName, String areaid, String areaName)
    {
        this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.context = context;
        this.list = list;
        this.parkid = parkid;
        this.parkName = parkName;
        this.areaid = areaid;
        this.areaName = areaName;
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
            convertView = listContainer.inflate(R.layout.inputgoodsnumberadapter, null);
            listItemView = new ListItemView();
            listItemView.tv_fl = (TextView) convertView.findViewById(R.id.tv_fl);
            listItemView.et_number = (TextView) convertView.findViewById(R.id.et_number);
            listItemView.tv_dw = (TextView) convertView.findViewById(R.id.tv_dw);
            Bundle bundle = new Bundle();
            bundle.putString("pi", parkid);
            bundle.putString("pn", parkName);
            bundle.putString("ai", areaid);
            bundle.putString("an", areaName);
            listItemView.tv_fl.setTag(bundle);
            listItemView.tv_fl.setText(list.get(arg0).getgoodsName());
            listItemView.tv_dw.setText(list.get(arg0).getgoodsSpec());

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
        TextView et_number;
        TextView tv_dw;
    }

    public List<goodslisttab> getGoosList()
    {
        for (int i = 0; i < list.size(); i++)
        {
            ListItemView listItemView = (ListItemView) lmap.get(i).getTag();
            Bundle bundle = (Bundle) listItemView.tv_fl.getTag();
            list.get(i).setYL(listItemView.et_number.getText().toString());
            list.get(i).setParkId(bundle.get("pi").toString());
            list.get(i).setParkName(bundle.get("pn").toString());
            list.get(i).setAreaId(bundle.get("ai").toString());
            list.get(i).setAreaName(bundle.get("an").toString());
        }
        return list;
    }

}

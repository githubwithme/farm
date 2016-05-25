package com.farm.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.SellOrderDetail_New;

import java.util.HashMap;
import java.util.List;

public class Adapter_SellOrderDetail extends BaseAdapter
{
    private Activity context;// 运行上下文
    private List<SellOrderDetail_New> listItems;// 数据集合
    private LayoutInflater listContainer;// 视图容器
    SellOrderDetail_New SellOrderDetail;

    static class ListItemView
    {
        public TextView tv_area;
        public TextView tv_actualweight;
        public TextView tv_actualnumber;
        public TextView tv_plannumber;

        public TextView tv_yq;
        public TextView tv_pq;
    }

    public Adapter_SellOrderDetail(Activity context, List<SellOrderDetail_New> data)
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
        SellOrderDetail = listItems.get(position);
        // 自定义视图
        ListItemView listItemView = null;
        if (lmap.get(position) == null)
        {
            // 获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.adapter_sellorderdetail, null);
            listItemView = new ListItemView();
            // 获取控件对象
            listItemView.tv_area = (TextView) convertView.findViewById(R.id.tv_area);
            listItemView.tv_plannumber = (TextView) convertView.findViewById(R.id.tv_plannumber);
            listItemView.tv_actualnumber = (TextView) convertView.findViewById(R.id.tv_actualnumber);
            listItemView.tv_actualweight = (TextView) convertView.findViewById(R.id.tv_actualweight);
            // 设置控件集到convertView
            lmap.put(position, convertView);
            convertView.setTag(listItemView);
        } else
        {
            convertView = lmap.get(position);
            listItemView = (ListItemView) convertView.getTag();
        }
        // 设置文字和图片
        if (SellOrderDetail.getactualnumber().equals(""))
        {
            listItemView.tv_actualnumber.setText("实售0株");
        } else
        {
            listItemView.tv_actualnumber.setText("实售" + SellOrderDetail.getactualnumber() + "株");
        }
        if (SellOrderDetail.getactualweight().equals(""))
        {
            listItemView.tv_actualweight.setText("实重0斤");
        } else
        {
            listItemView.tv_actualweight.setText("实重" + SellOrderDetail.getactualweight() + "斤");
        }
        listItemView.tv_plannumber.setText("拟售" + SellOrderDetail.getplannumber() + "株");
        listItemView.tv_area.setText(SellOrderDetail.getparkname() + SellOrderDetail.getareaname() + SellOrderDetail.getcontractname());
        return convertView;
    }


}
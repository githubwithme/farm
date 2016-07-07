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

/**
 * Created by hasee on 2016/7/7.
 */
public class Adapter_New_SellDetail extends BaseAdapter
{
    private Activity context;// 运行上下文
    private List<SellOrderDetail_New> listItems;// 数据集合
    private LayoutInflater listContainer;// 视图容器
    SellOrderDetail_New SellOrderDetail;

    static class ListItemView
    {
        public TextView tv_address;
        public TextView tv_plan;
        public TextView tv_actual;
        public TextView tv_jingzhong;


    }

    public Adapter_New_SellDetail(Activity context, List<SellOrderDetail_New> data)
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
            convertView = listContainer.inflate(R.layout.adapter_new_order, null);
            listItemView = new ListItemView();
            // 获取控件对象
            listItemView.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
            listItemView.tv_plan = (TextView) convertView.findViewById(R.id.tv_plan);
            listItemView.tv_actual = (TextView) convertView.findViewById(R.id.tv_actual);
            listItemView.tv_jingzhong = (TextView) convertView.findViewById(R.id.tv_jingzhong);
            // 设置控件集到convertView
            lmap.put(position, convertView);
            convertView.setTag(listItemView);
        } else
        {
            convertView = lmap.get(position);
            listItemView = (ListItemView) convertView.getTag();
        }
        // 设置文字和图片
        listItemView.tv_plan.setText(SellOrderDetail.getplannumber());
        if (SellOrderDetail.getactualnumber().equals(""))
        {
            listItemView.tv_actual.setText("0");
        } else
        {
            listItemView.tv_actual.setText(SellOrderDetail.getactualnumber() );
        }
        if (SellOrderDetail.getactualweight().equals(""))
        {
            listItemView.tv_jingzhong.setText("0");
        } else
        {
            listItemView.tv_jingzhong.setText(SellOrderDetail.getactualweight());
        }
//        listItemView.tv_jingzhong.setText(SellOrderDetail.getplannumber() );
//        SellOrderDetail.getparkname() +"\n"+
        listItemView.tv_address.setText( SellOrderDetail.getareaname() +"\n"+ SellOrderDetail.getcontractname());
        return convertView;
    }
}

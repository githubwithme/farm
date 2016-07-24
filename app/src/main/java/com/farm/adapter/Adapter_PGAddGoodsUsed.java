package com.farm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.ContractGoodsUsed;

import java.util.List;

public class Adapter_PGAddGoodsUsed extends BaseAdapter
{
    private Context context;// 运行上下文
    private List<ContractGoodsUsed> listItems;// 数据集合
    private LayoutInflater listContainer;// 视图容器
    ContractGoodsUsed contractGoodsUsed;
    ListItemView listItemView = null;

    static class ListItemView
    {
        public TextView tv_name;
        public EditText et_number;
    }

    public Adapter_PGAddGoodsUsed(Context context, List<ContractGoodsUsed> data)
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


    public View getView(int position, View convertView, ViewGroup parent)
    {
        contractGoodsUsed = listItems.get(position);
        // 自定义视图

        if (convertView == null)
        {
            // 获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.adapter_pgaddgoodsused, null);
            listItemView = new ListItemView();
            // 获取控件对象
            listItemView.et_number = (EditText) convertView.findViewById(R.id.et_number);
            listItemView.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(listItemView);
        }

        // 设置文字和图片
//        listItemView.et_number.setText("150");
        listItemView.tv_name.setText(listItems.get(position).getGoodsName());
        return convertView;
    }
}
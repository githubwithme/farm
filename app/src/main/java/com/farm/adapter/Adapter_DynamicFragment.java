package com.farm.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.DynamicEntity;
import com.farm.widget.CircleImageView;

import java.util.HashMap;
import java.util.List;

@SuppressLint("NewApi")
public class Adapter_DynamicFragment extends BaseAdapter
{
    private Context context;// 运行上下文
    private List<DynamicEntity> listItems;// 数据集合
    private LayoutInflater listContainer;// 视图容器
    DynamicEntity dynamicEntity;
    static class ListItemView
    {
        public TextView tv_note;
        public TextView tv_date;
        public TextView tv_title;
        public CircleImageView circle_img;
    }

    public Adapter_DynamicFragment(Context context, List<DynamicEntity> data)
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
        dynamicEntity = listItems.get(position);
        // 自定义视图
        ListItemView listItemView = null;
        if (lmap.get(position) == null)
        {
            // 获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.adapter_dynamic, null);
            listItemView = new ListItemView();
            // 获取控件对象
            listItemView.tv_note = (TextView) convertView.findViewById(R.id.tv_note);
            listItemView.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            listItemView.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            listItemView.circle_img = (CircleImageView) convertView.findViewById(R.id.circle_img);
            // 设置控件集到convertView
            lmap.put(position, convertView);
            convertView.setTag(listItemView);


            listItemView.tv_title.setText(dynamicEntity.getTitle());
            listItemView.tv_date.setText(dynamicEntity.getDate());
            listItemView.tv_note.setText(dynamicEntity.getNote());
            String type = dynamicEntity.getType();
            if (type.equals("ZL"))
            {
                listItemView.circle_img.setImageResource(R.drawable.bg_importance1);
            } else if (type.equals("GZ"))
            {
                listItemView.circle_img.setImageResource(R.drawable.quick_set_create_report);
            } else if (type.equals("MQ"))
            {
                listItemView.circle_img.setImageResource(R.drawable.bg_importance3);
            } else if (type.equals("XS"))
            {
                listItemView.circle_img.setImageResource(R.drawable.ic_shoppingcart);
            } else if (type.equals("KC"))
            {
                listItemView.circle_img.setImageResource(R.drawable.bg_importance2);
            } else if (type.equals("SP"))
            {
                listItemView.circle_img.setImageResource(R.drawable.ic_shoppingcart);
            } else if (type.equals("SJ"))
            {
                listItemView.circle_img.setImageResource(R.drawable.quick_set_create_report);
            } else if (type.equals("DL"))
            {
                listItemView.circle_img.setImageResource(R.drawable.bg_importance1);
            }


        } else
        {
            convertView = lmap.get(position);
            listItemView = (ListItemView) convertView.getTag();
        }
        return convertView;
    }


}
package com.farm.adapter;

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

/**
 * Created by hasee on 2016/5/29.
 */
public class DongtaiListviewAdapter extends BaseAdapter
{
    private Context context;// 运行上下文
    private List<DynamicEntity> listItems;// 数据集合
    private LayoutInflater listContainer;
    DynamicEntity dynamicEntity;
    String type;

    public DongtaiListviewAdapter(Context context, List<DynamicEntity> data,String type)
    {
        this.context = context;
        this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.type = type;
        this.listItems = data;
    }

    static class ListItemView
    {
        public CircleImageView circle_img;
        public TextView tv_note;
        public TextView tv_date;
        public TextView tv_title;
    }

    @Override
    public int getCount()
    {
        return listItems.size();
    }

    @Override
    public Object getItem(int i)
    {
        return null;
    }

    @Override
    public long getItemId(int i)
    {
        return 0;
    }

    HashMap<Integer, View> lmap = new HashMap<Integer, View>();

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        dynamicEntity = listItems.get(i);
        // 自定义视图
        ListItemView listItemView = null;
        if (lmap.get(i) == null)
        {
            // 获取list_item布局文件的视图
            view = listContainer.inflate(R.layout.dongtailistview_item, null);
            listItemView = new ListItemView();
            // 获取控件对象
            listItemView.circle_img = (CircleImageView) view.findViewById(R.id.circle_img);
            listItemView.tv_title = (TextView) view.findViewById(R.id.tv_title);
            listItemView.tv_date = (TextView) view.findViewById(R.id.tv_date);
            listItemView.tv_note = (TextView) view.findViewById(R.id.tv_note);
            lmap.put(i, view);
            view.setTag(listItemView);
        } else
        {
            view = lmap.get(i);
            listItemView = (ListItemView) view.getTag();
        }

        listItemView.tv_title.setText(dynamicEntity.getTitle());
        listItemView.tv_note.setText(dynamicEntity.getNote());
        listItemView.tv_date.setText(dynamicEntity.getDate());
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
        return view;
    }
}

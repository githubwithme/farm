package com.farm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.Wz_Storehouse;

import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 2016/2/26.
 */
public class NCZ_WZ_CKXXlistAdapter extends BaseAdapter
{
    private Context context;// 运行上下文
    private List<Wz_Storehouse> listItems;// 数据集合
    private LayoutInflater listContainer;
    Wz_Storehouse Wz_Storehouse;

    public NCZ_WZ_CKXXlistAdapter(Context context, List<Wz_Storehouse> data)
    {
        this.context = context;
        this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.listItems = data;
    }

    static class ListItemView
    {
        public TextView parkName;
        public TextView storehouseName;
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
        Wz_Storehouse = listItems.get(i);
        // 自定义视图
        ListItemView listItemView = null;
        if (lmap.get(i) == null)
        {
            // 获取list_item布局文件的视图
            view = listContainer.inflate(R.layout.ncz_wz_ck_item, null);
            listItemView = new ListItemView();
            // 获取控件对象
            listItemView.parkName = (TextView) view.findViewById(R.id.parkName);
            listItemView.storehouseName = (TextView) view.findViewById(R.id.storehouseName);
            lmap.put(i, view);
            view.setTag(listItemView);
        } else
        {
            view = lmap.get(i);
            listItemView = (ListItemView) view.getTag();
        }
        listItemView.parkName.setText(Wz_Storehouse.getParkName() + "-");
        listItemView.storehouseName.setText(Wz_Storehouse.getStorehouseName());
        return view;
    }

}

package com.farm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.planttab;

import java.util.HashMap;
import java.util.List;

/**
 *
 */
public class AddPlantObservationAdapter extends BaseAdapter
{
    ListItemView listItemView = null;
    String audiopath;
    private Context context;// 运行上下文
    private List<planttab> listItems;// 数据集合
    private LayoutInflater listContainer;// 视图容器
    planttab planttab;

    static class ListItemView
    { // 自定义控件集合

        public TextView tv_one;

    }

    /**
     * 实例化Adapter
     *
     * @param context
     * @param data
     * @param context
     */
    public AddPlantObservationAdapter(Context context, List<planttab> data)
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

    /**
     * ListView Item设置
     */
    public View getView(int position, View convertView, ViewGroup parent)
    {
        planttab = listItems.get(position);
        // 自定义视图

        if (lmap.get(position) == null)
        {
            // 获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.addplantobservationadapter, null);
            listItemView = new ListItemView();
            // 获取控件对象
            listItemView.tv_one = (TextView) convertView.findViewById(R.id.tv_one);
            // 设置控件集到convertView
            lmap.put(position, convertView);
            convertView.setTag(listItemView);
        } else
        {
            convertView = lmap.get(position);
            listItemView = (ListItemView) convertView.getTag();
        }
        listItemView.tv_one.setText(planttab.getplantName());
        return convertView;
    }
}
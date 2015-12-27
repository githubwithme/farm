package com.farm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.planttab;

import java.util.List;

/**
 *
 */
public class PlantObservationAdapter extends BaseAdapter
{
    ListItemView listItemView = null;
    String audiopath;
    private Context context;// 运行上下文
    private List<planttab> listItems;// 数据集合
    private LayoutInflater listContainer;// 视图容器
    planttab planttab;

    static class ListItemView
    { // 自定义控件集合

        public TextView tv_plantname;

    }

    /**
     * 实例化Adapter
     *
     * @param context
     * @param data
     * @param context
     */
    public PlantObservationAdapter(Context context, List<planttab> data)
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


    /**
     * ListView Item设置
     */
    public View getView(int position, View convertView, ViewGroup parent)
    {
        planttab = listItems.get(position);
        convertView = listContainer.inflate(R.layout.plantobservationadapter, null);
        listItemView = new ListItemView();
        listItemView.tv_plantname = (TextView) convertView.findViewById(R.id.tv_plantname);
        listItemView.tv_plantname.setText(planttab.getplantName());

        return convertView;
    }
}
package com.farm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.planttab;

import java.util.List;

/**
 *
 */
public class AddPlantObservationAdapter_MakeSure extends BaseAdapter
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
        public TextView tv_zg;
        public TextView tv_wj;
        public TextView tv_ys;
        public TextView tv_lys;
        public TextView tv_lysj;
        public CheckBox cb_sfcl;
        public CheckBox cb_sfyz;

    }

    /**
     * 实例化Adapter
     *
     * @param context
     * @param data
     * @param context
     */
    public AddPlantObservationAdapter_MakeSure(Context context, List<planttab> data)
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
        convertView = listContainer.inflate(R.layout.addplantobservationadapter_makesure, null);
        listItemView = new ListItemView();
        listItemView.tv_plantname = (TextView) convertView.findViewById(R.id.tv_plantname);
        listItemView.tv_zg = (TextView) convertView.findViewById(R.id.tv_zg);
        listItemView.tv_wj = (TextView) convertView.findViewById(R.id.tv_wj);
        listItemView.tv_ys = (TextView) convertView.findViewById(R.id.tv_ys);
        listItemView.tv_lys = (TextView) convertView.findViewById(R.id.tv_lys);
        listItemView.tv_lysj = (TextView) convertView.findViewById(R.id.tv_lysj);
        listItemView.cb_sfcl = (CheckBox) convertView.findViewById(R.id.cb_sfcl);
        listItemView.cb_sfyz = (CheckBox) convertView.findViewById(R.id.cb_sfyz);
        listItemView.tv_plantname.setText(planttab.getplantName());
        listItemView.tv_zg.setText(planttab.gethNum());
        listItemView.tv_wj.setText(planttab.getwNum());
        listItemView.tv_ys.setText(planttab.getyNum());
        listItemView.tv_lys.setText(planttab.getxNum());
        listItemView.tv_lysj.setText(planttab.getzDate());
        listItemView.cb_sfcl.setChecked(true);
        listItemView.cb_sfyz.setChecked(true);
        return convertView;
    }
}
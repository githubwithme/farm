package com.farm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.WZ_Pcxx;

import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 2016/4/7.
 */
public class NCZ_WZ_FBAdapter extends BaseAdapter
{
    private Context context;// 运行上下文
    private List<WZ_Pcxx> listItems;// 数据集合
    private LayoutInflater listContainer;
    WZ_Pcxx wz_pcxx;
    public NCZ_WZ_FBAdapter(Context context, List<WZ_Pcxx> data)
    {
        this.context = context;
        this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.listItems = data;
    }
    static class ListItemView
    {
        public TextView local;
        public TextView number;

    }
    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    HashMap<Integer, View> lmap = new HashMap<Integer, View>();
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        wz_pcxx = listItems.get(i);
        // 自定义视图
        ListItemView listItemView = null;
        if (lmap.get(i) == null) {
            // 获取list_item布局文件的视图
            view = listContainer.inflate(R.layout.ncz_wz_fbadapter, null);
            listItemView = new ListItemView();
            // 获取控件对象

            listItemView.local = (TextView) view.findViewById(R.id.local);
            listItemView.number = (TextView) view.findViewById(R.id.number);

            lmap.put(i, view);
            view.setTag(listItemView);
        }else {
            view = lmap.get(i);
            listItemView = (ListItemView) view.getTag();
        }

        listItemView.local.setText(wz_pcxx.getParkName()+"-"+wz_pcxx.getStorehouseName());
        listItemView.number.setText("数量:"+wz_pcxx.getNumber());


        return view;
    }
}

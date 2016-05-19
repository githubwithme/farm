package com.farm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.farm.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 2016/5/19.
 */
public class NCZ_Statistics_Adapter extends BaseAdapter
{
    private Context context;// 运行上下文
    private List<String> listItems;// 数据集合
    private LayoutInflater listContainer;
    String data;

    public NCZ_Statistics_Adapter(Context context, List<String> data) {
        this.context = context;
        this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.listItems = data;

    }

    static class ListItemView {
        public TextView name;
        public TextView num;
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
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        data = listItems.get(i);
        // 自定义视图
        ListItemView listItemView = null;
        if (lmap.get(i) == null) {
            // 获取list_item布局文件的视图
            view = listContainer.inflate(R.layout.ncz_statistics_adapter, null);
            listItemView = new ListItemView();
            // 获取控件对象s
            listItemView.num = (TextView) view.findViewById(R.id.num);

          /*  listItemView.name.setText(breakOff_new.getcontractname());
            listItemView.num.setText(breakOff_new.getnumberofbreakoff());*/
            listItemView.num.setText(data);
            lmap.put(i, view);
            view.setTag(listItemView);
        } else

        {
            view = lmap.get(i);
            listItemView = (ListItemView) view.getTag();
        }


        return view;
    }

}


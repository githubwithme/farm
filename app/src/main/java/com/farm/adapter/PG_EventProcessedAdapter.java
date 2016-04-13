package com.farm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.HandleBean;

import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 2016/4/12.
 */
public class PG_EventProcessedAdapter extends BaseAdapter
{
    private Context context;// 运行上下文
    private List<com.farm.bean.HandleBean> listItems;// 数据集合
    private LayoutInflater listContainer;
    HandleBean HandleBean;

    public PG_EventProcessedAdapter(Context context, List<HandleBean> data)
    {
        this.context = context;
        this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.listItems = data;
    }
    static class ListItemView
    {
        public TextView name;
        public TextView state;
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
        HandleBean = listItems.get(i);
        // 自定义视图
        ListItemView listItemView = null;
        if (lmap.get(i) == null) {
            // 获取list_item布局文件的视图
            view = listContainer.inflate(R.layout.ncz_eventhandleadapter, null);
            listItemView = new ListItemView();
            // 获取控件对象
            listItemView.name = (TextView) view.findViewById(R.id.name);
            listItemView.state = (TextView) view.findViewById(R.id.state);
            lmap.put(i, view);
            view.setTag(listItemView);
        }else {
            view = lmap.get(i);
            listItemView = (ListItemView) view.getTag();
        }
        listItemView.name.setText(HandleBean.getResultId());
        if(HandleBean.getState().equals("0"))
        {
            listItemView.state.setText("未处理");
        }else
        {
            listItemView.state.setText("已处理");
        }


        return view;
    }

}

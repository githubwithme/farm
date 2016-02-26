package com.farm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.commandtab;

import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 2016/2/26.
 */
public class NCZ_WZ_CKXXlistAdapter extends BaseAdapter {
    private Context context;// 运行上下文
    private List<commandtab> listItems;// 数据集合
    private LayoutInflater listContainer;
    commandtab commandtab;
    public NCZ_WZ_CKXXlistAdapter(Context context, List<commandtab> data)
    {
        this.context = context;
        this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.listItems = data;
    }
    static class ListItemView
    {
        public TextView wz_ggg;
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
        commandtab = listItems.get(i);
        // 自定义视图
        ListItemView listItemView = null;
        if (lmap.get(i) == null) {
            // 获取list_item布局文件的视图
            view = listContainer.inflate(R.layout.ncz_wz_ck_item, null);
            listItemView = new ListItemView();
            // 获取控件对象
            listItemView.wz_ggg = (TextView) view.findViewById(R.id.wz_ggg);
        }
        lmap.put(i, view);
        view.setTag(listItemView);
        return view;
    }
}

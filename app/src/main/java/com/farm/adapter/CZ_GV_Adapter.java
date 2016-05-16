package com.farm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.BreakOff_New;
import com.farm.bean.contractTab;

import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 2016/5/3.
 */
public class CZ_GV_Adapter extends BaseAdapter {

    private Context context;// 运行上下文
    private List<BreakOff_New> listItems;// 数据集合
    private LayoutInflater listContainer;
    BreakOff_New breakOff_new;

    public CZ_GV_Adapter(Context context, List<BreakOff_New> data) {
        this.context = context;
        this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.listItems = data;

    }

    static class ListItemView {
        public TextView name;
        public TextView num;
        public TextView areaname;
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
        breakOff_new = listItems.get(i);
        // 自定义视图
        ListItemView listItemView = null;
        if (lmap.get(i) == null) {
            // 获取list_item布局文件的视图
            view = listContainer.inflate(R.layout.cz_gv_adapter, null);
            listItemView = new ListItemView();
            // 获取控件对象
            listItemView.name = (TextView) view.findViewById(R.id.name);
            listItemView.num = (TextView) view.findViewById(R.id.num);
            listItemView.areaname = (TextView) view.findViewById(R.id.areaname);

            listItemView.name.setText(breakOff_new.getareaname());
            listItemView.areaname.setText(breakOff_new.getcontractname());
            listItemView.num.setText(breakOff_new.getnumberofbreakoff());
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

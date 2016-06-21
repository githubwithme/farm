package com.farm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.BatchTime;

import java.util.HashMap;
import java.util.List;

/**
 * Created by hasee on 2016/6/20.
 */
public class NCZ_New_farmsaleItem extends BaseAdapter
{
    private Context context;// 运行上下文
    private List<BatchTime> listItems;// 数据集合
    private LayoutInflater listContainer;
    BatchTime batchTime;

    public NCZ_New_farmsaleItem(Context context, List<BatchTime> listItems)
    {
        this.context = context;
        this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.listItems = listItems;
    }

    static class ListItemView
    {
        public TextView batchtimes;
        public TextView allnum;
        public TextView allsalefor;
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
        batchTime = listItems.get(i);
        // 自定义视图
        ListItemView listItemView = null;
        if (lmap.get(i) == null) {
            // 获取list_item布局文件的视图
            view = listContainer.inflate(R.layout.ncz_new_farmsaleitem, null);
            listItemView = new ListItemView();
            // 获取控件对象

            listItemView.batchtimes = (TextView) view.findViewById(R.id.batchtimes);
            listItemView.allnum = (TextView) view.findViewById(R.id.allnum);
            listItemView.allsalefor = (TextView) view.findViewById(R.id.allsalefor);




            lmap.put(i, view);
            view.setTag(listItemView);
        }else {
            view = lmap.get(i);
            listItemView = (ListItemView) view.getTag();
        }
        int allnumber = Integer.valueOf(batchTime.getAllsaleout()) + Integer.valueOf(batchTime.getAllsalein()) + Integer.valueOf(batchTime.getAllnewsale()) + Integer.valueOf(batchTime.getAllsalefor());
        listItemView.batchtimes.setText(batchTime.getBatchTime());
        listItemView.allnum.setText(allnumber+"");
        listItemView.allsalefor.setText(batchTime.getAllsalefor());

        return view;
    }
}

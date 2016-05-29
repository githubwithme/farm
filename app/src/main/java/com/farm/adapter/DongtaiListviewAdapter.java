package com.farm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.DynamicEntity;

import java.util.HashMap;
import java.util.List;

/**
 * Created by hasee on 2016/5/29.
 */
public class DongtaiListviewAdapter extends BaseAdapter {
    private Context context;// 运行上下文
    private List<DynamicEntity> listItems;// 数据集合
    private LayoutInflater listContainer;
    DynamicEntity dynamicEntity;
    public DongtaiListviewAdapter(Context context, List<DynamicEntity> data)
    {
        this.context = context;
        this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.listItems = data;
    }
    static class ListItemView
    {
        public TextView title;
        public TextView note;
        public TextView date;
        public TextView type;
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
        dynamicEntity = listItems.get(i);
        // 自定义视图
        ListItemView listItemView = null;
        if (lmap.get(i) == null) {
            // 获取list_item布局文件的视图
            view = listContainer.inflate(R.layout.dongtailistview_item, null);
            listItemView = new ListItemView();
            // 获取控件对象
            listItemView.title = (TextView) view.findViewById(R.id.title);
            listItemView.note = (TextView) view.findViewById(R.id.note);
            listItemView.date = (TextView) view.findViewById(R.id.date);
            listItemView.type = (TextView) view.findViewById(R.id.type);
            lmap.put(i, view);
            view.setTag(listItemView);
        }else {
            view = lmap.get(i);
            listItemView = (ListItemView) view.getTag();
        }
/*        listItemView.batchNumber.setText("批次号:"+wz_pcxx.getBatchName());
        listItemView.inDate.setText(wz_pcxx.getInDate());
        listItemView.local.setText(wz_pcxx.getParkName()+"-"+wz_pcxx.getStorehouseName());
        listItemView.quantity.setText(wz_pcxx.getQuantity());
        listItemView.inGoodsValue.setText(wz_pcxx.getInGoodsValue()+"元");*/

        listItemView.title.setText(dynamicEntity.getTitle());
        listItemView.note.setText(dynamicEntity.getNote());
        listItemView.date.setText(dynamicEntity.getDate());
        listItemView.type.setText(dynamicEntity.getType());

        return view;
    }
}

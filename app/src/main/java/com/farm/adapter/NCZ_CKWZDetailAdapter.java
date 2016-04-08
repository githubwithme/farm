package com.farm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.Wz_Storehouse;

import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 2016/4/6.
 */
public class NCZ_CKWZDetailAdapter extends BaseAdapter
{
    private Context context;// 运行上下文
    private List<com.farm.bean.Wz_Storehouse> listItems;// 数据集合
    private LayoutInflater listContainer;
    Wz_Storehouse Wz_Storehouse;
    public NCZ_CKWZDetailAdapter(Context context, List<Wz_Storehouse> data)
    {
        this.context = context;
        this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.listItems = data;
    }
    static class ListItemView
    {
        public TextView batchName;
        public TextView quantity;
        public TextView inGoodsValue;
        public TextView expDate;
        public TextView inDate;
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
        Wz_Storehouse = listItems.get(i);
        // 自定义视图
        ListItemView listItemView = null;
        if (lmap.get(i) == null) {
            // 获取list_item布局文件的视图
            view = listContainer.inflate(R.layout.ncz_ckwzdetailadapter, null);
            listItemView = new ListItemView();
            // 获取控件对象
            listItemView.batchName = (TextView) view.findViewById(R.id.batchName);
            listItemView.quantity = (TextView) view.findViewById(R.id.quantity);
            listItemView.inGoodsValue = (TextView) view.findViewById(R.id.inGoodsValue);
            listItemView.expDate = (TextView) view.findViewById(R.id.expDate);
            listItemView.inDate = (TextView) view.findViewById(R.id.inDate);
            lmap.put(i, view);
            view.setTag(listItemView);
        }else {
            view = lmap.get(i);
            listItemView = (ListItemView) view.getTag();
        }
        String a=Wz_Storehouse.getExpDate().substring(0,Wz_Storehouse.getExpDate().length()-8);
        String b=Wz_Storehouse.getInDate().substring(0,Wz_Storehouse.getInDate().length()-8);
        listItemView.batchName.setText(Wz_Storehouse.getBatchName());
        listItemView.quantity.setText(Wz_Storehouse.getQuantity());
        listItemView.inGoodsValue.setText(Wz_Storehouse.getInGoodsValue()+"元");
        listItemView.expDate.setText(a);
        listItemView.inDate.setText(b);
        return view;
    }
}

package com.farm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.WZ_YCxx;

import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 2016/4/8.
 */
public class NCZ_WZ_YClistAdapter extends BaseAdapter
{
    private Context context;// 运行上下文
    private List<WZ_YCxx> listItems;// 数据集合
    private LayoutInflater listContainer;
    WZ_YCxx wz_yCxx;
    public NCZ_WZ_YClistAdapter(Context context, List<WZ_YCxx> data)
    {
        this.context = context;
        this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.listItems = data;
    }
    static class ListItemView
    {
        public TextView indata;
        public TextView local;
        public TextView goodname;
        public TextView expdata;
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
        wz_yCxx = listItems.get(i);
        // 自定义视图
        ListItemView listItemView = null;
        if (lmap.get(i) == null) {
            // 获取list_item布局文件的视图
            view = listContainer.inflate(R.layout.ncz_wz_yc_item, null);
            listItemView = new ListItemView();
            // 获取控件对象
            listItemView.indata = (TextView) view.findViewById(R.id.indata);
            listItemView.local = (TextView) view.findViewById(R.id.local);
            listItemView.goodname = (TextView) view.findViewById(R.id.goodname);
            listItemView.expdata = (TextView) view.findViewById(R.id.expdata);

            //数据添加
            listItemView.indata.setText(wz_yCxx.getNowDate());
            listItemView.local.setText(wz_yCxx.getParkName()+"-"+wz_yCxx.getStorehouseName());
            listItemView.goodname.setText(wz_yCxx.getGoodsName());
            if (wz_yCxx.getType().equals("0"))
            {
                listItemView.expdata.setText("将要过期");
//                listItemView.expdata.setText(wz_yCxx.getGoodsName()+"将要过期");
            }else if (wz_yCxx.getType().equals("1"))
            {
                listItemView.expdata.setText("库存过低");
//                listItemView.expdata.setText(wz_yCxx.getGoodsName()+"库存过低");
            }else
            {
                listItemView.expdata.setText("将要过期|库存过低");
//                listItemView.expdata.setText(wz_yCxx.getGoodsName()+"将要过期|库存过低");
            }




            lmap.put(i, view);
            view.setTag(listItemView);
        }else {
            view = lmap.get(i);
            listItemView = (ListItemView) view.getTag();
        }


        return view;
    }

}

package com.farm.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.SellOrder_New;

import java.util.HashMap;
import java.util.List;

@SuppressLint("NewApi")
public class NCZ_NotPayCost_adapter extends BaseAdapter
{
    String broadcast;
    private Context context;// 运行上下文
    private List<SellOrder_New> listItems;// 数据集合
    private LayoutInflater listContainer;// 视图容器
    SellOrder_New sellOrder;

    static class ListItemView
    {
        public TextView tv_buyer;
        public TextView tv_state;
        public TextView tv_batchtime;
        public Button btn_cancleorder;
        public Button btn_editorder;
    }

    public NCZ_NotPayCost_adapter(Context context, List<SellOrder_New> data, String broadcast)
    {
        this.context = context;
        this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.listItems = data;
        this.broadcast = broadcast;
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

    HashMap<Integer, View> lmap = new HashMap<Integer, View>();

    public View getView(int position, View convertView, ViewGroup parent)
    {
        sellOrder = listItems.get(position);
        // 自定义视图
        ListItemView listItemView = null;
        if (lmap.get(position) == null)
        {
            // 获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.ncz_notpaycost_adapter, null);
            listItemView = new ListItemView();
            // 获取控件对象
            listItemView.tv_buyer = (TextView) convertView.findViewById(R.id.tv_buyer);
            listItemView.tv_state = (TextView) convertView.findViewById(R.id.tv_state);
            listItemView.tv_batchtime = (TextView) convertView.findViewById(R.id.tv_batchtime);
            listItemView.btn_cancleorder = (Button) convertView.findViewById(R.id.btn_cancleorder);
            listItemView.btn_editorder = (Button) convertView.findViewById(R.id.btn_editorder);
            // 设置控件集到convertView
            lmap.put(position, convertView);
            convertView.setTag(listItemView);


            listItemView.btn_cancleorder.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                }
            });
            listItemView.btn_editorder.setTag(R.id.tag_postion, position);
            listItemView.btn_editorder.setTag(R.id.tag_bean, sellOrder);
            listItemView.btn_editorder.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = (int) v.getTag(R.id.tag_postion);
                }
            });
        } else
        {
            convertView = lmap.get(position);
            listItemView = (ListItemView) convertView.getTag();
        }
        return convertView;
    }


}
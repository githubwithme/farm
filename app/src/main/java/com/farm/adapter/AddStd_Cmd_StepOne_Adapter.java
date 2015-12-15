package com.farm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.farm.R;
import com.farm.bean.Dictionary;

import java.util.HashMap;
import java.util.List;

public class AddStd_Cmd_StepOne_Adapter extends BaseAdapter
{
    private Context context;// 运行上下文
    private List<Dictionary> listItems;// 数据集合
    private LayoutInflater listContainer;// 视图容器
    Dictionary dictionary;
    ListItemView listItemView = null;

    static class ListItemView
    {
        public Button btn_zl;

    }

    public AddStd_Cmd_StepOne_Adapter(Context context, List<Dictionary> data)
    {
        this.context = context;
        this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.listItems = data;
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
        dictionary = listItems.get(position);
        // 自定义视图

        if (lmap.get(position) == null)
        {
            // 获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.add_std_cmd_stepone_item, null);
            listItemView = new ListItemView();
            // 获取控件对象
            listItemView.btn_zl = (Button) convertView.findViewById(R.id.btn_zl);
            listItemView.btn_zl.setId(position);
            listItemView.btn_zl.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                }
            });
            // 设置控件集到convertView
            lmap.put(position, convertView);
            convertView.setTag(listItemView);
        } else
        {
            convertView = lmap.get(position);
            listItemView = (ListItemView) convertView.getTag();
        }
        listItemView.btn_zl.setText(listItems.get(position).getFirstItemName().toString());

        return convertView;
    }
}
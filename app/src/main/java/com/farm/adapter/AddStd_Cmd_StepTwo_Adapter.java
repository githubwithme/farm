package com.farm.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.farm.R;
import com.farm.com.custominterface.FragmentCallBack;

import java.util.HashMap;

public class AddStd_Cmd_StepTwo_Adapter extends BaseAdapter
{
    FragmentCallBack fragmentCallBack;
    private Context context;// 运行上下文
    private LayoutInflater listContainer;// 视图容器
    ListItemView listItemView = null;
    String[] secondItemName;

    String item = "";

    static class ListItemView
    {
        public Button btn_zl;

    }

    public AddStd_Cmd_StepTwo_Adapter(Context context, String[] data, FragmentCallBack fragmentCallBack)
    {
        this.context = context;
        this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        secondItemName = data;
        this.fragmentCallBack=fragmentCallBack;
    }

    public int getCount()
    {
        return secondItemName.length;
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
        item = secondItemName[position];
        // 自定义视图

        if (lmap.get(position) == null)
        {
            // 获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.add_std_cmd_steptwo_item, null);
            listItemView = new ListItemView();
            // 获取控件对象
            listItemView.btn_zl = (Button) convertView.findViewById(R.id.btn_zl);
            listItemView.btn_zl.setId(position);
            listItemView.btn_zl.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Bundle bundle = new Bundle();
                    bundle.putInt("INDEX", 1);
                    fragmentCallBack.callbackFun2(bundle);
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
        listItemView.btn_zl.setText(item);

        return convertView;
    }
}
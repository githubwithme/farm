package com.farm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.DepartmentBean;

import java.util.HashMap;
import java.util.List;

public class ContractDepartment_Adapter extends BaseAdapter
{
    private Context context;// 运行上下文
    private List<DepartmentBean> list_department;// 数据集合
    private LayoutInflater listContainer;// 视图容器
    ListItemView listItemView = null;

    DepartmentBean DepartmentBean;

    static class ListItemView
    {
        public TextView tv_name;
        public ImageView iv_selectet;
    }

    public ContractDepartment_Adapter(Context context, List<DepartmentBean> data)
    {
        this.context = context;
        this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.list_department = data;
    }

    public int getCount()
    {
        return list_department.size();
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
        DepartmentBean = list_department.get(position);
        // 自定义视图

        if (lmap.get(position) == null)
        {
            // 获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.department_item, null);
            listItemView = new ListItemView();
            // 获取控件对象
            listItemView.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            listItemView.iv_selectet = (ImageView) convertView.findViewById(R.id.iv_selectet);
            // 设置控件集到convertView
            lmap.put(position, convertView);
            convertView.setTag(listItemView);
        } else
        {
            convertView = lmap.get(position);
            listItemView = (ListItemView) convertView.getTag();
        }
        // 设置文字和图片
//        if (!list_department.get(position).getContractid().equals(""))
//        {
//            listItemView.tv_name.setText((list_department.get(position).getName()));
//        }
        listItemView.tv_name.setText((list_department.get(position).getName()));
        return convertView;
    }
}